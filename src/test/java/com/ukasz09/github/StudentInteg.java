package com.ukasz09.github;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentInteg {
    @Test
    public void givenRunningMongoDStudentNotInDbBWhenAddThenTrue() {
        Student student = new Student("John", "Carter", new GradesManagerDb());
        assertTrue(student.add());
    }
}
