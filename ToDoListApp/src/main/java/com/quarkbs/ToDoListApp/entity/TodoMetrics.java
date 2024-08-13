package com.quarkbs.ToDoListApp.entity;

public class TodoMetrics {
    private Long avgTime;
    private Long avgTimeLow;
    private Long avgTimeMedium;
    private Long avgTimeHigh;

    public Long getAvgTime(){
        return avgTime;
    }

    public void setAvgTime(Long avgTime) {
        this.avgTime = avgTime;
    }
    
    public Long getAvgTimeLow(){
        return avgTimeLow;
    }

    public void setAvgTimeLow(Long avgTimeLow) {
        this.avgTimeLow = avgTimeLow;
    }

    public Long getAvgTimeMedium(){
        return avgTimeMedium;
    }

    public void setAvgTimeMedium(Long avgTimeMedium) {
        this.avgTimeMedium = avgTimeMedium;
    }

    public Long getAvgTimeHigh(){
        return avgTimeHigh;
    }

    public void setAvgTimeHigh(Long avgTimeHigh) {
        this.avgTimeHigh = avgTimeHigh;
    }

}
