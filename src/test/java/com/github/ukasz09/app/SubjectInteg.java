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
public class SubjectInteg {
    private GradesManagerDb dbManager = new GradesManagerDb();

    //----------------------------------------------------------------------------------------------------------------//
    @BeforeEach
    public void clearDb() {
        dbManager.dropDb();
    }

    @Nested
    class AddSubject {
        @Test
        public void givenEmptyCollectionWhenAddThenAdded() {
            assertTrue(new Subject("Math", dbManager).add());
            assertEquals(1, dbManager.countSubjects());
        }

        @Test
        public void givenNotEmptySubjectCollectionWhenAddDifferentSubjectThenAdded() {
            assertTrue(new Subject("Math", dbManager).add());
            assertTrue(new Subject("Biology", dbManager).add());
            assertTrue(new Subject("Physic", dbManager).add());
            assertEquals(3, dbManager.countSubjects());
            assertTrue(dbManager.existInDb(new Subject("Math", dbManager)));
            assertTrue(dbManager.existInDb(new Subject("Biology", dbManager)));
            assertTrue(dbManager.existInDb(new Subject("Physic", dbManager)));
        }

        @Test
        public void given1SubjectInCollectionWhenAddTheSameSubjectThenNotAdded() {
            new Subject("Math", dbManager).add();
            assertFalse(new Subject("Math", dbManager).add());
            assertEquals(1, dbManager.countSubjects());
            assertTrue(dbManager.existInDb(new Subject("Math", dbManager)));
        }
    }

    @Nested
    class Count {
        @Test
        public void givenEmptyCollectionWhenCountThen0() {
            assertEquals(0, dbManager.countSubjects());
        }

        @Test
        public void given4SizeCollectionWhenCountThen4() {
            dbManager.add(new Subject("Math", dbManager));
            dbManager.add(new Subject("Biology", dbManager));
            dbManager.add(new Subject("Physic", dbManager));
            dbManager.add(new Subject("PE", dbManager));
            assertEquals(4, dbManager.countSubjects());
        }
    }

    @Nested
    class Delete {
        @Test
        public void givenEmptyCollectionWhenRemoveThenNoChanges() {
            Subject subject = new Subject("Math", dbManager);
            assertFalse(subject.delete());
            assertEquals(0, dbManager.countSubjects());
            assertFalse(dbManager.existInDb(subject));
        }

        @Test
        public void given1SubjectInCollectionWhenRemoveDifferentThenNoChanges() {
            new Subject("Biology", dbManager).add();
            Subject subject = new Subject("Math", dbManager);
            assertFalse(subject.delete());
            assertFalse(dbManager.existInDb(subject));
            assertEquals(1, dbManager.countSubjects());
        }

        @Test
        public void given1SubjectInCollectionAndNoGradesFromItWhenRemoveThisSubjectThenRemoved() {
            new Subject("Biology", dbManager).add();
            Subject subject = new Subject("Biology", dbManager);
            assertTrue(subject.delete());
            assertEquals(0, dbManager.countSubjects());
        }

        @Test
        public void given1SubjectInCollectionAnd1StudentWithGradesFromItWhenRemoveThisSubjectThenRemovedFromAll() {
            Subject subject = new Subject("Biology", dbManager);
            subject.add();
            Student student = new Student("Joshua", "Carter", dbManager);
            student.add();
            student.addGrade(subject, 2);
            student.addGrade(subject, 3);
            student.addGrade(subject, 2);

            assertTrue(subject.delete());
            assertEquals(0, dbManager.countSubjects());
            assertTrue(dbManager.getGrades(student, subject).isEmpty());
            assertTrue(dbManager.existInDb(student));
        }

        @Test
        public void given2SubjectsInCollectionAnd1StudentsWithGradesFromItWhenRemoveOnOfSubjectsThenRemovedFromAll() {
            Subject subject1 = new Subject("Biology", dbManager);
            Subject subject2 = new Subject("Math", dbManager);
            subject1.add();
            subject2.add();
            Student student = new Student("Joshua", "Carter", dbManager);
            student.add();
            student.addGrade(subject1, 2);
            student.addGrade(subject1, 3);
            student.addGrade(subject2, 2);
            student.addGrade(subject2, 4);

            assertTrue(subject2.delete());
            assertEquals(1, dbManager.countSubjects());
            assertTrue(dbManager.existInDb(subject1));
            assertTrue(dbManager.getGrades(student, subject2).isEmpty());
            List<Integer> biologyGrades = dbManager.getGrades(student, subject1);
            biologyGrades.sort(Comparator.naturalOrder());
            assertTrue(dbManager.existInDb(student));
            assertEquals(Arrays.asList(2, 3), biologyGrades);
        }

        @Test
        public void given2SubjectsInCollectionAnd2StudentsWithGradesFromItWhenRemove1OfSubjectsThenRemovedFormAll() {
            Subject subject1 = new Subject("Biology", dbManager);
            Subject subject2 = new Subject("Math", dbManager);
            subject1.add();
            subject2.add();
            Student student1 = new Student("Joshua", "Carter", dbManager);
            student1.add();
            student1.addGrade(subject1, 2);
            student1.addGrade(subject2, 2);
            student1.addGrade(subject2, 4);
            Student student2 = new Student("Micke", "Jagger", dbManager);
            student2.add();
            student2.addGrade(subject2, 3);
            student2.addGrade(subject2, 5);
            student2.addGrade(subject1, 2);
            student2.addGrade(subject1, 4);
            student2.addGrade(subject1, 4);

            assertTrue(subject2.delete());
            assertEquals(1, dbManager.countSubjects());
            assertTrue(dbManager.existInDb(subject1));
            assertTrue(dbManager.getGrades(student1, subject2).isEmpty());
            assertTrue(dbManager.getGrades(student2, subject2).isEmpty());
            List<Integer> biologyGrades = dbManager.getGrades(student1, subject1);
            biologyGrades.sort(Comparator.naturalOrder());
            assertEquals(Collections.singletonList(2), biologyGrades);
            assertTrue(dbManager.existInDb(student1));
            assertTrue(dbManager.existInDb(student2));
            List<Integer> biologyGrades2 = dbManager.getGrades(student2, subject1);
            biologyGrades2.sort(Comparator.naturalOrder());
            assertEquals(Arrays.asList(2, 4, 4), biologyGrades2);
        }
    }
}
