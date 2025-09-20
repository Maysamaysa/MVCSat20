package model;

public class User {
    public enum Role {
        ADMIN, STUDENT
    }

    public String username;
    public String password;
    public Role role;
    public String studentCodeOrNull;

    public User(String u, String p, Role r, String s) {
        username = u;
        password = p;
        role = r;
        studentCodeOrNull = s;
    }
}