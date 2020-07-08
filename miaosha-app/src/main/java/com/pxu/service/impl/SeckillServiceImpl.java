package com.pxu.service.impl;

import com.pxu.domain.SeckillProduct;
import com.pxu.dto.Exposer;
import com.pxu.dto.SeckillExecution;
import com.pxu.enums.SeckillStateEnum;
import com.pxu.exception.RepeatKillException;
import com.pxu.exception.SeckillCloseException;
import com.pxu.exception.SeckillException;
import com.pxu.service.SeckillProductService;
import com.pxu.service.SeckillService;
import com.pxu.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author pxu31@qq.com
 * @date 2020/7/6 21:20
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    SeckillProductService productService;

    @Override
    public SeckillProduct getOneFromDb(long id) {
        return productService.findById(id);
    }

    @Override
    public List<SeckillProduct> getAllFromDb() {
        return productService.getAll();
    }

    @Override
    public Exposer exportSeckillUrl(Long seckillId) {
        //查数据
        SeckillProduct seckill = productService.findById(seckillId);

        if (seckill == null) {
            return new Exposer(false, seckillId);
        }

        System.err.println(seckill);
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 系统当前时间
        Date nowTime = new Date();

        System.err.println("miaosha wei kaishi***************************");
        //秒杀已经开始或已经结束
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        System.err.println("miaoshakaishi***************************");
        //返回秒杀已经开始的操作
        // 转化特定字符串的过程，不可逆
        String md5 = MD5Utils.getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(MD5Utils.getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }

        // 执行秒杀逻辑：减库存 + 记录购买行为
        Date now = new Date();
        try {
            // 记录购买行为
            int insertCount = productService.insertSuccessKilled(seckillId, userPhone);
            // 唯一：seckillId,userPhone
            if (insertCount <= 0) {
                // 重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                // 减库存，热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, now);
                if (updateCount <= 0) {
                    // 没有更新到记录也就是没抢到, rollback不记录此次秒杀行为,秒杀结束
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    // 秒杀成功 commit,根据id查询SuccessKilled并携带秒杀产品对象实体
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            //e1,e2不同类型catch异常,代表秒杀关闭,正常逻辑允许的异常
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 所有编译期异常转换为运行期异常,有异常时候会rollback,无论前面发生什么错误,都rollback
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

}
