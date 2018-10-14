package com.bsb.properties;

/**
 * @author zeng on 18-10-14.
 * @version 1.0
 */
public class ScheduleProperties {

    private Integer hour;

    private Long lockTimeOut;

    public Long getLockTimeOut() {
        return lockTimeOut;
    }

    public void setLockTimeOut(Long lockTimeOut) {
        this.lockTimeOut = lockTimeOut;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }
}
