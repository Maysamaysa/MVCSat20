package view;

import controller.*;
import repository.*;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class LoginView extends JFrame {
    JTextField userField = new JTextField(20);
    JPasswordField passField = new JPasswordField(20);
    JButton loginBtn = new JButton("เข้าสู่ระบบ");
    JButton signupBtn = new JButton("สมัครนักเรียนใหม่");

    public LoginView(AuthController auth, CsvDataStore store, StudentRepository sRepo, SubjectRepository subRepo,
            SubjectStructureRepository stRepo, RegisteredSubjectRepository rRepo) {
        super("Login – Pre‑Enrollment");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        p.add(new JLabel("ชื่อผู้ใช้ (อีเมลนักเรียน หรือ admin)"), c);
        c.gridx = 1;
        c.gridy = 0;
        p.add(userField, c);
        c.gridx = 0;
        c.gridy = 1;
        p.add(new JLabel("รหัสผ่าน"), c);
        c.gridx = 1;
        c.gridy = 1;
        p.add(passField, c);
        c.gridx = 1;
        c.gridy = 2;
        p.add(loginBtn, c);
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        row.add(signupBtn);
        row.add(loginBtn);
        c.gridx = 1;
        c.gridy = 2;
        p.add(row, c);
        add(p);
        pack();
        setLocationRelativeTo(null);

        loginBtn.addActionListener(e -> {
            String u = userField.getText().trim();
            String pa = new String(passField.getPassword());
            Optional<User> ou = auth.login(u, pa);
            if (ou.isPresent()) {
                User user = ou.get();
                if (user.role == User.Role.ADMIN) {
                    new AdminStudentListView(store, sRepo, subRepo, stRepo, rRepo, user).setVisible(true);
                } else {
                    new StudentProfileView(store, sRepo, subRepo, stRepo, rRepo, user).setVisible(true);
                }
                dispose();
            } else
                JOptionPane.showMessageDialog(this, "เข้าสู่ระบบไม่สำเร็จ หรืออายุน้อยกว่า 15 ปี", "Error",
                        JOptionPane.ERROR_MESSAGE);
        });

        signupBtn.addActionListener(e -> {
            RegistrationController reg = new RegistrationController(store, sRepo, subRepo, stRepo,
                    new UserRepository(store));
            NewStudentRegisterDialog dlg = new NewStudentRegisterDialog(this, reg, stRepo);
            dlg.setVisible(true);
            User created = dlg.getCreatedUser();
            if (created != null) { // auto-login เมื่อลงทะเบียนสำเร็จ
                if (created.role == User.Role.STUDENT) {
                    new StudentProfileView(store, sRepo, subRepo, stRepo, rRepo, created).setVisible(true);
                    dispose();
                }
            }
        });
    }
}