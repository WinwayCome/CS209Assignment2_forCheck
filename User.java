package com.winway.onlinechat.common;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
    private final String name;
    private final String password;
    public ArrayList<ChatMessage> chatHistory;
    
    public User(String name, String password, ArrayList<ChatMessage> chatHistory)
    {
        this.name = name;
        this.password = password;
        this.chatHistory = chatHistory;
    }
    
    public String getPassword()
    {
        return this.password;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public ArrayList<ChatMessage> getChatHistory()
    {
        return this.chatHistory;
    }
}
