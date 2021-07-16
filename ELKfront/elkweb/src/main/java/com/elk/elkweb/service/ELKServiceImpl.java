package com.elk.elkweb.service;

import com.elk.elkweb.entity.NodeSoftMap;
import com.elk.elkweb.mapper.ELKMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class ELKServiceImpl implements ELKService{
    @Autowired
    private ELKMapper elkMapper;
//    查询所有table
    public List<NodeSoftMap> queryTableData() {
        List<NodeSoftMap> nodeSoftMaps = elkMapper.queryTableData();
        return nodeSoftMaps;
    }

//    删除
    public boolean del(String nodeType) {
        boolean res = elkMapper.del(nodeType);
        if(res)
            return true;
        else
            return false;
    }

//    新增
    public boolean add(String nodeType,int nodeId,String softName,Long workLoad) {
        boolean res = elkMapper.add(nodeType,nodeId,softName,workLoad);
        if(res)
            return true;
        else
            return false;
    }

//    判断记录是否存在
//    不为0就是存在
    public int isExits(String nodeType, int nodeId, String softName) {
        int i = elkMapper.isExit(nodeType, nodeId, softName);
        System.out.println("elkservice中的是否存在:"+i);
        if(i==0)
            return 0;
        else
            return -1;
    }


}
