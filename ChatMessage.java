package com.winway.onlinechat.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ChatMessage implements Serializable
{
    private final Date time;
    private final ArrayList<String> to;
    private final String text;
    private final String from;
    private final String chatName;
    private String receiver;
    
    public ChatMessage(Date time, String chatName, String text, String from, ArrayList<String> to)
    {
        this.time = time;
        this.chatName = chatName;
        this.text = text;
        this.from = from;
        this.to = to;
    }
    
    public ChatMessage(ChatMessage chatMessage, String receiver)
    {
        this.time = chatMessage.getTime();
        this.chatName = chatMessage.getChatName();
        this.text = chatMessage.getText();
        this.from = chatMessage.getFrom();
        this.to = chatMessage.getTo();
        this.receiver = receiver;
    }
    
    public String getReceiver()
    {
        return receiver;
    }
    
    public Date getTime()
    {
        return time;
    }
    
    public String getChatName()
    {
        return chatName;
    }
    
    public String getText()
    {
        return text;
    }
    
    public String getFrom()
    {
        return from;
    }
    
    public ArrayList<String> getTo()
    {
        return to;
    }
}
