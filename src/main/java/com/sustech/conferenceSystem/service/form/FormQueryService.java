package com.sustech.conferenceSystem.service.form;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.dto.Form;
import com.sustech.conferenceSystem.mapper.DeviceMapper;
import com.sustech.conferenceSystem.mapper.FormMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制设备的查询模块service层
 */
@Service
public class FormQueryService {
    @Resource
    private FormMapper formMapper;


    /**
     * 模糊查询业务逻辑
     * @param formName 表单名称
     * @param page 页数
     * @param volume 容量
     * @return 结果集合
     */
    public Map<String,Object> formSearchService(String formName,int page,int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<Form> list=formMapper.fuzzySearchForm(formName);
        PageInfo<Form> pageInfo=new PageInfo<>(list);
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        return res;
    }

    /**
     * 分页查询业务逻辑
     * @param page 页数
     * @param volume 容量
     * @return 结果集合
     */
    public Map<String,Object> formSearchPageService(int page,int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<Form> list=formMapper.fuzzySearchForm("");
        PageInfo<Form> pageInfo=new PageInfo<>(list);
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        return res;
    }

    /**
     * 查询指定表单详细信息
     * @param form 表单信息
     * @return 结果0或1
     */
    public Form formDetailService(Form form){
        List<Form> res = formMapper.searchForm(form);
        return res.get(0);
    }

}
