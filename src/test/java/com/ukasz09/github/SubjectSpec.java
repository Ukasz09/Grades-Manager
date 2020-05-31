package com.ukasz09.github;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SubjectSpec {
    GradesManagerDb dbSpy;

    //----------------------------------------------------------------------------------------------------------------//
    @BeforeEach
    public void initialize() {
        dbSpy = spy(GradesManagerDb.class);
    }

    @Nested
    class Equality {
        @Test
        public void givenTwoSubjectWhenTheSameNameThenEqual() {
            Subject s1 = new Subject("Math", dbSpy);
            Subject s2 = new Subject("Math", dbSpy);
            assertEquals(s1, s2);
        }

        @Test
        public void givenTwoSubjectsWhenDifferentNameThenNotEqual() {
            Subject s1 = new Subject("Math", dbSpy);
            Subject s2 = new Subject("Biology", dbSpy);
            assertNotEquals(s1, s2);
        }

        @Test
        public void givenTwoSubjectEqualWhenHashCodeCompareThenTheSame() {
            Subject s1 = new Subject("Math", dbSpy);
            Subject s2 = new Subject("Math", dbSpy);
            assertEquals(s1.hashCode(), s2.hashCode());
        }
    }

    @Nested
    class Adding {
        @Test
        public void whenAddSubjectWithNameContainsDigitsThenFalse() {
            doReturn(true).when(dbSpy).add(any(Student.class));
            assertFalse(new Subject("M2ath", dbSpy).add());
        }

        @Test
        public void whenAddSubjectWithNameStartedWithLowercaseThenFalse() {
            doReturn(true).when(dbSpy).add(any(Student.class));
            assertFalse(new Subject("biology", dbSpy).add());
        }
    }
}
