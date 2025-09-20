package view;

import controller.*;
import repository.*;
import model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class AdminStudentListView extends JFrame {
    CsvDataStore store;
    StudentRepository sRepo;
    SubjectRepository subRepo;
    SubjectStructureRepository stRepo;
    RegisteredSubjectRepository rRepo;
    User user;
    AdminController controller;
    JTable table;
    JTextField qField = new JTextField(12), schoolField = new JTextField(12);
    JComboBox<String> sortBox = new JComboBox<>(new String[] { "name", "age" });
    JButton searchBtn = new JButton("ค้นหา/กรอง"), viewBtn = new JButton("ดูประวัติ"),
            gradesBtn = new JButton("กรอกเกรด"), logoutBtn = new JButton("ออกจากระบบ");
    DefaultTableModel model = new DefaultTableModel(
            new Object[] { "รหัส", "ชื่อ", "อายุ", "โรงเรียน", "อีเมล", "หลักสูตร" }, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    public AdminStudentListView(CsvDataStore store, StudentRepository sRepo, SubjectRepository subRepo,
            SubjectStructureRepository stRepo, RegisteredSubjectRepository rRepo, User user) {
        super("รวมนักเรียน (Admin)");
        this.store = store;
        this.sRepo = sRepo;
        this.subRepo = subRepo;
        this.stRepo = stRepo;
        this.rRepo = rRepo;
        this.user = user;
        this.controller = new AdminController(store, sRepo, subRepo, rRepo);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        JPanel top = new JPanel();
        top.add(new JLabel("ค้นหา"));
        top.add(qField);
        top.add(new JLabel("โรงเรียน"));
        top.add(schoolField);
        top.add(new JLabel("เรียง"));
        top.add(sortBox);
        top.add(searchBtn);
        top.add(gradesBtn);
        top.add(logoutBtn);
        table = new JTable(model);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        bottom.add(viewBtn);
        add(bottom, BorderLayout.SOUTH);
        refresh();
        searchBtn.addActionListener(e -> refresh());
        viewBtn.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i < 0)
                return;
            String code = (String) model.getValueAt(i, 0);
            Student s = sRepo.get(code);
            new AdminViewStudentDialog(this, store, sRepo, subRepo, stRepo, rRepo, s).setVisible(true);
        });
        gradesBtn.addActionListener(e -> {
            new AdminGradeSelectDialog(this, store, sRepo, subRepo, stRepo, rRepo).setVisible(true);
        });
        logoutBtn.addActionListener(e -> {
            new LoginView(new controller.AuthController(new repository.UserRepository(store), sRepo), store, sRepo,
                    subRepo, stRepo, rRepo).setVisible(true);
            dispose();
        });
    }

    void refresh() {
        model.setRowCount(0);
        String q = qField.getText(), school = schoolField.getText(), sort = (String) sortBox.getSelectedItem();
        for (Student s : controller.searchStudents(q, school, sort))
            model.addRow(new Object[] { s.studentCode, s.fullName(), s.getAge(), s.school, s.email, s.curriculumCode });
    }
}
