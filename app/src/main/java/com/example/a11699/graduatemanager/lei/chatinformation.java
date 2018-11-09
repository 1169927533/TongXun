package com.example.a11699.graduatemanager.lei;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class chatinformation implements Serializable{
    public static ArrayList<chatinformation> list=new ArrayList<>();
    /**
     * 姓名
     */
    private String name;
    /**
     * 聊天内容
     */
    private String chatMessage;
    /**
     *
     * @return 是否为本人发送
     */
    private boolean isMeSend;

    public chatinformation(String name, String chatMessage, boolean isMeSend) {
        this.name = name;
        this.chatMessage = chatMessage;
        this.isMeSend = isMeSend;
    }
    public chatinformation(String chatMessage, boolean isMeSend) {
        this.name = name;
        this.chatMessage = chatMessage;
        this.isMeSend = isMeSend;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public boolean isMeSend() {
        return isMeSend;
    }

    public void setMeSend(boolean meSend) {
        isMeSend = meSend;
    }
}
