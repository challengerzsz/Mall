package com.bsb.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zeng
 */
@ConfigurationProperties(prefix = "bsb.mall")
public class MallProperties {

    private FtpProperties ftp;

    private AliPayProperties aliPay;

    private ScheduleProperties task;

    public ScheduleProperties getTask() {
        return task;
    }

    public void setTask(ScheduleProperties task) {
        this.task = task;
    }

    public AliPayProperties getAliPay() {
        return aliPay;
    }

    public void setAliPay(AliPayProperties aliPay) {
        this.aliPay = aliPay;
    }

    public FtpProperties getFtp() {
        return ftp;
    }

    public void setFtp(FtpProperties ftp) {
        this.ftp = ftp;
    }
}
