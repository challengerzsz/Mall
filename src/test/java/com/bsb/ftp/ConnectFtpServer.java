package com.bsb.ftp;

import com.bsb.properties.MallProperties;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author zeng
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ConnectFtpServer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MallProperties mallProperties;

    @Test
    public void connect() throws IOException {

        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(mallProperties.getFtp().getServerIp());
        boolean isSuccess = ftpClient.login(mallProperties.getFtp().getUser(), mallProperties.getFtp().getPassword());
        logger.info("正在登录:{}", isSuccess);
    }
}
