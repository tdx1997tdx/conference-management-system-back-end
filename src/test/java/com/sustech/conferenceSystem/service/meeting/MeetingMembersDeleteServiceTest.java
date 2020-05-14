package com.sustech.conferenceSystem.service.meeting;
import com.sustech.conferenceSystem.dto.MeetingFull;
import com.sustech.conferenceSystem.dto.User;
import com.sustech.conferenceSystem.mapper.MeetingMapper;
import com.sustech.conferenceSystem.mapper.UserAndMeetingMapper;
import com.sustech.conferenceSystem.mapper.UserMapper;
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
public class MeetingMembersDeleteServiceTest {
    @InjectMocks
    private MeetingManagerService meetingManagerServcie = new MeetingManagerService();
    @Mock
    private MeetingMapper meetingMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserAndMeetingMapper userAndMeetingMapper;
    @Mock
    private InformService informService;

    @Test
    public void testMeetingMembersDeleteService1() {
        MeetingFull meetingFull=new MeetingFull();
        meetingFull.setMeetingId(1);
        Mockito.when(meetingMapper.meetingMembersDelete(meetingFull)).thenReturn(false);
        Map<String,String> ret=meetingManagerServcie.meetingMembersDeleteService(meetingFull);
        assertEquals("0",ret.get("state"));
        assertEquals("删除失败",ret.get("message"));
    }

    @Test
    public void testMeetingMembersDeleteService2() {
        MeetingFull meetingFull=new MeetingFull();
        meetingFull.setMeetingId(1);
        Mockito.when(meetingMapper.meetingMembersDelete(meetingFull)).thenReturn(true);
        Map<String,String> ret=meetingManagerServcie.meetingMembersDeleteService(meetingFull);
        assertEquals("1",ret.get("state"));
        assertEquals("删除成功",ret.get("message"));
    }
    
}
