package com.xboxbedrock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class Parser {
    private String[] args;
    private ArrayList<String> keys = new ArrayList();
    private HashMap<String, String> vals = new HashMap();

    protected Parser(String[] args) {
        this.args = args;
    }

    public void addKey(String... keyarray) {
        this.keys.addAll(Arrays.asList(keyarray));
    }

    public HashMap<String, String> parse() {
        String[] var4;
        int var3 = (var4 = this.args).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            String arg = var4[var2];
            arg = arg.substring(arg.indexOf("_") + 1, arg.lastIndexOf("<"));
            Iterator var6 = this.keys.iterator();

            while(var6.hasNext()) {
                String key = (String)var6.next();
                if (arg.startsWith("key")) {
                    this.vals.put(key, this.sub(arg));
                }
            }
        }

        return this.vals;
    }

    private String sub(String str) {
        return str.substring(str.indexOf(">") + 1);
    }
}
