package com.rfeoi.scrapt.script.interpreter;

import java.util.ArrayList;
import java.util.Arrays;


public class Command {
    private Executer executer;
    private String spirit;
    private String command;
    private String[] argsUnparsed;

    public Command(String spirit, String command, String[] args, Executer executer) {
        this.executer = executer;
        this.spirit = spirit;
        this.command = command;
        this.argsUnparsed = args;
    }

    public Command(String string, Executer executer) {
        this.executer = executer;
        if (string.split("\\(")[0].contains(Interpreter.classSymbol)) {
            spirit = string.split(Interpreter.classSymbol)[0];
        } else spirit = null;
        command = string.split(Interpreter.classSymbol)[1].split("\\(")[0];
        int remove = 1;
        if (string.endsWith(")}")) remove = 2;
        String unparsedArgs = string.replace(spirit + ";" + command + "(", "");
        unparsedArgs = unparsedArgs.substring(0, unparsedArgs.length() - remove);
        ArrayList<String> unparsedArgsArray = new ArrayList<>();
        if (unparsedArgs.contains(", ")) {
            unparsedArgsArray.addAll(Arrays.asList(unparsedArgs.split(", ")));
        } else {
            unparsedArgsArray.add(unparsedArgs);
        }
        argsUnparsed = (String[]) unparsedArgsArray.toArray();
    }

    public String getValue(String executedSpirit) {
        return executer.getValue(spirit, command, parseArgs(executedSpirit), executedSpirit);
    }

    private String[] parseArgs(String executedSpirit) {
        ArrayList<String> parsed = new ArrayList<>();
        for (String s : argsUnparsed) {
            parseArg(s, executedSpirit);
        }
        return (String[]) parsed.toArray();
    }

    private String parseArg(String arg, String executedSpirit) {
        String request;
        if ((request = getArimeticString(arg, executedSpirit)) != null) return request;
        if ((request = getVar(arg, executedSpirit)) != null) return request;
        if (arg.contains(Interpreter.classSymbol)) {
            String object = arg.split(Interpreter.classSymbol)[0];
            String command = arg.split(Interpreter.classSymbol)[1].split("\\(")[0];
            //TODO...
            // String args = arg.replace(object + Interpreter.classSymbol + command, "");
            // return executer.getValue(object, command, args, executedSpirit);
        }
        return arg;
    }

    private String getArimeticString(String arg, String executedSpirit) {
        String arith = "";
        for (char c : arg.toCharArray()) {
            switch (c + "") {
                case "-":
                    arith = "-";
                    break;
                case "+":
                    arith = "\\+";
                    break;
                case "*":
                    arith = "\\*";
                    break;
                case "/":
                    arith = "/";
                    break;
                case " .. ":
                    arith = " .. ";
                    break;
            }
            if (!arith.isEmpty()) break;
        }
        if (arith.isEmpty()) return null;
        String side0Raw = arg.split(arith)[0];
        String side1Raw = arg.replaceFirst(side0Raw, "");
        String side0;
        String side1;
        //TODO make this do not create new Command instance each time
        if (side0Raw.contains(Interpreter.classSymbol))
            side0 = new Command(side0Raw, executer).getValue(executedSpirit);
        else side0 = side0Raw;
        if (side1Raw.contains(Interpreter.classSymbol))
            side1 = new Command(side1Raw, executer).getValue(executedSpirit);
        else side1 = side1Raw;
        switch (arith) {
            case "-": {
                int result = Integer.parseInt(side0) - Integer.parseInt(side1);
                return result + "";
            }
            case "\\+": {
                int result = Integer.parseInt(side0) + Integer.parseInt(side1);
                return result + "";
            }
            case "\\*": {
                int result = Integer.parseInt(side0) * Integer.parseInt(side1);
                return result + "";
            }
            case "/": {
                int result = Integer.parseInt(side0) / Integer.parseInt(side1);
                return result + "";
            }
            case " .. ":
                return side0 + side1;

        }
        return null;
    }

    private String getVar(String arg, String executedSpirit) {
        if (arg.startsWith("$")) {
            if (arg.contains(Interpreter.classSymbol)) {
                if (arg.contains("=")) {
                    executer.setPrivVar(arg.split("=")[0].split(Interpreter.classSymbol)[1].replaceFirst("$", ""), arg.split(Interpreter.classSymbol)[0], arg.split("=")[1]);
                }
                return executer.getPrivVar(arg.replaceFirst("$", ""), executedSpirit);
            } else {
                if (arg.contains("=")) {
                    executer.setGlobVar(arg.split("=")[0].replaceFirst("$", ""), arg.split("=")[1]);
                }
                return executer.getGlobVar(arg.replaceFirst("$", ""));
            }
        }
        return null;
    }


}
