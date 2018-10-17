package com.bsb.task;

import com.bsb.common.Const;
import com.bsb.common.RedissonManager;
import com.bsb.properties.MallProperties;
import com.bsb.util.RedisUtil;
import com.bsb.web.service.IOrderService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedissonManager redissonManager;

    /**
     * 关闭tomcat的时候删除锁避免分布式锁的死锁
     * kill命令会直接杀掉tomcat的进程不会执行此方法
     */
//    @PreDestroy
//    public void delLock() {
//        redisUtil.delete(Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
//    }

    /**
     * 每分钟执行一次
     */
//    @Scheduled(cron = "0 */1 * * * ?")
//    public void closeOrderTaskV1() {
//
//        logger.info("关闭订单定时任务启动");
//        Integer hour = mallProperties.getTask().getHour();
//        orderService.closeOrder(hour);
//        logger.info("关闭订单定时任务结束");
//    }

    /**
     * 集群中的某一台tomcat判断是否能够占有锁去执行定时任务
     * 会存在死锁情况
     */
//    @Scheduled(cron = "0 */1 * * * ?")
//    public void closeOrderTaskV2() {
//
//        logger.info("关闭订单定时任务启动");
//
//        //redis分布式锁的上锁时间ms
//        long lockTimeOut = mallProperties.getTask().getLockTimeOut();
//        Boolean setIfAbsentResult = redisUtil.setIfAbsen(Const.RedisLock.CLOSE_ORDER_TASK_LOCK,
//                String.valueOf(System.currentTimeMillis() + lockTimeOut));
//
//        if (setIfAbsentResult) {
//            //若返回值为true则说明获取到了分布式锁，原先没有服务器占用锁
//            closeOrder(Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
//        } else {
//            logger.info("未获取到分布式锁");
//        }
//
//        logger.info("关闭订单定时任务结束");
//    }


//    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTask() {

        logger.info("关闭订单定时任务启动");

        //redis分布式锁的上锁时间ms
        long lockTimeOut = mallProperties.getTask().getLockTimeOut();
        Boolean setIfAbsentResult = redisUtil.setIfAbsen(Const.RedisLock.CLOSE_ORDER_TASK_LOCK,
                String.valueOf(System.currentTimeMillis() + lockTimeOut));
        if (setIfAbsentResult) {
            //若返回值为true则说明获取到了分布式锁，原先没有服务器占用锁
            closeOrder(Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
        } else {
            //未获取到锁 判断时间戳判断是否可以重置锁
            String lockValueStr = redisUtil.getRedisValue(Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
            //若该当前时间超过该锁本该释放的时间，但是由于某些原因未被释放则重置该锁
            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)) {
                String getSetResult = redisUtil.getSetRedisValue(Const.RedisLock.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeOut));
                //将获取到的值与之前的值进行比较若相同则说明原先应该释放的锁没有被释放这个时候可以重置
                //若不相同则说明在这个时间段内另一台tomcat集群已经使获取到了分布式锁这个时候只能是获取不到这个分布式锁
                if (getSetResult == null || (getSetResult != null && StringUtils.equals(getSetResult, lockValueStr))) {
                    closeOrder(Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
                } else {
                    logger.info("未获取到分布式锁");
                }
            } else {
                logger.info("未获取到分布式锁");
            }
        }

        logger.info("关闭订单定时任务结束");
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskWithRedisson() {

        logger.info("关闭订单定时任务启动");

        RLock lock = redissonManager.getRedisson().getLock(Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        //尝试获取锁
        try {
            //是否获取到锁
            //如果不设置waitTime为0的话如果一个逻辑或者sql执行的非常快的情况下，就会造成另一个Tomcat进程也会获取到锁执行一遍schedule
            if (getLock = lock.tryLock(0, 5, TimeUnit.SECONDS)) {
                logger.info("Redisson 获取到分布式锁:{} ThreadName:{}", Const.RedisLock.CLOSE_ORDER_TASK_LOCK,
                        Thread.currentThread().getName());
                Integer hour = mallProperties.getTask().getHour();
                orderService.closeOrder(hour);
            } else {
                logger.info("Redisson 没有获取到分布式锁:{} ThreadName:{}", Const.RedisLock.CLOSE_ORDER_TASK_LOCK,
                        Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            logger.error("Redisson 分布式锁获取异常");
        } finally {
            //未获取到锁的话就不需要释放锁，判断getLock
            if (!getLock) {
                return;
            }
            lock.unlock();
            logger.info("Redisson 释放分布式锁");
        }
        logger.info("关闭订单定时任务结束");
    }

    private void closeOrder(String lockName) {

        //设置初获取锁的时候有效期，避免永久有效 时间单位s
        redisUtil.expire(lockName, 5);
        logger.info("获取{}, ThreadName:{}", Const.RedisLock.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        Integer hour = mallProperties.getTask().getHour();
        orderService.closeOrder(hour);
        //释放锁
        redisUtil.delete(Const.RedisLock.CLOSE_ORDER_TASK_LOCK);
        logger.info("释放{}, ThreadName", Const.RedisLock.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
    }
}
