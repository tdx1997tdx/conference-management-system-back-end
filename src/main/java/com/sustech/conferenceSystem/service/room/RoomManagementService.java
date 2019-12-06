package com.sustech.conferenceSystem.service.room;

import com.sustech.conferenceSystem.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 控制房间的增删改模块service层
 */
@Service
public class RoomManagementService {
    @Resource
    private UserMapper userMapper;

}
