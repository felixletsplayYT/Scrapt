package com.rfeoi.scrapt.API.multiplayer.server;


import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerSide {
    ServerSocket socket;
    private Thread[] clientReaders;
    public ServerSide() {

    }
    public void start(int port, int maxPlayer) throws IOException {
        //Creates new Socket with port
        socket = new ServerSocket(port);
        //C


    }

}
