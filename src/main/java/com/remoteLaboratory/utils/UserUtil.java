package com.remoteLaboratory.utils;

import com.remoteLaboratory.entities.User;

/**
 * 用户工具类
 *
 * @Author: yupeng
 */
public class UserUtil {
    public static Boolean isAdmin(User user) {
        return user.getUserType().equals(Constants.USER_TYPE_ADMIN) || user.getUserType().equals(Constants.USER_TYPE_TEACHER);
    }
}
