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
    private Thread readThread;
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
        setUpANDmakeHandshake();
    }


    private synchronized void setUpANDmakeHandshake() throws IOException {
        readThread = new Thread(readThreadRunnable);
        readThread.start();
        writer.write(server.getServerVersion());
        connectionStatus = ConnectionStatus.HANDSHAKE_VERSION;
    }

    public void kick(String reason) throws IOException {
        writer.write("KICK:" + reason);
        server.kicked(this);
    }

    private Runnable readThreadRunnable = () -> {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String input;
            while ((input = reader.readLine()) != null) {
                switch (connectionStatus.getID()) {
                    case 0:
                        System.err.println("Client is connected, but it is disconnected.(That does not make sense)");
                        break;
                    case 1:
                        if (!server.getServerVersion().equals(input)) kick("old version");
                        connectionStatus = ConnectionStatus.HANDSHAKE_USERNAME;
                        writer.write("REQUEST:USERNAME");
                        break;
                    case 2:
                        if (!input.startsWith("USERNAME:")) break;
                        username = input.replace("USERNAME:", "");
                        connectionStatus = ConnectionStatus.CONNECTED;
                        writer.write("OK");
                        break;
                    case 3:
                        if (input.startsWith("KEY_")) server.keyEvent(input.replace("KEY_", ""));
                        else if (input.startsWith("MOUSE_"))
                            server.mousePositionChanged(Integer.parseInt(input.replace("MOUSE_", "").split(":")[0]), Integer.parseInt(input.replace("MOUSE_", "").split(":")[1]));
                        else if (input.startsWith("CHAT_")) server.chat(input.replace("CHAT_", ""));
                        else System.err.println("Client sent: \"" + input + "\", did not understood.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    public void execute(String string) throws IOException {
        writer.write("EXECUTE:" + string);
    }

    public String getUsername() {
        return username;
    }
}
