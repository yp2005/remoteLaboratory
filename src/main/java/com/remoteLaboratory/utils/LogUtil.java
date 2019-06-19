package com.remoteLaboratory.utils;

import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import org.apache.commons.lang.StringUtils;

public class LogUtil {
    public static void add(LogRecordRepository logRecordRepository, String type, String object, User user, Integer objectId, String objectName) {
        LogRecord logRecord = new LogRecord();
        logRecord.setType(type);
        logRecord.setObject(object);
        logRecord.setUserId(user.getId());
        logRecord.setUserName(StringUtils.isEmpty(user.getPersonName()) ? user.getUserName() : user.getPersonName());
        logRecord.setObjectId(objectId);
        logRecord.setObjectName(objectName);
        logRecordRepository.save(logRecord);
    }
}
