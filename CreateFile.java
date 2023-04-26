package com.winway.onlinechat.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CreateFile
{
    public static void main(String[] args) throws IOException
    {
        ArrayList<User> a = new ArrayList<>();
        a.add(new User("admin", "admin", new ArrayList<>()));
        a.add(new User("test1", "test1", new ArrayList<>()));
        a.add(new User("test2", "test2", new ArrayList<>()));
        a.add(new User("test3", "test3", new ArrayList<>()));
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("Users.dat"));
        outputStream.writeObject(a);
        outputStream.flush();
    }
}
