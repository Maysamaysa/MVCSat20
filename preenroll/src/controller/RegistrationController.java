package controller;

import repository.*;
import model.*;

import java.io.*;
import java.time.*;
import java.util.*;
import java.util.regex.*;

public class RegistrationController {
    private final CsvDataStore store;
    private final StudentRepository studentRepo;
    private final SubjectRepository subjectRepo;
    private final SubjectStructureRepository stRepo;
    private final UserRepository userRepo;

    public RegistrationController(CsvDataStore store, StudentRepository sr, SubjectRepository subr,
            SubjectStructureRepository str, UserRepository ur) {
        this.store = store;
        this.studentRepo = sr;
        this.subjectRepo = subr;
        this.stRepo = str;
        this.userRepo = ur;
    }

    public String generateNextStudentCode() {
        int max = 69000000; // ค่าเริ่มต้นก่อนตัวแรก (69 000000)

        for (String code : store.students.keySet()) {
            if (code != null && code.matches("69\\d{6}")) {
                int v = Integer.parseInt(code);
                if (v > max)
                    max = v;
            }
        }

        int next = Math.max(max + 1, 69000001); // ต้อง +1 เสมอ และอย่างน้อย 69000001
        if (next > 69999999) {
            throw new IllegalStateException("Student code overflow (69xxxxxx เต็มแล้ว)");
        }

        String s = String.format("%08d", next);
        if (!s.startsWith("69")) {
            s = "69" + String.format("%06d", next % 1_000_000);
        }
        return s;
    }

    private boolean isValidEmail(String email) {
        return email != null
                && email.contains("@")
                && email.toLowerCase().endsWith(".com");
    }

    public Optional<User> registerNewStudent(String title, String first, String last, LocalDate dob, String school,
            String email, String curriculumCode, String password) throws IOException {
        if (first == null || first.isBlank() || last == null || last.isBlank())
            throw new IllegalArgumentException("กรอกชื่อ-นามสกุลให้ครบ");
        if (dob == null)
            throw new IllegalArgumentException("กรอกวันเกิด");
        int age = (int) java.time.temporal.ChronoUnit.YEARS.between(dob, LocalDate.now());
        if (age < 15)
            throw new IllegalArgumentException("นักเรียนต้องมีอายุอย่างน้อย 15 ปี");
        if (school == null || school.isBlank())
            throw new IllegalArgumentException("กรอกโรงเรียน");
        if (!isValidEmail(email))
            throw new IllegalArgumentException("อีเมลไม่ถูกต้อง");
        if (userRepo.existsUsername(email) || studentRepo.emailExists(email))
            throw new IllegalArgumentException("อีเมลนี้ถูกใช้แล้ว");
        boolean curriculumOk = stRepo.listCurricula().stream().anyMatch(c -> c.code.equals(curriculumCode));
        if (!curriculumOk)
            throw new IllegalArgumentException("รหัสหลักสูตรไม่ถูกต้อง");
        if (password == null || password.length() < 3)
            throw new IllegalArgumentException("รหัสผ่านอย่างน้อย 3 ตัวอักษร");

        String code = generateNextStudentCode();
        Student s = new Student(code, title, first, last, dob, school, email, curriculumCode);
        studentRepo.add(s);
        User u = new User(email, password, User.Role.STUDENT, code);
        userRepo.add(u);
        return Optional.of(u);
    }
}
