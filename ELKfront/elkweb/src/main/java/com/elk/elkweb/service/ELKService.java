package com.elk.elkweb.service;

import com.elk.elkweb.entity.NodeSoftMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public interface ELKService {
//    查询所有table数据
    List<NodeSoftMap> queryTableData();
//    删除
    boolean del(String nodeType);
//    新增
    boolean add(String nodeType,int nodeId,String softName,Long workLoad);
//    记录是否存在
    int isExits(String nodeType,int nodeId,String softName);

}
