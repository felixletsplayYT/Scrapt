package com.rfeoi.scrapt.script.fileInterpreter;

import com.rfeoi.scrapt.API.Listener;
import com.rfeoi.scrapt.API.frame.WindowFrame;
import com.rfeoi.scrapt.API.objects.Spirit;
import com.rfeoi.scrapt.script.interpreter.Executer;
import com.rfeoi.scrapt.script.interpreter.Interpreter_OLD;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Loader implements Executer, Listener {

    private String name = "";
    private WindowFrame windowFrame;
    private File folder;
    private HashMap<String, ArrayList<Interpreter_OLD>> keyEvents;
    private HashMap<String, String> globVariables;
    private ArrayList<Interpreter_OLD> starter;
    private HashMap<String, HashMap<String, Interpreter_OLD>> blocks;
    private HashMap<String, HashMap<String, String>> privVars;
    private ArrayList<Interpreter_OLD> mouseListener;
    private ScraptFileParser parser;

    public Loader(File configFile) throws IOException {
        parser = new ScraptFileParser(configFile);
        this.folder = configFile.getParentFile();
        keyEvents = new HashMap<>();
        globVariables = new HashMap<>();
        mouseListener = new ArrayList<>();
        starter = new ArrayList<>();
        blocks = new HashMap<>();
        privVars = new HashMap<>();
        loadConfigFile(configFile);
        windowFrame = new WindowFrame(new Dimension(1000, 1000), name, this);
        loadClasses();
        startRoutine();
    }
    private void loadConfigFile(File configFile){
        if (parser.getValues().get("name") != null) {
            name = parser.getValues().get("name");
            if (!name.equals(configFile.getName().replace(".scrapt", ""))) {
                System.err.println("Error: name attribute is not equals to file name");
            }
        } else {
            System.err.println("name attribute missing");
            System.exit(1);
        }
    }
    private void loadClasses() throws IOException {
        if (!folder.isDirectory()) return;
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                if (file.getName().equalsIgnoreCase("background")) {
                    //TODO add Background listener
                    File file1 = file.listFiles()[0];
                    if (file1.isDirectory()) return;
                    windowFrame.setBackgroundImage(file1);
                } else {
                    ArrayList<File> costumes = new ArrayList<>();
                    for (File file1 : file.listFiles()) {
                        if (file1.isDirectory()) {
                            if (file1.getName().equalsIgnoreCase("costumes")) {
                                costumes.addAll(Arrays.asList(file1.listFiles()));
                            }
                        } else {
                            if (file1.getName().startsWith("keylistener_")) {
                                String key = file1.getName().replace("keylistener_", "");
                                addListener(key, new Interpreter_OLD(file1, this, file.getName()));
                            } else if (file1.getName().equalsIgnoreCase("start")) {
                                starter.add(new Interpreter_OLD(file1, this, file.getName()));
                            } else if (file1.getName().equalsIgnoreCase("mouselistener")) {
                                mouseListener.add(new Interpreter_OLD(file1, this, file.getName()));
                            } else if (file1.getName().startsWith("block_")) {
                                String blockname = file1.getName().replace("block_", "");
                                blocks.computeIfAbsent(file.getName(), k -> new HashMap<>());
                                blocks.get(file.getName()).put(blockname, new Interpreter_OLD(file1, this, file.getName()));
                            }
                        }
                    }
                    windowFrame.spirits.put(file.getName(), new Spirit(costumes.get(0), 100, 10));
                    for (File costume : costumes) {
                        windowFrame.spirits.get(file.getName()).addCostumeImage(costume);
                    }
                }
            }
        }
        if (name.isEmpty()) {
            System.err.println("No config File found");
            System.exit(1);
        }
    }

    private void addListener(String key, Interpreter_OLD interpreter) {
        keyEvents.computeIfAbsent(key, k -> new ArrayList<>());
        keyEvents.get(key).add(interpreter);
    }

    @Override
    public void execute(String spiritname, String command, String value, String executedSpirit) {
        String spirit = null;
        if (spiritname.equalsIgnoreCase("this")) spirit = executedSpirit;
        else spirit = spiritname;
        switch (command) {
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
                break;
            case "stopAtWall":
                windowFrame.stopAtWall(spirit);
                break;
            case "setWidth":
                if (value == null || value.isEmpty()) break;
                windowFrame.spirits.get(spirit).setWidth(Integer.parseInt(value));
                break;
            case "setHeight":
                if (value == null || value.isEmpty()) break;
                windowFrame.spirits.get(spirit).setHeight(Integer.parseInt(value));
                break;
            case "debug":
                if (value == null || value.isEmpty()) break;
                System.out.println("Debug message by Scrapt Language (Spirit: " + spirit + "): " + value);
                break;
            default:
                if (blocks.get(spirit) != null) {
                    if (blocks.get(spirit).get(command) != null) {
                        start(blocks.get(spirit).get(command));
                    }
                }
                break;
        }
    }

    private void start(Interpreter_OLD interpreter) {
        new Thread(() -> interpreter.produce()).start();
    }

    @Override
    public String getValue(String spiritname, String command, String value, String executedSpirit) {
        String spirit = null;
        if (spiritname.equalsIgnoreCase("this")) spirit = executedSpirit;
        else spirit = spiritname;
        if (spirit.equalsIgnoreCase("mouse")) {
            switch (command) {
                case "getX":
                    return "" + windowFrame.getMouseX();
                case "getY":
                    return "" + windowFrame.getMouseY();
            }
        }
        switch (command) {
            case "getX":
                return "" + windowFrame.spirits.get(spirit).getLocationX();
            case "getY":
                return "" + windowFrame.spirits.get(spirit).getLocationY();
            case "touches":
                if (value == null || value.isEmpty()) return null;
                return windowFrame.touches(spirit, value) + "";
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

    private void startRoutine() {
        for (int i = 0; i < starter.size(); i++) {
            start(starter.get(i));
        }
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if (mouseListener.size() > 0) {
            for (int i = 0; i < mouseListener.size(); i++) {
                start(mouseListener.get(i));
            }
        }
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
        if (keyEvents.get(keyEvent.getKeyCode() + "") == null) return;
        for (int i = 0; i < keyEvents.get(keyEvent.getKeyCode() + "").size(); i++) {
            start(keyEvents.get(keyEvent.getKeyCode() + "").get(i));
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
