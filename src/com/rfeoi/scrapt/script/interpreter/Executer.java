package com.rfeoi.scrapt.script.interpreter;

public interface Executer {
    void execute(String spiritname, String command, String value, String executedSpirit);
    String getValue(String spiritname, String command, String value, String executedSpirit);
    String getGlobVar(String name);
    void setGlobVar(String name, String value);
    String getPrivVar(String name, String spirit);
    void setPrivVar(String name, String spirit, String value);
}
