package model;

public class RegisteredSubject {
    public String studentCode;
    public String subjectCode;
    public String grade; // A,B+,B,C+,C,D+,D,F or null

    public RegisteredSubject(String s, String sub, String g) {
        studentCode = s;
        subjectCode = sub;
        grade = g;
    }
}