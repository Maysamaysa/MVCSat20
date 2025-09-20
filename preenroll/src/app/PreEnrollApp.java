package app;

import repository.*;
import controller.*;
import view.*;

import javax.swing.*;
import java.nio.file.*;

public class PreEnrollApp {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(()->{
            try {
                CsvDataStore store = new CsvDataStore(Paths.get("data/"));
                store.ensureLoaded();
                StudentRepository sRepo = new StudentRepository(store);
                SubjectRepository subRepo = new SubjectRepository(store);
                SubjectStructureRepository stRepo = new SubjectStructureRepository(store);
                RegisteredSubjectRepository rRepo = new RegisteredSubjectRepository(store);
                UserRepository uRepo = new UserRepository(store);
                AuthController auth = new AuthController(uRepo, sRepo);

                System.out.println("Sample student logins (password=1234):");
                store.students.values().stream().sorted((a,b)->a.email.compareTo(b.email)).forEach(s-> System.out.println("  "+s.email));

                new LoginView(auth, store, sRepo, subRepo, stRepo, rRepo).setVisible(true);
            } catch (Exception e){ e.printStackTrace(); }
        });
    }
}