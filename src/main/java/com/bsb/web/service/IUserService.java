package com.bsb.web.service;

import com.bsb.common.ServerResponse;
import com.bsb.web.pojo.User;

/**
 * @author zeng
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> getQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken);

    ServerResponse<String> resetPassword(User user, String oldPassword, String newPassword);

    ServerResponse<User> updateInfo(User user);

    ServerResponse<User> getInfo(Integer userId);
}
