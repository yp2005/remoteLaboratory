package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * 用户服务接口定义
 *
 * @Author: yupeng
 */

public interface UserService {
    /**
     * 添加用户信息
     * @param user
     * @return
     */
    User add(User user) throws BusinessException;

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    User update(User user) throws BusinessException;

    /**
     * 根据条件查询用户信息列表
     * @param listInput
     * @return ListOutput
     */
    ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据用户类型条件查询用户信息列表
     * @param listInput
     * @return ListOutput
     */
    ListOutput listByUserType(ListInput listInput, String userType) throws BusinessException;

    /**
     * 根据id获取用户信息
     * @param id
     * @return User
     */
    User get(Integer id) throws BusinessException;

    /**
     * 修改用户信息
     * @param ids
     * @param loginUser
     */
    void delete(List<Integer> ids, User loginUser) throws BusinessException;

    /**
     * 导入公司学生账号
     * @param workbook
     */
    void importStudent(Workbook workbook) throws BusinessException;
}
