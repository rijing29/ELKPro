package com.elk.elkweb.controller;

import com.elk.elkweb.entity.NodeSoftMap;
import com.elk.elkweb.service.ELKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/elk")
public class ELKController {
    @Qualifier("ELKServiceImpl")
    @Autowired
    private ELKService elkService;
//    查询所有table数据
    @ResponseBody
    @RequestMapping(value = "/queryTable",produces = "application/json;charset=utf-8" )
    public List<NodeSoftMap> show(){
        List<NodeSoftMap> nodeSoftMaps = elkService.queryTableData();
        for (int i=0;i<nodeSoftMaps.size();i++){
            System.out.println("从数据库返回来的值：");
            System.out.println(nodeSoftMaps.get(i).toString());
        }
        return nodeSoftMaps;
    }
}
