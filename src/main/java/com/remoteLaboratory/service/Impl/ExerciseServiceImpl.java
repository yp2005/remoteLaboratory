package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.entities.Course;
import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.Exercise;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.ExerciseRepository;
import com.remoteLaboratory.service.CourseService;
import com.remoteLaboratory.service.ExerciseService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.ExerciseUtil;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
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
 * 习题服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class ExerciseServiceImpl implements ExerciseService {
    private static Logger log = LoggerFactory.getLogger(ExerciseServiceImpl.class);

    private ExerciseRepository exerciseRepository;

    private LogRecordRepository logRecordRepository;

    private CourseService courseService;

    @Autowired
    public ExerciseServiceImpl(ExerciseRepository exerciseRepository,
                               CourseService courseService,
                               LogRecordRepository logRecordRepository) {
        this.exerciseRepository = exerciseRepository;
        this.logRecordRepository = logRecordRepository;
        this.courseService = courseService;
    }

    @Override
    public Exercise add(Exercise exercise) throws BusinessException {
        if(exercise.getTestType().equals(2)
                && !exercise.getExercisesType().equals(1)
                && !exercise.getExercisesType().equals(2)
                && !exercise.getExercisesType().equals(5)) {
            throw new BusinessException(Messages.CODE_40010, "问卷调查题目只能是选择题、问答题！");
        }
        exercise = exerciseRepository.save(exercise);
        return exercise;
    }

    @Override
    public Exercise update(Exercise exercise) throws BusinessException {
        exercise = exerciseRepository.save(exercise);
        return exercise;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            Exercise exercise = this.exerciseRepository.findOne(id);
            if(exercise != null) {
                Course course = this.courseService.get(exercise.getCourseId());
                if(!loginUser.getUserType().equals(Constants.USER_TYPE_ADMIN) && !course.getTeacherId().equals(loginUser.getId())) {
                    throw new BusinessException(Messages.CODE_50200);
                }
                exerciseRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("习题");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(exercise.getId());
                logRecord.setObjectName(ExerciseUtil.getTypeName(exercise.getExercisesType()));
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
            Page<Exercise> list = exerciseRepository.findAll(new MySpecification<Exercise>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<Exercise> list = exerciseRepository.findAll(new MySpecification<Exercise>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public Exercise get(Integer id) throws BusinessException {
        Exercise exercise = exerciseRepository.findOne(id);
        if (exercise == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return exercise;
    }

}
