package repository;

import model.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.*;

public class StudentRepository {
    private final CsvDataStore store;

    public StudentRepository(CsvDataStore store) {
        this.store = store;
    }

    public List<Student> list() {
        return new ArrayList<>(store.students.values());
    }

    public Student get(String code) {
        return store.students.get(code);
    }

    public List<RegisteredSubject> regsOf(String studentCode) {
        List<RegisteredSubject> list = new ArrayList<>();
        for (RegisteredSubject r : store.registrations)
            if (r.studentCode.equals(studentCode))
                list.add(r);
        return list;
    }

    public boolean emailExists(String email) {
        for (Student s : store.students.values())
            if (s.email.equalsIgnoreCase(email))
                return true;
        return false;
    }

    public void add(Student s) throws IOException {
        store.students.put(s.studentCode, s);
        store.saveAll();
    }
}