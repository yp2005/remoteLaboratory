package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.*;
import com.remoteLaboratory.repositories.ChapterRepository;
import com.remoteLaboratory.repositories.ChapterStudyRecordRepository;
import com.remoteLaboratory.repositories.CourseStudyRecordRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.ChapterService;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.CourseStudyRecordService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程章服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class ChapterServiceImpl implements ChapterService {
    private static Logger log = LoggerFactory.getLogger(ChapterServiceImpl.class);

    private ChapterRepository chapterRepository;

    private LogRecordRepository logRecordRepository;

    private CourseService courseService;

    private CourseStudyRecordRepository courseStudyRecordRepository;

    private CourseStudyRecordService courseStudyRecordService;

    private ChapterStudyRecordRepository chapterStudyRecordRepository;

    @Autowired
    public ChapterServiceImpl(ChapterRepository chapterRepository,
                              CourseService courseService,
                              CourseStudyRecordRepository courseStudyRecordRepository,
                              CourseStudyRecordService courseStudyRecordService,
                              ChapterStudyRecordRepository chapterStudyRecordRepository,
                              LogRecordRepository logRecordRepository) {
        this.chapterRepository = chapterRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
        this.courseStudyRecordRepository = courseStudyRecordRepository;
        this.chapterStudyRecordRepository = chapterStudyRecordRepository;
        this.courseStudyRecordService = courseStudyRecordService;
    }

    @Override
    public Chapter add(Chapter chapter) throws BusinessException {
        chapter = chapterRepository.save(chapter);
        List<CourseStudyRecord> courseStudyRecordList = this.courseStudyRecordRepository.findByCourseIdAndStatus(chapter.getCourseId(), 0);
        if(CollectionUtils.isNotEmpty(courseStudyRecordList)) {
            for(CourseStudyRecord courseStudyRecord : courseStudyRecordList) {
                ChapterStudyRecord chapterStudyRecord = new ChapterStudyRecord();
                chapterStudyRecord.setChapterId(chapter.getId());
                chapterStudyRecord.setChapterName(chapter.getName());
                chapterStudyRecord.setChapterTitle(chapter.getTitle());
                chapterStudyRecord.setCourseStudyRecordId(courseStudyRecord.getId());
                chapterStudyRecord.setStudied(0.0);
                chapterStudyRecord.setUserId(courseStudyRecord.getUserId());
                chapterStudyRecord.setUserName(courseStudyRecord.getUserName());
                chapterStudyRecord = this.chapterStudyRecordRepository.save(chapterStudyRecord);
                this.courseStudyRecordService.update(courseStudyRecord.getId());
            }
        }
        return chapter;
    }

    @Override
    public Chapter update(Chapter chapter) throws BusinessException {
        chapter = chapterRepository.save(chapter);
        return chapter;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            Chapter chapter = this.chapterRepository.findOne(id);
            if(chapter != null) {
                Course course = this.courseService.get(chapter.getCourseId());
                if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                if(course.getStatus().equals(1)) {
                    throw new BusinessException(Messages.CODE_40010, "课程进行中不能执行删除操作");
                }
                chapterRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("课程章");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(chapter.getId());
                logRecord.setObjectName(chapter.getTitle());
                logRecords.add(logRecord);
            }
        }
        for(LogRecord logRecord : logRecords) {
            logRecordRepository.save(logRecord);
        }
    }

    @Override
    public ListOutput list(ListInput listInput) throws BusinessException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if (StringUtils.isNotBlank(listInput.getSortDirection())
                && StringUtils.isNotBlank(listInput.getSortProperties())) {
            sort = new Sort(Sort.Direction.fromString(listInput.getSortDirection()), listInput.getSortProperties());
        }
        Pageable pageable = null;
        if (listInput.getPage() != null && listInput.getPageSize() != null) {
            pageable = new PageRequest(listInput.getPage(), listInput.getPageSize(), sort);
        }
        ListOutput listOutput = new ListOutput();
        if (pageable != null) {
            Page<Chapter> list = chapterRepository.findAll(new MySpecification<Chapter>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<Chapter> list = chapterRepository.findAll(new MySpecification<Chapter>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public Chapter get(Integer id) throws BusinessException {
        Chapter chapter = chapterRepository.findOne(id);
        if (chapter == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return chapter;
    }

}
