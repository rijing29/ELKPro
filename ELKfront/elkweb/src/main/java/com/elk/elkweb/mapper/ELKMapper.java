package com.elk.elkweb.mapper;

import com.elk.elkweb.entity.NodeSoftMap;
import com.elk.elkweb.entity.searchItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
@Mapper
public interface ELKMapper {
//    查询所有信息
    List<NodeSoftMap> queryTableData();
//   删除
    boolean del(@Param("nodeType") String nodeType);
//    新增
    boolean add(@Param("nodeType")String nodeType,
                @Param("nodeId")int nodeId,
                @Param("softName") String softName,
                @Param("workLoad")Long workLoad);
//   判断某个节点是否存在
    int isExit(@Param("nodeType") String nodeTpe,
               @Param("nodeId")int nodeId,
               @Param("softName")String softName);

}
