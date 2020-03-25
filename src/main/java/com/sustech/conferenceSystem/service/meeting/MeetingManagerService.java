package com.sustech.conferenceSystem.service.meeting;

import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.dto.UserAndMeeting;
import com.sustech.conferenceSystem.mapper.MeetingMapper;
import com.sustech.conferenceSystem.mapper.UserAndMeetingMapper;
import com.sustech.conferenceSystem.mapper.UserMapper;
import com.sustech.conferenceSystem.service.inform.InformConstants.InformReason;
import com.sustech.conferenceSystem.service.inform.InformService;
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
    @Resource
    private InformService informService;
    /**
     * 修改会议
     * @param meeting 传入javabean的meeting对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> meetingModifyService(MeetingFull meeting){
        Map<String,String> res = new HashMap<>();
        //判断会议是否20分钟开始，如果是则不能创建
        if(meeting.getStartTime()!=null){
            long beginTime=meeting.getStartTime().getTime();
            long nowTime = System.currentTimeMillis();
            if(beginTime<nowTime+20 * 60 * 1000){
                res.put("state","0");
                res.put("message","修改失败,会议在20分钟内开始");
                return res;
            }
        }
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
        //通知相关人员
        MeetingFull meetingFull = meetingMapper.meetingSearchCertain(meeting.getMeetingId());
        informService.meetingInform(meetingFull, InformReason.MODIFY);
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
        //判断会议是否20分钟开始，如果是则不能创建
        long beginTime=meeting.getStartTime().getTime();
        long nowTime = System.currentTimeMillis();
        if(beginTime<nowTime+20 * 60 * 1000){
            res.put("state","0");
            res.put("message","添加失败,会议在20分钟内开始");
            return res;
        }
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
            List<MeetingSimple> resList=meetingMapper.meetingIntervalSearch(members.get(i).getUserId(),meetingSimple);
            if(resList.size()!=0){
                res.put("state","2");
                res.put("message","成员中在该时间段存在会议时间冲突:"+members.get(i).getName());
                return res;
            }
        }
        //添加成员
        boolean isOk=meetingMapper.meetingMembersAdd(meeting);
        if(!isOk){
            res.put("state","0");
            res.put("message","添加失败");
            return res;
        }
        //通知相关人员
        MeetingFull meetingFull = meetingMapper.meetingSearchCertain(meeting.getMeetingId());
        informService.meetingInform(meetingFull, InformReason.MODIFY);

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
        MeetingFull m=meetingMapper.meetingSearchCertain(meeting.getMeetingId());
        //删除成员
        boolean isOk=meetingMapper.meetingMembersDelete(meeting);
        if(!isOk){
            res.put("state","0");
            res.put("message","删除失败");
            return res;
        }
        //通知相关人员
        MeetingFull meetingFull = meetingMapper.meetingSearchCertain(meeting.getMeetingId());
        informService.meetingInform(meetingFull, InformReason.MODIFY);

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
        //判断host是否存在
        User host = userMapper.findUserById(meeting.getHost().getUserId());
        if(host == null){
            res.put("state","0");
            res.put("message","host不存在");
            return res;
        }
        meeting.setHost(host);
        //判断recoder是否存在
        User recorder = userMapper.findUserById(meeting.getRecorder().getUserId());
        if(recorder == null){
            res.put("state","0");
            res.put("message","记录者不存在");
            return res;
        }
        meeting.setRecorder(recorder);
        //判断member是否存在
        List<User> members=meeting.getMembers();
        //将host和recorder也加入成员中
        members.add(host);
        members.add(recorder);
        for(int i=0;i<members.size();i++){
            User user=userMapper.findUserById(members.get(i).getUserId());
            if(user == null){
                res.put("state","1");
                res.put("message","成员已不存在，请刷新页面后重试");
                return res;
            }else{
                members.set(i,user);
            }
        }
        //判断成员时间是否冲突(未完成)
        for(int i=0;i<members.size();i++){
            MeetingSimple meetingSimple=new MeetingSimple();
            meetingSimple.setStartTime(meeting.getStartTime());
            meetingSimple.setEndTime(meeting.getEndTime());
            List<MeetingSimple> resList=meetingMapper.meetingIntervalSearch(members.get(i).getUserId(),meetingSimple);
            if(resList.size()!=0){
                res.put("state","2");
                res.put("message","成员中在该时间段存在会议时间冲突:"+members.get(i).getName());
                return res;
            }
        }

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

        //通知相关人员
        MeetingFull meetingFull = meetingMapper.meetingSearchCertain(meeting.getMeetingId());
        informService.meetingInform(meetingFull, InformReason.CREATE);

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
        MeetingFull meetingFull = meetingMapper.meetingSearchCertain(meetingId);
        //判断是否是会议前20分钟，如果在20分钟时，不能删除
        long beginTime = meetingFull.getStartTime().getTime();
        long nowTime = System.currentTimeMillis();
        if(beginTime<nowTime + 20 * 60 * 1000){
            res.put("state","0");
            res.put("message","删除失败,会议在20分钟内开始");
            return res;
        }
        boolean delRes=meetingMapper.meetingDelete(meetingId);
        if(!delRes){
            res.put("state","0");
            res.put("message","会议删除失败,无指定id会议");
            return res;
        }

        //通知相关人员
        informService.meetingInform(meetingFull, InformReason.DELETE);

        res.put("state","1");
        res.put("message","会议删除成功");
        return res;
    }

    /**
     * 签到
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> signInService(Integer userId,Integer meetingId){
        Map<String,String> res = new HashMap<>();
        List<MeetingSimple> l=userAndMeetingMapper.isBegin(meetingId);
        if(l.size()==0){
            res.put("state","0");
            res.put("message","会议未开始");
            return res;
        }
        userAndMeetingMapper.updateJoin(userId,meetingId,1);
        res.put("state","1");
        res.put("message","签到成功");
        return res;
    }

    /**
     * 接受
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> acceptService(Integer userId,Integer meetingId){
        Map<String,String> res = new HashMap<>();
        userAndMeetingMapper.updateJoin(userId,meetingId,0);
        res.put("state","1");
        res.put("message","接受成功");
        return res;
    }

    /**
     * 拒绝
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> rejectService(Integer userId,Integer meetingId){
        Map<String,String> res = new HashMap<>();
        userAndMeetingMapper.updateJoin(userId,meetingId,3);
        informService.informHost(userId, meetingId);

        res.put("state","1");
        res.put("message","拒绝成功");
        return res;
    }
}
