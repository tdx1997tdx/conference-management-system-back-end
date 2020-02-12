package com.sustech.conferenceSystem.service.meeting;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.MeetingSimple;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.MeetingMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeetingService {
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
    public Map<String,Object> meetingSearchService(String meetingName, String roomName, String meetingState, int page, int volume){
        Map<String,Object> res=new HashMap<>();
        PageHelper.startPage(page, volume);
        List<MeetingSimple> list=meetingMapper.meetingSearch(meetingName,roomName,meetingState);
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
        return meetingMapper.meetingSearch(null,null,null);
    }


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
     * @param meeting 传入javabean的user对象
     * @return map类型的结果state 0代表失败1代表成功
     */
    public Map<String,String> meetingCreateService(MeetingFull meeting){
        Map<String,String> res = new HashMap<>();
        System.out.println("create");
        System.out.println(meeting);
        List<MeetingFull> meetings = meetingMapper.isMeetingExist(meeting);
        System.out.println(meetings);
        if(meetings.size()==0){
            System.out.println(meeting);
            meetingMapper.meetingCreate(meeting);
            res.put("state","Success");
            res.put("message","会议创建成功");
        }else {
            res.put("state","Failed");
            res.put("message","创建会议与其他会议冲突，创建失败");
        }
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
