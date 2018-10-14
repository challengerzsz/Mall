package com.bsb.task;

import com.bsb.properties.MallProperties;
import com.bsb.web.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zeng on 18-10-13.
 * @version 1.0
 */
@Component
public class CloseOrderTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IOrderService orderService;
    @Autowired
    private MallProperties mallProperties;

    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV1() {

        logger.info("关闭订单定时任务启动");
        Integer hour = mallProperties.getTask().getHour();
        orderService.closeOrder(hour);
        logger.info("关闭订单定时任务结束");
    }
}
