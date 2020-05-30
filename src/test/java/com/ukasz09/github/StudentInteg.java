package com.ukasz09.github;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
    class AddStudent {
        @Test
        public void givenEmptyCollectionWhenAddThenTrue() {
            assertTrue(new Student("John", "Carter", dbManager).add());
        }

        @Test
        public void given1StudentInCollectionWhenAddTheSameStudentThenFalse() {
            new Student("John", "Carter", dbManager).add();
            assertFalse(new Student("John", "Carter", dbManager).add());
        }

        @Test
        public void givenNotEmptyCollectionWhenAddDifferentStudentThenTrue() {
            assertTrue(new Student("John", "Carter", dbManager).add());
            assertTrue(new Student("Will", "Smith", dbManager).add());
            assertTrue(new Student("Penelope", "Cruz", dbManager).add());
        }

        @Test
        public void givenNotEmptyCollectionWhenAddStudentWithOnlyTheSameNameThenTrue() {
            new Student("John", "Carter", dbManager).add();
            assertTrue(new Student("Johny", "Dicaprio", dbManager).add());
        }

        @Test
        public void givenNotEmptyCollectionWhenAddStudentWithTheOnlySameSurnameThenTrue() {
            new Student("John", "Carter", dbManager).add();
            assertTrue(new Student("Eleonore", "Carter", dbManager).add());
        }
    }
}
