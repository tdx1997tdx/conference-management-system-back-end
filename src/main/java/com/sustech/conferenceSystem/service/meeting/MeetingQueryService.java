package com.sustech.conferenceSystem.service.meeting;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.dto.Room;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.MeetingMapper;
import com.sustech.conferenceSystem.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MeetingQueryService {
    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private RoomMapper roomMapper;

    /**
     * 获取指定用户的id
     * @param userId 会议id名称
     * @return 符合要求会议集合
     */
    public Map<String,Object> userMeeting7SearchService(Integer userId){
        Map<String,Object> res=new HashMap<>();
        Date nowDate=new Date(System.currentTimeMillis());
        //7天后的日期
        Date endDate=new Date(nowDate.getTime() + 7 * 24 * 60 * 60 * 1000);
        List<MeetingSimple> list=meetingMapper.userMeeting7Search(userId,nowDate,endDate);
        res.put("list",list);
        res.put("total",list.size());
        return res;
    }

    /**
     * 获取会议
     * @param meetingName 会议名称，模糊搜索
     * @param roomName 会议室名称
     * @param meetingState 会议状态
     * @param page 第几页
     * @param volume 一页几个
     * @return 符合要求会议集合
     */
    public Map<String,Object> meetingSearchService(Integer userId,String meetingName, String roomName, String meetingState, int page, int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        MeetingSimple meetingSimple=new MeetingSimple();
        meetingSimple.setMeetingName(meetingName);
        meetingSimple.setMeetingName(meetingState);
        List<MeetingSimple> list=meetingMapper.meetingSearch(userId,roomName,meetingSimple);
        PageInfo<MeetingSimple> pageInfo=new PageInfo<>(list);
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        return res;
    }

    public Map<String,Object> meetingSearch2Service(MeetingSimple meetingSimple){
        Map<String,Object> res=new HashMap<>();
        List<MeetingSimple> list=meetingMapper.meetingSearch2(meetingSimple);
        res.put("list",list);
        res.put("total",list.size());
        return res;
    }

    public List<Map<String,Object>> meetingSearch3Service(MeetingSimple meetingSimple, Room room){
        List<Map<String,Object>> res=new ArrayList<>();
        List<Room> rooms=roomMapper.searchRoom(room);
        for(Room r:rooms){
            Map<String,Object> m=new HashMap<>();
            m.put("room_id",r.getRoomId());
            m.put("room_name",r.getRoomName());
            meetingSimple.setRoomId(r.getRoomId());
            List<MeetingSimple> list=meetingMapper.meetingSearch2(meetingSimple);
            m.put("meeting_list",list);
            res.add(m);
        }
        return res;
    }

    public MeetingFull meetingSearchCertainService(int meetingId){
        MeetingFull res=meetingMapper.meetingSearchCertain(meetingId);
        User host=res.getHost();
        User recorder=res.getRecorder();
        res.setHost(host.userFilter());
        res.setRecorder(recorder.userFilter());
        return res;
    }

    /**
     * 处理获取所有会议的业务逻辑
     * @return 所有会议
     */
    public List<MeetingSimple> meetingGetService(){
        return meetingMapper.meetingGetAll();
    }

}
