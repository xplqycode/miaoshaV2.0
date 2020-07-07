package com.pxu.controller;

import com.pxu.domain.SeckillProduct;
import com.pxu.dto.Exposer;
import com.pxu.dto.SeckillResult;
import com.pxu.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 秒杀主程序入口
 * @author pxu31@qq.com
 * @date 2020/7/6 21:13
 */
@Slf4j
@Controller
@RequestMapping(value = "/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    @RequestMapping("/test")
    public String test(){
        System.out.println("test");
        log.info("test");
        return "index";
    }

    @RequestMapping("/list")
    public String findSeckillList(Model model) {
        List<SeckillProduct> list = seckillService.getAllFromDb();
        model.addAttribute("list", list);
        return "list";
    }

    @ResponseBody
    @RequestMapping("/findById")
    public SeckillProduct findById(@RequestParam("id") Long id) {
        return seckillService.getOneFromDb(id);
    }

    @RequestMapping("/{seckillId}/detail")
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "page/seckill";
        }
        SeckillProduct seckill = seckillService.getOneFromDb(seckillId);
        model.addAttribute("seckill", seckill);
        if (seckill == null) {
            return "list";
        }
        return "detail";
    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }
//
//    @RequestMapping(value = "/{seckillId}/{md5}/execution",
//            method = RequestMethod.POST,
//            produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
//    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
//                                                   @PathVariable("md5") String md5,
//                                                   @RequestParam("money") BigDecimal money,
//                                                   @CookieValue(value = "killPhone", required = false) Long userPhone) {
//        if (userPhone == null) {
//            return new SeckillResult<SeckillExecution>(false, "未注册");
//        }
//        try {
//            SeckillExecution execution = seckillService.executeSeckill(seckillId, money, userPhone, md5);
//            return new SeckillResult<SeckillExecution>(true, execution);
//        } catch (RepeatKillException e) {
//            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
//            return new SeckillResult<SeckillExecution>(true, seckillExecution);
//        } catch (SeckillCloseException e) {
//            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END);
//            return new SeckillResult<SeckillExecution>(true, seckillExecution);
//        } catch (SeckillException e) {
//            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
//            return new SeckillResult<SeckillExecution>(true, seckillExecution);
//        }
//    }
//
//    @ResponseBody
//    @GetMapping(value = "/time/now")
//    public SeckillResult<Long> time() {
//        Date now = new Date();
//        return new SeckillResult(true, now.getTime());
//    }
}
