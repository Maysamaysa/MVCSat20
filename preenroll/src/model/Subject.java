package model;

public class Subject {
    public String code;
    public String name;
    public int credits;
    public String teacher;
    public String prereqCode; // nullable

    public Subject(String c, String n, int cr, String t, String pre) {
        code = c;
        name = n;
        credits = cr;
        teacher = t;
        prereqCode = pre;
    }
}