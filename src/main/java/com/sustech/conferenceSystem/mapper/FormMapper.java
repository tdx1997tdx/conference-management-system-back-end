package com.sustech.conferenceSystem.mapper;

import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.dto.Form;

import java.util.List;

public interface FormMapper {

    /**
     *
     * 添加一个form
     * @param form
     * @return
     */
    boolean addForm(Form form);

    /**
     * 删除一个form
     * @param formId 唯一的表单id
     * @return
     */
    boolean deleteForm(int formId);

    /**
     * 修改一个form
     * @param form 表单
     * @return
     */
    boolean updateForm(Form form);

    /**
     * 模糊查询表单
     * @param formName 表单名字
     * @return
     */
    List<Form> fuzzySearchForm(String formName);

    /**
     * 查询符合限制的表单
     * @param form 表单
     * @return
     */
    List<Form> searchForm(Form form);

    /**
     * 根据id查找表单
     * @param formId
     * @return
     */
    List<Form> findFormById(int formId);

}
