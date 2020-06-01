package com.github.ukasz09.app;

import com.mongodb.DB;
import com.mongodb.MongoException;
import org.jongo.MongoCollection;
import org.jongo.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GradesManagerDbSpec {
    private GradesManagerDb dbSpy;
    private MongoCollection jongoMock;

    //----------------------------------------------------------------------------------------------------------------//
    @BeforeEach
    public void initializeCollection() {
        dbSpy = spy(new GradesManagerDb());
        jongoMock = mock(MongoCollection.class);
    }

    @Nested
    class InstantiatingAndDroppingDatabase {
        @Test
        public void whenInstantiatedThenMongoHasProperDbName() {
            dbSpy = new GradesManagerDb();
            assertEquals(GradesManagerDb.DATABASE_NAME, dbSpy.getStudentsCollection().getDBCollection().getDB().getName());
            assertEquals(GradesManagerDb.DATABASE_NAME, dbSpy.getSubjectsCollection().getDBCollection().getDB().getName());
        }

        @Test
        public void whenInstantiatedThenMongoStudentsCollectionHasProperName() {
            dbSpy = new GradesManagerDb();
            assertEquals(GradesManagerDb.STUDENTS_COLLECTION_NAME, dbSpy.getStudentsCollection().getDBCollection().getName());
        }

        @Test
        public void whenInstantiatedThenMongoSubjectsCollectionHasProperName() {
            dbSpy = new GradesManagerDb();
            assertEquals(GradesManagerDb.SUBJECTS_COLLECTION_NAME, dbSpy.getSubjectsCollection().getDBCollection().getName());
        }

        @Test
        public void givenExceptionWhenDropDatabaseThenFalse() {
            DB getDbMock = mock(DB.class);
            doReturn(getDbMock).when(dbSpy).getDb();
            doThrow(MongoException.class).when(getDbMock).dropDatabase();
            assertFalse(dbSpy.dropDb());
        }
    }

    @Nested
    class AddingToDatabase {
        @BeforeEach
        public void initializeCollection() {
            doReturn(jongoMock).when(dbSpy).getStudentsCollection();
            doReturn(jongoMock).when(dbSpy).getSubjectsCollection();
        }

        @Nested
        class Students {
            @Test
            public void whenStudentAddThenInvokeMongoCollectionInsert() {
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                Student student = new Student("Josh", "Carter", dbSpy);
                dbSpy.add(student);
                verify(jongoMock, times(1)).insert(anyString(), anyString(), anyString());
            }

            @Test
            public void whenStudentAddToEmptyCollectionThenTrue() {
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                assertTrue(dbSpy.add(new Student("Josh", "Carter", dbSpy)));
            }

            @Test
            public void givenExceptionWhenAddStudentThenFalse() {
                doThrow(new MongoException("bla")).when(jongoMock).insert(anyString(), anyString(), anyString());
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                assertFalse(dbSpy.add(new Student("John", "Carter", dbSpy)));
            }

            @Test
            public void given1StudentInCollectionWhenAddDifferentStudentThenTrue() {
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                dbSpy.add(new Student("Josh", "Carter", dbSpy));
                assertTrue(dbSpy.add(new Student("Penelope", "Cruz", dbSpy)));
            }

            @Test
            public void given1StudentInCollectionWhenAddTheSameStudentThenFalse() {
                doReturn(true).when(dbSpy).existInDb(any(Student.class));
                doReturn(true).when(dbSpy).existInDb(any(Subject.class));
                dbSpy.add(new Student("Josh", "Carter", dbSpy));
                assertFalse(dbSpy.add(new Student("Josh", "Carter", dbSpy)));
            }
        }

        @Nested
        class Subjects {
            @Test
            public void whenSubjectAddThenInvokeMongoCollectionInsert() {
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                Subject subject = new Subject("Biology", dbSpy);
                dbSpy.add(subject);
                verify(jongoMock, times(1)).insert(anyString(), anyString());
            }

            @Test
            public void whenSubjectAddToEmptyCollectionThenTrue() {
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                assertTrue(dbSpy.add(new Subject("Biology", dbSpy)));
            }

            @Test
            public void givenExceptionWhenAddSubjectThenFalse() {
                doThrow(new MongoException("bla")).when(jongoMock).insert(anyString(), anyString());
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                assertFalse(dbSpy.add(new Subject("Biology", dbSpy)));
            }

            @Test
            public void given1SubjectInCollectionWhenAddDifferentSubjectThenTrue() {
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                dbSpy.add(new Subject("Biology", dbSpy));
                assertTrue(dbSpy.add(new Subject("Math", dbSpy)));
            }

            @Test
            public void given1SubjectInCollectionWhenAddTheSameSubjectThenFalse() {
                doReturn(true).when(dbSpy).existInDb(any(Student.class));
                doReturn(true).when(dbSpy).existInDb(any(Subject.class));
                dbSpy.add(new Subject("Biology", dbSpy));
                assertFalse(dbSpy.add(new Subject("Biology", dbSpy)));
            }
        }

        @Nested
        class Grades {
            @Test
            public void whenGradesAddThenInvokeMongoCollectionUpdate() {
                doReturn(true).when(dbSpy).existInDb(any(Student.class));
                doReturn(true).when(dbSpy).existInDb(any(Subject.class));
                doReturn(mock(Update.class)).when(jongoMock).update(anyString(), anyString(), anyString());
                Student student = new Student("John", "Kennedy", dbSpy);
                Subject subject = new Subject("Biology", dbSpy);
                dbSpy.addGrade(student, subject, 4);
                verify(jongoMock, times(1)).update(anyString(), anyString(), anyString());
            }

            @Test
            public void whenAddGradeForNotExistingStudentThenFalse() {
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(true).when(dbSpy).existInDb(any(Subject.class));
                doReturn(mock(Update.class)).when(jongoMock).update(anyString(), anyString(), anyString());
                Student student = new Student("John", "Carter", dbSpy);
                Subject subject = new Subject("Biology", dbSpy);
                assertFalse(dbSpy.addGrade(student, subject, 2));
            }

            @Test
            public void whenAddGradeForNotExistingSubjectThenFalse() {
                doReturn(true).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                doReturn(mock(Update.class)).when(jongoMock).update(anyString(), anyString(), anyString());
                Student student = new Student("John", "Carter", dbSpy);
                Subject subject = new Subject("Biology", dbSpy);
                assertFalse(dbSpy.addGrade(student, subject, 2));
            }

            @Test
            public void whenAddGradeWithCorrectDataTheTrue() {
                doReturn(true).when(dbSpy).existInDb(any(Student.class));
                doReturn(true).when(dbSpy).existInDb(any(Subject.class));
                doReturn(mock(Update.class)).when(jongoMock).update(anyString(), anyString(), anyString());
                Student student = new Student("John", "Carter", dbSpy);
                Subject subject = new Subject("Biology", dbSpy);
                assertTrue(dbSpy.addGrade(student, subject, 3));
            }
        }
    }

    @Nested
    class DeletingFromDatabase {
        @BeforeEach
        public void initializeCollection() {
            doReturn(jongoMock).when(dbSpy).getStudentsCollection();
            doReturn(jongoMock).when(dbSpy).getSubjectsCollection();
        }

        @Nested
        class Students {
            @Test
            public void givenExceptionWhenDeleteStudentThenFalse() {
                doThrow(new MongoException("bla")).when(jongoMock).remove(anyString());
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                assertFalse(dbSpy.delete(new Student("John", "Carter", dbSpy)));
            }

            @Test
            public void givenEmptyCollectionWhenDeleteStudentThenFalse() {
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                assertFalse(dbSpy.delete(new Student("Josh", "Carter", dbSpy)));
            }

            @Test
            public void given1StudentInCollectionWhenDeleteDifferentStudentThenFalse() {
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                dbSpy.add(new Student("Josh", "Carter", dbSpy));
                assertFalse(dbSpy.delete(new Student("Mike", "Spencer", dbSpy)));
            }

            @Test
            public void givenOneStudentInCollectionWhenDeleteThisStudentThenTrue() {
                doReturn(true).when(dbSpy).existInDb(any(Student.class));
                doReturn(true).when(dbSpy).existInDb(any(Subject.class));
                dbSpy.add(new Student("Josh", "Carter", dbSpy));
                assertTrue(dbSpy.delete(new Student("Josh", "Carter", dbSpy)));
            }
        }

        @Nested
        class Subjects {
            @Test
            public void givenExceptionWhenDeleteSubjectThenFalse() {
                doThrow(new MongoException("bla")).when(jongoMock).remove(anyString());
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                assertFalse(dbSpy.delete(new Subject("Biology", dbSpy)));
            }

            @Test
            public void givenEmptyCollectionWhenDeleteSubjectThenFalse() {
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                assertFalse(dbSpy.delete(new Subject("Biology", dbSpy)));
            }

            @Test
            public void givenOneSubjectInCollectionWhenDeleteDifferentSubjectThenFalse() {
                doReturn(false).when(dbSpy).existInDb(any(Student.class));
                doReturn(false).when(dbSpy).existInDb(any(Subject.class));
                dbSpy.add(new Subject("Biology", dbSpy));
                assertFalse(dbSpy.delete(new Subject("Math", dbSpy)));
            }

            @Test
            public void givenOneSubjectInCollectionWhenDeleteThisSubjectThenTrue() {
                doReturn(true).when(dbSpy).existInDb(any(Student.class));
                doReturn(true).when(dbSpy).existInDb(any(Subject.class));
                doReturn(jongoMock).when(dbSpy).getSubjectsCollection();
                doReturn(jongoMock).when(dbSpy).getStudentsCollection();
                Update updateMock = mock(Update.class);
                doReturn(updateMock).when(jongoMock).update(anyString());
                doReturn(updateMock).when(updateMock).multi();
                dbSpy.add(new Subject("Biology", dbSpy));
                assertTrue(dbSpy.delete(new Subject("Biology", dbSpy)));
            }
        }
    }

    @Nested
    class CountingElementsInDatabase {
        @BeforeEach
        public void initializeCollection() {
            doReturn(jongoMock).when(dbSpy).getStudentsCollection();
            doReturn(jongoMock).when(dbSpy).getSubjectsCollection();
        }

        @Nested
        class Students {
            @Test
            public void givenCollectionSize2WhenCountStudentThen2() {
                doReturn(2L).when(jongoMock).count();
                assertEquals(2, dbSpy.countStudents());
            }

            @Test
            public void givenEmptyCollectionWhenCountStudentThen0() {
                doReturn(0L).when(jongoMock).count(anyString());
                assertEquals(0, dbSpy.countStudents());
            }
        }

        @Nested
        class Subjects {
            @Test
            public void givenCollectionSize2WhenCountSubjectsThen2() {
                doReturn(2L).when(jongoMock).count();
                assertEquals(2, dbSpy.countSubjects());
            }

            @Test
            public void givenEmptyCollectionWhenCountSubjectsThen0() {
                doReturn(0L).when(jongoMock).count(anyString());
                assertEquals(0, dbSpy.countSubjects());
            }
        }
    }

    @Nested
    class AvgGrades {
        @BeforeEach
        public void initializeCollection() {
            doReturn(jongoMock).when(dbSpy).getStudentsCollection();
            doReturn(jongoMock).when(dbSpy).getSubjectsCollection();
        }

        @Test
        public void givenGradesNullThen0() {
            doReturn(new ArrayList<Integer>()).when(dbSpy).getGrades(any(Student.class), any(Subject.class));
            Subject subject = new Subject("Biology", dbSpy);
            Student student = new Student("John", "Carter", dbSpy);
            assertEquals(0d, dbSpy.avgGrade(student, subject));
        }

        @Test
        public void given1GradeWhenAvgThenThis1Grade() {
            ArrayList<Integer> mockedGrades = new ArrayList<>(Collections.singletonList(3));
            doReturn(mockedGrades).when(dbSpy).getGrades(any(Student.class), any(Subject.class));
            Subject subject = new Subject("Biology", dbSpy);
            Student student = new Student("John", "Carter", dbSpy);
            assertEquals(3d, dbSpy.avgGrade(student, subject));
        }

        @Test
        public void given3TheSameGradesFromSubjectWhenAvgThenGivenGrade() {
            ArrayList<Integer> mockedGrades = new ArrayList<>(Arrays.asList(2, 2, 2));
            doReturn(mockedGrades).when(dbSpy).getGrades(any(Student.class), any(Subject.class));
            Subject subject = new Subject("Biology", dbSpy);
            Student student = new Student("John", "Carter", dbSpy);
            assertEquals(2d, dbSpy.avgGrade(student, subject));
        }

        @Test
        public void given3GradesFromSubjectWhenAvgThenProperResult() {
            ArrayList<Integer> mockedGrades = new ArrayList<>(Arrays.asList(3, 2, 5));
            doReturn(mockedGrades).when(dbSpy).getGrades(any(Student.class), any(Subject.class));
            Subject subject = new Subject("Biology", dbSpy);
            Student student = new Student("John", "Carter", dbSpy);
            assertEquals(10d / 3d, dbSpy.avgGrade(student, subject));
        }
    }
}
