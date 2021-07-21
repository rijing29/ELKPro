package com.elk.elkweb.controller;
import com.elk.elkweb.entity.TableExport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
     * @method:softName日效率
     */
    @RequestMapping(value = "/calSoftName",produces = "application/json;charset=utf-8" )
    @ResponseBody
    public dataResults softNameEfficiency(@RequestParam("softName") String softName,
                                          @RequestParam("startTime") String startTime,
                                          @RequestParam("stopTime") String stopTime) throws ParseException {
        float day = calculateTimeGapDay(startTime, stopTime);
        LinkedHashMap<String, Double> EfficiencyMap = new LinkedHashMap<String, Double>();
        double aveEffici = calNodeTypeAveEffici(startTime, stopTime, day, softName, EfficiencyMap);
        dataResults dataResults = new dataResults();
        System.out.println("----------------------------平均效率-----------------------"+aveEffici);
        return res(dataResults, aveEffici, EfficiencyMap);
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
        float day = calculateTimeGapDay(startTime, stopTime);
        LinkedHashMap<String, Double> EfficiencyMap = new LinkedHashMap<String, Double>();
        double aveEffici = calSoftNameAveEffici(startTime, stopTime, day, nodeType, nodeId, EfficiencyMap);
        dataResults dataResults = new dataResults();
        System.out.println("----------------------------平均效率-----------------------"+aveEffici);
        return res(dataResults, aveEffici, EfficiencyMap);
    }

    /**
     * Description:
     * date: 2021/7/21 11:05
     * @author: whj
     * @method:ShowTable里的方法softName
     */
    @RequestMapping(value = "/tableSoftName",produces = "application/json;charset=utf-8" )
    @ResponseBody
    public JSONArray nodeType(@RequestParam("softName") String softName,
                              @RequestParam("startTime") String startTime,
                              @RequestParam("stopTime") String stopTime) throws ParseException {
        float day = calculateTimeGapDay(startTime, stopTime);
        LinkedHashMap<String, Double> EfficiencyMap = new LinkedHashMap<String, Double>();
        JSONArray jsonArray = jsonArraySoftNameAveEffici(startTime, stopTime, day, softName, EfficiencyMap);
        return jsonArray;
    }
    /**
     * Description:
     * date: 2021/7/21 11:16
     * @author: whj
     * @method:ShowTable里的方法nodeType
     */
    @RequestMapping(value = "/tableNodeType",produces = "application/json;charset=utf-8" )
    @ResponseBody
    public JSONArray nodeType(@Param("nodeType")String nodeType,
                              @Param("nodeId")String nodeId,
                              @Param("startTime")String startTime,
                              @Param("stopTime")String stopTime) throws ParseException {
        float day = calculateTimeGapDay(startTime, stopTime);
        LinkedHashMap<String, Double> EfficiencyMap = new LinkedHashMap<String, Double>();
        JSONArray jsonArray = jsonArrayNodeTypeAveEffici(startTime, stopTime, day, nodeType, nodeId, EfficiencyMap);
        return jsonArray;
    }



    /**
     * Description:
     * date: 2021/7/20 10:09
     * @author: whj
     * @method:nodeType计算平均效率总和
     */
    public double calSoftNameAveEffici(String startTime,String stopTime,float day,String nodeType,String nodeId,LinkedHashMap<String, Double> EfficiencyMap) throws ParseException {
        Date addOneDay=null;
        addOneDay=transferDate(startTime);
        double total=0;
//        定义一个json数组对象
        JSONArray array1 = null;
        JSONArray array2 = null;
        for(int i=0;i<day;i++){
            //        startTime
            String dayStopTime = dayStopTime(transferString(addOneDay));//2020/7/14 23:59:59
            String dayStart=transferString(addOneDay);//2020/7/14 7:45:26
            String dayStop=dayStopTime;//2020/7/14 23:59:59
            NodeSoftMap nodeSoftMap = new NodeSoftMap(nodeType,null,nodeId);
            //        各节点工作量之和(分母)
            int sumNodeWorkLoad = efficiService.sumNodeTypeWorkLoad(nodeSoftMap);
            //        工作总量（分子）
            int sumTotalWorkLoad = efficiService.nodeTypeEfficiency(nodeType,nodeId,dayStart,dayStop);
            //        日效率
            double oneDayEfficiency = calOneDayEfficiency(sumTotalWorkLoad, sumNodeWorkLoad);
            total=total+oneDayEfficiency;
            String yAxis = splitStartTime(dayStart);
            EfficiencyMap.put(yAxis,oneDayEfficiency);

            //        日期加1
            addOneDay  = addOneDay(addOneDay);//+1
        }
        JSONArray jsonArray = joinJSON(EfficiencyMap);
        System.out.println("json字符串："+jsonArray);
        double aveEffici=calAveEfficiency(startTime,stopTime,total);
        return aveEffici;
    }
    /**
    * Description:
    * date: 2021/7/20 15:00
    * @author: whj
    * @method:切割字符串切掉时分秒
    */
    public String splitStartTime(String startTime){
        int i = startTime.indexOf(" ");
        System.out.println(i);
        startTime=startTime.substring(0,i);
        return startTime;
    }

    /**
     * Description:
     * date: 2021/7/20 10:38
     * @author: whj
     * @method:softName计算平均效率
     */
    public double calNodeTypeAveEffici(String startTime,String stopTime,float day,String softName,LinkedHashMap<String, Double> EfficicencyMap) throws ParseException {
        //        加一天的变量
        Date addOneDay=null;
        addOneDay=transferDate(startTime);
        //        总个数
        double total=0;
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
            total=total+oneDayEfficiency;
            String yAxis = splitStartTime(dayStart);
            EfficicencyMap.put(yAxis,oneDayEfficiency);
            //        日期加1
            addOneDay  = addOneDay(addOneDay);//+1
        }
        JSONArray jsonArray = joinJSON(EfficicencyMap);
        System.out.println("json字符串："+jsonArray);
        double aveEffici=calAveEfficiency(startTime,stopTime,total);
        return aveEffici;
    }

    /**
    * Description:
    * date: 2021/7/21 10:34
    * @author: whj
    * @method:softName返回JSONArray
    */
    public JSONArray jsonArraySoftNameAveEffici(String startTime,String stopTime,float day,String softName,LinkedHashMap<String, Double> EfficicencyMap) throws ParseException {
        //        加一天的变量
        Date addOneDay=null;
        addOneDay=transferDate(startTime);
        //        总个数
        double total=0;
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
            total=total+oneDayEfficiency;
            String yAxis = splitStartTime(dayStart);
            EfficicencyMap.put(yAxis,oneDayEfficiency);
            //        日期加1
            addOneDay  = addOneDay(addOneDay);//+1
        }
        return joinJSON(EfficicencyMap);
    }
    /**
    * Description:
    * date: 2021/7/21 11:18
    * @author: whj
    * @method:nodeType返回jsonArray
    */
    public JSONArray jsonArrayNodeTypeAveEffici(String startTime,String stopTime,float day,String nodeType,String nodeId,LinkedHashMap<String, Double> EfficiencyMap) throws ParseException {
        Date addOneDay=null;
        addOneDay=transferDate(startTime);
        double total=0;
//        定义一个json数组对象
        JSONArray array1 = null;
        JSONArray array2 = null;
        for(int i=0;i<day;i++){
            //        startTime
            String dayStopTime = dayStopTime(transferString(addOneDay));//2020/7/14 23:59:59
            String dayStart=transferString(addOneDay);//2020/7/14 7:45:26
            String dayStop=dayStopTime;//2020/7/14 23:59:59
            NodeSoftMap nodeSoftMap = new NodeSoftMap(nodeType,null,nodeId);
            //        各节点工作量之和(分母)
            int sumNodeWorkLoad = efficiService.sumNodeTypeWorkLoad(nodeSoftMap);
            //        工作总量（分子）
            int sumTotalWorkLoad = efficiService.nodeTypeEfficiency(nodeType,nodeId,dayStart,dayStop);
            //        日效率
            double oneDayEfficiency = calOneDayEfficiency(sumTotalWorkLoad, sumNodeWorkLoad);
            total=total+oneDayEfficiency;
            String yAxis = splitStartTime(dayStart);
            EfficiencyMap.put(yAxis,oneDayEfficiency);

            //        日期加1
            addOneDay  = addOneDay(addOneDay);//+1
        }
        return joinJSON(EfficiencyMap);
    }



    /**
    * Description:
    * date: 2021/7/21 10:17
    * @author: whj
    * @method:向jsonArray中添加jsonObject
    */
    public JSONArray joinJSON(LinkedHashMap<String, Double> EfficicencyMap){
        JSONArray array = new JSONArray();
        Iterator iter = EfficicencyMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            JSONObject object = new JSONObject();
            object.put("time",key);
            object.put("efficiency",val);
            array.add(object);
        }
        return array;
    }


    /**
     * Description:
     * date: 2021/7/20 10:17
     * @author: whj
     * @method:把key value ave三个数组放入dataRestults
     */
    public dataResults res(dataResults dataResults,double aveEffici,LinkedHashMap<String, Double> EfficiencyMap){
        double totalArr[]=new double[1];
        totalArr[0]=aveEffici;
        dataResults.setAve(totalArr);
        Iterator it=EfficiencyMap.values().iterator();
        while(it.hasNext()) {
            System.out.print("今天的日效率为："+it.next());
        }
        int i=0;
        for (Map.Entry<String, Double> entry : EfficiencyMap.entrySet()) {
            i++;
        }
        String key[] = new String[i];
        double value[] = new double[i];
        int j=0;
        for (Map.Entry<String, Double> entry : EfficiencyMap.entrySet()) {
            String num = entry.getKey();
            double count = entry.getValue();
            System.out.println(num+"---"+count);
            key[j]=num;
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
