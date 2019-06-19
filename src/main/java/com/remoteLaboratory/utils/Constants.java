package com.remoteLaboratory.utils;

/**
 * 常量
 *
 * @author yupeng
 */
public interface Constants {
    /*****************用户名常量*****************/
    String ADMIN_USER_NAME = "admin"; // 管理员

    /*****************REDIS数据前缀*****************/
    String USER_TOKEN = "user_token#"; // 用户token

    /***************用户类型*****************/
    String USER_TYPE_ADMIN = "1"; // 管理员
    String USER_TYPE_TEACHER = "2"; // 老师
    String USER_TYPE_STUDENT = "3"; // 学生

    /***************数字类常量*****************/
    Integer TOKEN_EXPIRE_TIME = 30 * 60; // 用户token过期时间30分钟

    /***************系统设置key*****************/
    String LOG_RETAIN_TIME = "logRetainTime"; // 日志保存时间
}
