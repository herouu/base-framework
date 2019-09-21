//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class ParameterFilterUtils {
    private static final Map<String, String> filterMap = new HashMap();
    private static String[] EscapeChars;

    private ParameterFilterUtils() {
        throw new UnsupportedOperationException("不允许创建实例");
    }

    public static String filterParameter(String parameter) {
        if (StringUtils.isEmpty(parameter)) {
            return parameter;
        } else {
            Iterator var1 = filterMap.entrySet().iterator();

            while(var1.hasNext()) {
                Entry<String, String> entry = (Entry)var1.next();
                if (parameter.contains((CharSequence)entry.getKey())) {
                    parameter = parameter.replaceAll((String)entry.getKey(), (String)entry.getValue());
                }
            }

            return parameter;
        }
    }

    static boolean inEscapeChars(Character character) {
        String cs = character.toString();
        String[] var2 = EscapeChars;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            if (s.equals(cs)) {
                return true;
            }
        }

        return false;
    }

    public static String escapeLike(String parameter) {
        if (StringUtils.isEmpty(parameter)) {
            return parameter;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            char[] var2 = parameter.toCharArray();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Character character = var2[var4];
                if (inEscapeChars(character)) {
                    stringBuilder.append("/");
                }

                stringBuilder.append(character);
            }

            return stringBuilder.toString();
        }
    }

    public static String escapeParameter(String parameter) {
        if (StringUtils.isEmpty(parameter)) {
            return parameter;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            char[] var2 = parameter.toCharArray();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Character character = var2[var4];
                stringBuilder.append("/").append(character);
            }

            return stringBuilder.toString();
        }
    }

    static {
        filterMap.put("select", "");
        filterMap.put("insert", "");
        filterMap.put("update", "");
        filterMap.put("delete", "");
        filterMap.put("union", "");
        filterMap.put("into", "");
        filterMap.put("load_file", "");
        filterMap.put("outfile", "");
        filterMap.put("script", "");
        filterMap.put("!", "");
        filterMap.put("@", "");
        filterMap.put("#", "");
        filterMap.put("$", "");
        filterMap.put("^", "");
        filterMap.put("*", "");
        filterMap.put("%", "");
        filterMap.put("\\(", "");
        filterMap.put("\\)", "");
        filterMap.put("\\+", "");
        filterMap.put("\\-", "");
        filterMap.put("\\[", "");
        filterMap.put("\\]", "");
        filterMap.put("\\{", "");
        filterMap.put("\\}", "");
        filterMap.put(";", "");
        filterMap.put(":", "");
        filterMap.put("&", "&amp;");
        filterMap.put("<", "&lt;");
        filterMap.put(">", "&gt;");
        filterMap.put("\"", "&quot;");
        filterMap.put("\\'", "&apos;");
        filterMap.put("？", "&iexcl;");
        filterMap.put("?", "&not;");
        filterMap.put(",", "");
        filterMap.put(".", "");
        filterMap.put("/", "");
        filterMap.put("\r", "");
        filterMap.put("\n", "");
        EscapeChars = new String[]{"/", "_", "%"};
    }
}
