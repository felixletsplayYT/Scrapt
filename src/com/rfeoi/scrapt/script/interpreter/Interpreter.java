package com.rfeoi.scrapt.script.interpreter;

import java.io.File;
import java.util.ArrayList;

public class Interpreter {
    private ArrayList<String> lines;
    private Executer executer;
    public Interpreter(File file, Executer executer){
        lines = new ArrayList<>();
        this.executer = executer;
    }

    public void produce(){
        for (String s : lines){
            if (s.contains("->")){
                String object = s.split("->")[0];
                String command = s.split("->")[1].split("\\(")[0];
                String args = s.split("\\(")[1].replace(")", "");
                executer.execute(object, command, args);
            }else{
                if (s.contains("(")){
                    String command = s.split("\\(")[0];
                    String args = s.split("\\(")[1].replace(")", "");
                    //TODO IF!
                    switch(command){
                        case "wait":
                            try {
                                Thread.sleep(Long.parseLong(args) * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "if":
                            System.err.println("not included yet.");
                            break;
                    }

                }
            }
        }
    }
}
