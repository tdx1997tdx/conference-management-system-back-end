package com.sustech.conferenceSystem.controler.form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.dto.Form;
import com.sustech.conferenceSystem.service.device.DeviceQueryService;
import com.sustech.conferenceSystem.service.form.FormQueryService;
import com.sustech.conferenceSystem.util.Filter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 控制设备的增删改模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/form")
public class FormQueryControler {
    @Resource
    private FormQueryService formQueryService;

    /**
     * /form/form_search 接口，根据名称模糊分页查询
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/form_search", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deviceSearch(@RequestBody JSONObject jsonParam){
        String formName=jsonParam.getString("form_name");
        int page=Integer.parseInt(jsonParam.getString("page"));
        int volume=Integer.parseInt(jsonParam.getString("volume"));
        Map<String,Object> result=formQueryService.formSearchService(formName,page,volume);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * /form/form_search_page 接口，用于分页查询
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/form_search_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deviceSearchPage(@RequestBody JSONObject jsonParam){
        int page=Integer.parseInt(jsonParam.getString("page"));
        int volume=Integer.parseInt(jsonParam.getString("volume"));
        Map<String,Object> result=formQueryService.formSearchPageService(page,volume);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * /form/form_detail 接口，接收指定id,用于显示表单相关信息
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/form_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String deviceDetail(@RequestBody JSONObject jsonParam){
        Form form = JSON.parseObject(jsonParam.toString(), Form.class);
        Filter.attributeFilter(form);
        Form result=formQueryService.formDetailService(form);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

}
