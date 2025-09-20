package repository;

import model.*;
import java.util.*;

public class SubjectRepository {
    private final CsvDataStore store;

    public SubjectRepository(CsvDataStore s) {
        this.store = s;
    }

    public Subject get(String code) {
        return store.subjects.get(code);
    }

    public List<Subject> list() {
        return new ArrayList<>(store.subjects.values());
    }
}