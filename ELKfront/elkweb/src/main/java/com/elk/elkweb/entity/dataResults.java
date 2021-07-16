package com.elk.elkweb.entity;

import java.util.Arrays;

public class dataResults {
    private int key[];
    private double value[];

    public dataResults() {
    }

    public int[] getKey() {
        return key;
    }

    public void setKey(int[] key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "dataResults{" +
                "key=" + Arrays.toString(key) +
                ", value=" + Arrays.toString(value) +
                '}';
    }

    public double[] getValue() {
        return value;
    }

    public void setValue(double[] value) {
        this.value = value;
    }

    public dataResults(int[] key, double[] value) {
        this.key = key;
        this.value = value;
    }
}
