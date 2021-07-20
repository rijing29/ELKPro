package com.elk.elkweb.entity;

import java.util.Arrays;

public class dataResults {
    private String key[];
    private double value[];
    private double ave[];

    public dataResults() {
    }

    @Override
    public String toString() {
        return "dataResults{" +
                "key=" + Arrays.toString(key) +
                ", value=" + Arrays.toString(value) +
                ", ave=" + Arrays.toString(ave) +
                '}';
    }

    public String[] getKey() {
        return key;
    }

    public void setKey(String[] key) {
        this.key = key;
    }

    public double[] getValue() {
        return value;
    }

    public void setValue(double[] value) {
        this.value = value;
    }

    public double[] getAve() {
        return ave;
    }

    public void setAve(double[] ave) {
        this.ave = ave;
    }

    public dataResults(String[] key, double[] value, double[] ave) {
        this.key = key;
        this.value = value;
        this.ave = ave;
    }
}
