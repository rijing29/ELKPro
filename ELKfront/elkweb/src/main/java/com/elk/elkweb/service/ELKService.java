package com.elk.elkweb.service;

import com.elk.elkweb.entity.NodeSoftMap;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ELKService {
//    查询所有table数据
    List<NodeSoftMap> queryTableData();
}
