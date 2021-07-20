package com.elk.elkweb.controller;

import com.elk.elkweb.service.ELKService;
import com.elk.elkweb.service.EfficiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/elk")
public class ELKTableController {
    @Autowired
    private EfficiService efficiService;
    /**
    * Description:
    * date: 2021/7/20 9:51
    * @author: whj
    * @method:输入月份查出该月平均效率
    */
    public void searchNTE(@RequestParam("softName") String softName,
                          @RequestParam("startTime") String startTime,
                          @RequestParam("stopTime") String stopTime){

    }

}
