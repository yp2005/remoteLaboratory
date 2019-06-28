package com.remoteLaboratory.redis;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.entities.UserOnlineTime;
import com.remoteLaboratory.repositories.UserOnlineTimeRepository;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.SpringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
public class KeyExpiredListener extends KeyExpirationEventMessageListener {
    private static final Logger log = LoggerFactory.getLogger(KeyExpiredListener.class);

    private RedisClient redisClient;

    private UserOnlineTimeRepository userOnlineTimeRepository;

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
        this.redisClient = SpringUtil.getBean(RedisClient.class);
        this.userOnlineTimeRepository = SpringUtil.getBean(UserOnlineTimeRepository.class);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody(),StandardCharsets.UTF_8);
        log.info("redis key expired: " + key);
        if(key.indexOf(Constants.USER_TOKEN) != -1) {
            key = key.replace(Constants.USER_TOKEN, "");
            try {
                String userStr = this.redisClient.get(Constants.USER_LOGIN_TIME + key);
                if (userStr != null) { // 记录用户在线时间
                    User user = JSONObject.parseObject(userStr, User.class);
                    UserOnlineTime userOnlineTime = new UserOnlineTime();
                    userOnlineTime.setUserId(user.getId());
                    userOnlineTime.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
                    userOnlineTime.setLoginTime(user.getLastLoginTime());
                    userOnlineTime.setLoginOutTime(new Date());
                    userOnlineTime.setOnlineTime(userOnlineTime.getLoginOutTime().getTime() - userOnlineTime.getLoginTime().getTime());
                    userOnlineTime = this.userOnlineTimeRepository.save(userOnlineTime);
                    this.redisClient.remove(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
