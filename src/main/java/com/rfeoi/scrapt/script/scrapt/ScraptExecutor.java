package com.rfeoi.scrapt.script.scrapt;

import com.rfeoi.scrapt.API.Listener;
import com.rfeoi.scrapt.API.frame.WindowFrame;
import com.rfeoi.scrapt.API.objects.Spirit;
import com.rfeoi.scrapt.script.fileInterpreter.FileInterpreter;
import com.rfeoi.scrapt.script.interpreter.Executor;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class ScraptExecutor implements Executor {
    private WindowFrame frame;
    private HashMap<String, String> globVariables;
    private HashMap<String, HashMap<String, String>> privVars;
    public ScraptExecutor(Dimension dimesion, String title, Listener listener){
        frame = new WindowFrame(dimesion, title, listener);
        globVariables = new HashMap<>();
        privVars = new HashMap<>();
    }
    public void createSpirit(String name, File[] costumes) throws IOException {
        frame.spirits.put(name, new Spirit(costumes[0], 100, 10));
    }

    @Override
    public void execute(String spiritname, String command, String[] args, String executedSpirit) {
        String value = args[0];
        String spirit = null;
        if (spiritname.equalsIgnoreCase("this")) spirit = executedSpirit;
        else spirit = spiritname;
        switch (command) {
            case "setX":
                if (value == null || value.isEmpty()) break;
                frame.spirits.get(spirit).setLocationX(Integer.parseInt(value));
                break;
            case "setY":
                if (value == null || value.isEmpty()) break;
                frame.spirits.get(spirit).setLocationY(Integer.parseInt(value));
                break;
            case "step":
                if (value == null || value.isEmpty()) break;
                frame.spirits.get(spirit).step(Integer.parseInt(value));
                break;
            case "show":
                frame.spirits.get(spirit).show();
                break;
            case "hide":
                frame.spirits.get(spirit).hide();
                break;
            case "changeCostume":
                if (value == null || value.isEmpty()) break;
                frame.spirits.get(spirit).setCostume(Integer.parseInt(value));
                break;
            case "stopAtWall":
                frame.stopAtWall(spirit);
                break;
            case "setWidth":
                if (value == null || value.isEmpty()) break;
                frame.spirits.get(spirit).setWidth(Integer.parseInt(value));
                break;
            case "setHeight":
                if (value == null || value.isEmpty()) break;
                frame.spirits.get(spirit).setHeight(Integer.parseInt(value));
                break;
            case "debug":
                if (value == null || value.isEmpty()) break;
                System.out.println("Debug message by Scrapt Language (Spirit: " + spirit + "): " + value);
                break;
            default:
               /* if (blocks.get(spirit) != null) {
                    if (blocks.get(spirit).get(command) != null) {
                        start(blocks.get(spirit).get(command), spirit);
                    }
                }*/
                break;
        }
    }

    private void start(FileInterpreter interpreter, String spirit) {
        new Thread(() -> interpreter.execute(spirit)).start();
    }

    @SuppressWarnings("Duplicates")
    @Override
    public String getValue(String spiritname, String command, String[] args, String executedSpirit) {
        String value = args[0];
        String spirit = null;
        if (spiritname.equalsIgnoreCase("this")) spirit = executedSpirit;
        else spirit = spiritname;
        if (spirit.equalsIgnoreCase("mouse")) {
            switch (command) {
                case "getX":
                    return "" + frame.getMouseX();
                case "getY":
                    return "" + frame.getMouseY();
            }
        }
        switch (command) {
            case "getX":
                return "" + frame.spirits.get(spirit).getLocationX();
            case "getY":
                return "" + frame.spirits.get(spirit).getLocationY();
            case "touches":
                if (value == null || value.isEmpty()) return null;
                return frame.touches(spirit, value) + "";
        }
        return null;
    }

    @Override
    public String getGlobVar(String name) {
        return globVariables.get(name);
    }

    @Override
    public void setGlobVar(String name, String value) {
        globVariables.put(name, value);
    }

    @Override
    public String getPrivVar(String name, String spirit) {
        if (privVars.get(spirit) == null) return null;
        return privVars.get(spirit).get(name);
    }

    @Override
    public void setPrivVar(String name, String spirit, String value) {
        privVars.computeIfAbsent(spirit, k -> new HashMap<>());
        privVars.get(spirit).put(name, value);
    }

}
