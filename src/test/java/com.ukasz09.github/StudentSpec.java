package com.ukasz09.github;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentSpec {

    @Test
    public void givenTwoStudentsWhenTheSameNameAndDifferentSurnameThenStudentsNotEqual() {
        Student s1 = new Student("John", "Carter");
        Student s2 = new Student("John", "Jerros");
        assertNotEquals(s1, s2);
    }

    @Test
    public void givenTwoStudentsWhenTheSameSurnameAndDifferentNameThenStudentsNotEqual() {
        Student s1 = new Student("John", "Carter");
        Student s2 = new Student("Derek", "Carter");
        assertNotEquals(s1, s2);
    }

    @Test
    public void givenTwoStudentsWhenDifferentSurnameAndNameThenStudentsNotEqual() {
        Student s1 = new Student("John", "Carter");
        Student s2 = new Student("Derek", "Smith");
        assertNotEquals(s1, s2);
    }

    @Test
    public void givenTwoStudentsWhenNameAndSurnameEqualThenStudentsEqual() {
        Student s1 = new Student("John", "Carter");
        Student s2 = new Student("John", "Carter");
        assertEquals(s1, s2);
    }

    @Test
    public void whenAddGradeWithIncorrectGradeValueThenFalse() {
        Student student = new Student("John", "Carter");
        Subject subject = new Subject("Biology");
        assertFalse(student.addGrade(subject, 1));
        assertFalse(student.addGrade(subject, 6));
        assertFalse(student.addGrade(subject, -2));
        assertFalse(student.addGrade(subject, 0));
    }
}