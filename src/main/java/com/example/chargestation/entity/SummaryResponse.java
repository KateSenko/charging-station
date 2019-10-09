package com.example.chargestation.entity;

public class SummaryResponse {

    private int totalCount;
    private int startedCount;
    private int stoppedCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getStartedCount() {
        return startedCount;
    }

    public void setStartedCount(int startedCount) {
        this.startedCount = startedCount;
    }

    public int getStoppedCount() {
        return stoppedCount;
    }

    public void setStoppedCount(int stoppedCount) {
        this.stoppedCount = stoppedCount;
    }
}
