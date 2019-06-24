package com.remoteLaboratory.controller;

import com.remoteLaboratory.config.LoginRequired;
import com.remoteLaboratory.entities.Chapter;
import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.ChapterService;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.utils.CommonResponse;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.LogUtil;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 课程章接口
 *
 * @Author: yupeng
 */
@RestController
@RequestMapping("/chapter")
@Api(description = "课程章")
@LoginRequired
public class ChapterController {

    private static Logger log = LoggerFactory.getLogger(ChapterController.class);

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private LogRecordRepository logRecordRepository;

    @Autowired
    private CourseService courseService;

    @PostMapping(path = "/list")
    @ApiOperation(value = "课程章列表", notes = "查询课程章信息列表")
    public CommonResponse list(@RequestBody ListInput listInput,  @ApiIgnore User loginUser) throws BusinessException {
        CommonResponse commonResponse = CommonResponse.getInstance();
        commonResponse.setResult(chapterService.list(listInput));
        LogUtil.add(this.logRecordRepository, "列表查询", "课程章", loginUser, null, null);
        return commonResponse;
    }

    @PostMapping
    @ApiOperation(value = "添加课程章", notes = "添加课程章信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse add(@Validated({Chapter.Validation.class}) @RequestBody Chapter chapter, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(chapter.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        chapter = chapterService.add(chapter);
        CommonResponse commonResponse = CommonResponse.getInstance(chapter);
        LogUtil.add(this.logRecordRepository, "添加", "课程章", loginUser, chapter.getId(), chapter.getTitle());
        return commonResponse;
    }

    @PutMapping
    @ApiOperation(value = "修改课程章", notes = "修改课程章信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse update(@Validated({Chapter.Validation.class}) @RequestBody Chapter chapter, @ApiIgnore User loginUser) throws BusinessException {
        Course course = this.courseService.get(chapter.getCourseId());
        if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
            throw new BusinessException(Messages.CODE_50200);
        }
        chapter = chapterService.update(chapter);
        CommonResponse commonResponse = CommonResponse.getInstance(chapter);
        LogUtil.add(this.logRecordRepository, "修改", "课程章", loginUser, chapter.getId(), chapter.getTitle());
        return commonResponse;
    }

    @DeleteMapping
    @ApiOperation(value = "删除课程章", notes = "删除课程章信息接口")
    @LoginRequired(teacherRequired = "1")
    public CommonResponse delete(@NotNull(message = "课程章编号不能为空") @RequestBody List<Integer> ids, @ApiIgnore User loginUser) throws BusinessException {
        chapterService.delete(ids, loginUser);
        CommonResponse commonResponse = CommonResponse.getInstance();
        return commonResponse;
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "查询课程章", notes = "根据ID查询课程章")
    public CommonResponse get(@NotNull(message = "课程章编号不能为空") @PathVariable Integer id, @ApiIgnore User loginUser) throws BusinessException {
        Chapter chapter = chapterService.get(id);
        CommonResponse commonResponse = CommonResponse.getInstance(chapter);
        LogUtil.add(this.logRecordRepository, "查询", "课程章", loginUser, chapter.getId(), chapter.getTitle());
        return commonResponse;
    }
}
