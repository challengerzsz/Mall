package com.bsb.common;

/**
 * @author zeng
 */
public class Const {

    public static final String TOKEN_PREFIX = "TOKEN_";

    public static final String CURRENT_USER = "CURRENT_USER";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }
}
