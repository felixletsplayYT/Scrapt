package com.rfeoi.scrapt.API.multiplayer.server;

public interface ServerConnection {
    boolean allowUsername(String username);
    String getServerVersion();
    void kicked(Player player);
    //Events
    void keyEvent(String keyCode);
    void mousePositionChanged(int x, int y);
    void chat(String message);

}
