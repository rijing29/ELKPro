package com.elk.elkweb.controller;

import com.alibaba.fastjson.JSON;
import com.elk.elkweb.entity.NodeSoftMap;
import com.elk.elkweb.service.EfficiService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static com.elk.elkweb.controller.ELKController.calOneDayEfficiency;

@Controller
@RequestMapping("/elk")
public class ELKTableController {
    @Autowired
    private EfficiService efficiService;
    /**
    * Description:
    * date: 2021/7/20 9:51
    * @author: whj
    * @method:查出所有的softName
    */
    @RequestMapping(value = "/searchSoftName",produces = "application/json;charset=utf-8" )
    @ResponseBody
    public List searchSoftName(){
        List softname = efficiService.getSoftName();
        String softName = JSON.toJSONString(softname);
        System.out.println("遍历出来的所有softName:"+softName);
//        测试
        String a[] ={"str1","str2"};
        System.out.println(a.length);
        for(int i=0;i<a.length;i++) {
            a[i] = " ";
        }
        for(int i=0;i<a.length;i++)
            System.out.println(a[i]);

        return softname;
    }
    /**
    * Description:
    * date: 2021/7/21 20:17
    * @author: whj
    * @method:按年月查出所有软件效率
    */
    @RequestMapping(value = "/searchSoftNameEfficiency",produces = "application/json;charset=utf-8" )
    @ResponseBody
    public JSONArray searchSoftNameEfficiency(@Param("year") String year,
                                         @Param("month") String month) throws ParseException {
        String[] ave = collectAveEfficiency(year, month);
        List softName = searchSoftName();
        JSONArray combineJSON = combineJSON(ave, softName, year, month);
        return combineJSON;
    }
    /**
    * Description:
    * date: 2021/7/23 10:32
    * @author: whj
    * @method:按年月查出所有节点效率
    */
    @ResponseBody
    @RequestMapping(value = "/searchNodeTypeEfficiency",produces = "application/json;charset=utf-8")
    public JSONArray searchNodeTypeEfficiency(@Param("softName") String softName,
                                         @Param("year") String year,
                                         @Param("month") String month){
        List<Map<String, Object>> nodeInfo = efficiService.getNodeInfo(softName);
        String[] aveNodeType = calNodeTypeEfficiency(softName, year, month);
        JSONArray nodeTypeJSON = combineNodeTypeJSON(nodeInfo, year, month, aveNodeType);
        System.out.println(nodeTypeJSON);
        return nodeTypeJSON;
    }
    /**
    * Description:
    * date: 2021/7/23 15:30
    * @author: whj
    * @method:开始遍历nodeType和nodeId进行效率计算
    */
    public String[] calNodeTypeEfficiency(String softName,String year,String month){
        List<Map<String, Object>> nodeInfo = efficiService.getNodeInfo(softName);
        for(int i=0;i<nodeInfo.size();i++) {
            System.out.println(nodeInfo.get(i).get("NODE_TYPE"));
            System.out.println(nodeInfo.get(i).get("NODE_ID"));
        }
//        year和month的时间转换
        String[] time = new String[2];
        String[] Time = formatMonth(year, month, time);
        String startTime=Time[0];
        String stopTime=Time[1];
        int len=nodeInfo.size();
        String[] aveNodeType=new String[len];
        for(int i=0;i<nodeInfo.size();i++){
            String nodeType= (String) nodeInfo.get(i).get("NODE_TYPE");
            String nodeId= nodeInfo.get(i).get("NODE_ID").toString();
            String aveEfficiency = calNodeTypeAveEfficiency(nodeInfo, nodeType, nodeId, startTime, stopTime);
            aveNodeType[i]=aveEfficiency;
        }
        for(int i=0;i<aveNodeType.length;i++) {
            System.out.println(aveNodeType[i]);
        }
        return aveNodeType;
    }

