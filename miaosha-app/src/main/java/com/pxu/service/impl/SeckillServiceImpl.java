package com.pxu.service.impl;

import com.pxu.domain.SeckillProduct;
import com.pxu.dto.Exposer;
import com.pxu.service.SeckillProductService;
import com.pxu.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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

    @Autowired
    protected StringRedisTemplate redisTemplate;

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
//        //此处需要优化，由于所有秒杀单子都需要调用秒杀暴露接口，通过redis缓存起来，降低数据库压力
//        //超时的基础上维护一致性
//        //方便地做了缓存解决热品秒杀问题
//        //1-访问redis
//        SeckillProduct seckill = redisTemplate.queryById(seckillId);
//        SeckillProduct seckill1 = productService.findById(seckillId);
//        if(seckill == null){
//            //2-访问数据库
//            seckill = seckillDao.queryById(seckillId);
//            if(seckill == null){
//                //返回秒杀单号不存在
//                return new Exposer(false, seckillId);
//            }else{
//                //3-放入redis
//                redisDao.putSeckill(seckill);
//            }
//        }
//
//
//        if (seckill == null) {
//            return new Exposer(false, seckillId);
//        }
//
//        System.err.println(seckill);
//        Date startTime = seckill.getStartTime();
//        Date endTime = seckill.getEndTime();
//        // 系统当前时间
//        Date nowTime = new Date();
//
//        System.err.println("miaosha wei kaishi***************************");
//        //秒杀已经开始或已经结束
//        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
//            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
//        }
//
//        System.err.println("miaoshakaishi***************************");
//        //返回秒杀已经开始的操作
//        // 转化特定字符串的过程，不可逆
//        String md5 = getMD5(seckillId);
//        return new Exposer(true, md5, seckillId);
        return null;
    }
}
