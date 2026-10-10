package ru.akozadaev.students;

public class Student {
    public final String name, group, email;
    public final double grade;
    public Student(String name, String group, String email, double grade) {
        this.name = name; this.group = group; this.email = email; this.grade = grade;
    }
}