    /**
    * Description:
    * date: 2021/7/23 15:58
    * @author: whj
    * @method:nodeType计算效率
    */
    public String calNodeTypeAveEfficiency(List<Map<String, Object>> nodeInfo,String nodeType,String nodeId,String startTime,String stopTime){
        int sumTotalWorkLoad = efficiService.nodeTypeEfficiency(nodeType, nodeId, startTime, stopTime);
        NodeSoftMap nodeSoftMap = new NodeSoftMap(nodeType,null,nodeId);
        int sumNodeWorkLoad = efficiService.sumNodeTypeWorkLoad(nodeSoftMap);
        double aveEfficiency = calOneDayEfficiency(sumTotalWorkLoad, sumNodeWorkLoad);
        double aveEffici=calAveEfficiency(aveEfficiency);
        return aveTransferPercen(aveEffici);
    }
    /**
    * Description:
    * date: 2021/7/23 16:25
    * @author: whj
    * @method:nodeType合并三个json成为一个jsonObject
    */
    public JSONArray combineNodeTypeJSON(List<Map<String, Object>> nodeInfo,String year,String month,String[] aveNodeType){
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<nodeInfo.size();i++){
            JSONObject jo=new JSONObject();
            jo.put("nodeType",nodeInfo.get(i).get("NODE_TYPE").toString());
            jo.put("nodeId",nodeInfo.get(i).get("NODE_ID").toString());
            jo.put("time",year+"年"+month+"月");
            jo.put("efficiency",aveNodeType[i]);
            jsonArray.add(jo);
        }
        return jsonArray;
    }
    /**
    * Description:
    * date: 2021/7/22 15:32
    * @author: whj
    * @method:softName合并三个json成为一个jsonObject
    */
    public JSONArray combineJSON(String[] ave,List softName,String year,String month){
        JSONArray json = new JSONArray();
        for(int i=0;i<4;i++){
            JSONObject jo=new JSONObject();
            jo.put("softName",softName.get(i));
            jo.put("time",year+"年"+month+"月");
            jo.put("efficiency",ave[i]);
            json.add(jo);
        }
        return json;
    }

    /**
    * Description:
    * date: 2021/7/22 14:28
    * @author: whj
    * @method:收集软件的平均效率
    */
    public String[] collectAveEfficiency(String year, String month) throws ParseException {
        String[] ave = new String[4];
        String[] time = new String[2];
        String[] Time = formatMonth(year, month, time);
        String startTime=Time[0];
        String stopTime=Time[1];
        String[] softName={"Pardiam","GEOEASTDL","Geoeast","WCC"};
        for(int i=0;i<softName.length;i++){
            Double aveEffici = calSoftNameAveEffici(startTime, stopTime, softName[i]);
            System.out.println("每天的效率为："+aveEffici);
            ave[i]=aveTransferPercen(aveEffici);
        }
        return ave;
    }
    /**
    * Description:
    * date: 2021/7/22 16:08
    * @author: whj
    * @method:把效率换成百分比
    */
    public String aveTransferPercen(Double aveEffici){
            System.out.println(aveEffici);
            NumberFormat nFromat = NumberFormat.getPercentInstance();
            String avePercent = nFromat.format(aveEffici);
            return avePercent;
    }
    /**
    * Description:
    * date: 2021/7/21 20:31
    * @author: whj
    * @method:月份判断
    */
    public String[] formatMonth(String year,String month,String[] time){
        String startTime=year;
        String stopTime=year;
        if(month.equals("1")){startTime=year+"/1/1 00:00:00";stopTime=year+"/1/31 23:59:59";
        }else if(month.equals("2")){startTime=year+"/2/1 00:00:00";stopTime=year+"/2/28 23:59:59";
        }else if(month.equals("3")){startTime=year+"/3/1 00:00:00";stopTime=year+"/3/31 23:59:59";
        }else if(month.equals("4")){startTime=year+"/4/1 00:00:00";stopTime=year+"/4/30 23:59:59";
        }else if(month.equals("5")){startTime=year+"/5/1 00:00:00";stopTime=year+"/5/31 23:59:59";
        }else if(month.equals("6")){startTime=year+"/6/1 00:00:00";stopTime=year+"/6/30 23:59:59";
        }else if(month.equals("7")){startTime=year+"/7/1 00:00:00";stopTime=year+"/7/31 23:59:59";
        }else if(month.equals("8")){startTime=year+"/8/1 00:00:00";stopTime=year+"/8/31 23:59:59";
        }else if(month.equals("9")){startTime=year+"/9/1 00:00:00";stopTime=year+"/9/30 23:59:59";
        }else if(month.equals("10")){startTime=year+"/10/1 00:00:00";stopTime=year+"/10/31 23:59:59";
        }else if(month.equals("11")){startTime=year+"/11/1 00:00:00";stopTime=year+"/11/30 23:59:59";
        }else if(month.equals("12")){startTime=year+"/12/1 00:00:00";stopTime=year+"/12/31 23:59:59";
        }
        time[0]=startTime;
        time[1]=stopTime;
        return time;
    }
    /**
     * Description:
     * date: 2021/7/20 10:38
     * @author: whj
     * @method:softName计算平均效率总和
     */
    public double calSoftNameAveEffici(String startTime,String stopTime,String softName) throws ParseException {
        //        startTime
        NodeSoftMap nodeSoftMap = new NodeSoftMap("",null,softName);
        //        各节点工作量之和(分母)
        int sumNodeWorkLoad = efficiService.sumWorkLoad(nodeSoftMap);
        //        工作总量（分子）
        System.out.println("即将计算了的softName"+softName);
        int sumTotalWorkLoad = efficiService.softNameEfficiency(softName, startTime, stopTime);
        double aveEfficiency = calOneDayEfficiency(sumTotalWorkLoad, sumNodeWorkLoad);
        //        月平均效率
        double aveEffici=calAveEfficiency(aveEfficiency);
        return aveEffici;
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
    /**
     * Description:
     * date: 2021/7/15 14:50
     * @author: whj
     * @method:stopTime转dayStopTime
     */
    public String dayStopTime(String s){
        StringBuilder builder = new StringBuilder(s);
        builder.replace(10,s.length()," 23:59:59");
        String dayStopTime = builder.toString();
        return dayStopTime;
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
     * date: 2021/7/16 14:46
     * @author: whj
     * @method:计算平均效率
     */
    public static double calAveEfficiency(double total){
        float day = 30;
        return total/day;
    }
}
