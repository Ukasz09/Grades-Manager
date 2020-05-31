package com.ukasz09.github;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

//----------------------------------------------------------------------------------------------------------------//
//  IMPORTANT: assuming that: given MongoDB running, to every test
//----------------------------------------------------------------------------------------------------------------//
public class StudentInteg {

    private GradesManagerDb dbManager = new GradesManagerDb();

    //----------------------------------------------------------------------------------------------------------------//
    @BeforeEach
    public void clearDb() {
        dbManager.dropDb();
    }

    @Test
    public void whenDropDbThenCollectionsEmpty() {
        dbManager.dropDb();
        assertEquals(0L, dbManager.getStudentsCollection().count());
        assertEquals(0L, dbManager.getSubjectsCollection().count());
    }

    @Nested
    class Add {
        @Test
        public void givenEmptyCollectionWhenAddThenAdded() {
            assertTrue(new Student("John", "Carter", dbManager).add());
            assertEquals(1, dbManager.countStudents());
            assertTrue(dbManager.existInDb(new Student("John", "Carter", dbManager)));
        }

        @Test
        public void given1StudentInCollectionWhenAddTheSameStudentThenNotAdded() {
            new Student("John", "Carter", dbManager).add();
            assertFalse(new Student("John", "Carter", dbManager).add());
            assertEquals(1, dbManager.countStudents());
        }

        @Test
        public void givenNotEmptyCollectionWhenAddDifferentStudentThenAdded() {
            assertTrue(new Student("John", "Carter", dbManager).add());
            assertTrue(new Student("Will", "Smith", dbManager).add());
            assertTrue(new Student("Penelope", "Cruz", dbManager).add());
            assertEquals(3, dbManager.countStudents());
            assertTrue(dbManager.existInDb(new Student("John", "Carter", dbManager)));
            assertTrue(dbManager.existInDb(new Student("Will", "Smith", dbManager)));
            assertTrue(dbManager.existInDb(new Student("Penelope", "Cruz", dbManager)));
        }

        @Test
        public void givenNotEmptyCollectionWhenAddStudentWithOnlyTheSameNameThenAdded() {
            new Student("John", "Carter", dbManager).add();
            assertTrue(new Student("Johny", "Dicaprio", dbManager).add());
            assertEquals(2, dbManager.countStudents());
            assertTrue(dbManager.existInDb(new Student("John", "Carter", dbManager)));
            assertTrue(dbManager.existInDb(new Student("Johny", "Dicaprio", dbManager)));
        }

        @Test
        public void givenNotEmptyCollectionWhenAddStudentWithTheOnlySameSurnameThenAdded() {
            new Student("John", "Carter", dbManager).add();
            assertTrue(new Student("Eleonore", "Carter", dbManager).add());
            assertEquals(2, dbManager.countStudents());
            assertTrue(dbManager.existInDb(new Student("John", "Carter", dbManager)));
            assertTrue(dbManager.existInDb(new Student("Eleonore", "Carter", dbManager)));
        }
    }

    @Nested
    class Del {
        @Test
        public void givenEmptyCollectionWhenDeleteStudentThenNoChanges() {
            new Student("John", "Mackenzi", dbManager).delete();
            assertEquals(0, dbManager.countStudents());
        }

        @Test
        public void given1StudentInCollectionWhenDeleteThisStudentThenDeleted() {
            new Student("John", "Carter", dbManager).add();
            assertTrue(new Student("John", "Carter", dbManager).delete());
            assertEquals(0, dbManager.countStudents());
            assertFalse(dbManager.existInDb(new Student("John", "Carter", dbManager)));
        }

        @Test
        public void given3StudentsInCollectionWhenDeleteDifferentStudentThenNoChanges() {
            new Student("John", "Carter", dbManager).add();
            new Student("Penelope", "Garsia", dbManager).add();
            new Student("Emily", "Blunt", dbManager).add();
            assertFalse(new Student("Johnny", "Bravo", dbManager).delete());
            assertEquals(3, dbManager.countStudents());
            assertFalse(dbManager.existInDb(new Student("Johnny", "Bravo", dbManager)));
        }

        @Test
        public void given3StudentsInCollectionWhenDeleteOneOfThemThenDeleted() {
            new Student("John", "Carter", dbManager).add();
            new Student("Penelope", "Garsia", dbManager).add();
            new Student("Emily", "Blunt", dbManager).add();
            assertTrue(new Student("Penelope", "Garsia", dbManager).delete());
            assertEquals(2, dbManager.countStudents());
            assertTrue(dbManager.existInDb(new Student("John", "Carter", dbManager)));
            assertTrue(dbManager.existInDb(new Student("Emily", "Blunt", dbManager)));
        }
    }

    @Nested
    class Count {
        @Test
        public void givenEmptyCollectionWhenCountThen0() {
            assertEquals(0, dbManager.countStudents());
        }

        @Test
        public void given4SizeCollectionWhenCountThen4() {
            dbManager.add(new Student("John", "Kenezie", dbManager));
            dbManager.add(new Student("Derek", "Kenezie", dbManager));
            dbManager.add(new Student("Penelope", "Garsia", dbManager));
            dbManager.add(new Student("Emily", "Costa", dbManager));
            assertEquals(4, dbManager.countStudents());
        }
    }

