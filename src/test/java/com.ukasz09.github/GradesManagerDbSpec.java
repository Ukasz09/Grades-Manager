package com.ukasz09.github;

import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import org.jongo.MongoCollection;
import org.jongo.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GradesManagerDbSpec {

    private GradesManagerDb gradesManagerDb;
    private MongoCollection jongoMock;

    @BeforeEach
    public void initializeCollection() {
        gradesManagerDb = spy(new GradesManagerDb());
        jongoMock = mock(MongoCollection.class);
    }

    @Nested
    class WhenExistInDbIsFalse {
        @BeforeEach
        public void initializeCollection() {
            doReturn(false).when(gradesManagerDb).existInDb(any(Student.class));
            doReturn(false).when(gradesManagerDb).existInDb(any(Subject.class));
        }

        @Test
        public void whenStudentAddThenInvokeMongoCollectionInsert() {
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            Student student = new Student("Josh", "Carter");
            gradesManagerDb.add(student);
            verify(jongoMock, times(1)).insert(student);
        }

        @Test
        public void whenSubjectAddThenInvokeMongoCollectionInsert() {
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            Subject subject = new Subject("Biology");
            gradesManagerDb.add(subject);
            verify(jongoMock, times(1)).insert(subject);
        }

        @Test
        public void whenStudentAddToEmptyCollectionThenTrue() {
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            assertTrue(gradesManagerDb.add(new Student("Josh", "Carter")));
        }

        @Test
        public void whenSubjectAddToEmptyCollectionThenTrue() {
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            assertTrue(gradesManagerDb.add(new Subject("Biology")));
        }

        @Test
        public void givenExceptionWhenAddStudentThenFalse() {
            doThrow(new MongoException("bla")).when(jongoMock).insert(any(Student.class));
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            assertFalse(gradesManagerDb.add(new Student("John", "Carter")));
        }

        @Test
        public void givenExceptionWhenAddSubjectThenFalse() {
            doThrow(new MongoException("bla")).when(jongoMock).insert(any(Subject.class));
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            assertFalse(gradesManagerDb.add(new Subject("Biology")));
        }

        @Test
        public void givenOneStudentInCollectionWhenAddDifferentStudentThenTrue() {
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            gradesManagerDb.add(new Student("Josh", "Carter"));
            assertTrue(gradesManagerDb.add(new Student("Penelope", "Cruz")));
        }

        @Test
        public void givenOneSubjectInCollectionWhenAddDifferentSubjectThenTrue() {
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            gradesManagerDb.add(new Subject("Biology"));
            assertTrue(gradesManagerDb.add(new Subject("Math")));
        }

        @Test
        public void givenExceptionWhenDeleteStudentThenFalse() {
            doThrow(new MongoException("bla")).when(jongoMock).remove(anyString());
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            assertFalse(gradesManagerDb.delete(new Student("John", "Carter")));
        }

        @Test
        public void givenExceptionWhenDeleteSubjectThenFalse() {
            doThrow(new MongoException("bla")).when(jongoMock).remove(anyString());
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            assertFalse(gradesManagerDb.delete(new Subject("Biology")));
        }

        @Test
        public void givenEmptyCollectionWhenDeleteStudentThenFalse() {
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            assertFalse(gradesManagerDb.delete(new Student("Josh", "Carter")));
        }

        @Test
        public void givenEmptyCollectionWhenDeleteSubjectThenFalse() {
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            assertFalse(gradesManagerDb.delete(new Subject("Biology")));
        }

        @Test
        public void givenOneStudentInCollectionWhenDeleteDifferentStudentThenFalse() {
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            gradesManagerDb.add(new Student("Josh", "Carter"));
            assertFalse(gradesManagerDb.delete(new Student("Mike", "Spencer")));
        }

        @Test
        public void givenOneSubjectInCollectionWhenDeleteDifferentSubjectThenFalse() {
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            gradesManagerDb.add(new Subject("Biology"));
            assertFalse(gradesManagerDb.delete(new Subject("Math")));
        }
    }

    @Nested
    class WhenExistInDbIsTrue {
        @BeforeEach
        public void initializeCollection() {
            doReturn(true).when(gradesManagerDb).existInDb(any(Student.class));
            doReturn(true).when(gradesManagerDb).existInDb(any(Subject.class));
        }

        @Test
        public void givenOneStudentInCollectionWhenAddTheSameStudentThenFalse() {
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            gradesManagerDb.add(new Student("Josh", "Carter"));
            assertFalse(gradesManagerDb.add(new Student("Josh", "Carter")));
        }

        @Test
        public void givenOneSubjectInCollectionWhenAddTheSameSubjectThenFalse() {
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            gradesManagerDb.add(new Subject("Biology"));
            assertFalse(gradesManagerDb.add(new Subject("Biology")));
        }

        @Test
        public void givenOneStudentInCollectionWhenDeleteThisStudentThenTrue() {
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            gradesManagerDb.add(new Student("Josh", "Carter"));
            assertTrue(gradesManagerDb.delete(new Student("Josh", "Carter")));
        }

        @Test
        public void givenOneSubjectInCollectionWhenDeleteThisSubjectThenTrue() {
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            gradesManagerDb.add(new Subject("Biology"));
            assertTrue(gradesManagerDb.delete(new Subject("Biology")));
        }

        @Test
        public void whenAddGradeWithCorrectDataTheTrue() {
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            doReturn(mock(Update.class)).when(jongoMock).update(anyString(), any(Object.class));
            Student student = new Student("John", "Carter");
            Subject subject = new Subject("Biology");
            assertTrue(gradesManagerDb.addGrade(student, subject, 2));
        }
    }

    @Nested
    class WhenNotUsedExistInDb {
        @Test
        public void whenInstantiatedThenMongoHasProperDbName() {
            gradesManagerDb = new GradesManagerDb();
            assertEquals(GradesManagerDb.DATABASE_NAME, gradesManagerDb.getStudentsCollection().getDBCollection().getDB().getName());
            assertEquals(GradesManagerDb.DATABASE_NAME, gradesManagerDb.getSubjectsCollection().getDBCollection().getDB().getName());
        }

        @Test
        public void whenInstantiatedThenMongoStudentsCollectionHasProperName() {
            gradesManagerDb = new GradesManagerDb();
            assertEquals(GradesManagerDb.STUDENTS_COLLECTION_NAME, gradesManagerDb.getStudentsCollection().getDBCollection().getName());
        }

        @Test
        public void whenInstantiatedThenMongoSubjectsCollectionHasProperName() {
            gradesManagerDb = new GradesManagerDb();
            assertEquals(GradesManagerDb.SUBJECTS_COLLECTION_NAME, gradesManagerDb.getSubjectsCollection().getDBCollection().getName());
        }

        @Test
        public void givenCollectionSize2WhenCountStudentThen2() {
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            doReturn(2L).when(jongoMock).count();
            assertEquals(2, gradesManagerDb.countStudents());
        }

        @Test
        public void givenCollectionSize2WhenCountSubjectsThen2() {
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            doReturn(2L).when(jongoMock).count();
            assertEquals(2, gradesManagerDb.countSubjects());
        }

        @Test
        public void givenEmptyCollectionWhenCountStudentThen0() {
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            doReturn(0L).when(jongoMock).count(anyString());
            assertEquals(0, gradesManagerDb.countStudents());
        }

        @Test
        public void givenEmptyCollectionWhenCountSubjectsThen0() {
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            doReturn(0L).when(jongoMock).count(anyString());
            assertEquals(0, gradesManagerDb.countSubjects());
        }
    }

    @Nested
    class Other {
        @BeforeEach
        public void initialize(){
            doReturn(jongoMock).when(gradesManagerDb).getStudentsCollection();
            doReturn(jongoMock).when(gradesManagerDb).getSubjectsCollection();
            doReturn(mock(Update.class)).when(jongoMock).update(anyString(), any(Object.class));
        }

        @Test
        public void whenAddGradeForNotExistingStudentThenFalse() {
            doReturn(false).when(gradesManagerDb).existInDb(any(Student.class));
            doReturn(true).when(gradesManagerDb).existInDb(any(Subject.class));
            Student student = new Student("John", "Carter");
            Subject subject = new Subject("Biology");
            assertFalse(gradesManagerDb.addGrade(student, subject, 2));
        }

        @Test
        public void whenAddGradeForNotExistingSubjectThenFalse() {
            doReturn(true).when(gradesManagerDb).existInDb(any(Student.class));
            doReturn(false).when(gradesManagerDb).existInDb(any(Subject.class));
            Student student = new Student("John", "Carter");
            Subject subject = new Subject("Biology");
            assertFalse(gradesManagerDb.addGrade(student, subject, 2));
        }
    }
}
