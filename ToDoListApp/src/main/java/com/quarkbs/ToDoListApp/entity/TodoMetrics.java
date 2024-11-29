package com.quarkbs.ToDoListApp.entity;

/**
 * Entity class representing metrics for Todo items.
 */
public class TodoMetrics {
    /**
     * The average elapsed time until completion for all todos.
     */
    private double avgTime;

    /**
     * The average elapsed time until completion for low priority todos.
     */
    private double avgTimeLow;

    /**
     * The average elapsed time until completion for medium priority todos.
     */
    private double avgTimeMedium;

    /**
     * The average elapsed time until completion for high priority todos.
     */
    private double avgTimeHigh;

    // Getters and setters

    public double getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(double avgTime) {
        this.avgTime = avgTime;
    }

    public double getAvgTimeLow() {
        return avgTimeLow;
    }

    public void setAvgTimeLow(double avgTimeLow) {
        this.avgTimeLow = avgTimeLow;
    }

    public double getAvgTimeMedium() {
        return avgTimeMedium;
    }

    public void setAvgTimeMedium(double avgTimeMedium) {
        this.avgTimeMedium = avgTimeMedium;
    }

    public double getAvgTimeHigh() {
        return avgTimeHigh;
    }

    public void setAvgTimeHigh(double avgTimeHigh) {
        this.avgTimeHigh = avgTimeHigh;
    }
}