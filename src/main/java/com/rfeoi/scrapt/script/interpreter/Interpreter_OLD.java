package com.rfeoi.scrapt.script.interpreter;

import java.io.*;
import java.util.ArrayList;

@Deprecated
public class Interpreter_OLD {
    private final String classSymbol = ";";
    private ArrayList<String> lines;
    private Executer executer;
    private String spiritname;

    public Interpreter_OLD(File file, Executer executer, String spiritName) throws IOException {
        lines = new ArrayList<>();
        this.executer = executer;
        this.spiritname = spiritName;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
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
                    if (!s.contains("if") && !s.contains("wait")) {
                        String object = s.split(classSymbol)[0];
                        String command = s.split(classSymbol)[1].split("\\(")[0];
                        String args = s.replace(object + classSymbol + command + "(", "");
                        args = args.substring(0, args.length() - 1);
                        if (args.contains(classSymbol)) args = getValue(args);
                        executer.execute(object, command, args, spiritname);
                    } else {
                        if (s.contains("(")) {
                            String command = s.split("\\(")[0];
                            String args = getSecondbyChar(s, "(".toCharArray()[0]).replace("){", "");
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
                                    if (args.contains("=")) {
                                        String side1 = args.split("=")[0];
                                        String side2 = args.split("=")[1];
                                        if (side1.contains(classSymbol)) {
                                            side1 = getValue(side1);
                                        }
                                        if (side2.contains(classSymbol)) {
                                            side2 = getValue(side2);
                                        }
                                        if (side1.equalsIgnoreCase(side2)) ifTrue = true;
                                    }
                                    if (args.contains(">")) {
                                        String side1 = args.split(">")[0];
                                        String side2 = args.split(">")[1];
                                        if (side1.contains(classSymbol)) {
                                            side1 = getValue(side1);
                                        }
                                        if (side2.contains(classSymbol)) {
                                            side2 = getValue(side2);
                                        }
                                        if (Integer.parseInt(side1) > Integer.parseInt(side2)) ifTrue = true;
                                    }
                                    if (args.contains("<")) {
                                        String side1 = args.split("<")[0];
                                        String side2 = args.split("<")[1];
                                        if (side1.contains(classSymbol)) {
                                            side1 = getValue(side1);
                                        }
                                        if (side2.contains(classSymbol)) {
                                            side2 = getValue(side2);
                                        }
                                        if (Integer.parseInt(side1) < Integer.parseInt(side2)) ifTrue = true;
                                    }
                            }

                        }
                    }
                }
            }
        }
    }

    private String getValue(String cmd) {
        String result;
        if (cmd.contains("-")) {
            String[] sides = cmd.split("-");
            int side0;
            int side1;
            if (sides[0].contains(classSymbol)) side0 = Integer.parseInt(getValue(sides[0]));
            else side0 = Integer.parseInt(sides[0]);
            if (sides[1].contains(classSymbol)) side1 = Integer.parseInt(getValue(sides[1]));
            else side1 = Integer.parseInt(sides[1]);
            int resultInt = side0 - side1;
            result = resultInt + "";
        } else if (cmd.contains("+")) {
            String[] sides = cmd.split("\\+");
            int side0;
            int side1;
            if (sides[0].contains(classSymbol)) side0 = Integer.parseInt(getValue(sides[0]));
            else side0 = Integer.parseInt(sides[0]);
            if (sides[1].contains(classSymbol)) side1 = Integer.parseInt(getValue(sides[1]));
            else side1 = Integer.parseInt(sides[1]);
            int resultInt = side0 + side1;
            result = resultInt + "";
        } else if (cmd.contains("*")) {
            String[] sides = cmd.split("\\*");
            int side0;
            int side1;
            if (sides[0].contains(classSymbol)) side0 = Integer.parseInt(getValue(sides[0]));
            else side0 = Integer.parseInt(sides[0]);
            if (sides[1].contains(classSymbol)) side1 = Integer.parseInt(getValue(sides[1]));
            else side1 = Integer.parseInt(sides[1]);
            int resultInt = side0 * side1;
            result = resultInt + "";
        } else if (cmd.contains("/")) {
            String[] sides = cmd.split("/");
            int side0;
            int side1;
            if (sides[0].contains(classSymbol)) side0 = Integer.parseInt(getValue(sides[0]));
            else side0 = Integer.parseInt(sides[0]);
            if (sides[1].contains(classSymbol)) side1 = Integer.parseInt(getValue(sides[1]));
            else side1 = Integer.parseInt(sides[1]);
            int resultInt = side0 / side1;
            result = resultInt + "";
        } else {
            String object = cmd.split(classSymbol)[0];
            String command = cmd.split(classSymbol)[1].split("\\(")[0];
            String args = cmd.split("\\(")[1].replace(")", "");
            result = executer.getValue(object, command, args, spiritname);
        }

        return result;
    }

    private String getSecondbyChar(String string, char ch) {
        String s = "";
        boolean add = false;
        for (char c : string.toCharArray()) {
            if (add) {
                s += c;
            } else {
                if (c == ch) {
                    add = true;
                }
            }
        }
        return s;
    }

}
