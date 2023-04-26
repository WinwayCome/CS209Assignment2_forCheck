package com.winway.onlinechat.server;

import com.winway.onlinechat.common.ChatMessage;
import com.winway.onlinechat.common.DataType;
import com.winway.onlinechat.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ServerSingleThread extends Thread {
  private final Socket singleSocket;
  private final ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<>(5000);
  boolean connect = true;
  private ObjectInputStream inputStream;
  private ObjectOutputStream outputStream;
  private boolean CANACCEPT = false;

  public ServerSingleThread(Socket linkSocket) throws IOException {
    this.singleSocket = linkSocket;
    System.out.println("Thread given");
  }

  private void setStream() {
    try {
      outputStream = new ObjectOutputStream(singleSocket.getOutputStream());
      inputStream = new ObjectInputStream(singleSocket.getInputStream());
      System.out.println("Connect.");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void listen() {
    System.out.println("check point listen");
    DataType currentData;
    try {
      if (connect
          && singleSocket.isConnected()
          && (currentData = (DataType) inputStream.readObject()) != null) {
        System.out.println("check point listening");
        switch (currentData) {
          case CHECK -> {
            User cur_check = (User) inputStream.readObject();
            User user = ServerCore.userCheckMap.get(cur_check.getName());
            queue.add(DataType.CHECK);
            if (user.getPassword().equals(cur_check.getPassword())) {
              queue.add(true);
              queue.add(user);
              this.setName(user.getName());
              ServerCore.currentUsers.put(user.getName(), user);
            } else {
              queue.add(false);
            }
          }
          case MESSAGE -> {
            System.out.println("Server read");
            ChatMessage action = (ChatMessage) inputStream.readObject();
            System.out.printf(
                "Thread: %s, Action: %s, %s, %s, %s\n",
                this.getName(),
                action.getChatName(),
                action.getFrom(),
                action.getTo(),
                action.getText());
            System.out.println("Server read done");
            for (String receive : action.getTo())
              ServerCore.actions.add(new ChatMessage(action, receive));
          }
          case SHUTDOWN -> {
            System.out.println("Shutdown " + this.getName());
            ServerCore.currentUsers.remove(this.getName());
            connect = false;
          }
          case ASK -> {
            System.out.println("Ask Users");
            queue.add(DataType.ASK);
          }
          case ACCEPT -> this.CANACCEPT = true;
        }
      }
      System.out.println("Check point listen down");
    } catch (SocketException es) {
      System.exit(0);
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void sendAll() {
    try {
      if (CANACCEPT)
        for (ChatMessage action : ServerCore.actions) {
          System.out.printf(
              "Thread: %s, Action: %s, %s, %s, %s\n",
              this.getName(),
              action.getChatName(),
              action.getFrom(),
              action.getTo(),
              action.getText());
          if (action.getReceiver().equals(this.getName())) {
            outputStream.writeObject(DataType.MESSAGE);
            outputStream.writeObject(action);
            if (ServerCore.actions.remove(action))
              System.out.printf(
                  "Thread: %s, Remove Action: %s, %s, %s, %s\n",
                  this.getName(),
                  action.getChatName(),
                  action.getFrom(),
                  action.getTo(),
                  action.getText());
            outputStream.flush();
          }
        }
      // System.out.println("Check Point");
      while (!queue.isEmpty()) {
        System.out.println("Check Point in queue");
        DataType link = (DataType) queue.poll(5000, TimeUnit.SECONDS);
        System.out.println("Check Point after poll");
        if (link != null) {
          switch (link) {
            case ASK -> {
              outputStream.writeObject(DataType.ASK);
              outputStream.flush();
              outputStream.writeObject(new HashMap<>(ServerCore.currentUsers));
              outputStream.flush();
            }
            case CHECK -> {
              outputStream.writeObject(DataType.CHECK);
              if (queue.poll(5000, TimeUnit.SECONDS) == Boolean.TRUE) {
                outputStream.writeBoolean(true);
                outputStream.writeObject(queue.poll(5000, TimeUnit.SECONDS));
              } else outputStream.writeBoolean(false);
              outputStream.flush();
            }
          }
        }
      }
      // System.out.println("Check Point after while.");
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void run() {
    System.out.println("Start");
    setStream();
    System.out.println("Start1");
    new Thread(
            () -> {
              while (connect) listen();
            })
        .start();
    new Thread(
            () -> {
              while (connect) sendAll();
            })
        .start();
    System.out.println("Start2");
  }
}
