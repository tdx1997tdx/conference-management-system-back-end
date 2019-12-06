package com.sustech.conferenceSystem.controler.room;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sustech.conferenceSystem.service.user.AdminService;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.util.Filter;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 控制房间的增删改模块controler层
 */
@ResponseBody
@RestController
@CrossOrigin
@RequestMapping(value = "/room")
public class RoomManagementControler {
}
