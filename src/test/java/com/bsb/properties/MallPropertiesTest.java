package com.bsb.properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zeng
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MallPropertiesTest {

    @Autowired
    private MallProperties mallProperties;

    @Test
    public void testProperties() {
        System.out.println(mallProperties.getHost());
    }
}
