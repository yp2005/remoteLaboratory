package com.remoteLaboratory.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.remoteLaboratory.bo.SearchPara;
import com.remoteLaboratory.bo.SearchParas;
import com.remoteLaboratory.entities.UploadFile;
import com.remoteLaboratory.entities.LogRecord;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.repositories.UploadFileRepository;
import com.remoteLaboratory.repositories.LogRecordRepository;
import com.remoteLaboratory.service.UploadFileService;
import com.remoteLaboratory.utils.MySpecification;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.utils.message.Messages;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 上传文件服务接口实现
 *
 * @Author: yupeng
 */

@Service
@Transactional
public class UploadFileServiceImpl implements UploadFileService {
    private static Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);

    private UploadFileRepository uploadFileRepository;

    private LogRecordRepository logRecordRepository;

    @Value("${UPLOAD_FILE_PATH}")
    private String uploadFilePath;

    @Autowired
    public UploadFileServiceImpl(UploadFileRepository uploadFileRepository, LogRecordRepository logRecordRepository) {
        this.uploadFileRepository = uploadFileRepository;
        this.logRecordRepository = logRecordRepository;
    }

    @Override
    public UploadFile add(UploadFile uploadFile) throws BusinessException {
        uploadFile = uploadFileRepository.save(uploadFile);
        return uploadFile;
    }

    @Override
    public UploadFile update(UploadFile uploadFile) throws BusinessException {
        uploadFile = uploadFileRepository.save(uploadFile);
        return uploadFile;
    }

    @Override
    public void delete(List<Integer> ids, User loginUser) throws BusinessException {
        List<LogRecord> logRecords = new ArrayList<>();
        List<UploadFile> uploadFiles = new ArrayList<>();
        for (int id : ids) {
            UploadFile uploadFile = this.uploadFileRepository.findOne(id);
            if(uploadFile != null) {
                uploadFiles.add(uploadFile);
                uploadFileRepository.delete(id);
                LogRecord logRecord = new LogRecord();
                logRecord.setType("删除");
                logRecord.setObject("上传文件");
                logRecord.setUserId(loginUser.getId());
                logRecord.setUserName(StringUtils.isEmpty(loginUser.getPersonName()) ? loginUser.getUserName() : loginUser.getPersonName());
                logRecord.setObjectId(uploadFile.getId());
                logRecord.setObjectName(uploadFile.getName());
                logRecords.add(logRecord);
            }
        }
        for(LogRecord logRecord : logRecords) {
            logRecordRepository.save(logRecord);
        }
        for(UploadFile uploadFile : uploadFiles) {
            String localFilePath = this.uploadFilePath + File.separator + uploadFile.getUploadFileName().split("|")[1];
            File file = new File(localFilePath);
            if (file.exists()) {
                file.delete();
            }
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
            Page<UploadFile> list = uploadFileRepository.findAll(new MySpecification<UploadFile>(listInput.getSearchParas()), pageable);
            listOutput.setPage(listInput.getPage());
            listOutput.setPageSize(listInput.getPageSize());
            listOutput.setTotalNum((int) list.getTotalElements());
            listOutput.setList(list.getContent());
        } else {
            List<UploadFile> list = uploadFileRepository.findAll(new MySpecification<UploadFile>(listInput.getSearchParas()));
            listOutput.setTotalNum(list.size());
            listOutput.setList(list);
        }
        return listOutput;
    }

    @Override
    public ListOutput listByUserId(ListInput listInput, Integer userId) throws BusinessException {
        SearchPara searchPara = new SearchPara();
        searchPara.setName("userId");
        searchPara.setOp("eq");
        searchPara.setValue(userId);
        if(listInput.getSearchParas() == null) {
            listInput.setSearchParas(new SearchParas());
        }
        if(listInput.getSearchParas().getConditions() == null) {
            listInput.getSearchParas().setConditions(new ArrayList<>());
        }
        listInput.getSearchParas().getConditions().add(searchPara);
        return this.list(listInput);
    }

    @Override
    public UploadFile get(Integer id) throws BusinessException {
        UploadFile uploadFile = uploadFileRepository.findOne(id);
        if (uploadFile == null) {
            throw new BusinessException(Messages.CODE_20001);
        }
        return uploadFile;
    }

}
