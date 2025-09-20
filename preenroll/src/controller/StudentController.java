package controller;

import repository.*;
import model.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class StudentController {
    private final CsvDataStore store;
    private final StudentRepository studentRepo;
    private final SubjectRepository subjectRepo;
    private final SubjectStructureRepository stRepo;
    private final RegisteredSubjectRepository regRepo;

    public StudentController(CsvDataStore store, StudentRepository sr, SubjectRepository subr,
            SubjectStructureRepository str, RegisteredSubjectRepository rr) {
        this.store = store;
        this.studentRepo = sr;
        this.subjectRepo = subr;
        this.stRepo = str;
        this.regRepo = rr;
    }

    private static final Set<String> PASSING = new HashSet<>(Arrays.asList("A", "B+", "B", "C+", "C", "D+", "D"));

    public Student get(String code) {
        return studentRepo.get(code);
    }

    public List<RegisteredSubject> getRegistrations(String code) {
        return studentRepo.regsOf(code);
    }

    public boolean prereqSatisfied(String studentCode, Subject s) {
        if (s.prereqCode == null)
            return true;
        for (RegisteredSubject r : store.registrations) {
            if (r.studentCode.equals(studentCode) && r.subjectCode.equals(s.prereqCode) && r.grade != null
                    && PASSING.contains(r.grade))
                return true;
        }
        return false;
    }

    public List<AvailRow> availableFor(String studentCode) {
        Student s = studentRepo.get(studentCode);
        Set<String> existing = getRegistrations(studentCode).stream().map(r -> r.subjectCode)
                .collect(Collectors.toSet());
        List<AvailRow> out = new ArrayList<>();
        for (SubjectStructure st : stRepo.ofCurriculum(s.curriculumCode)) {
            Subject sub = subjectRepo.get(st.subjectCode);
            if (sub == null)
                continue;
            if (existing.contains(sub.code))
                continue;
            if (!prereqSatisfied(studentCode, sub))
                continue;
            out.add(new AvailRow(st.term, sub));
        }
        out.sort(Comparator.comparingInt((AvailRow a) -> a.term).thenComparing(a -> a.subject.code));
        return out;
    }

    public boolean register(String studentCode, String subjectCode) throws IOException {
        Subject sub = subjectRepo.get(subjectCode);
        if (sub == null)
            return false;
        if (!prereqSatisfied(studentCode, sub))
            return false;
        if (regRepo.exists(studentCode, subjectCode))
            return false;
        regRepo.add(studentCode, subjectCode);
        return true;
    }

    public static class AvailRow {
        public int term;
        public Subject subject;

        public AvailRow(int t, Subject s) {
            term = t;
            subject = s;
        }
    }
}
