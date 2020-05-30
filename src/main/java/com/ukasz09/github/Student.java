package com.ukasz09.github;

import java.util.Objects;

public class Student extends Entity {
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

    public boolean addGrade(Subject subject, int i) {
        if (Subject.AVAILABLE_GRADES.contains(i)) {
            // TODO: 30.05.2020 add connection with db
            return true;
        }
        return false;
    }

    @Override
    public boolean add() {
        return dbManager.add(this);
    }
}
