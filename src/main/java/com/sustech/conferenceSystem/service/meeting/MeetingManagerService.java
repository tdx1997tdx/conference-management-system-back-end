package com.sustech.conferenceSystem.service.meeting;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.dto.UserAndMeeting;
import com.sustech.conferenceSystem.mapper.MeetingMapper;
import com.sustech.conferenceSystem.mapper.UserAndMeetingMapper;
import com.sustech.conferenceSystem.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeetingManagerService {
    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserAndMeetingMapper userAndMeetingMapper;

    /**
     * 获取符合要求的会议
     * @param meeting，其中有meetingName,roomName,meetingState，为空代表所有数据
     * @return 符合要求会议集合
     */
    public List<MeetingFull> meetingOrderService(MeetingFull meeting){
        List<MeetingFull> meetings = meetingMapper.meetingOrder(meeting);
        return meetings;
    }



    /**
     * 创建会议
     * @param meeting 传入javabean的meeting对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> meetingAddService(MeetingFull meeting){
        Map<String,String> res = new HashMap<>();
        MeetingSimple meetingSimple=meetingMapper.meetingSearch(null,meeting.getMeetingName(),null,null).get(0);
        //判断recoder是否存在
        List<User> recorder=userMapper.searchUser(meeting.getRecorder());
        if(recorder.size()==0){
            res.put("state","0");
            res.put("message","recorder不存在");
            return res;
        }
        meeting.setRecorder(recorder.get(0));
        //判断member是否存在
        List<User> members=meeting.getMembers();
        boolean isOk=true;
        for(int i=0;i<members.size();i++){
            List<User> user=userMapper.searchUser(members.get(i));
            if(user.size()==0){
                isOk=false;
                break;
            }else{
                members.set(i,user.get(0));
            }
        }
        if(!isOk){
            res.put("state","1");
            res.put("message","成员中存在姓名不合法的情况");
            return res;
        }
        //判断成员时间是否冲突

        //添加user和meeting的映射表
        for(User u:members){
            UserAndMeeting userAndMeeting=new UserAndMeeting();
            userAndMeeting.setUserId(u.getUserId());
            userAndMeeting.setMeetingId(meetingSimple.getMeetingId());
            userAndMeeting.setState(2);
            userAndMeetingMapper.addUserAndMeeting(userAndMeeting);
        }

        meeting.setMeetingState(2);
        boolean state=meetingMapper.meetingCreate(meeting);
        if(state){
            res.put("state","2");
            res.put("message","添加成功");
            return res;
        }
        res.put("state","3");
        res.put("message","未知错误");
        return res;
    }

    /**
     * 创建会议
     * @param meeting 传入javabean的user对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> meetingDeleteService(MeetingFull meeting){
        Map<String,String> res = new HashMap<>();
        System.out.println("delete");
        List<MeetingFull> meetings = null;
        //meetingMapper.meetingSearch(meeting);
        System.out.println(meetings);

        if(meetings.size()==1){
            System.out.println(meeting);
            meetingMapper.meetingDelete(meeting);
            res.put("state","Success");
            res.put("message","会议删除成功");
        }else if(meetings.size()==0){
            res.put("state","Failed");
            res.put("message","没有找到对应会议，删除失败");
        } else {
            res.put("state","Failed");
            res.put("message","对应会议不止一个，请重新核对，删除失败");
        }
        return res;
    }
}
