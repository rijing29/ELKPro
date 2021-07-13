package com.elk.elkweb.controller;

import com.elk.elkweb.entity.NodeSoftMap;
import com.elk.elkweb.service.ELKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/elk")
public class ELKController {
    @Qualifier("ELKServiceImpl")
    @Autowired
    private ELKService elkService;
//    查询所有table数据
    @RequestMapping(value = "/queryTable",produces = "application/json;charset=utf-8" )
    @ResponseBody
    public List<NodeSoftMap> show(){
        List<NodeSoftMap> nodeSoftMaps = elkService.queryTableData();
        for (int i=0;i<nodeSoftMaps.size();i++){
            System.out.println("从数据库返回来的值：");
            System.out.println(nodeSoftMaps.get(i).toString());
        }
        return nodeSoftMaps;
    }
//    删除
    @ResponseBody
    @RequestMapping(value = "/del",produces = "application/json;charset=utf-8" )
    public void del(@RequestParam("nodeType") String nodeType) {
        System.out.println("从前端传回来的："+nodeType);
        elkService.del(nodeType);
    }
//    新增
    @RequestMapping(value = "/add",produces = "application/json;charset=utf-8" )
    @ResponseBody
    public void add(
                    @RequestParam("nodeType") String nodeType,
                    @RequestParam("softName") String softName,
                    @RequestParam("startValue") int startValue,
                    @RequestParam("stopValue") int stopValue,
                    @RequestParam("workLoad") Long workLoad){
        int count=stopValue-startValue+1;
        for(int i=startValue;i<=stopValue;i++){
            int res = elkService.isExits(nodeType,i,softName);
            System.out.println(res);
            if(res<0){
                System.out.println("未插入成功的id："+i);
            }else{
                System.out.println("可以插入的值:"+i);
                elkService.add(nodeType,i, softName,workLoad);
            }

        }
    }
}
