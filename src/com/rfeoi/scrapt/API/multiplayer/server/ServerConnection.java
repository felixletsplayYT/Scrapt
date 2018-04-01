package com.rfeoi.scrapt.API.multiplayer.server;

public interface ServerConnection {
    boolean allowUsername(String username);
    String getServerVersion();

}
