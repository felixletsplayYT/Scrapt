package com.rfeoi.scrapt.script.interpreter;

import java.io.*;
import java.util.HashMap;

public class ScraptFileParser {
    private HashMap<String, String> values;
    ScraptFileParser(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        values = new HashMap<>();
        while ((line = reader.readLine()) != null){
            if (line.contains(":")){
                String setting = line.split(": ")[0];
                String value = line.split(": ")[1];
                values.put(setting, value);
            }
        }
    }

    public HashMap<String, String> getValues() {
        return values;
    }
}
