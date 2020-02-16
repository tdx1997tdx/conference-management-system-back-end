package com.sustech.conferenceSystem.service.form;

import com.sustech.conferenceSystem.dto.Device;
import com.sustech.conferenceSystem.dto.Form;
import com.sustech.conferenceSystem.dto.Room;
import com.sustech.conferenceSystem.mapper.DeviceMapper;
import com.sustech.conferenceSystem.mapper.FormMapper;
import com.sustech.conferenceSystem.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制设备的增删改模块service层
 */
@Service
public class FormManagementService {
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private RoomMapper roomMapper;
    @Resource
    private FormMapper formMapper;
    /**
     * 添加表单业务逻辑
     * @param form 表单信息
     * @return 结果0或1或2
     */
    public Map<String,String> formAddService(Form form){
        Map<String,String> res=new HashMap<>();
        //检查设备是否存在
        Device d=new Device();
        d.setDeviceName(form.getDeviceName());
        List<Device> deviceRes=deviceMapper.searchDevice(d);
        if(deviceRes.size()==0){
            res.put("state","0");
            res.put("message","设备填写有误");
            return res;
        }else {
            form.setDeviceId(deviceRes.get(0).getDeviceId());
        }
        //检查房间是否存在
        Room r=new Room();
        r.setRoomName(form.getRoomName());
        List<Room> roomRes=roomMapper.searchRoom(r);
        if(roomRes.size()==0){
            res.put("state","1");
            res.put("message","房间填写有误");
            return res;
        }else {
            form.setRoomId(roomRes.get(0).getRoomId());
        }
        formMapper.addForm(form);
        res.put("state","2");
        res.put("message","添加成功");
        return res;
    }

    /**
     * 删除表单业务逻辑
     * @param formId 表单id
     * @return 结果0或1
     */
    public Map<String,String> formDeleteService(int formId){
        Map<String,String> res=new HashMap<>();
        boolean isSuccess=formMapper.deleteForm(formId);
        if(isSuccess){
            res.put("state","1");
            res.put("message","删除表单成功");
        }else {
            res.put("state","0");
            res.put("message","删除表单失败");
        }
        return res;
    }

    /**
     * 更改表单业务逻辑
     * @param form 表单信息
     * @return 结果0或1
     */
    public Map<String,String> formModifyService(Form form){
        Map<String,String> res=new HashMap<>();
        if(formMapper.findFormById(form.getFormId()).size()==0){
            res.put("state","0");
            res.put("message","找不到表单");
            return res;
        }
        //检查设备是否存在
        Device d=new Device();
        d.setDeviceName(form.getDeviceName());
        List<Device> deviceRes=deviceMapper.searchDevice(d);
        if(deviceRes.size()==0){
            res.put("state","1");
            res.put("message","设备填写有误");
            return res;
        }else {
            form.setDeviceId(deviceRes.get(0).getDeviceId());
        }
        //检查房间是否存在
        Room r=new Room();
        r.setRoomName(form.getRoomName());
        List<Room> roomRes=roomMapper.searchRoom(r);
        if(roomRes.size()==0){
            res.put("state","2");
            res.put("message","房间填写有误");
            return res;
        }else {
            form.setRoomId(roomRes.get(0).getRoomId());
        }
        if(!formMapper.updateForm(form)){
            res.put("state","4");
            res.put("message","更改设备失败");
            return res;
        }

        res.put("state","3");
        res.put("message","更改设备成功");
        return res;
    }
}
