package com.sustech.conferenceSystem.service.meeting;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.MeetingMapper;
import com.sustech.conferenceSystem.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeetingQueryService {
    @Resource
    private MeetingMapper meetingMapper;

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
        List<MeetingSimple> list=meetingMapper.meetingSearch(userId,meetingName,roomName,meetingState);
        PageInfo<MeetingSimple> pageInfo=new PageInfo<>(list);
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
        return res;
    }

    public Map<String,Object> meetingSearch2Service(MeetingSimple meetingSimple, int page, int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<MeetingSimple> list=meetingMapper.meetingSearch2(meetingSimple);
        PageInfo<MeetingSimple> pageInfo=new PageInfo<>(list);
        res.put("list",list);
        res.put("total",pageInfo.getTotal());
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
