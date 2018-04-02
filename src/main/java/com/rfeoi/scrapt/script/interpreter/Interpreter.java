package com.rfeoi.scrapt.script.interpreter;

public class Interpreter {
    private final String classSymbol = ";";
    /*
    0: If it is in If
    1: If if condition is true
     */
    private boolean[] var_if = {false, false};
    public Interpreter(){

    }
    public void interpret(String line){
        if (defaultTests(line)) return;
        if(line.contains("(")){
            if (line.split("\\(")[0].contains(";")) {
                if (specialCommand(line)) return;
            }else{
               if (normalCommand(line)) return;
            }
        }else{
            System.err.println("'(' missing");
        }
    }

    /**
     * @param line which should be inspected
     * @return if line is finished
     */
    private boolean defaultTests(String line){
        if (var_if[0] && line.equals("}")){
            var_if[0] = false;
            var_if[1] = false;
            return true;
        }
        if (var_if[0] && !var_if[1]) return true;
        return false;
    }

    private boolean specialCommand(String line){
        //TODO
        return false;
    }

    private boolean normalCommand(String line){
        //TODO
        return false;
    }

}
