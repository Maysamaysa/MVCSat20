package view;

import controller.*;
import repository.*;
import model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class AdminGradeSelectDialog extends JDialog {
    DefaultTableModel model = new DefaultTableModel(new Object[] { "รหัส", "ชื่อวิชา", "ลงทะเบียนทั้งหมด" }, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    JButton openBtn = new JButton("กรอกเกรด"), closeBtn = new JButton("ปิด");
    JTable table = new JTable(model);
    AdminController controller;

    public AdminGradeSelectDialog(JFrame owner, CsvDataStore store, StudentRepository sRepo, SubjectRepository subRepo,
            SubjectStructureRepository stRepo, RegisteredSubjectRepository rRepo) {
        super(owner, "เลือกวิชาสำหรับกรอกเกรด", true);
        setSize(700, 500);
        setLocationRelativeTo(owner);
        controller = new AdminController(store, sRepo, subRepo, rRepo);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel s = new JPanel();
        s.add(openBtn);
        s.add(closeBtn);
        add(s, BorderLayout.SOUTH);
        refresh();
        openBtn.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i < 0)
                return;
            String code = (String) model.getValueAt(i, 0);
            new AdminGradeEntryDialog(owner, store, sRepo, subRepo, stRepo, rRepo, code).setVisible(true);
            refresh();
        });
        closeBtn.addActionListener(e -> {
            dispose();
        });
    }

    void refresh() {
        model.setRowCount(0);
        for (Subject sub : controller.listAllSubjects()) {
            model.addRow(new Object[] { sub.code, sub.name, controller.countRegistered(sub.code) });
        }
    }
}