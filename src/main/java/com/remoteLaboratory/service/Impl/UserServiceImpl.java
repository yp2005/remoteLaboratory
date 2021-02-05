package com.remoteLaboratory.service.Impl;

import com.remoteLaboratory.bo.SearchPara;
import com.remoteLaboratory.bo.SearchParas;
import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.repositories.UserRepository;
import com.remoteLaboratory.service.UserService;
import com.remoteLaboratory.utils.Constants;
import com.remoteLaboratory.utils.EncodeUtils;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private LogRecordRepository logRecordRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, LogRecordRepository logRecordRepository) {
        this.userRepository = userRepository;
        this.logRecordRepository = logRecordRepository;
    }

    @Override
    public User add(User user) throws BusinessException {
        User oldUser = this.userRepository.findByUserName(user.getUserName());
        if(oldUser != null) {
            throw new BusinessException(Messages.CODE_40010, "用户名已存在！");
        }
        oldUser = this.userRepository.findByUserKey(user.getUserKey());
        if(oldUser != null) {
            throw new BusinessException(Messages.CODE_40010, "该学号已注册！");
        }
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User update(User user) throws BusinessException {
        User oldUser = this.userRepository.findByUserName(user.getUserName());
        if(oldUser != null && oldUser.getId().equals(user.getId())) {
            throw new BusinessException(Messages.CODE_40010, "用户名已存在！");
        }
        oldUser = this.userRepository.findByUserKey(user.getUserKey());
        if(oldUser != null && oldUser.getId().equals(user.getId())) {
            throw new BusinessException(Messages.CODE_40010, "该学号已注册！");
        }
        user = userRepository.save(user);
        return user;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        for (int id : ids) {
            User user = this.userRepository.findOne(id);
            if(user != null) {
                userRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("用户");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(user.getId());
                logRecord.setObjectName(user.getUserName());
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
            Page<User> list = userRepository.findAll(new MySpecification<User>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<User> list = userRepository.findAll(new MySpecification<User>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public ListOutput listByUserType(ListInput listInput, String userType) throws BusinessException {
        SearchPara searchPara = new SearchPara();
        searchPara.setName("userType");
        searchPara.setOp("eq");
        searchPara.setValue(userType);
        if (listInput.getSearchParas() == null) {
            listInput.setSearchParas(new SearchParas());
        }
        if (listInput.getSearchParas().getConditions() == null) {
            listInput.getSearchParas().setConditions(new ArrayList<>());
        }
        listInput.getSearchParas().getConditions().add(searchPara);
        return this.list(listInput);
    }

    @Override
    public User get(Integer id) throws BusinessException {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return user;
    }

    @Override
    public void importStudent(Workbook workbook) throws BusinessException {
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            throw new BusinessException(Messages.CODE_40010, "文件内容不合要求");
        }
        //获得当前sheet的开始行
        int firstRowNum = sheet.getFirstRowNum();
        //获得当前sheet的结束行
        int lastRowNum = sheet.getLastRowNum();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 从第二行开始
        for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
            //获得当前行
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            String userKey = this.getCellValue(row.getCell(0));
            String userName = this.getCellValue(row.getCell(1));
            if(userKey == null || userName == null) {
                throw new BusinessException(Messages.CODE_40010, "学号和用户名不能为空！");
            }
            User oldUser = this.userRepository.findByUserKey(userKey);
            if(oldUser != null) {
                throw new BusinessException(Messages.CODE_40010, "该学号已注册：" + userKey);
            }
            String password = this.getCellValue(row.getCell(2));
            if (password == null) {
                password = "123456";
            }
            try {
                password = encoder.encode(EncodeUtils.encodeSHA("123456".getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(Messages.CODE_50000);
            }
            String personName = this.getCellValue(row.getCell(3));
            String sex = this.getCellValue(row.getCell(4));
            String minzu = this.getCellValue(row.getCell(5));
            String nativePlace = this.getCellValue(row.getCell(6));
            String telphone = this.getCellValue(row.getCell(7));
            String college = this.getCellValue(row.getCell(8));
            String major = this.getCellValue(row.getCell(9));
            String grade = this.getCellValue(row.getCell(10));
            String class1 = this.getCellValue(row.getCell(11));

            User user = new User();
            user.setUserKey(userKey);
            user.setUserName(userName);
            user.setPassword(password);
            user.setPersonName(personName);
            user.setSex(sex);
            user.setMinzu(minzu);
            user.setNativePlace(nativePlace);
            user.setTelphone(telphone);
            user.setCollege(college);
            user.setMajor(major);
            user.setGrade(grade);
            user.setClass1(class1);
            user.setUserType(Constants.USER_TYPE_STUDENT);
            user.setForumForbidden(0);
            this.userRepository.save(user);
        }
    }

    private String getCellValue(Cell cell) throws BusinessException {
        String cellValue;
        if (cell == null) {
            return null;
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = null;
                break;
            default:
                throw new BusinessException(Messages.CODE_40010, "Excel文件读取错误！");
        }
        return cellValue;
    }
}
