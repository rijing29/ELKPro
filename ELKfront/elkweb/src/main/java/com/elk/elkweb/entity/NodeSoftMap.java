package com.elk.elkweb.entity;

public class NodeSoftMap {
    private String nodeType;

    private Integer nodeId;

    private String softName;

    private Long workLoad;

    public NodeSoftMap() {
    }

    public NodeSoftMap(String nodeType, Integer nodeId, String softName) {
        this.nodeType = nodeType;
        this.nodeId = nodeId;
        this.softName = softName;
    }

    public NodeSoftMap(String nodeType, Integer nodeId, String softName, Long workLoad) {
        this.nodeType = nodeType;
        this.nodeId = nodeId;
        this.softName = softName;
        this.workLoad = workLoad;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType == null ? null : nodeType.trim();
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public String getSoftName() {
        return softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName == null ? null : softName.trim();
    }

    public Long getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Long workLoad) {
        this.workLoad = workLoad;
    }

    @Override
    public String toString() {
        return "NodeSoftMap{" +
                "nodeType='" + nodeType + '\'' +
                ", nodeId=" + nodeId +
                ", softName='" + softName + '\'' +
                ", workLoad=" + workLoad +
                '}';
    }
}