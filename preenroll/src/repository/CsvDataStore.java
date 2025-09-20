package repository;

import model.*;
import util.CsvUtil;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public class CsvDataStore {
    public final Path dataDir;
    public Map<String, User> users = new HashMap<>();
    public Map<String, Student> students = new HashMap<>();
    public Map<String, Subject> subjects = new HashMap<>();
    public List<SubjectStructure> structures = new ArrayList<>();
    public List<RegisteredSubject> registrations = new ArrayList<>();

    public CsvDataStore(Path dir) {
        this.dataDir = dir;
    }

    public void ensureLoaded() throws IOException {
        if (!Files.exists(dataDir))
            Files.createDirectories(dataDir);
        loadUsers();
        loadStudents();
        loadSubjects();
        loadStructures();
        loadRegistrations();
        if (users.isEmpty() || students.isEmpty() || subjects.isEmpty() || structures.isEmpty())
            seed();
    }

    private void seed() throws IOException {
        // Subjects
        subjects.put("05500001", new Subject("05500001", "Programming Fundamentals", 3, "Dr. Somchai", null));
        subjects.put("05500002", new Subject("05500002", "Discrete Mathematics", 3, "Dr. Anong", null));
        subjects.put("05500003", new Subject("05500003", "Calculus I", 3, "Dr. Suchart", null));
        subjects.put("05500004", new Subject("05500004", "Data Structures", 3, "Dr. Busaba", "05500001"));
        subjects.put("05500005", new Subject("05500005", "Computer Organization", 3, "Dr. Prasert", null));
        subjects.put("05500006", new Subject("05500006", "Intro to Software Engineering", 3, "Dr. Lalita", "05500001"));
        subjects.put("90690001", new Subject("90690001", "Academic English I", 2, "Ms. Mali", null));
        subjects.put("90690002", new Subject("90690002", "Thai Civilization", 2, "Mr. Thana", null));
        subjects.put("90690003", new Subject("90690003", "Digital Citizenship", 2, "Ms. Noon", null));
        subjects.put("05500007", new Subject("05500007", "Linear Algebra", 3, "Dr. Kitti", "05500002"));
        subjects.put("05500008", new Subject("05500008", "Calculus II", 3, "Dr. Suchart", "05500003"));

        // Structures
        structures.add(new SubjectStructure("80000001", "BSc Computer Science", "Computer Science", "05500001", 1));
        structures.add(new SubjectStructure("80000001", "BSc Computer Science", "Computer Science", "05500002", 1));
        structures.add(new SubjectStructure("80000001", "BSc Computer Science", "Computer Science", "05500003", 1));
        structures.add(new SubjectStructure("80000001", "BSc Computer Science", "Computer Science", "90690001", 1));
        structures.add(new SubjectStructure("80000001", "BSc Computer Science", "Computer Science", "05500004", 2));
        structures.add(new SubjectStructure("80000001", "BSc Computer Science", "Computer Science", "05500005", 2));
        structures.add(new SubjectStructure("80000001", "BSc Computer Science", "Computer Science", "90690002", 2));
        structures.add(new SubjectStructure("80000001", "BSc Computer Science", "Computer Science", "05500008", 2));

        structures.add(new SubjectStructure("80000002", "BSc Information Technology", "Information Technology",
                "05500001", 1));
        structures.add(new SubjectStructure("80000002", "BSc Information Technology", "Information Technology",
                "90690003", 1));
        structures.add(new SubjectStructure("80000002", "BSc Information Technology", "Information Technology",
                "90690001", 1));
        structures.add(new SubjectStructure("80000002", "BSc Information Technology", "Information Technology",
                "05500003", 1));
        structures.add(new SubjectStructure("80000002", "BSc Information Technology", "Information Technology",
                "05500006", 2));
        structures.add(new SubjectStructure("80000002", "BSc Information Technology", "Information Technology",
                "05500007", 2));
        structures.add(new SubjectStructure("80000002", "BSc Information Technology", "Information Technology",
                "90690002", 2));

        // Students & Users
        for (int i = 1; i <= 11; i++) {
            String code = String.format("69%06d", i);
            String title = (i % 2 == 1 ? "Mr." : "Ms.");
            LocalDate dob = LocalDate.of(i % 2 == 1 ? 2007 : 2006, (i % 12) + 1, Math.min(20, (i % 28) + 1));
            String first = new String[] { "Arthit", "Benja", "Chai", "Duang", "Ekk", "Fah", "Gao", "Hnin", "Ink",
                    "Jin","Pokemon" }[i - 1];
            String last = new String[] { "Suksan", "Wong", "Prasert", "Lamsai", "Kraisri", "Arun", "Meesuk", "Suwan",
                    "Chan", "Pong", "Pikachu" }[i - 1];
            String school = new String[] { "Suranaree School", "Suranaree School", "Samsen School", "Samsen School",
                    "Triam Udom", "Triam Udom", "Benchamaracharungsarit", "Satriwitthaya", "Satriwitthaya",
                    "Yothinburana", "Hogwarts" }[i - 1];
            String cur = (i % 2 == 1 ? "80000001" : "80000002");
            String email = (first.toLowerCase() + "." + last.toLowerCase() + "@example.com");
            Student s = new Student(code, title, first, last, dob, school, email, cur);
            students.put(code, s);
        }
        users.put("admin", new User("admin", "admin123", User.Role.ADMIN, null));
        students.values().forEach(s -> users.put(s.email, new User(s.email, "1234", User.Role.STUDENT, s.studentCode)));

        // pre-registrations
        registrations.add(new RegisteredSubject("69000001", "05500001", "B"));
        registrations.add(new RegisteredSubject("69000001", "05500002", "C+"));
        registrations.add(new RegisteredSubject("69000001", "90690001", "A"));

        saveAll();
    }

    public void saveAll() throws IOException {
        saveUsers();
        saveStudents();
        saveSubjects();
        saveStructures();
        saveRegistrations();
    }

    private Path p(String name) {
        return dataDir.resolve(name);
    }

    private void loadUsers() throws IOException {
        users.clear();
        Path f = p("users.csv");
        if (!Files.exists(f))
            return;
        List<String> lines = Files.readAllLines(f);
        for (String ln : lines) {
            if (ln.trim().isEmpty())
                continue;
            List<String> v = CsvUtil.parseLine(ln);
            if (v.size() < 4)
                continue;
            User.Role role = User.Role.valueOf(v.get(2));
            users.put(v.get(0), new User(v.get(0), v.get(1), role, emptyToNull(v.get(3))));
        }
    }

    private void saveUsers() throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(p("users.csv"))) {
            for (User u : users.values()) {
                w.write(String.join(",", CsvUtil.esc(u.username), CsvUtil.esc(u.password), CsvUtil.esc(u.role.name()),
                        CsvUtil.esc(nvl(u.studentCodeOrNull))));
                w.newLine();
            }
        }
    }

    private void loadStudents() throws IOException {
        students.clear();
        Path f = p("students.csv");
        if (!Files.exists(f))
            return;
        for (String ln : Files.readAllLines(f)) {
            if (ln.trim().isEmpty())
                continue;
            List<String> v = CsvUtil.parseLine(ln);
            if (v.size() < 8)
                continue;
            Student s = new Student(v.get(0), v.get(1), v.get(2), v.get(3), LocalDate.parse(v.get(4)), v.get(5),
                    v.get(6), v.get(7));
            students.put(s.studentCode, s);
        }
    }

    private void saveStudents() throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(p("students.csv"))) {
            for (Student s : students.values()) {
                w.write(String.join(",", CsvUtil.esc(s.studentCode), CsvUtil.esc(s.title), CsvUtil.esc(s.firstName),
                        CsvUtil.esc(s.lastName), CsvUtil.esc(s.dob.toString()), CsvUtil.esc(s.school),
                        CsvUtil.esc(s.email), CsvUtil.esc(s.curriculumCode)));
                w.newLine();
            }
        }
    }

    private void loadSubjects() throws IOException {
        subjects.clear();
        Path f = p("subjects.csv");
        if (!Files.exists(f))
            return;
        for (String ln : Files.readAllLines(f)) {
            if (ln.trim().isEmpty())
                continue;
            List<String> v = CsvUtil.parseLine(ln);
            if (v.size() < 5)
                continue;
            subjects.put(v.get(0),
                    new Subject(v.get(0), v.get(1), Integer.parseInt(v.get(2)), v.get(3), emptyToNull(v.get(4))));
        }
    }

    private void saveSubjects() throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(p("subjects.csv"))) {
            for (Subject s : subjects.values()) {
                w.write(String.join(",", CsvUtil.esc(s.code), CsvUtil.esc(s.name),
                        CsvUtil.esc(Integer.toString(s.credits)), CsvUtil.esc(s.teacher),
                        CsvUtil.esc(nvl(s.prereqCode))));
                w.newLine();
            }
        }
    }

    private void loadStructures() throws IOException {
        structures.clear();
        Path f = p("subject_structure.csv");
        if (!Files.exists(f))
            return;
        for (String ln : Files.readAllLines(f)) {
            if (ln.trim().isEmpty())
                continue;
            List<String> v = CsvUtil.parseLine(ln);
            if (v.size() < 5)
                continue;
            structures.add(new SubjectStructure(v.get(0), v.get(1), v.get(2), v.get(3), Integer.parseInt(v.get(4))));
        }
    }

    private void saveStructures() throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(p("subject_structure.csv"))) {
            for (SubjectStructure s : structures) {
                w.write(String.join(",", CsvUtil.esc(s.curriculumCode), CsvUtil.esc(s.curriculumName),
                        CsvUtil.esc(s.department), CsvUtil.esc(s.subjectCode), CsvUtil.esc(Integer.toString(s.term))));
                w.newLine();
            }
        }
    }

    private void loadRegistrations() throws IOException {
        registrations.clear();
        Path f = p("registered_subjects.csv");
        if (!Files.exists(f))
            return;
        for (String ln : Files.readAllLines(f)) {
            if (ln.trim().isEmpty())
                continue;
            List<String> v = CsvUtil.parseLine(ln);
            if (v.size() < 3)
                continue;
            registrations.add(new RegisteredSubject(v.get(0), v.get(1), emptyToNull(v.get(2))));
        }
    }

    private void saveRegistrations() throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(p("registered_subjects.csv"))) {
            for (RegisteredSubject r : registrations) {
                w.write(String.join(",", CsvUtil.esc(r.studentCode), CsvUtil.esc(r.subjectCode),
                        CsvUtil.esc(nvl(r.grade))));
                w.newLine();
            }
        }
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}