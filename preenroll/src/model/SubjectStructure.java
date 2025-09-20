package model;

public class SubjectStructure {
    public String curriculumCode;
    public String curriculumName;
    public String department;
    public String subjectCode;
    public int term; // 1 or 2

    public SubjectStructure(String cur, String cname, String dept, String sub, int term) {
        this.curriculumCode = cur;
        this.curriculumName = cname;
        this.department = dept;
        this.subjectCode = sub;
        this.term = term;
    }
}