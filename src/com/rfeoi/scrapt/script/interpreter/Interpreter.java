package com.rfeoi.scrapt.script.interpreter;

import javax.sound.midi.SysexMessage;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;

public class Interpreter {
    private ArrayList<String> lines;
    private Executer executer;
    private String spiritname;

    public Interpreter(File file, Executer executer, String spiritName) throws IOException {
        lines = new ArrayList<>();
        this.executer = executer;
        this.spiritname = spiritName;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null){
            lines.add(line);
        }
        reader.close();
    }

    public void produce() {
        boolean inIf = false;
        boolean ifTrue = false;
        for (String s : lines) {
            if (s.equals("}")) {
                inIf = false;
                ifTrue = false;
            } else {
                if (!(inIf && !ifTrue)) {
                    if (s.contains("->")) {
                        String object = s.split("->")[0];
                        String command = s.split("->")[1].split("\\(")[0];
                        String args = s.replace(object + "->" + command + "(", "");
                        args = args.replaceFirst("\\)", "");
                        if (args.contains("->")) args = getValue(args);
                        executer.execute(object, command, args, spiritname);
                    } else {
                        if (s.contains("(")) {
                            String command = s.split("\\(")[0];
                            String args = s.split("\\(")[1].replace(")", "");
                            switch (command) {
                                case "wait":
                                    try {
                                        Thread.sleep(Long.parseLong(args) * 1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "if":
                                    inIf = true;
                                    //TODO more testing options
                                    if (args.contains("=")) {
                                        String side1 = args.split("=")[0];
                                        String side2 = args.split("=")[1];
                                        if (side1.contains("->")){
                                            side1 = getValue(side1);
                                        }
                                        if (side2.contains("->")){
                                            side2 = getValue(side2);
                                        }
                                        if (side1.equalsIgnoreCase(side2)) ifTrue = true;
                                    }
                                    break;
                            }

                        }
                    }
                }
            }
        }
    }
    private String getValue(String cmd){
        String object = cmd.split("->")[0];
        String command = cmd.split("->")[1].split("\\(")[0];
        String args = cmd.split("\\(")[1].replace(")", "");
        return executer.getValue(object, command, args, spiritname);
    }
}
