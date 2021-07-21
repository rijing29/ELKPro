package com.elk.elkweb.entity;

public class TableExport {
    private String date;
    private double efficiency;

    public TableExport() {
    }

    @Override
    public String toString() {
        return "TableExport{" +
                "date='" + date + '\'' +
                ", efficiency=" + efficiency +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public TableExport(String date, double efficiency) {
        this.date = date;
        this.efficiency = efficiency;
    }
}
