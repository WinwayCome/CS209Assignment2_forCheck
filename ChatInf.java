package com.winway.onlinechat.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class ChatInf
{
    ArrayList<ChatMessage> chatMessages;
    Date lastDate;
    String chatName;
    ArrayList<String> users;
    
    public ChatInf(ChatMessage chat)
    {
        this.chatMessages = new ArrayList<>();
        this.chatMessages.add(chat);
        this.lastDate = chat.getTime();
        this.chatName = chat.getChatName();
        this.users = chat.getTo();
    }
    
    public ChatInf()
    {
        this.chatMessages = new ArrayList<>();
        this.lastDate = null;
        this.chatName = null;
        this.users = new ArrayList<>();
    }
    
    public ChatInf(String chatName, ArrayList<User> users)
    {
        this.chatMessages = new ArrayList<>();
        this.lastDate = new Date(1);
        this.chatName = chatName;
        this.users = (ArrayList<String>) users.stream().map(User::getName).collect(Collectors.toList());
    }
    
    public ArrayList<String> getUsers()
    {
        return users;
    }
    
    public ArrayList<ChatMessage> getChatMessages()
    {
        return chatMessages;
    }
    
    public String getChatName()
    {
        return chatName;
    }
    
    public void add(ChatMessage chat)
    {
        this.chatMessages.add(chat);
        if(this.lastDate == null || this.lastDate.before(chat.getTime())) this.lastDate = chat.getTime();
    }
}
