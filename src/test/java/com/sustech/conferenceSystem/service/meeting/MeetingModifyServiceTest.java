package com.sustech.conferenceSystem.service.meeting;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.MeetingMapper;
import com.sustech.conferenceSystem.mapper.UserAndMeetingMapper;
import com.sustech.conferenceSystem.mapper.UserMapper;
import com.sustech.conferenceSystem.service.inform.InformConstants;
import com.sustech.conferenceSystem.service.inform.InformService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class MeetingModifyServiceTest {
    @InjectMocks
    private MeetingManagerService meetingManagerServcie = new MeetingManagerService();
    @Mock
    private MeetingMapper meetingMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private InformService informService;

    @Test
    public void testMeetingModifyService1() {
        MeetingFull meetingFull=new MeetingFull();
        meetingFull.setStartTime(new Date(System.currentTimeMillis()+10 * 60 * 1000));
        Map<String,String> ret=meetingManagerServcie.meetingModifyService(meetingFull);
        assertEquals("0",ret.get("state"));
        assertEquals("修改失败,会议在20分钟内开始",ret.get("message"));
    }

    @Test
    public void testMeetingModifyService2() {
        MeetingFull meetingFull=new MeetingFull();
        Map<String,String> ret=meetingManagerServcie.meetingModifyService(meetingFull);
        assertEquals("0",ret.get("state"));
        assertEquals("开始时间为null",ret.get("message"));
    }

    @Test
    public void testMeetingModifyService3() {
        MeetingFull meetingFull=new MeetingFull();
        meetingFull.setStartTime(new Date(System.currentTimeMillis()+30 * 60 * 1000));
        Map<String,String> ret=meetingManagerServcie.meetingModifyService(meetingFull);
        assertEquals("0",ret.get("state"));
        assertEquals("Recorder为null",ret.get("message"));
    }

    @Test
    public void testMeetingModifyService4() {
        MeetingFull meetingFull=new MeetingFull();
        meetingFull.setStartTime(new Date(System.currentTimeMillis()+30 * 60 * 1000));
        User recorder=new User();
        recorder.setUserId(1);
        meetingFull.setRecorder(recorder);
        List<User> recorders=new ArrayList<>();
        Mockito
                .when(userMapper.searchUser(meetingFull.getRecorder()))
                .thenReturn(recorders);
        Map<String,String> ret=meetingManagerServcie.meetingModifyService(meetingFull);
        assertEquals("0",ret.get("state"));
        assertEquals("recorder不存在",ret.get("message"));
    }

    @Test
    public void testMeetingModifyService5() {
        MeetingFull meeting=new MeetingFull();
        meeting.setMeetingId(1);
        meeting.setStartTime(new Date(System.currentTimeMillis()+30 * 60 * 1000));
        User recorder=new User();
        recorder.setUserId(1);
        meeting.setRecorder(recorder);
        List<User> recorders=new ArrayList<>();
        recorders.add(recorder);
        Mockito.when(userMapper.searchUser(meeting.getRecorder())).thenReturn(recorders);
        Mockito.when(meetingMapper.meetingModify(meeting)).thenReturn(true);
        Mockito.when(meetingMapper.meetingSearchCertain(meeting.getMeetingId())).thenReturn(null);
        Map<String,String> ret=meetingManagerServcie.meetingModifyService(meeting);
        assertEquals("0",ret.get("state"));
        assertEquals("会议不存在",ret.get("message"));
    }

    @Test
    public void testMeetingModifyService6() {
        MeetingFull meeting=new MeetingFull();
        meeting.setMeetingId(1);
        meeting.setStartTime(new Date(System.currentTimeMillis()+30 * 60 * 1000));
        User recorder=new User();
        recorder.setUserId(1);
        meeting.setRecorder(recorder);
        meeting.setHost(recorder);
        List<User> recorders=new ArrayList<>();
        recorders.add(recorder);
        Mockito.when(userMapper.searchUser(meeting.getRecorder())).thenReturn(recorders);
        Mockito.when(meetingMapper.meetingModify(meeting)).thenReturn(true);
        Mockito.when(meetingMapper.meetingSearchCertain(meeting.getMeetingId())).thenReturn(meeting);
        Mockito.when(meetingMapper.meetingSearchCertain(meeting.getMeetingId())).thenReturn(meeting);
        Mockito.doNothing().when(informService).meetingInform(meeting, InformConstants.InformReason.MODIFY);
        Map<String,String> ret=meetingManagerServcie.meetingModifyService(meeting);
        assertEquals("1",ret.get("state"));
        assertEquals("修改成功",ret.get("message"));
    }
}
