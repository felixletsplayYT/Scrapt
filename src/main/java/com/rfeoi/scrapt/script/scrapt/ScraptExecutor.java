package com.rfeoi.scrapt.script.scrapt;

import com.rfeoi.scrapt.script.interpreter.Executor;

public class ScraptExecutor implements Executor {
    @Override
    public void execute(String spiritname, String command, String[] args, String executedSpirit) {

    }

    @Override
    public String getValue(String spiritname, String command, String[] args, String executedSpirit) {
        return null;
    }

    @Override
    public String getGlobVar(String name) {
        return null;
    }

    @Override
    public void setGlobVar(String name, String value) {

    }

    @Override
    public String getPrivVar(String name, String spirit) {
        return null;
    }

    @Override
    public void setPrivVar(String name, String spirit, String value) {

    }
}
