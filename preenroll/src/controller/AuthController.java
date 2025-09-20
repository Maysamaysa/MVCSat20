package controller;

import repository.*;
import model.*;

import java.util.*;

public class AuthController {
    private final UserRepository userRepo;
    private final StudentRepository studentRepo;

    public AuthController(UserRepository u, StudentRepository s) {
        this.userRepo = u;
        this.studentRepo = s;
    }

    public Optional<User> login(String u, String p) {
        Optional<User> user = userRepo.find(u, p);
        if (user.isPresent() && user.get().role == User.Role.STUDENT) {
            Student s = studentRepo.get(user.get().studentCodeOrNull);
            if (s == null || s.getAge() < 15)
                return Optional.empty();
        }
        return user;
    }
}