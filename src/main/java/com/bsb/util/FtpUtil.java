package com.bsb.util;

import com.bsb.properties.FtpProperties;
import com.bsb.properties.MallProperties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author zeng
 */
@Component
public class FtpUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MallProperties mallProperties;

    private String ftpIp;
    private Integer ftpPort;
    private String ftpUser;
    private String ftpPwd;
    private FTPClient ftpClient;

    public void init() {
        this.ftpIp = mallProperties.getFtp().getServerIp();
        this.ftpPort = Integer.valueOf(mallProperties.getFtp().getServerPort());
        this.ftpUser = mallProperties.getFtp().getUser();
        this.ftpPwd = mallProperties.getFtp().getPassword();
    }

    public boolean uploadFile(List<File> fileList) throws IOException {

        this.init();
        logger.info("开始连接ftp");
        boolean result = uploadFile("img", fileList);
        logger.info("开始连接，结束上传，上传结果 {}", result);

        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {

        boolean uploaded = true;
        FileInputStream fis = null;
        //连接FTP
        if (connectServer(this.ftpIp, this.ftpPort, this.ftpUser, this.ftpPwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                //二进制
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                //打开被动模式
                ftpClient.enterLocalPassiveMode();

                for (File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
            } catch (IOException e) {
                uploaded = false;
                logger.error("上传文件异常", e);
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }

        return uploaded;

    }

    private boolean connectServer(String ip, Integer ftpPort, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            ftpClient.setDefaultPort(ftpPort);
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接ftp异常", e);
        }
        return isSuccess;
    }

    public MallProperties getMallProperties() {
        return mallProperties;
    }

    public void setMallProperties(MallProperties mallProperties) {
        this.mallProperties = mallProperties;
    }

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPwd() {
        return ftpPwd;
    }

    public void setFtpPwd(String ftpPwd) {
        this.ftpPwd = ftpPwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
