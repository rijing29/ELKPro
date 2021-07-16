package com.elk.elkweb.controller;

import com.elk.elkweb.entity.NodeSoftMap;
import com.elk.elkweb.entity.dataResults;
import com.elk.elkweb.service.ELKService;
import com.elk.elkweb.service.EfficiService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/elk")
public class ELKController {
    @Qualifier("ELKServiceImpl")
    @Autowired
    private ELKService elkService;
    @Autowired
    private EfficiService efficiService;
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

    /**
    * Description:
    * date: 2021/7/15 14:40
    * @author: whj
    * @method:软件名日效率
    */
    @RequestMapping(value = "/calSoftName",produces = "application/json;charset=utf-8" )
    @ResponseBody
    public dataResults softNameEfficiency(@RequestParam("softName") String softName,
                                   @RequestParam("startTime") String startTime,
                                   @RequestParam("stopTime") String stopTime) throws ParseException {
        //        所选总天数（分母）
        float day = calculateTimeGapDay(startTime, stopTime);
        HashMap<Integer, Double> EfficicencyMap = new HashMap<Integer, Double>();
        Date addOneDay=null;
        addOneDay=transferDate(startTime);
        for(int i=0;i<day;i++){
    //        startTime
            String dayStopTime = dayStopTime(transferString(addOneDay));//2020/7/14 23:59:59
            String dayStart=transferString(addOneDay);//2020/7/14 7:45:26
            String dayStop=dayStopTime;//2020/7/14 23:59:59
            NodeSoftMap nodeSoftMap = new NodeSoftMap("",null,softName);
    //        各节点工作量之和(分母)
            int sumNodeWorkLoad = efficiService.sumWorkLoad(nodeSoftMap);
    //        工作总量（分子）
            int sumTotalWorkLoad = efficiService.softNameEfficiency(softName, dayStart, dayStop);
    //        日效率
            double oneDayEfficiency = calOneDayEfficiency(sumTotalWorkLoad, sumNodeWorkLoad);
            EfficicencyMap.put(i,oneDayEfficiency);
            //        日期加1
            addOneDay  = addOneDay(addOneDay);//+1
        }
        Iterator it=EfficicencyMap.values().iterator();
        while(it.hasNext()) {
            System.out.print("今天的日效率为："+it.next());
        }
        dataResults dataResults = new dataResults();
        int i=0;
        for (Map.Entry<Integer, Double> entry : EfficicencyMap.entrySet()) {
            i++;
        }
        int key[] = new int[i];
        double value[] = new double[i];
        int j=0;
        for (Map.Entry<Integer, Double> entry : EfficicencyMap.entrySet()) {
            int num = entry.getKey();
            double count = entry.getValue();
            System.out.println(num+"---"+count);
            key[j]=num+1;
            value[j]=count;
            j++;
        }
        dataResults.setKey(key);
        dataResults.setValue(value);
        System.out.println(dataResults);
        return dataResults;
    }

    /**
    * Description:
    * date: 2021/7/15 16:01
    * @author: whj
    * @method:nodeType日效率
    */
    @RequestMapping(value = "/calNodeType",produces = "application/json;charset=utf-8" )
    @ResponseBody
    public dataResults nodeTypeEfficiency(@Param("nodeType")String nodeType,
                                   @Param("nodeId")String nodeId,
                                   @Param("startTime")String startTime,
                                   @Param("stopTime")String stopTime) throws ParseException {
        //        所选总天数（分母）
        float day = calculateTimeGapDay(startTime, stopTime);
        HashMap<Integer, Double> EfficicencyMap = new HashMap<Integer, Double>();
        Date addOneDay=null;
        addOneDay=transferDate(startTime);
//        总个数
        double total=0;
        for(int i=0;i<day;i++){
            //        startTime
            String dayStopTime = dayStopTime(transferString(addOneDay));//2020/7/14 23:59:59
            String dayStart=transferString(addOneDay);//2020/7/14 7:45:26
            String dayStop=dayStopTime;//2020/7/14 23:59:59
            NodeSoftMap nodeSoftMap = new NodeSoftMap(nodeType,null,nodeId);
            //        各节点工作量之和(分母)
            int sumNodeWorkLoad = efficiService.sumNodeTypeWorkLoad(nodeSoftMap);
            //        工作总量（分子）
            int sumTotalWorkLoad = efficiService.nodeTypeEfficiency(nodeType,nodeId,startTime,stopTime);
            //        日效率
            double oneDayEfficiency = calOneDayEfficiency(sumTotalWorkLoad, sumNodeWorkLoad);
            total=total+oneDayEfficiency;

            EfficicencyMap.put(i,oneDayEfficiency);
            //        日期加1
            addOneDay  = addOneDay(addOneDay);//+1
        }
        double aveEffici=calAveEfficiency(startTime,stopTime,total);
        dataResults dataResults = new dataResults();
        System.out.println("----------------------------平均效率-----------------------"+aveEffici);
        double totalArr[]=new double[1];
        totalArr[0]=aveEffici;
        dataResults.setAve(totalArr);
        Iterator it=EfficicencyMap.values().iterator();
        while(it.hasNext()) {
            System.out.print("今天的日效率为："+it.next());
        }


        int i=0;
        for (Map.Entry<Integer, Double> entry : EfficicencyMap.entrySet()) {
            i++;
        }
        int key[] = new int[i];
        double value[] = new double[i];
        int j=0;
        for (Map.Entry<Integer, Double> entry : EfficicencyMap.entrySet()) {
            int num = entry.getKey();
            double count = entry.getValue();
            System.out.println(num+"---"+count);
            key[j]=num+1;
            value[j]=count;
            j++;
        }
        dataResults.setKey(key);
        dataResults.setValue(value);
        System.out.println(dataResults);
        return dataResults;
    }


    /**
    * Description:
    * date: 2021/7/15 15:06
    * @author: whj
    * @method:日期+1天
    */
    public Date addOneDay(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        return date;
    }
    /**
    * Description:
    * date: 2021/7/15 14:50
    * @author: whj
    * @method:stopTime转dayStopTime
    */
    public String dayStopTime(String s){
        StringBuilder builder = new StringBuilder(s);
        builder.replace(10,s.length(),"23:59:59");
        String dayStopTime = builder.toString();
        return dayStopTime;
    }
    /**
    * Description:
    * date: 2021/7/15 14:30
    * @author: whj
    * @method:日效率计算
    */
    public static double calOneDayEfficiency(int sumTotalWorkLoad,int sumNodeWorkLoad){
        double fenMu=24*2*sumNodeWorkLoad;
        double res=sumTotalWorkLoad/fenMu;
        return res;
    }
    /**
    * Description:
    * date: 2021/7/15 9:46
    * @author: whj
    * @method:比较两个时间相差天数
    */
    public static float calculateTimeGapDay(String time1, String time2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss");
        float day = 0;
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
            long millisecond = date2.getTime() - date1.getTime();
            day = millisecond / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (day);
    }
    /**
    * Description:
    * date: 2021/7/16 14:46
    * @author: whj
    * @method:计算平均效率
    */
    public static double calAveEfficiency(String time1,String time2,double total){
        float day = calculateTimeGapDay(time1, time2);
        return total/day;
    }

    /**
    * Description:
    * date: 2021/7/15 9:57
    * @author: whj
    * @method:string转date
    */
    public static Date transferDate(String Time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = sdf.parse(Time);
        return date;
    }
    /**
    * Description:
    * date: 2021/7/15 15:20
    * @author: whj
    * @method:date转string
    */
    public static String transferString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String res = formatter.format(date);
        return res;
    }

}
