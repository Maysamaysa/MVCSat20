package repository;

import model.*;

import java.io.IOException;
import java.util.*;

public class UserRepository {
    private final CsvDataStore store;

    public UserRepository(CsvDataStore s) {
        this.store = s;
    }

    public Optional<User> find(String username, String password) {
        User u = store.users.get(username);
        return (u != null && u.password.equals(password)) ? Optional.of(u) : Optional.empty();
    }

    public boolean existsUsername(String username) {
        return store.users.containsKey(username);
    }

    public void add(User u) throws IOException {
        store.users.put(u.username, u);
        store.saveAll();
    }
}