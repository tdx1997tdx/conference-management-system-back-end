package com.sustech.conferenceSystem.service.inform;

public class InformConstants {
    enum Role {HOST, RECORDER, MEMBER}
    enum Reason {MODIFY, CREATE, DELETE, OPENBEFORE, CLOSEBEFORE}
    public static final long BEFORE_MEETING_OPEN_INFORM = 20;
    public static final long BEFORE_MEETING_OPEN = 10;
    public static final long BEFORE_MEETING_CLOSE_INFORM = 20;
    public static final String WEBSOCKET_URI = "ws://localhost:8080/";

    public static final boolean INFORM_TEST_ON = false;

}
