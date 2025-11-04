package com.example.campus_placements.user.model;

public class Student extends User {
    private  int StudentId;
    private int yearOfStudy;
    private String course;

    public Student(String firstName, String lastName, String email, String passwordHash, Role role, int studentId, int yearOfStudy, String course) {
        super(firstName, lastName, email, passwordHash, role);
        StudentId = studentId;
        this.yearOfStudy = yearOfStudy;
        this.course = course;
    }

    public int getStudentId() {
        return StudentId;
    }

    public void setStudentId(int studentId) {
        StudentId = studentId;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
