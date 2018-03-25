package com.rfeoi.scrapt.script.interpreter;

import com.rfeoi.scrapt.API.frame.WindowFrame;
import com.rfeoi.scrapt.API.objects.Spirit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Loader implements Executer{
    private WindowFrame windowFrame;
    private File folder;
    private HashMap<String, ArrayList<Interpreter>> keyEvents;
    private ArrayList<Interpreter> starter;
    private HashMap<String, Interpreter> blocks;
    private void loadClasses() throws IOException {
        if (!folder.isDirectory()) return;
        for (File file : folder.listFiles()){
            if (file.isDirectory()){
                if (file.getName().equalsIgnoreCase("background")){
                    //TODO add Background listener
                }else{
                    ArrayList<File> costumes = new ArrayList<>();
                    for (File file1 : file.listFiles()){
                        if (file1.isDirectory()){
                            if (file1.getName().equalsIgnoreCase("costumes")){
                                costumes.addAll(Arrays.asList(file1.listFiles()));
                            }
                        }else{
                            if (file1.getName().startsWith("keylistener_")){
                                String key = file.getName().split("keylistener_")[1];
                                addListener(key, new Interpreter(file1, this, file.getName()));
                            }else if (file1.getName().equalsIgnoreCase("start")){
                                starter.add(new Interpreter(file1, this, file.getName()));
                            }
                        }
                    }
                    //TOD widt, height non static
                    windowFrame.spirits.put(file.getName(), new Spirit(costumes.get(0), 200, 60));
                }
            }
        }
    }
    private void addListener(String key, Interpreter interpreter){
        //TODO
    }
    @Override
    public void execute(String spiritname, String command, String value, String executedSpirit) {
        String spirit = null;
        if (spiritname.equalsIgnoreCase("this")) spiritname = executedSpirit;
        else spirit = spiritname;
        switch (command){
            case "setX":
                if (value == null) break;
                windowFrame.spirits.get(spirit).setLocationX(Integer.parseInt(value));
                break;
            case "setY":
                if (value == null) break;
                windowFrame.spirits.get(spirit).setLocationY(Integer.parseInt(value));
                break;
            case "step":
                if (value == null) break;
                windowFrame.spirits.get(spirit).step(Integer.parseInt(value));
                break;
        }
    }

    @Override
    public String getValue(String spiritname, String command, String value, String executedSpirit) {
        String spirit = null;
        if (spiritname.equalsIgnoreCase("this")) spiritname = executedSpirit;
        else spirit = spiritname;
        switch (command){
            case "getX":
                if (value != null || !value.isEmpty()) break;
                return "" + windowFrame.spirits.get(spirit).getLocationX();
            case "getY":
                if (value != null || !value.isEmpty()) break;
                return "" + windowFrame.spirits.get(spirit).getLocationY();
        }
        return null;
    }
}
