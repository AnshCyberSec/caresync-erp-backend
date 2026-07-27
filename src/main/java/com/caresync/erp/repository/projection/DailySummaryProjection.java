package com.caresync.erp.repository.projection;

public class DailySummaryProjection {

    private final Integer day;
    private final Long count;

    public DailySummaryProjection(Integer day, Long count) {
        this.day = day;
        this.count = count;
    }

    public Integer getDay() { return day; }
    public Long getCount() { return count; }
}