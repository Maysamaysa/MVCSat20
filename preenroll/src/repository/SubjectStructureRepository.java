package repository;

import model.*;
import java.util.*;

public class SubjectStructureRepository {
    private final CsvDataStore store;

    public SubjectStructureRepository(CsvDataStore s) {
        this.store = s;
    }

    public List<SubjectStructure> ofCurriculum(String code) {
        List<SubjectStructure> out = new ArrayList<>();
        for (SubjectStructure st : store.structures)
            if (st.curriculumCode.equals(code))
                out.add(st);
        return out;
    }

    public static class CurriculumRef {
        public final String code, name, dept;

        public CurriculumRef(String c, String n, String d) {
            code = c;
            name = n;
            dept = d;
        }

        public String toString() {
            return code + " — " + name + " (" + dept + ")";
        }

    }

    public List<CurriculumRef> listCurricula() {
        Map<String, CurriculumRef> map = new LinkedHashMap<>();
        for (SubjectStructure st : store.structures) {
            map.putIfAbsent(st.curriculumCode, new CurriculumRef(st.curriculumCode, st.curriculumName, st.department));
        }
        return new ArrayList<>(map.values());
    }
}