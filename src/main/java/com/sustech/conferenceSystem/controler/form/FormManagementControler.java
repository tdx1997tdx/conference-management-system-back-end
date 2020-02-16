package com.sustech.conferenceSystem.controler.form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.dto.Form;
import com.sustech.conferenceSystem.service.device.DeviceManagementService;
import com.sustech.conferenceSystem.service.form.FormManagementService;
import com.sustech.conferenceSystem.util.Filter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 控制表单的增删改模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/form")
public class FormManagementControler {
    @Resource
    private FormManagementService formManagementService;

    /**
     * /form/form_add 接口，用于添加表单
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/form_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String formAdd(@RequestBody JSONObject jsonParam){
        Form form = JSON.parseObject(jsonParam.toString(), Form.class);
        Filter.attributeFilter(form);
        Map result=formManagementService.formAddService(form);
        return JSON.toJSONString(result);
    }

    /**
     * /form/form_delete 接口，用于删除表单
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/form_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String formDelete(@RequestBody JSONObject jsonParam){
        int formId = Integer.parseInt(jsonParam.getString("form_id"));
        Map result=formManagementService.formDeleteService(formId);
        return JSON.toJSONString(result);
    }

    /**
     * /form/form_modify 接口，用于更改表单信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/form_modify", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String formModify(@RequestBody JSONObject jsonParam){
        Form form = JSON.parseObject(jsonParam.toString(), Form.class);
        Filter.attributeFilter(form);
        Map result=formManagementService.formModifyService(form);
        return JSON.toJSONString(result);
    }

}
