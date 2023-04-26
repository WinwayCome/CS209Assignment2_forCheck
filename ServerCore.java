package com.winway.onlinechat.server;

import com.winway.onlinechat.common.ChatMessage;
import com.winway.onlinechat.common.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServerCore
{
    public static ConcurrentHashMap<String, User> userCheckMap;
    public static ServerSocket server;
    public static ConcurrentHashMap<String, User> currentUsers;
    public static ArrayList<User> logUsers;
    public static ArrayBlockingQueue<ChatMessage> actions;
    
    public ServerCore() throws IOException, ClassNotFoundException
    {
        server = new ServerSocket(14889);
        currentUsers = new ConcurrentHashMap<>();
        ObjectInputStream userInputStream = new ObjectInputStream(new FileInputStream("Users.dat"));
        logUsers = (ArrayList<User>) userInputStream.readObject();
        userCheckMap = new ConcurrentHashMap<>(logUsers.stream().collect(Collectors.toMap(User::getName, Function.identity())));
        actions = new ArrayBlockingQueue<>(5000);
    }
    
    public void serverCoreThread()
    {
        try
        {
            System.out.println("Server Listening...");
            //noinspection InfiniteLoopStatement
            while(true)
            {
                Socket cur_connection = server.accept();
                System.out.println("Accept");
                ServerSingleThread socketThread = new ServerSingleThread(cur_connection);
                socketThread.start();
                System.out.println("Connecting with client.");
            }
        }
        catch(IOException e)
        {
            System.out.println("Print IOException");
            //e.printStackTrace();
        }
    }
}
