package com.bsb.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zeng
 */
@ConfigurationProperties(prefix = "bsb.mall")
public class MallProperties {

    private FtpProperties ftp;

    public FtpProperties getFtp() {
        return ftp;
    }

    public void setFtp(FtpProperties ftp) {
        this.ftp = ftp;
    }
}
