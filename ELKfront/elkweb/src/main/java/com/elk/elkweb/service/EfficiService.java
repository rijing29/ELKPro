package com.elk.elkweb.service;

import com.elk.elkweb.entity.NodeSoftMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public interface EfficiService {
    //    效率
    int softNameEfficiency(String softName, String startTime, String stopTime);
    // 各节点工作量之和
    int sumWorkLoad(NodeSoftMap nodeSoftMap);
//    nodeType效率
    int nodeTypeEfficiency(String nodeType,String nodeId,String startTime,String stopTime);
//    nodeType各节点工作量之和
    int sumNodeTypeWorkLoad(NodeSoftMap nodeSoftMap);
//取出所有的softName
    List getSoftName();
//    根据softName取出nodeType和nodeId
    List<Map< String, Object >> getNodeInfo(String softName);
}
