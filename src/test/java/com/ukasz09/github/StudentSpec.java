package com.ukasz09.github;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class StudentSpec {
    private GradesManagerDb dbMock;

    //----------------------------------------------------------------------------------------------------------------//
    @BeforeEach
    public void initialize() {
        dbMock = mock(GradesManagerDb.class);
    }

    @Test
    public void givenTwoStudentsWhenTheSameNameAndDifferentSurnameThenStudentsNotEqual() {
        Student s1 = new Student("John", "Carter", dbMock);
        Student s2 = new Student("John", "Jerros", dbMock);
        assertNotEquals(s1, s2);
    }

    @Test
    public void givenTwoStudentsWhenTheSameSurnameAndDifferentNameThenStudentsNotEqual() {
        Student s1 = new Student("John", "Carter", dbMock);
        Student s2 = new Student("Derek", "Carter", dbMock);
        assertNotEquals(s1, s2);
    }

    @Test
    public void givenTwoStudentsWhenDifferentSurnameAndNameThenStudentsNotEqual() {
        Student s1 = new Student("John", "Carter", dbMock);
        Student s2 = new Student("Derek", "Smith", dbMock);
        assertNotEquals(s1, s2);
    }

    @Test
    public void givenTwoStudentsWhenNameAndSurnameEqualThenStudentsEqual() {
        Student s1 = new Student("John", "Carter", dbMock);
        Student s2 = new Student("John", "Carter", dbMock);
        assertEquals(s1, s2);
    }

    @Test
    public void whenAddGradeWithIncorrectGradeValueThenFalse() {
        Student student = new Student("John", "Carter", dbMock);
        Subject subject = new Subject("Biology");
        assertFalse(student.addGrade(subject, 1));
        assertFalse(student.addGrade(subject, 6));
        assertFalse(student.addGrade(subject, -2));
        assertFalse(student.addGrade(subject, 0));
    }
}
