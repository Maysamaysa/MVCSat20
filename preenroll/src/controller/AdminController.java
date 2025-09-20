package controller;

import repository.*;
import model.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class AdminController {
    private final CsvDataStore store;
    private final StudentRepository studentRepo;
    private final SubjectRepository subjectRepo;
    private final RegisteredSubjectRepository regRepo;

    public AdminController(CsvDataStore store, StudentRepository sr, SubjectRepository subr,
            RegisteredSubjectRepository rr) {
        this.store = store;
        this.studentRepo = sr;
        this.subjectRepo = subr;
        this.regRepo = rr;
    }

    public List<Student> searchStudents(String q, String school, String sort) {
        Stream<Student> st = studentRepo.list().stream();
        if (q != null && !q.trim().isEmpty()) {
            String qq = q.toLowerCase();
            st = st.filter(s -> (s.firstName + " " + s.lastName).toLowerCase().contains(qq));
        }
        if (school != null && !school.trim().isEmpty()) {
            String ss = school.toLowerCase();
            st = st.filter(s -> s.school.toLowerCase().contains(ss));
        }
        List<Student> list = st.collect(Collectors.toList());
        if ("age".equals(sort))
            list.sort(Comparator.comparingInt(Student::getAge));
        else
            list.sort(Comparator.comparing((Student s) -> s.firstName.toLowerCase())
                    .thenComparing(s -> s.lastName.toLowerCase()));
        return list;
    }

    public long countRegistered(String subjectCode) {
        return regRepo.countBySubject(subjectCode);
    }

    public List<Subject> listAllSubjects() {
        return subjectRepo.list();
    }

    public List<RegisteredSubject> regsBySubject(String code) {
        return regRepo.bySubject(code);
    }

    public void setGrade(RegisteredSubject r, String grade) throws IOException {
        regRepo.setGrade(r, grade);
    }
}