package view;

import controller.*;
import repository.*;
import model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class StudentProfileView extends JFrame {
    CsvDataStore store;
    StudentRepository sRepo;
    SubjectRepository subRepo;
    SubjectStructureRepository stRepo;
    RegisteredSubjectRepository rRepo;
    User user;
    StudentController controller;
    Student s;
    DefaultTableModel model = new DefaultTableModel(
            new Object[] { "รหัสวิชา", "ชื่อวิชา", "หน่วยกิต", "อาจารย์", "เกรด" }, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    JButton regBtn = new JButton("ลงทะเบียนเรียน"), logoutBtn = new JButton("ออกจากระบบ");

    public StudentProfileView(CsvDataStore store, StudentRepository sRepo, SubjectRepository subRepo,
            SubjectStructureRepository stRepo, RegisteredSubjectRepository rRepo, User user) {
        super("ประวัตินักเรียน");
        this.store = store;
        this.sRepo = sRepo;
        this.subRepo = subRepo;
        this.stRepo = stRepo;
        this.rRepo = rRepo;
        this.user = user;
        this.controller = new StudentController(store, sRepo, subRepo, stRepo, rRepo);
        this.s = sRepo.get(user.studentCodeOrNull);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        JPanel info = new JPanel(new GridLayout(0, 1));
        info.add(new JLabel("ชื่อ: " + s.fullName() + " (" + s.studentCode + ")"));
        info.add(new JLabel("เกิด: " + s.dob + " (" + s.getAge() + " ปี)"));
        info.add(new JLabel("โรงเรียน: " + s.school));
        info.add(new JLabel("อีเมล: " + s.email));
        info.add(new JLabel("หลักสูตร: " + s.curriculumCode));
        JTable table = new JTable(model);
        JPanel btns = new JPanel();
        btns.add(regBtn);
        btns.add(logoutBtn);
        add(info, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btns, BorderLayout.SOUTH);
        refresh();
        regBtn.addActionListener(e -> {
            new RegisterDialog(this, store, controller, s).setVisible(true);
            refresh();
        });
        logoutBtn.addActionListener(e -> {
            new LoginView(new controller.AuthController(new repository.UserRepository(store), sRepo), store, sRepo,
                    subRepo, stRepo, rRepo).setVisible(true);
            dispose();
        });
    }

    void refresh() {
        model.setRowCount(0);
        for (RegisteredSubject r : controller.getRegistrations(s.studentCode)) {
            Subject sub = subRepo.get(r.subjectCode);
            model.addRow(
                    new Object[] { sub.code, sub.name, sub.credits, sub.teacher, r.grade == null ? "-" : r.grade });
        }
    }
}
