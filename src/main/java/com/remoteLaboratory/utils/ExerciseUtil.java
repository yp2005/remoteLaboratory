package com.remoteLaboratory.utils;

public class ExerciseUtil {
    public static String getTypeName(Integer type) {
        // 题目类型 1-单选题 2-多选题 3-填空题 4-判断题 5-问答题 6-计算题
        switch (type) {
            case 1:
                return "单选题";
            case 2:
                return "多选题";
            case 3:
                return "填空题";
            case 4:
                return "判断题";
            case 5:
                return "问答题";
            case 6:
                return "计算题";
        }
        return "";
    }

    public static Integer getType(Integer type) {
        // 题目类型 1-单选题 2-多选题 3-填空题 4-判断题 5-问答题 6-计算题
        // 类型 1-客观题 2-主观题
        switch (type) {
            case 1:
            case 2:
            case 3:
            case 4:
                return 1;
            case 5:
            case 6:
                return 2;
        }
        return null;
    }
}
