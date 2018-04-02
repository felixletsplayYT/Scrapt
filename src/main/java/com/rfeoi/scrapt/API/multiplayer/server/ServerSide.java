package com.rfeoi.scrapt.API.multiplayer.server;




import java.io.IOException;
import java.net.ServerSocket;

public class ServerSide {
    ServerSocket socket;
    public ServerSide() {

    }
    public void start(int port, int maxPlayer) throws IOException {
        //Creates new Socket with port
        socket = new ServerSocket(port);



    }

}
