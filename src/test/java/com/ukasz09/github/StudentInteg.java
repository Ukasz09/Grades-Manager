package com.ukasz09.github;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    class AddStudent {
        @Test
        public void givenEmptyCollectionWhenAddThenTrue() {
            assertTrue(new Student("John", "Carter", dbManager).add());
        }

        @Test
        public void givenStudentInCollectionWhenAddTheSameStudentThenFalse() {
            new Student("John", "Carter", dbManager).add();
            assertFalse(new Student("John", "Carter", dbManager).add());
        }

        @Test
        public void givenNotEmptyCollectionWhenAddDifferentStudentThenTrue() {
            new Student("John", "Carter", dbManager).add();
            new Student("Will", "Smith", dbManager).add();
            assertTrue(new Student("Penelope", "Cruz", dbManager).add());
        }

        @Test
        public void givenNotEmptyCollectionWhenAddStudentWithTheSameNameThenTrue() {
            new Student("John", "Carter", dbManager).add();
            assertTrue(new Student("Johny", "Dicaprio", dbManager).add());
        }

        @Test
        public void givenNotEmptyCollectionWhenAddStudentWithTheSameSurnameThenTrue() {
            new Student("John", "Carter", dbManager).add();
            assertTrue(new Student("Eleonore", "Carter", dbManager).add());
        }
    }
}
