package model;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class Student {
    public String studentCode; // 8 digits, starts with 69
    public String title, firstName, lastName;
    public LocalDate dob;
    public String school;
    public String email;
    public String curriculumCode; // first digit not 0

    public Student(String code, String title, String first, String last, LocalDate dob, String school, String email,
            String cur) {
        this.studentCode = code;
        this.title = title;
        this.firstName = first;
        this.lastName = last;
        this.dob = dob;
        this.school = school;
        this.email = email;
        this.curriculumCode = cur;
    }

    public int getAge() {
        return (int) ChronoUnit.YEARS.between(dob, LocalDate.now());
    }

    public String fullName() {
        return title + " " + firstName + " " + lastName;
    }
}