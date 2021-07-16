package com.elk.elkweb.service;

import com.elk.elkweb.entity.NodeSoftMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
}
