package com.github.ukasz09.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        assertTrue(dbManager.dropDb());
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
            assertTrue(new Student("Penelope", "Cruze", dbManager).add());
            assertEquals(3, dbManager.countStudents());
            assertTrue(dbManager.existInDb(new Student("John", "Carter", dbManager)));
            assertTrue(dbManager.existInDb(new Student("Will", "Smith", dbManager)));
            assertTrue(dbManager.existInDb(new Student("Penelope", "Cruze", dbManager)));
        }

        @Test
        public void givenNotEmptyCollectionWhenAddStudentWithTheSameNameThenAdded() {
            new Student("John", "Carter", dbManager).add();
            assertTrue(new Student("Johny", "Dicaprio", dbManager).add());
            assertEquals(2, dbManager.countStudents());
            assertTrue(dbManager.existInDb(new Student("John", "Carter", dbManager)));
            assertTrue(dbManager.existInDb(new Student("Johny", "Dicaprio", dbManager)));
        }

        @Test
        public void givenNotEmptyCollectionWhenAddStudentWithTheSameSurnameThenAdded() {
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
            assertFalse(new Student("Johnny", "Bravos", dbManager).delete());
            assertEquals(3, dbManager.countStudents());
            assertFalse(dbManager.existInDb(new Student("Johnny", "Bravo", dbManager)));
        }

        @Test
        public void given3StudentsInCollectionWhenDelete1OfThemThenDeleted() {
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
    class GradesGetting {
        @Test
        public void givenNoGradesWhenGetGradesThenEmpty() {
            Student student = new Student("Jonny", "Cart", dbManager);
            student.add();
            Subject subject = new Subject("Math", dbManager);
            subject.add();
            assertTrue(dbManager.getGrades(student, subject).isEmpty());
        }

        @Test
        public void given3GradesForSubjectWhenGetGradesForOtherSubjectThenEmpty() {
            Student student = new Student("Jonny", "Cart", dbManager);
            Subject subject = new Subject("Math", dbManager);
            student.add();
            subject.add();
            student.addGrade(subject, 3);
            student.addGrade(subject, 2);
            student.addGrade(subject, 4);
            assertTrue(dbManager.getGrades(student, new Subject("Biology", dbManager)).isEmpty());
        }

        @Test
        public void givenGradesFor2SubjectsWhenGetGradesFor1OfThemThenProperResults() {
            Student student = new Student("Jonny", "Cart", dbManager);
            student.add();
            Subject subject1 = new Subject("Math", dbManager);
            subject1.add();
            Subject subject2 = new Subject("Biology", dbManager);
            subject2.add();
            student.addGrade(subject1, 3);
            student.addGrade(subject1, 2);
            student.addGrade(subject1, 4);
            student.addGrade(subject2, 4);
            student.addGrade(subject2, 4);
            assertEquals(3, dbManager.getGrades(student, subject1).size());
            List<Integer> actual = dbManager.getGrades(student, subject1);
            Collections.sort(actual);
            assertEquals(Arrays.asList(2, 3, 4), actual);
        }

        @Test
        public void givenGradesFor1SubjectsAnd2StudentsWhenGetGradesFor1OfThemThenProperResults() {
            Student student1 = new Student("Jonny", "Cart", dbManager);
            student1.add();
            Student student2 = new Student("Stephan", "Jimis", dbManager);
            student2.add();
            Subject subject = new Subject("Math", dbManager);
            subject.add();
            student1.addGrade(subject, 3);
            student1.addGrade(subject, 2);
            student2.addGrade(subject, 4);
            student2.addGrade(subject, 4);
            student2.addGrade(subject, 4);
            assertEquals(2, dbManager.getGrades(student1, subject).size());
            List<Integer> actual = dbManager.getGrades(student1, subject);
            Collections.sort(actual);
            assertEquals(Arrays.asList(2, 3), actual);
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

    @Nested
    class Avg {
        @Test
        public void givenNoStudentsInCollectionWhenAvgThen0() {
            Subject subject = new Subject("Biology", dbManager);
            subject.add();
            assertEquals(0d, new Student("Joshua", "Carter", dbManager).avgGrade(subject));
        }

        @Test
        public void given1StudentsInCollectionWhenAvgOfDifferentStudentThen0() {
            Subject subject = new Subject("Biology", dbManager);
            subject.add();
            new Student("Monica", "Bellini", dbManager).add();
            assertEquals(0d, new Student("Joshua", "Carter", dbManager).avgGrade(subject));
        }

        @Test
        public void givenEmptySubjectsAnd1StudentInCollectionWhenAvgThen0() {
            Student student = new Student("Monica", "Belluci", dbManager);
            student.add();
            assertEquals(0d, student.avgGrade(new Subject("Math", dbManager)));
        }

        @Test
        public void given1SubjectsAnd1StudentInCollectionAndNoGradesWhenAvgThen0() {
            Student student = new Student("Monica", "Belluci", dbManager);
            student.add();
            Subject subject = new Subject("Math", dbManager);
            subject.add();
            assertEquals(0d, student.avgGrade(subject));
        }

        @Test
        public void given1GradeForGivenSubjectWhenAvgThenThisGrade() {
            Student student = new Student("Monica", "Belluci", dbManager);
            student.add();
            Subject subject = new Subject("Math", dbManager);
            subject.add();
            student.addGrade(subject, 4);
            assertEquals(4d, student.avgGrade(subject));
        }

        @Test
        public void given1GradeForOtherSubjectWhenAvgWithDifferentSubjectThen0() {
            Student student = new Student("Monica", "Belluci", dbManager);
            student.add();
            Subject subject = new Subject("Math", dbManager);
            subject.add();
            student.addGrade(subject, 4);
            assertEquals(0, student.avgGrade(new Subject("Biology", dbManager)));
        }

        @Test
        public void given4GradeFor1SubjectWhenAvgWithThenProperResult() {
            Student student = new Student("Monica", "Belluci", dbManager);
            student.add();
            Subject subject = new Subject("Math", dbManager);
            subject.add();
            student.addGrade(subject, 4);
            student.addGrade(subject, 2);
            student.addGrade(subject, 3);
            student.addGrade(subject, 3);
            assertEquals(3d, student.avgGrade(subject));
        }

        @Test
        public void given3GradeFor1SubjectWhenAvgWithThenProperResult() {
            Student student = new Student("Monica", "Belluci", dbManager);
            student.add();
            Subject subject = new Subject("Math", dbManager);
            subject.add();
            student.addGrade(subject, 2);
            student.addGrade(subject, 2);
            student.addGrade(subject, 3);
            assertEquals(7d / 3d, student.avgGrade(subject));
        }

        @Test
        public void given2SubjectWithGradesWhenAvgThenResultOnlyWithGradesFromOneSubject() {
            Student student = new Student("Monica", "Belluci", dbManager);
            student.add();
            Subject subject1 = new Subject("Math", dbManager);
            Subject subject2 = new Subject("Biology", dbManager);
            subject1.add();
            subject2.add();
            student.addGrade(subject1, 2);
            student.addGrade(subject1, 2);
            student.addGrade(subject1, 2);
            student.addGrade(subject2, 2);
            student.addGrade(subject2, 2);
            student.addGrade(subject1, 3);
            assertEquals(9d / 4d, student.avgGrade(subject1));
        }

        @Test
        public void given1SubjectAnd2StudentWithGradesFromItWhenAvgThenProperResult() {
            Student student1 = new Student("Monica", "Belluci", dbManager);
            Student student2 = new Student("Anthony", "Hopkins", dbManager);
            student1.add();
            student2.add();
            Subject subject = new Subject("Math", dbManager);
            subject.add();
            student1.addGrade(subject, 2);
            student1.addGrade(subject, 2);
            student1.addGrade(subject, 3);
            student2.addGrade(subject, 2);
            student2.addGrade(subject, 2);
            student2.addGrade(subject, 3);
            assertEquals(7d / 3d, student1.avgGrade(subject));
        }
    }
}
