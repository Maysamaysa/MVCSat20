package repository;

import model.*;
import java.io.*;
import java.util.*;

public class RegisteredSubjectRepository {
    private final CsvDataStore store;

    public RegisteredSubjectRepository(CsvDataStore s) {
        this.store = s;
    }

    public boolean exists(String studentCode, String subjectCode) {
        for (RegisteredSubject r : store.registrations)
            if (r.studentCode.equals(studentCode) && r.subjectCode.equals(subjectCode))
                return true;
        return false;
    }

    public void add(String studentCode, String subjectCode) throws IOException {
        store.registrations.add(new RegisteredSubject(studentCode, subjectCode, null));
        store.saveAll();
    }

    public List<RegisteredSubject> bySubject(String subjectCode) {
        List<RegisteredSubject> out = new ArrayList<>();
        for (RegisteredSubject r : store.registrations)
            if (r.subjectCode.equals(subjectCode))
                out.add(r);
        return out;
    }

    public long countBySubject(String subjectCode) {
        return store.registrations.stream().filter(r -> r.subjectCode.equals(subjectCode)).count();
    }

    public void setGrade(RegisteredSubject r, String grade) throws IOException {
        r.grade = (grade == null || grade.isEmpty()) ? null : grade;
        store.saveAll();
    }
}