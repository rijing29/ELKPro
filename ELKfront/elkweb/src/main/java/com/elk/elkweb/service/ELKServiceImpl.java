package com.elk.elkweb.service;

import com.elk.elkweb.entity.NodeSoftMap;
import com.elk.elkweb.mapper.ELKMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
