package com.rfeoi.scrapt.script.scrapt;

import com.rfeoi.scrapt.API.Listener;
import com.rfeoi.scrapt.script.fileInterpreter.FileInterpreter;
import com.rfeoi.scrapt.script.interpreter.Executor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ScraptLoader implements Listener {

    //VARS
    private Executor executor;
    private HashMap<String, HashMap<FileInterpreter, String>> keyListener;
    private HashMap<FileInterpreter, String> mouseListener;
    private HashMap<FileInterpreter, String> starter;

    public ScraptLoader(){
        //TODO Load from File
        executor = new ScraptExecutor(new Dimension(1000,1000), "Title", this);
        keyListener = new HashMap<>();
        mouseListener = new HashMap<>();
        starter = new HashMap<>();
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
                    try{
                        if (!scriptFile.getName().endsWith(".spt")) break;
                        if (scriptFile.getName().startsWith("keyListener_")){
                            addKeyListener(scriptFile.getName().split("_")[1], spirit.getName(), scriptFile);
                        }else if (scriptFile.getName().equals("mouseListener")){
                            addMouseListener(spirit.getName(), scriptFile);
                        }else if (scriptFile.getName().equals("start")){
                            addStartListener(spirit.getName(), scriptFile);
                        }
                    }catch(Exception ex){
                        System.err.println("Error in spt file \"" + scriptFile.getAbsolutePath() + "\" thrown with " + ex.getMessage());
                    }
                }
            }
        }
    }

    private void addStartListener(String spirit, File scriptFile) throws IOException {
        FileInterpreter interpreter = new FileInterpreter(executor);
        interpreter.readFile(scriptFile);
        starter.put(interpreter, spirit);
    }

    private void addMouseListener(String spirit, File scriptFile) throws IOException {
        FileInterpreter interpreter = new FileInterpreter(executor);
        interpreter.readFile(scriptFile);
        mouseListener.put(interpreter, spirit);
    }
    private void addKeyListener(String key, String spirit, File scriptFile) throws IOException {
        keyListener.computeIfAbsent(key, k -> new HashMap<>());
        FileInterpreter interpreter = new FileInterpreter(executor);
        interpreter.readFile(scriptFile);
        keyListener.get(key).put(interpreter, spirit);
    }
    private void loadCostumes(File folder, String spirit){

    }

    public void start(){
        for (FileInterpreter interpreter : starter.keySet()){
            interpreter.execute(starter.get(interpreter));
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        for (FileInterpreter interpreter : mouseListener.keySet()){
            interpreter.execute(mouseListener.get(interpreter));
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyListener.get(keyEvent.getKeyCode() + "") != null){
            HashMap<FileInterpreter, String> interpreters = keyListener.get(keyEvent.getKeyCode() + "");
            for (FileInterpreter interpreter : interpreters.keySet()){
                interpreter.execute(interpreters.get(interpreter));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
