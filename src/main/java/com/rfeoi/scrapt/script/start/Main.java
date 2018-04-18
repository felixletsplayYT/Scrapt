package com.rfeoi.scrapt.script.start;

import com.rfeoi.scrapt.script.fileInterpreter.Loader;
import com.rfeoi.scrapt.script.scrapt.ScraptLoader;

import java.io.File;
import java.io.IOException;

public class Main {
    private static ScraptLoader loader;
    public static void main(String[] args){
        if (args.length == 1){
            loader = new ScraptLoader();
            loader.loadFiles(new File(args[0]).getParentFile());
            loader.start();
        }else{
            System.out.println("usage: scrapt.jar <path to config file>");
        }
    }
}
