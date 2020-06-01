package com.ukasz09.github;

import java.util.Objects;
import java.util.regex.Pattern;

public class Student extends Entity {
    private static final Pattern PATTERN = Pattern.compile("[A-Z][a-zA-Z]+");

    private String name;
    private String surname;
    private GradesManagerDb dbManager;

    //----------------------------------------------------------------------------------------------------------------//
    public Student(String name, String surname, GradesManagerDb dbManager) {
        this.name = name;
        this.surname = surname;
        this.dbManager = dbManager;
    }

    //----------------------------------------------------------------------------------------------------------------//
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return name.equals(student.name) &&
                surname.equals(student.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }

    public boolean addGrade(Subject subject, int grade) {
        if (Subject.AVAILABLE_GRADES.contains(grade))
            return dbManager.addGrade(this, subject, grade);
        return false;
    }

    @Override
    public boolean add() {
        if (PATTERN.matcher(name).matches() && PATTERN.matcher(surname).matches())
            return dbManager.add(this);
        return false;
    }

    @Override
    public boolean delete() {
        return dbManager.delete(this);
    }

    public double avgGrade(Subject subject) {
        return dbManager.avgGrade(this, subject);
    }
}