    @Nested
    class AddGrades {
        @Test
        public void givenEmptyCollectionWhenAddGradeThenNoChanges() {
            Student student = new Student("John", "Carter", dbManager);
            Subject subject = new Subject("Math", dbManager);
            assertFalse(student.addGrade(subject, 2));
            assertTrue(dbManager.getGrades(student, subject).isEmpty());
        }

        @Test
        public void givenCorrectSubjectAndGradeAndIncorrectStudentWhenAddGradeThenNoChanges() {
            Student student = new Student("John", "Carter", dbManager);
            Subject subject = new Subject("Math", dbManager);
            subject.add();
            assertFalse(student.addGrade(subject, 2));
            assertTrue(dbManager.getGrades(student, subject).isEmpty());
        }

        @Test
        public void givenCorrectStudentAndGradeAndIncorrectSubjectWhenAddGradeThenNoChanges() {
            Student student = new Student("John", "Carter", dbManager);
            student.add();
            Subject subject = new Subject("Math", dbManager);
            assertFalse(student.addGrade(subject, 2));
            assertTrue(dbManager.getGrades(student, subject).isEmpty());
        }

        @Test
        public void givenCorrectStudentAndSubjectAndIncorrectGradeWhenAddGradeThenNoChanges() {
            Student student = new Student("John", "Carter", dbManager);
            Subject subject = new Subject("Math", dbManager);
            student.add();
            subject.add();
            assertFalse(student.addGrade(subject, 1));
            assertTrue(dbManager.getGrades(student, subject).isEmpty());
            assertFalse(student.addGrade(subject, 6));
            assertTrue(dbManager.getGrades(student, subject).isEmpty());
        }

        @Test
        public void givenCorrectDataAndStudentWithoutAnyGradesWhenAddGradeThenAdded() {
            Student student = new Student("John", "Carter", dbManager);
            Subject subject = new Subject("Math", dbManager);
            student.add();
            subject.add();
            assertTrue(student.addGrade(subject, 2));
            assertEquals(1, dbManager.getGrades(student, subject).size());
            assertEquals(2, dbManager.getGrades(student, subject).get(0).intValue());
        }

        @Test
        public void givenCorrectDataAndStudentWithoutGradesFromGivenSubjectWhenAddGradeThenAdded() {
            Student student = new Student("John", "Carter", dbManager);
            Subject subject1 = new Subject("Math", dbManager);
            Subject subject2 = new Subject("Biology", dbManager);
            student.add();
            subject1.add();
            subject2.add();
            student.addGrade(subject2, 3);
            assertTrue(student.addGrade(subject1, 2));
            assertEquals(1, dbManager.getGrades(student, subject1).size());
            assertEquals(2, dbManager.getGrades(student, subject1).get(0).intValue());
        }

        @Test
        public void givenCorrectDataAndStudentWith3DifferentGradesFromGivenSubjectWhenAddDifferentGradeThenAdded() {
            Student student = new Student("John", "Carter", dbManager);
            Subject subject = new Subject("Math", dbManager);
            student.add();
            subject.add();
            student.addGrade(subject, 2);
            student.addGrade(subject, 3);
            student.addGrade(subject, 4);
            assertTrue(student.addGrade(subject, 5));
            assertEquals(4, dbManager.getGrades(student, subject).size());
            ArrayList<Integer> actual = dbManager.getGrades(student, subject);
            ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(2, 3, 4, 5));
            actual.sort(Comparator.naturalOrder());
            assertEquals(expected, actual);
        }

        @Test
        public void givenCorrectDataAndStudentWith3TheSameGradesFromGivenSubjectWhenAddDifferentGradeThenAdded() {
            Student student = new Student("John", "Carter", dbManager);
            Subject subject = new Subject("Math", dbManager);
            student.add();
            subject.add();
            student.addGrade(subject, 2);
            student.addGrade(subject, 2);
            student.addGrade(subject, 2);
            assertTrue(student.addGrade(subject, 5));
            assertEquals(4, dbManager.getGrades(student, subject).size());
            ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(2, 2, 2, 5));
            ArrayList<Integer> actual = dbManager.getGrades(student, subject);
            actual.sort(Comparator.naturalOrder());
            assertEquals(expected, actual);
        }

        @Test
        public void givenCorrectDataAndStudentWith3TheSameGradesFromGivenSubjectWhenAddTheSameGradeThenAdded() {
            Student student = new Student("John", "Carter", dbManager);
            Subject subject = new Subject("Math", dbManager);
            student.add();
            subject.add();
            student.addGrade(subject, 3);
            student.addGrade(subject, 3);
            student.addGrade(subject, 3);
            assertTrue(student.addGrade(subject, 3));
            assertEquals(4, dbManager.getGrades(student, subject).size());
            ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(3, 3, 3, 3));
            ArrayList<Integer> actual = dbManager.getGrades(student, subject);
            assertEquals(expected, actual);
        }
    }
}
