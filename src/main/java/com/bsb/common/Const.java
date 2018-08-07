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

    public interface ProductListOrderBy {

        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");

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
}
