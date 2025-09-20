package view;

import controller.*;
import repository.*;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.time.*;

public class NewStudentRegisterDialog extends JDialog {
    private final RegistrationController controller;
    private final SubjectStructureRepository stRepo;
    private JComboBox<String> titleBox = new JComboBox<>(new String[] { "Mr.", "Ms." });
    private JTextField firstField = new JTextField(12), lastField = new JTextField(12);
    private JTextField dobField = new JTextField(10); // YYYY-MM-DD
    private JTextField schoolField = new JTextField(18);
    private JTextField emailField = new JTextField(18);
    private JPasswordField passField = new JPasswordField(12);
    private JComboBox<SubjectStructureRepository.CurriculumRef> curriculumBox = new JComboBox<>();
    private JButton registerBtn = new JButton("สมัครสมาชิก"), cancelBtn = new JButton("ยกเลิก");
    private User createdUser = null;

    public NewStudentRegisterDialog(JFrame owner, RegistrationController controller,
            SubjectStructureRepository stRepo) {
        super(owner, "สมัครนักเรียนใหม่", true);
        this.controller = controller;
        this.stRepo = stRepo;
        setSize(700, 360);
        setLocationRelativeTo(owner);
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        p.add(new JLabel("คำนำหน้า"), c);
        c.gridx = 1;
        c.gridy = 0;
        p.add(titleBox, c);
        c.gridx = 0;
        c.gridy++;
        p.add(new JLabel("ชื่อ"), c);
        c.gridx = 1;
        p.add(firstField, c);
        c.gridx = 0;
        c.gridy++;
        p.add(new JLabel("นามสกุล"), c);
        c.gridx = 1;
        p.add(lastField, c);
        c.gridx = 0;
        c.gridy++;
        p.add(new JLabel("วันเกิด (YYYY-MM-DD)"), c);
        c.gridx = 1;
        p.add(dobField, c);
        c.gridx = 0;
        c.gridy++;
        p.add(new JLabel("โรงเรียน"), c);
        c.gridx = 1;
        p.add(schoolField, c);
        c.gridx = 0;
        c.gridy++;
        p.add(new JLabel("อีเมล (เป็นชื่อผู้ใช้)"), c);
        c.gridx = 1;
        p.add(emailField, c);
        c.gridx = 0;
        c.gridy++;
        p.add(new JLabel("รหัสผ่าน"), c);
        c.gridx = 1;
        p.add(passField, c);
        c.gridx = 0;
        c.gridy++;
        p.add(new JLabel("หลักสูตร"), c);
        c.gridx = 1;
        p.add(curriculumBox, c);
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(registerBtn);
        south.add(cancelBtn);
        add(p, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
        for (SubjectStructureRepository.CurriculumRef ref : stRepo.listCurricula())
            curriculumBox.addItem(ref);

        registerBtn.addActionListener(e -> {
            try {
                String title = (String) titleBox.getSelectedItem();
                String first = firstField.getText().trim();
                String last = lastField.getText().trim();
                LocalDate dob = LocalDate.parse(dobField.getText().trim());
                String school = schoolField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passField.getPassword());
                SubjectStructureRepository.CurriculumRef ref = (SubjectStructureRepository.CurriculumRef) curriculumBox
                        .getSelectedItem();
                java.util.Optional<User> created = controller.registerNewStudent(title, first, last, dob, school, email,
                        ref.code, password);
                if (created.isPresent()) {
                    this.createdUser = created.get();
                    JOptionPane.showMessageDialog(this, "สมัครเรียบร้อย! รหัสนักเรียนใหม่ถูกสร้างและเข้าสู่ระบบแล้ว");
                    dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Register Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelBtn.addActionListener(e -> {
            dispose();
        });
    }

    public User getCreatedUser() {
        return createdUser;
    }
}
