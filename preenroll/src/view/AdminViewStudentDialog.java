package view;

import repository.*;
import model.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class AdminViewStudentDialog extends JDialog {
    DefaultTableModel model = new DefaultTableModel(
            new Object[] { "รหัสวิชา", "ชื่อวิชา", "หน่วยกิต", "อาจารย์", "เกรด" }, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    public AdminViewStudentDialog(JFrame owner, CsvDataStore store, StudentRepository sRepo, SubjectRepository subRepo,
            SubjectStructureRepository stRepo, RegisteredSubjectRepository rRepo, Student s) {
        super(owner, "ประวัตินักเรียน: " + s.fullName(), true);
        setSize(700, 500);
        setLocationRelativeTo(owner);
        JPanel info = new JPanel(new GridLayout(0, 1));
        info.add(new JLabel("รหัส: " + s.studentCode));
        info.add(new JLabel("ชื่อ: " + s.fullName()));
        info.add(new JLabel("เกิด: " + s.dob + " (" + s.getAge() + " ปี)"));
        info.add(new JLabel("โรงเรียน: " + s.school));
        info.add(new JLabel("อีเมล: " + s.email));
        info.add(new JLabel("หลักสูตร: " + s.curriculumCode));
        JTable table = new JTable(model);
        add(info, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        for (RegisteredSubject r : sRepo.regsOf(s.studentCode)) {
            Subject sub = subRepo.get(r.subjectCode);
            model.addRow(
                    new Object[] { sub.code, sub.name, sub.credits, sub.teacher, r.grade == null ? "-" : r.grade });
        }
    }
}