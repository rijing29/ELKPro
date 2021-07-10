package com.elk.elkweb.mapper;

import com.elk.elkweb.entity.NodeSoftMap;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface ELKMapper {
//    查询所有信息
    List<NodeSoftMap> queryTableData();
}
