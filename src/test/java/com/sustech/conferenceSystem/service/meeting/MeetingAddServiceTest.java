package com.sustech.conferenceSystem.service.meeting;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.MeetingMapper;
import com.sustech.conferenceSystem.mapper.UserMapper;
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
public class MeetingAddServiceTest {
    @InjectMocks
    private MeetingManagerService meetingManagerServcie = new MeetingManagerService();
    @Mock
    private MeetingMapper meetingMapper;
    @Mock
    private UserMapper userMapper;

    @Test
    public void testMeetingAddServiceService1() {
        MeetingFull meeting=new MeetingFull();
        User host=new User();
        host.setUserId(1);
        meeting.setHost(host);
        Mockito.when(userMapper.findUserById(meeting.getHost().getUserId())).thenReturn(null);
        Map<String,String> ret=meetingManagerServcie.meetingAddService(meeting);
        assertEquals("0",ret.get("state"));
        assertEquals("host不存在",ret.get("message"));
    }

    @Test
    public void testMeetingAddServiceService2() {
        MeetingFull meeting=new MeetingFull();
        User host=new User();
        host.setUserId(1);
        meeting.setHost(host);
        User recorder=new User();
        recorder.setUserId(1);
        meeting.setRecorder(recorder);
        Mockito.when(userMapper.findUserById(meeting.getHost().getUserId())).thenReturn(new User()).thenReturn(null);;
        Map<String,String> ret=meetingManagerServcie.meetingAddService(meeting);
        assertEquals("0",ret.get("state"));
        assertEquals("记录者不存在",ret.get("message"));
    }
    
}
