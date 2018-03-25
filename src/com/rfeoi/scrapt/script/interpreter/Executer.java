package com.rfeoi.scrapt.script.interpreter;

public interface Executer {
    void execute(String spiritname, String command, String value, String executedSpirit);
    String getValue(String spiritname, String command, String value, String executedSpirit);
}
