package com.rfeoi.scrapt.script.interpreter;

import java.io.File;
import java.util.ArrayList;

public class Interpreter {
    private ArrayList<String> lines;
    private Executer executer;

    public Interpreter(File file, Executer executer) {
        lines = new ArrayList<>();
        this.executer = executer;
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
                        String args = s.split("\\(")[1].replace(")", "");
                        executer.execute(object, command, args);
                    } else {
                        if (s.contains("(")) {
                            String command = s.split("\\(")[0];
                            String args = s.split("\\(")[1].replace(")", "");
                            //TODO IF!
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
        String side2_object = cmd.split("->")[0];
        String side2_command = cmd.split("->")[1].split("\\(")[0];
        String side2_args = cmd.split("\\(")[1].replace(")", "");
        return executer.getValue(side2_object, side2_command, side2_args);
    }
}
