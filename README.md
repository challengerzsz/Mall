# Mall
本项目的原型是慕课网上的一个商城后端项目，原项目采用SSM项目集成支付宝沙箱当面付SDK，本项目使用SpringBoot Gradle构建，ftp服务器以及部署服务器均为腾讯云

本项目采用单机nginx作为反向代理服务器 单Tomcat web服务器 redis缓存中间件 mysql数据库搭建
有时间的话会将这个项目做Tomcat集群 redis集群

# 接口文档
## Json说明
+ ServerResponse响应类，采用泛型封装，结构举例登录接口
```
{
    "status": 1,                    //状态 1成功 0失败 其余状态码下文会提及
    "msg": "登录成功",               //msg 响应信息，成功或失败的信息都会封装至此
    "data": {                   //封装的响应体，若该请求需要返回相应对象，将会封装至此，若不需要则无data
        "id": 1,
        "username": "zsz",
        "password": "",
        "email": "zeng@xiyou3g.com",
        "phone": "123423223",
        "question": "生日新问题",
        "answer": "19980305",
        "role": 0,
        "createTime": "2018-08-06T07:38:03.000+0000",
        "updateTime": "2018-08-06T08:07:01.000+0000"
    }
}
```
```
{
    "status": 0,                    //状态 1成功 0失败 其余状态码下文会提及
    "msg": "登录失败",               //msg 响应信息，成功或失败的信息都会封装至此
}
```

## 响应码status

+ 0 失败
+ 1 成功
+ 2 非法参数
+ 5 需要强制登录

响应码枚举类成员
```

    /**
     * 失败
     */
    ERROR(0, "ERROR"),

    /**
     * 成功
     */
    SUCCESS(1, "SUCCESS"),

    /**
     * 非法参数
     */
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),

    /**
     * 需要登录
     */
    NEED_LOGIN(5, "NEED_LOGIN");
```