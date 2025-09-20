package view;

import controller.*;
import repository.*;
import model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;

public class RegisterDialog extends JDialog {
    DefaultTableModel model = new DefaultTableModel(
            new Object[] { "เทอม", "รหัส", "ชื่อ", "หน่วยกิต", "อาจารย์", "บังคับก่อน" }, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    JButton registerBtn = new JButton("ลงทะเบียน"), closeBtn = new JButton("ปิด");
    JTable table = new JTable(model);

    public RegisterDialog(JFrame owner, repository.CsvDataStore store, StudentController controller, Student s) {
        super(owner, "ลงทะเบียนเรียน", true);
        setSize(800, 500);
        setLocationRelativeTo(owner);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel south = new JPanel();
        south.add(registerBtn);
        south.add(closeBtn);
        add(south, BorderLayout.SOUTH);
        Runnable load = () -> {
            model.setRowCount(0);
            for (StudentController.AvailRow a : controller.availableFor(s.studentCode)) {
                model.addRow(new Object[] { a.term, a.subject.code, a.subject.name, a.subject.credits,
                        a.subject.teacher, a.subject.prereqCode == null ? "-" : a.subject.prereqCode });
            }
        };
        load.run();
        registerBtn.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i < 0)
                return;
            String code = (String) model.getValueAt(i, 1);
            try {
                boolean ok = controller.register(s.studentCode, code);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "ลงทะเบียนสำเร็จ");
                    load.run();
                    dispose();
                } else
                    JOptionPane.showMessageDialog(this, "ยังไม่ผ่านวิชาบังคับก่อน หรือซ้ำ", "Error",
                            JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        closeBtn.addActionListener(e -> {
            dispose();
        });
    }
}