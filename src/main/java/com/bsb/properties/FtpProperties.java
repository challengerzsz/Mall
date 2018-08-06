package com.bsb.properties;

/**
 * @author zeng
 */
public class FtpProperties {


    private String ftp;
    private String serverIp;
    private String user;
    private String password;
    private String serverHttpPrefix;

    public String getFtp() {
        return ftp;
    }

    public void setFtp(String ftp) {
        this.ftp = ftp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerHttpPrefix() {
        return serverHttpPrefix;
    }

    public void setServerHttpPrefix(String serberHttpPrefix) {
        this.serverHttpPrefix = serberHttpPrefix;
    }
}
