package com.elk.elkweb.entity;

import java.util.Date;

public class NodeWorkDetail {
    private String nodeType;

    private Integer nodeId;

    private Date time;

    private String userName;

    private Integer workLoad;

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Integer workLoad) {
        this.workLoad = workLoad;
    }
}