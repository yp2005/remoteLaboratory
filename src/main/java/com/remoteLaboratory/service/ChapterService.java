package com.remoteLaboratory.service;


import com.remoteLaboratory.entities.Chapter;
import com.remoteLaboratory.entities.User;
import com.remoteLaboratory.utils.exception.BusinessException;
import com.remoteLaboratory.vo.ListInput;
import com.remoteLaboratory.vo.ListOutput;

import java.util.List;

/**
 * 课程章服务接口定义
 *
 * @Author: yupeng
 */

public interface ChapterService {
    /**
     * 添加课程章信息
     * @param chapter
     * @return
     */
    public Chapter add(Chapter chapter) throws BusinessException;

    /**
     * 修改课程章信息
     * @param chapter
     * @return
     */
    public Chapter update(Chapter chapter) throws BusinessException;

    /**
     * 根据条件查询课程章信息列表
     * @param listInput
     * @return ListOutput
     */
    public ListOutput list(ListInput listInput) throws BusinessException;

    /**
     * 根据id获取课程章信息
     * @param id
     * @return Chapter
     */
    public Chapter get(Integer id) throws BusinessException;

    /**
     * 修改课程章信息
     * @param ids
     * @param loginUser
     */
    public void delete(List<Integer> ids, User loginUser) throws BusinessException;
}
