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
public class MeetingMembersAddServiceTest {
    @InjectMocks
    private MeetingManagerService meetingManagerServcie = new MeetingManagerService();
    @Mock
    private MeetingMapper meetingMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private InformService informService;

    @Test
    public void testMeetingMembersAddService1() {
        MeetingFull meetingFull=new MeetingFull();
        meetingFull.setMeetingId(1);
        Mockito.when(meetingMapper.meetingSearchCertain(meetingFull.getMeetingId())).thenReturn(null);
        Map<String,String> ret=meetingManagerServcie.meetingMembersAddService(meetingFull);
        assertEquals("0",ret.get("state"));
        assertEquals("会议不存在",ret.get("message"));
    }

    @Test
    public void testMeetingMembersAddService2() {
        MeetingFull meetingFull=new MeetingFull();
        meetingFull.setMeetingId(1);
        Mockito.when(meetingMapper.meetingSearchCertain(meetingFull.getMeetingId())).thenReturn(meetingFull);
        meetingFull.setStartTime(new Date(System.currentTimeMillis()+10 * 60 * 1000));
        Map<String,String> ret=meetingManagerServcie.meetingMembersAddService(meetingFull);
        assertEquals("0",ret.get("state"));
        assertEquals("添加失败,会议在20分钟内开始",ret.get("message"));
    }

    @Test
    public void testMeetingMembersAddService3() {
        MeetingFull meetingFull=new MeetingFull();
        meetingFull.setMeetingId(1);
        List<User> members=new ArrayList<>();
        members.add(new User());
        meetingFull.setMembers(members);
        Mockito.when(meetingMapper.meetingSearchCertain(meetingFull.getMeetingId())).thenReturn(meetingFull);
        meetingFull.setStartTime(new Date(System.currentTimeMillis()+30 * 60 * 1000));
        Mockito.when(userMapper.searchUser(Mockito.any())).thenReturn(new ArrayList<>());
        Map<String,String> ret=meetingManagerServcie.meetingMembersAddService(meetingFull);
        assertEquals("0",ret.get("state"));
        assertEquals("成员已不存在，请刷新页面后重试",ret.get("message"));
    }

    @Test
    public void testMeetingMembersAddService4() {
        MeetingFull meetingFull=new MeetingFull();
        meetingFull.setMeetingId(1);
        List<User> members=new ArrayList<>();
        meetingFull.setMembers(members);
        Mockito.when(meetingMapper.meetingSearchCertain(meetingFull.getMeetingId())).thenReturn(meetingFull);
        meetingFull.setStartTime(new Date(System.currentTimeMillis()+30 * 60 * 1000));
        Mockito.when(meetingMapper.meetingMembersAdd(meetingFull)).thenReturn(false);
        Map<String,String> ret=meetingManagerServcie.meetingMembersAddService(meetingFull);
        assertEquals("0",ret.get("state"));
        assertEquals("添加失败",ret.get("message"));
    }

    @Test
    public void testMeetingMembersAddService5() {
        MeetingFull meetingFull=new MeetingFull();
        meetingFull.setMeetingId(1);
        List<User> members=new ArrayList<>();
        meetingFull.setMembers(members);
        Mockito.when(meetingMapper.meetingSearchCertain(meetingFull.getMeetingId())).thenReturn(meetingFull);
        meetingFull.setStartTime(new Date(System.currentTimeMillis()+30 * 60 * 1000));
        Mockito.when(meetingMapper.meetingMembersAdd(meetingFull)).thenReturn(true);
        Mockito.doNothing().when(informService).meetingInform(meetingFull, InformConstants.InformReason.MODIFY);

        Map<String,String> ret=meetingManagerServcie.meetingMembersAddService(meetingFull);
        assertEquals("1",ret.get("state"));
        assertEquals("添加成功",ret.get("message"));
    }

}
