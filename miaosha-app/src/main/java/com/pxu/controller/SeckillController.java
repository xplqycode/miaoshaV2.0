package com.pxu.controller;

import com.pxu.domain.SeckillProduct;
import com.pxu.dto.Exposer;
import com.pxu.dto.SeckillExecution;
import com.pxu.enums.SeckillStateEnum;
import com.pxu.exception.RepeatKillException;
import com.pxu.exception.SeckillCloseException;
import com.pxu.exception.SeckillException;
import com.pxu.vo.SeckillResultVo;
import com.pxu.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public String findSeckillList(Model model, HttpServletRequest request) {
        List<SeckillProduct> list = seckillService.getAllFromDb();
        model.addAttribute("list", list);
        return "list";
    }

    @ResponseBody
    @RequestMapping("/findById")
    public SeckillProduct findById(@RequestParam(value = "id", required = true) Long id) {
        return seckillService.findSeckillProduct(id);
    }

    @RequestMapping("/{seckillId}/detail")
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        SeckillProduct seckill = seckillService.findSeckillProduct(seckillId);
        model.addAttribute("seckill", seckill);
        if (seckill == null) {
            return "list";
        }
        return "detail";
    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public SeckillResultVo<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResultVo<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResultVo<Exposer>(true, exposer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = new SeckillResultVo<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public SeckillResultVo<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                     @PathVariable("md5") String md5,
                                                     @CookieValue(value = "killPhone", required = false) Long userPhone) {
        if (userPhone == null) {
            return new SeckillResultVo<SeckillExecution>(false, "未注册");
        }
        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            return new SeckillResultVo<SeckillExecution>(true, execution);
        } catch (RepeatKillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResultVo<SeckillExecution>(true, seckillExecution);
        } catch (SeckillCloseException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResultVo<SeckillExecution>(true, seckillExecution);
        } catch (SeckillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResultVo<SeckillExecution>(true, seckillExecution);
        }
    }

    @ResponseBody
    @GetMapping(value = "/time/now")
    public SeckillResultVo<Long> time() {
        Date now = new Date();
        return new SeckillResultVo(true, now.getTime());
    }
}
