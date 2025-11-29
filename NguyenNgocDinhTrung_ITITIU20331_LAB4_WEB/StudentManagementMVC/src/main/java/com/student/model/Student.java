package com.student.model;

public class Student {
    private int id;
    private String studentCode;
    private String fullName;
    private String email;
    private String major;

    public Student() {}

    public Student(String studentCode, String fullName, String email, String major) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.major = major;
    }

    public Student(int id, String studentCode, String fullName, String email, String major) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.major = major;
    }

    // ===== Getters and Setters =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
}
