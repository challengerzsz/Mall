package com.bsb.web.service.impls;

import com.bsb.common.Const;
import com.bsb.common.ServerResponse;
import com.bsb.util.MD5Util;
import com.bsb.util.RedisUtil;
import com.bsb.web.dao.IUserMapper;
import com.bsb.web.pojo.User;
import com.bsb.web.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.bsb.common.Const.TOKEN_PREFIX;

/**
 * @author zeng
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMsg("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);

    }

    @Override
    public ServerResponse<String> register(User user) {

        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);

        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("注册失败");
        }

        return ServerResponse.createBySuccessMsg("注册成功");
    }

    public ServerResponse<String> checkValid(String str, String type) {

        if (StringUtils.isNotBlank(type)) {

            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMsg("用户名已存在");
                }
            }

            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMsg("email已存在");
                }
            }

        } else {
            return ServerResponse.createByErrorMsg("参数错误");
        }

        return ServerResponse.createBySuccessMsg("校验成功");
    }

    public ServerResponse<String> getQuestion(String username) {

        ServerResponse validREsponse = this.checkValid(username, Const.USERNAME);
        if (validREsponse.isSuccess()) {
            return ServerResponse.createByErrorMsg("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }

        return ServerResponse.createByErrorMsg("找回密码问题未设置");
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            redisUtil.setRedisValue(TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }

        return ServerResponse.createByErrorMsg("问题的答案错误");
    }

    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {

        if (StringUtils.isAnyBlank(username, newPassword, forgetToken)) {
            return ServerResponse.createByErrorMsg("参数错误，token或密码为空");
        }

        ServerResponse validREsponse = this.checkValid(username, Const.USERNAME);
        if (validREsponse.isSuccess()) {
            return ServerResponse.createByErrorMsg("用户不存在");
        }

        String token = redisUtil.getRedisValue(TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMsg("token无效或过期");
        }

        if (StringUtils.equals(forgetToken, token)) {
            String newMD5Password = MD5Util.MD5EncodeUtf8(newPassword);

            int rowCount = userMapper.updatePasswordByUsername(username, newMD5Password);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMsg("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMsg("token错误,请再次获取充值密码的token");
        }

        return ServerResponse.createByErrorMsg("修改密码失败");
    }

    public ServerResponse<String> resetPassword(User user, String oldPassword, String newPassword) {

        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMsg("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMsg("密码更新成功");
        }

        return ServerResponse.createByErrorMsg("密码更新失败");
    }

    public ServerResponse<User> updateInfo(User user) {

        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMsg("email已经存在，请更换email");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }

        return ServerResponse.createByErrorMsg("更新个人信息失败");

    }

    public ServerResponse<User> getInfo(Integer userId) {

        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMsg("找不到当前用户");
        }

        user.setPhone(StringUtils.EMPTY);

        return ServerResponse.createBySuccess(user);
    }
}
