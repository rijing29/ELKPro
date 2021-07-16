package com.elk.elkweb.entity;

import java.util.Date;

public class searchItem {
    private String softName;
    private String nodeType;
    private int NodeId;
    private Date start;
    private Date stop;

    public searchItem() {
    }

    @Override
    public String toString() {
        return "searchItem{" +
                "softName='" + softName + '\'' +
                ", nodeType='" + nodeType + '\'' +
                ", NodeId=" + NodeId +
                ", start=" + start +
                ", stop=" + stop +
                '}';
    }

    public String getSoftName() {
        return softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public int getNodeId() {
        return NodeId;
    }

    public void setNodeId(int nodeId) {
        NodeId = nodeId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStop() {
        return stop;
    }

    public void setStop(Date stop) {
        this.stop = stop;
    }

    public searchItem(String softName, String nodeType, int nodeId, Date start, Date stop) {
        this.softName = softName;
        this.nodeType = nodeType;
        NodeId = nodeId;
        this.start = start;
        this.stop = stop;
    }
}
