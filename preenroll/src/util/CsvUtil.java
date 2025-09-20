package util;

import java.util.*;

public class CsvUtil {
    // Escape: wrap with quotes if contains comma/quote/newline; double quotes
    // inside
    public static String esc(String s) {
        if (s == null)
            return "";
        boolean need = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String v = s.replace("\"", "\"\"");
        return need ? ("\"" + v + "\"") : v;
    }

    public static List<String> parseLine(String line) {
        List<String> out = new ArrayList<>();
        if (line == null)
            return out;
        int i = 0;
        StringBuilder cur = new StringBuilder();
        boolean inQ = false;
        while (i < line.length()) {
            char ch = line.charAt(i);
            if (inQ) {
                if (ch == '\"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                        cur.append('"');
                        i++;
                    } else
                        inQ = false;
                } else
                    cur.append(ch);
            } else {
                if (ch == '\"')
                    inQ = true;
                else if (ch == ',') {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else
                    cur.append(ch);
            }
            i++;
        }
        out.add(cur.toString());
        return out;
    }
}