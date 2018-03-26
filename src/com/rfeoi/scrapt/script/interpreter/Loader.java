package com.rfeoi.scrapt.script.interpreter;

import com.rfeoi.scrapt.API.Listener;
import com.rfeoi.scrapt.API.frame.WindowFrame;
import com.rfeoi.scrapt.API.objects.Spirit;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Loader implements Executer, Listener{
    private WindowFrame windowFrame;
    private File folder;
    private HashMap<String, ArrayList<Interpreter>> keyEvents;
    private HashMap<String, String> globVariables;
    private ArrayList<Interpreter> starter;
    private HashMap<String, HashMap<String, Interpreter>> blocks;
    private HashMap<String, HashMap<String, String>> privVars;

    public Loader(File folder) throws IOException {
        this.folder = folder;
        keyEvents = new HashMap<>();
        globVariables = new HashMap<>();
        starter = new ArrayList<>();
        blocks = new HashMap<>();
        privVars = new HashMap<>();
        //TODO size dinamic
        windowFrame = new WindowFrame(new Dimension(1000, 1000), folder.getName(), this);
        loadClasses();
        startRoutine();
    }
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
                            }else if (file1.getName().startsWith("block_")){
                                String blockname = file1.getName().replace("block_", "");
                                if (blocks.get(file.getName()) == null) blocks.put(file.getName(), new HashMap<>());
                                blocks.get(file.getName()).put(blockname, new Interpreter(file1, this, file.getName()));
                            }
                        }
                    }
                    //TOD widt, height non static
                    windowFrame.spirits.put(file.getName(), new Spirit(costumes.get(0), 200, 60));
                    for (File costume : costumes){
                        windowFrame.spirits.get(file.getName()).addCostumeImage(costume);
                    }
                }
            }
        }
    }
    private void addListener(String key, Interpreter interpreter){
        if (keyEvents.get(key) == null) keyEvents.put(key, new ArrayList<>());
        keyEvents.get(key).add(interpreter);
    }
    @Override
    public void execute(String spiritname, String command, String value, String executedSpirit) {
        String spirit = null;
        if (spiritname.equalsIgnoreCase("this")) spiritname = executedSpirit;
        else spirit = spiritname;
        switch (command){
            case "setX":
                if (value == null || value.isEmpty()) break;
                windowFrame.spirits.get(spirit).setLocationX(Integer.parseInt(value));
                break;
            case "setY":
                if (value == null || value.isEmpty()) break;
                windowFrame.spirits.get(spirit).setLocationY(Integer.parseInt(value));
                break;
            case "step":
                if (value == null || value.isEmpty()) break;
                windowFrame.spirits.get(spirit).step(Integer.parseInt(value));
                break;
            case "show":
                windowFrame.spirits.get(spirit).show();
                break;
            case "hide":
                windowFrame.spirits.get(spirit).hide();
                break;
            case "changeCostume":
                if (value == null || value.isEmpty()) break;
                windowFrame.spirits.get(spirit).setCostume(Integer.parseInt(value));
            default:
                if (blocks.get(spirit).get(command) != null){
                    start(blocks.get(spirit).get(command));
                }
                break;
        }
    }

    private void start(Interpreter interpreter){
        new Thread(() -> interpreter.produce()).start();
    }
    @Override
    public String getValue(String spiritname, String command, String value, String executedSpirit) {
        String spirit = null;
        if (spiritname.equalsIgnoreCase("this")) spiritname = executedSpirit;
        else spirit = spiritname;
        if (spirit.equalsIgnoreCase("mouse")){
            switch (command){
                case "getX":
                    return "" + windowFrame.getMouseX();
                case "getY":
                    return "" + windowFrame.getMouseY();
            }
        }
        switch (command){
            case "getX":
                return "" + windowFrame.spirits.get(spirit).getLocationX();
            case "getY":
                return "" + windowFrame.spirits.get(spirit).getLocationY();
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
        if (privVars.get(spirit) == null)privVars.put(spirit, new HashMap<>());
        privVars.get(spirit).put(name, value);
    }
    private void startRoutine(){
        for (int i = 0 ; i< starter.size(); i++){
            start(starter.get(i));
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

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
        for (int i = 0; i < keyEvents.get(keyEvent.getKeyCode()).size(); i++){
            start(keyEvents.get(keyEvent.getKeyCode()).get(i));
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
