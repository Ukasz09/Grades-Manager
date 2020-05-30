package com.ukasz09.github;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
        public void givenEmptyCollectionWhenAddThenTrue() {
            assertTrue(new Subject("Math", dbManager).add());
        }

        @Test
        public void givenNotEmptySubjectInCollectionWhenAddDifferentSubjectThenTrue() {
            assertTrue(new Subject("Math", dbManager).add());
            assertTrue(new Subject("Biology", dbManager).add());
            assertTrue(new Subject("Physic", dbManager).add());
        }

        @Test
        public void given1SubjectInCollectionWhenAddTheSameSubjectThenFalse() {
            new Subject("Math", dbManager).add();
            assertFalse(new Subject("Math", dbManager).add());
        }
    }
}
