package com.kunaldhongadi.milkydiary;

public class Bill {

    private String month;
    private int count;
    private int totalCost;

    public Bill(String month, int count, int totalCost) {
        this.month = month;
        this.count = count;
        this.totalCost = totalCost;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
}
