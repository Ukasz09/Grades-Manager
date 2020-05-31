package com.ukasz09.github;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TODO: 31.05.2020 : Dodac testy integracyjne na del subj
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
}
