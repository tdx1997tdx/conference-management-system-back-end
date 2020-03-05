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
import io.swagger.models.auth.In;
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
     * 修改会议
     * @param meeting 传入javabean的meeting对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> meetingModifyService(MeetingFull meeting){
        Map<String,String> res = new HashMap<>();
        if(meeting.getRecorder()!=null){
            //判断recoder是否存在
            List<User> recorder=userMapper.searchUser(meeting.getRecorder());
            if(recorder.size()==0){
                res.put("state","0");
                res.put("message","recorder不存在");
                return res;
            }
            meeting.setRecorder(recorder.get(0));
        }
        //通知相关人员(未完成)

        //修改会议
        meetingMapper.meetingModify(meeting);
        res.put("state","1");
        res.put("message","修改成功");
        return res;
    }

    /**
     * 添加会议成员
     * @param meeting 传入javabean的meeting对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> meetingMembersAddService(MeetingFull meeting){
        Map<String,String> res = new HashMap<>();
        //添加成员
        boolean isOk=meetingMapper.meetingMembersAdd(meeting);
        if(!isOk){
            res.put("state","0");
            res.put("message","添加失败");
            return res;
        }
        //通知相关人员(未完成)

        res.put("state","1");
        res.put("message","添加成功");
        return res;
    }


    /**
     * 删除会议成员
     * @param meeting 传入javabean的meeting对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> meetingMembersDeleteService(MeetingFull meeting){
        Map<String,String> res = new HashMap<>();
        //删除成员
        boolean isOk=meetingMapper.meetingMembersDelete(meeting);
        if(!isOk){
            res.put("state","0");
            res.put("message","删除失败");
            return res;
        }
        //通知相关人员(未完成)

        res.put("state","1");
        res.put("message","删除成功");
        return res;
    }



    /**
     * 创建会议
     * @param meeting 传入javabean的meeting对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> meetingAddService(MeetingFull meeting){
        Map<String,String> res = new HashMap<>();

        //判断recoder是否存在
        List<User> recorder=userMapper.searchUser(meeting.getRecorder());
        if(recorder.size()==0){
            res.put("state","0");
            res.put("message","记录者不存在");
            return res;
        }
        meeting.setRecorder(recorder.get(0));
        //判断member是否存在
        List<User> members=meeting.getMembers();
        for(int i=0;i<members.size();i++){
            List<User> user=userMapper.searchUser(members.get(i));
            if(user.size()==0){
                res.put("state","1");
                res.put("message","成员已不存在，请刷新页面后重试");
                return res;
            }else{
                members.set(i,user.get(0));
            }
        }
        //判断成员时间是否冲突(未完成)
        for(int i=0;i<members.size();i++){
            MeetingSimple meetingSimple=new MeetingSimple();
            meetingSimple.setStartTime(meeting.getStartTime());
            meetingSimple.setEndTime(meeting.getEndTime());
            int count1=meetingMapper.meetingIntervalSearch(members.get(i).getUserId(),null);
            int count2=meetingMapper.meetingIntervalSearch(members.get(i).getUserId(),meetingSimple);
            if(count1-count2!=0){
                res.put("state","2");
                res.put("message","成员中在该时间段存在会议时间冲突:"+members.get(i).getName());
                return res;
            }
        }
        //通知相关人员(未完成)

        //添加会议
        meeting.setMeetingState(2);
        meetingMapper.meetingCreate(meeting);
        //添加user和meeting的映射表
        for(User u:members){
            UserAndMeeting userAndMeeting=new UserAndMeeting();
            userAndMeeting.setUserId(u.getUserId());
            userAndMeeting.setMeetingId(meeting.getMeetingId());
            userAndMeeting.setState(2);
            userAndMeetingMapper.addUserAndMeeting(userAndMeeting);
        }

        res.put("state","3");
        res.put("message","添加成功");
        return res;
    }

    /**
     * 创建会议
     * @param meetingId 传入meeting_id
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> meetingDeleteService(Integer meetingId){
        Map<String,String> res = new HashMap<>();
        boolean delRes=meetingMapper.meetingDelete(meetingId);
        if(!delRes){
            res.put("state","0");
            res.put("message","会议删除失败");
            return res;
        }
        res.put("state","1");
        res.put("message","会议删除成功");
        return res;
    }
}
