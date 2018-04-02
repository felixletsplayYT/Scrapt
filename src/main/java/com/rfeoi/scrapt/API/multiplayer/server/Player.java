package com.rfeoi.scrapt.API.multiplayer.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Server side
 */
public class Player {
    private Socket socket;
    private ConnectionStatus connectionStatus;
    private String username;
    private ServerConnection server;
    private OutputStreamWriter writer;
    public Player(Socket socket, ServerConnection serverConnection) throws IOException {
        this.socket = socket;
        this.server = serverConnection;
        connectionStatus = ConnectionStatus.DISCONNECTED;
        writer = new OutputStreamWriter(this.socket.getOutputStream());
    }

    private void makeHandshake() throws IOException {
        writer.write(server.getServerVersion());
        connectionStatus = ConnectionStatus.HANDSHAKE_VERSION;
    }
    public void kick(String reason){
        //TODO
    }
    private Runnable readThread = () -> {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;
            while((input = reader.readLine()) != null){
                switch(connectionStatus.getID()){
                    case 0:
                        System.err.println("Client is connected, but it is disconnected.");
                        break;
                    case 1:
                        //TODO
                        break;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

}
