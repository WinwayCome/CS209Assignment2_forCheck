package com.winway.onlinechat.server;

import java.io.IOException;

public class ServerApplication
{
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        ServerCore server = new ServerCore();
        server.serverCoreThread();
    }
}
