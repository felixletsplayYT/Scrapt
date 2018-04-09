package com.rfeoi.scrapt.script.interpreter;

import java.util.ArrayList;
import java.util.Arrays;


public class Command {
    private Executor executor;
    private String spirit;
    private String command;
    private String[] argsUnparsed;

    public Command(String spirit, String command, String[] args, Executor executor) {
        this.executor = executor;
        this.spirit = spirit;
        this.command = command;
        this.argsUnparsed = args;
    }

    public Command(String string, Executor executor) {
        this.executor = executor;
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
        return executor.getValue(spirit, command, parseArgs(executedSpirit), executedSpirit);
    }
    public void executeCommand(String executedSpirit){
        if (!specialCommands())
        executor.execute(spirit, command, parseArgs(executedSpirit), executedSpirit);
    }
    private boolean specialCommands(){
        //TODO
        return false;
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
            //TODO make this do not create new Command instance each time
            return new Command(arg, executor).getValue(executedSpirit);
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
            side0 = new Command(side0Raw, executor).getValue(executedSpirit);
        else side0 = side0Raw;
        if (side1Raw.contains(Interpreter.classSymbol))
            side1 = new Command(side1Raw, executor).getValue(executedSpirit);
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
                    executor.setPrivVar(arg.split("=")[0].split(Interpreter.classSymbol)[1].replaceFirst("$", ""), arg.split(Interpreter.classSymbol)[0], arg.split("=")[1]);
                }
                return executor.getPrivVar(arg.replaceFirst("$", ""), executedSpirit);
            } else {
                if (arg.contains("=")) {
                    executor.setGlobVar(arg.split("=")[0].replaceFirst("$", ""), arg.split("=")[1]);
                }
                return executor.getGlobVar(arg.replaceFirst("$", ""));
            }
        }
        return null;
    }


}
