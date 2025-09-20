package view;

import controller.*;
import repository.*;
import model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class AdminGradeEntryDialog extends JDialog {
    DefaultTableModel model = new DefaultTableModel(new Object[] { "นักเรียน", "อีเมล", "เกรดปัจจุบัน", "ให้เกรด" },
            0) {
        public boolean isCellEditable(int r, int c) {
            return c == 3;
        }
    };
    JTable table = new JTable(model) {
        @Override
        public TableCellEditor getCellEditor(int row, int column) {
            if (column == 3) {
                return new DefaultCellEditor(
                        new JComboBox<>(new String[] { "", "A", "B+", "B", "C+", "C", "D+", "D", "F" }));
            }
            return super.getCellEditor(row, column);
        }
    };
    JButton saveBtn = new JButton("บันทึก"), closeBtn = new JButton("ปิด");
    AdminController controller;
    java.util.List<RegisteredSubject> backing;

    public AdminGradeEntryDialog(JFrame owner, CsvDataStore store, StudentRepository sRepo, SubjectRepository subRepo,
            SubjectStructureRepository stRepo, RegisteredSubjectRepository rRepo, String subjectCode) {
        super(owner, "กรอกเกรด: " + subjectCode, true);
        setSize(800, 500);
        setLocationRelativeTo(owner);
        controller = new AdminController(store, sRepo, subRepo, rRepo);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel south = new JPanel();
        south.add(saveBtn);
        south.add(closeBtn);
        add(south, BorderLayout.SOUTH);
        backing = controller.regsBySubject(subjectCode);
        model.setRowCount(0);
        for (RegisteredSubject r : backing) {
            Student s = sRepo.get(r.studentCode);
            model.addRow(new Object[] { s.fullName() + " (" + s.studentCode + ")", s.email,
                    r.grade == null ? "-" : r.grade, r.grade == null ? "" : r.grade });
        }
        saveBtn.addActionListener(e -> {
            try {
                for (int i = 0; i < backing.size(); i++) {
                    String val = (String) model.getValueAt(i, 3);
                    controller.setGrade(backing.get(i), val == null || val.isEmpty() ? null : val);
                }
                JOptionPane.showMessageDialog(this, "บันทึกเรียบร้อย");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        closeBtn.addActionListener(e -> {
            dispose();
        });
    }
}
