package com.remoteLaboratory.entities;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 实验报告小题模板
 *
 * @Author: yupeng
 */

@Entity
@Table(name = "rl_test_exercise_template")
@ApiModel(value = "实验报告小题模板表")
@Data
public class TestExerciseTemplate implements Serializable {
    @Id
    @Column(length = 10, nullable = false)
    @ApiModelProperty(value = "ID", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 10)
    @ApiModelProperty(value = "习题ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer exerciseId;

    @Lob
    @ApiModelProperty(value = "习题内容")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private String exerciseContent;

    @Column(length = 10)
    @ApiModelProperty(value = "显示类型 1-文字 2-图片")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer displayType;

    @Column(length = 10)
    @ApiModelProperty(value = "类型 1-客观题 2-主观题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnore
    private Integer type;

    @Column(length = 10)
    @ApiModelProperty(value = "题目类型 1-单选题 2-多选题 3-填空题 4-判断题 5-问答题 6-计算题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer exercisesType;

    @Lob
    @ApiModelProperty(value = "选项 选择题填此字段JSON: [{'order': 'A', 'content': '1'},{'order': 'B', 'content': '2'}]")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String options;

    @Lob
    @ApiModelProperty(value = "正确答案 单选题-A 多选题-A,B,C 填空题-答案 判断题同选择题")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String answer;

    @Column(length = 10)
    @ApiModelProperty(value = "实验报告模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer testTemplateId;

    @Column(length = 10)
    @ApiModelProperty(value = "分项模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testSubsectionTemplateId;
    
    @Column(length = 10)
    @ApiModelProperty(value = "实验报告大题模板ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer testPartTemplateId;

    @Column(length = 10)
    @ApiModelProperty(value = "序号")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Integer serialNumber;

    @Column(length = 20)
    @ApiModelProperty(value = "分数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    private Double score;

    @Column(updatable = false)
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createTime;

    @ApiModelProperty(value = "修改时间", hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updateTime;

    public String getRandomOrderOptions() {
        // [{"order":"A", "content":"1"},{"order":"B", "content":"2"}]
        if(!this.getExercisesType().equals(1) && !this.getExercisesType().equals(2)) { // 非选择题返回null
            return null;
        }
        JSONArray options = JSONArray.parseArray(this.options);
        List<Character> orders = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            Character order = (char)('A' + i);
            orders.add(order);
        }
        JSONArray newOptions = new JSONArray();
        for (int i = 0; i < options.size(); i++) {
            JSONObject option = options.getJSONObject(i);
            JSONObject newOption = new JSONObject();
            newOption.put("order", option.getString("order"));
            newOption.put("content", option.getString("content"));
            int orderNo = (int) (Math.random() * orders.size());
            newOption.put("displayOrder", orders.get(orderNo).toString());
            orders.remove(orderNo);
            newOptions.add(newOption);
        }
        newOptions.sort(Comparator.comparing(option -> ((JSONObject) option).getString("displayOrder")));
        return newOptions.toJSONString();
    }

    public static void main(String[] args){
        TestExerciseTemplate testExerciseTemplate = new TestExerciseTemplate();
        testExerciseTemplate.setOptions("[{\"order\":\"A\", \"content\":\"1\"},{\"order\":\"B\", \"content\":\"2\"}]");
        testExerciseTemplate.setExercisesType(1);
        String op = testExerciseTemplate.getRandomOrderOptions();
        System.out.println(op);
//        int orderNo = (int) (Math.random() * 4);
//        System.out.println(orderNo);
    }
}
