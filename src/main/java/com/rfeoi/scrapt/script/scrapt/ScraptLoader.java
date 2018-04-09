package com.rfeoi.scrapt.script.scrapt;

import com.rfeoi.scrapt.script.fileInterpreter.FileInterpreter;
import com.rfeoi.scrapt.script.interpreter.Executor;

import java.io.File;
import java.util.HashMap;

public class ScraptLoader  {

    //VARS
    private Executor executor;
    private HashMap<String, HashMap<FileInterpreter, String>> keyListener;


    public ScraptLoader(){
        executor = new ScraptExecutor();
        keyListener = new HashMap<>();
    }
    private void loadFiles(File directory){
        if (!directory.isDirectory()) return;
        for (File spirit : directory.listFiles()){
            if (!spirit.isDirectory()) break;
            for(File scriptFile : spirit.listFiles()){
                if (scriptFile.isDirectory()){
                    switch (directory.getName()){
                        case "costumes":
                            loadCostumes(scriptFile, spirit.getName());
                            break;
                    }
                }else{
                    if (!scriptFile.getName().endsWith(".spt")) break;
                    if (scriptFile.getName().startsWith("keyListener_")){
                        try{
                            addKeyListener(scriptFile.getName().split("_")[1], spirit.getName(), scriptFile);
                        }catch(Exception ex){
                            System.err.println("Error in spt file \"" + scriptFile.getAbsolutePath() + "\" thrown with " + ex.getMessage());
                        }
                    }else if (scriptFile.getName().equals("mouseListener")){

                    }else if (scriptFile.getName().equals("start")){

                    }
                }
            }
        }
    }

    private void addKeyListener(String key, String spirit, File scriptFile){
        keyListener.computeIfAbsent(key, k -> new HashMap<>());
        keyListener.get(key).put(new FileInterpreter(executor), spirit);
        //TODO Let FileInterpreter read File
    }
    private void loadCostumes(File folder, String spirit){

    }
}
