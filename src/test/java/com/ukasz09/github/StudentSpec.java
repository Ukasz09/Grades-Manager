package com.ukasz09.github;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StudentSpec {
    private GradesManagerDb dbSpy;

    //----------------------------------------------------------------------------------------------------------------//
    @BeforeEach
    public void initialize() {
        dbSpy = spy(GradesManagerDb.class);
    }

    @Nested
    class Equality {
        @Test
        public void givenTwoStudentsWhenTheSameNameAndDifferentSurnameThenStudentsNotEqual() {
            Student s1 = new Student("John", "Carter", dbSpy);
            Student s2 = new Student("John", "Jerros", dbSpy);
            assertNotEquals(s1, s2);
        }

        @Test
        public void givenTwoStudentsWhenTheSameSurnameAndDifferentNameThenStudentsNotEqual() {
            Student s1 = new Student("John", "Carter", dbSpy);
            Student s2 = new Student("Derek", "Carter", dbSpy);
            assertNotEquals(s1, s2);
        }

        @Test
        public void givenTwoStudentsWhenDifferentSurnameAndNameThenStudentsNotEqual() {
            Student s1 = new Student("John", "Carter", dbSpy);
            Student s2 = new Student("Derek", "Smith", dbSpy);
            assertNotEquals(s1, s2);
        }

        @Test
        public void givenTwoStudentsWhenNameAndSurnameEqualThenStudentsEqual() {
            Student s1 = new Student("John", "Carter", dbSpy);
            Student s2 = new Student("John", "Carter", dbSpy);
            assertEquals(s1, s2);
        }
    }

    @Nested
    class Adding {
        @Test
        public void whenAddGradeWithIncorrectGradeValueThenFalse() {
            Student student = new Student("John", "Carter", dbSpy);
            Subject subject = new Subject("Biology", dbSpy);
            assertFalse(student.addGrade(subject, 1));
            assertFalse(student.addGrade(subject, 6));
            assertFalse(student.addGrade(subject, -2));
            assertFalse(student.addGrade(subject, 0));
        }

        @Test
        public void whenAddStudentWithNameContainsDigitsThenFalse() {
            doReturn(true).when(dbSpy).add(any(Student.class));
            Student student = new Student("Joh21n", "Cannady", dbSpy);
            assertFalse(student.add());
        }

        @Test
        public void whenAddStudentWithSurnameContainsDigitsThenFalse() {
            doReturn(true).when(dbSpy).add(any(Student.class));
            Student student = new Student("Joshua", "Ca21nnas12dy", dbSpy);
            assertFalse(student.add());
        }

        @Test
        public void whenAddStudentWithNameStartedLowercaseThenFalse() {
            doReturn(true).when(dbSpy).add(any(Student.class));
            Student student = new Student("john", "Cannady", dbSpy);
            assertFalse(student.add());
        }

        @Test
        public void whenAddStudentWithSurnameStartedLowercaseThenFalse() {
            doReturn(true).when(dbSpy).add(any(Student.class));
            Student student = new Student("John", "kendric", dbSpy);
            assertFalse(student.add());
        }
    }
}
