package com.bsb.common;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zeng
 */
public class Const {

    public static final String TOKEN_PREFIX = "TOKEN_";

    public static final String CURRENT_USER = "CURRENT_USER";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public interface RedisCacheExTime {
        int REDIS_SESSION_EXTIME = 30 * 60;
    }

    public interface ProductListOrderBy {

        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");

    }

    public interface Cart {
        //购物车中产品选中状态
        int CHECKED = 1;
        //购物车中产品未选中状态
        int UN_CHECKED = 0;
        //产品库存<购物车库存
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        //产品库存>购物车库存
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public enum ProductStatusEnum {

        ON_SALE(1, "在线");


        private String value;
        private int code;

        ProductStatusEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }


        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已支付"),
        SHIPPED(30, "已发货"),
        ORDER_SUCCESS(40, "订单完成"),
        ORDER_CLOSE(50, "订单关闭");


        OrderStatusEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }

            throw new RuntimeException("未找到该枚举类型");
        }
    }

    public interface AlipayCallBack {

        public static final String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        public static final String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        public static final String RESPONSE_SUCCESS = "success";
        public static final String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatformEnum {

        ALIPAY(1, "支付宝");

        PayPlatformEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public enum PaymentTypeEnum {

        ONLINE_PAY(1, "在线支付");

        PaymentTypeEnum(int code, String value) {
            this.value = value;
            this.code = code;
        }

        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public static PaymentTypeEnum codeOf(int code) {
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }

            throw new RuntimeException("未找到该枚举类型");
        }
    }
}
