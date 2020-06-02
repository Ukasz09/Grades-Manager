package com.github.ukasz09.appInterface;

import com.github.ukasz09.app.GradesManagerDb;
import com.github.ukasz09.app.Student;
import com.github.ukasz09.app.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppSpec {
    private App spyApp;
    private GradesManagerDb mockDb;
    private String[] params;

    //----------------------------------------------------------------------------------------------------------------//
    @BeforeEach
    public void initialize() {
        spyApp = spy(new App());
        mockDb = mock(GradesManagerDb.class);
        doReturn(mockDb).when(spyApp).getDbManager();

    }

    @Nested
    class DoOperationMethod {
        @BeforeEach
        public void initialize() {
            params = new String[1];
            doReturn("").when(spyApp).doAddOperation(params);
            doReturn("").when(spyApp).doDelOperation(params);
            doReturn("").when(spyApp).doCountOperation(params);
            doReturn("").when(spyApp).doSetOperation(params);
            doReturn("").when(spyApp).calcAvg(params);
        }

        @Test
        public void givenParamAddWhenDoOperationThenAddOperationInvoked() {
            params[0] = "add";
            spyApp.doOperation(params);
            verify(spyApp, times(1)).doAddOperation(params);
            verify(spyApp, times(0)).doDelOperation(params);
            verify(spyApp, times(0)).doCountOperation(params);
            verify(spyApp, times(0)).doSetOperation(params);
            verify(spyApp, times(0)).calcAvg(params);
        }

        @Test
        public void givenParamDelWhenDoOperationThenDelOperationInvoked() {
            params[0] = "del";
            spyApp.doOperation(params);
            verify(spyApp, times(0)).doAddOperation(params);
            verify(spyApp, times(1)).doDelOperation(params);
            verify(spyApp, times(0)).doCountOperation(params);
            verify(spyApp, times(0)).doSetOperation(params);
            verify(spyApp, times(0)).calcAvg(params);
        }

        @Test
        public void givenParamCountWhenDoOperationThenCountOperationInvoked() {
            params[0] = "count";
            spyApp.doOperation(params);
            verify(spyApp, times(0)).doAddOperation(params);
            verify(spyApp, times(0)).doDelOperation(params);
            verify(spyApp, times(1)).doCountOperation(params);
            verify(spyApp, times(0)).doSetOperation(params);
            verify(spyApp, times(0)).calcAvg(params);
        }

        @Test
        public void givenParamSetWhenDoOperationThenSetOperationInvoked() {
            params[0] = "set";
            spyApp.doOperation(params);
            verify(spyApp, times(0)).doAddOperation(params);
            verify(spyApp, times(0)).doDelOperation(params);
            verify(spyApp, times(0)).doCountOperation(params);
            verify(spyApp, times(1)).doSetOperation(params);
            verify(spyApp, times(0)).calcAvg(params);
        }

        @Test
        public void givenParamAverageWhenDoOperationThenAverageOperationInvoked() {
            params[0] = "average";
            spyApp.doOperation(params);
            verify(spyApp, times(0)).doAddOperation(params);
            verify(spyApp, times(0)).doDelOperation(params);
            verify(spyApp, times(0)).doCountOperation(params);
            verify(spyApp, times(0)).doSetOperation(params);
            verify(spyApp, times(1)).calcAvg(params);
        }

        @Test
        public void givenNullParamArrWhenDoOperationThenNothingInvoked() {
            params = null;
            spyApp.doOperation(params);
            verify(spyApp, times(0)).doAddOperation(params);
            verify(spyApp, times(0)).doDelOperation(params);
            verify(spyApp, times(0)).doCountOperation(params);
            verify(spyApp, times(0)).doSetOperation(params);
            verify(spyApp, times(0)).calcAvg(params);
        }

        @Test
        public void givenLength0ArrParamWhenDoOperationThenNothingInvoked() {
            params = new String[0];
            spyApp.doOperation(params);
            verify(spyApp, times(0)).doAddOperation(params);
            verify(spyApp, times(0)).doDelOperation(params);
            verify(spyApp, times(0)).doCountOperation(params);
            verify(spyApp, times(0)).doSetOperation(params);
            verify(spyApp, times(0)).calcAvg(params);
        }

        @Test
        public void givenNullParamWhenDoOperationThenNothingInvoked() {
            params[0] = null;
            spyApp.doOperation(params);
            verify(spyApp, times(0)).doAddOperation(params);
            verify(spyApp, times(0)).doDelOperation(params);
            verify(spyApp, times(0)).doCountOperation(params);
            verify(spyApp, times(0)).doSetOperation(params);
            verify(spyApp, times(0)).calcAvg(params);
        }
    }

    @Nested
    class Add {
        @BeforeEach
        public void initialize() {
            params = new String[2];
            doReturn("").when(spyApp).addStudent(params);
            doReturn("").when(spyApp).addSubject(params);
        }

        @Test
        public void givenParamStudentWhenDoAddOperationThenAddStudentInvoked() {
            params[1] = "student";
            spyApp.doAddOperation(params);
            verify(spyApp, times(1)).addStudent(params);
            verify(spyApp, times(0)).addSubject(params);
        }

        @Test
        public void givenParamSubjectWhenDoAddOperationThenAddSubjectInvoked() {
            params[1] = "subject";
            spyApp.doAddOperation(params);
            verify(spyApp, times(0)).addStudent(params);
            verify(spyApp, times(1)).addSubject(params);
        }

        @Test
        public void givenNullParamWhenDoAddOperationThenNothingInvoked() {
            params[1] = null;
            spyApp.doAddOperation(params);
            verify(spyApp, times(0)).addStudent(params);
            verify(spyApp, times(0)).addSubject(params);
        }

        @Test
        public void givenIncorrectParamWhenDoAddOperationThenNothingInvoked() {
            params[1] = "stud";
            spyApp.doAddOperation(params);
            verify(spyApp, times(0)).addStudent(params);
            verify(spyApp, times(0)).addSubject(params);
        }

        @Test
        public void givenCorrectParamsWhenAddStudentThenAdded() {
            doReturn(true).when(mockDb).add(any(Student.class));
            String[] params = new String[4];
            params[2] = "Joshua";
            params[3] = "Carter";
            String result = spyApp.addStudent(params);
            assertEquals(spyApp.getStringResult(true), result);
        }

        @Test
        public void givenIncorrectParamsWhenAddStudentThenNotAdded() {
            doReturn(false).when(mockDb).add(any(Student.class));
            String[] params = new String[4];
            params[2] = null;
            params[3] = "Carter";
            String result = spyApp.addStudent(params);
            assertEquals(spyApp.getStringResult(false), result);
        }

        @Test
        public void givenCorrectParamWhenAddSubjectThenAdded() {
            doReturn(true).when(mockDb).add(any(Subject.class));
            String[] params = new String[3];
            params[2] = "PE";
            String result = spyApp.addSubject(params);
            assertEquals(spyApp.getStringResult(true), result);
        }

        @Test
        public void givenIncorrectParamsWhenAddSubjectThenNotAdded() {
            doReturn(false).when(mockDb).add(any(Subject.class));
            String[] params = new String[3];
            params[2] = null;
            String result = spyApp.addSubject(params);
            assertEquals(spyApp.getStringResult(false), result);
        }
    }

    @Nested
    class Delete {
        @BeforeEach
        public void initialize() {
            params = new String[2];
            doReturn("").when(spyApp).delStudent(params);
            doReturn("").when(spyApp).delSubject(params);
        }

        @Test
        public void givenParamStudentWhenDoDelOperationThenDelStudentInvoked() {
            params[1] = "student";
            spyApp.doDelOperation(params);
            verify(spyApp, times(1)).delStudent(params);
            verify(spyApp, times(0)).delSubject(params);
        }

        @Test
        public void givenParamSubjectWhenDoAddOperationThenAddSubjectInvoked() {
            params[1] = "subject";
            spyApp.doDelOperation(params);
            verify(spyApp, times(0)).delStudent(params);
            verify(spyApp, times(1)).delSubject(params);
        }

        @Test
        public void givenNullParamWhenDoAddOperationThenNothingInvoked() {
            params[1] = null;
            spyApp.doDelOperation(params);
            verify(spyApp, times(0)).delStudent(params);
            verify(spyApp, times(0)).delSubject(params);
        }

        @Test
        public void givenIncorrectParamWhenDoAddOperationThenNothingInvoked() {
            params[1] = "stud";
            spyApp.doDelOperation(params);
            verify(spyApp, times(0)).delStudent(params);
            verify(spyApp, times(0)).delSubject(params);
        }

        @Test
        public void givenCorrectParamsWhenDelStudentThenDeleted() {
            doReturn(true).when(mockDb).delete(any(Student.class));
            String[] params = new String[4];
            params[2] = "Joshua";
            params[3] = "Carter";
            String result = spyApp.delStudent(params);
            assertEquals(spyApp.getStringResult(true), result);
        }

        @Test
        public void givenIncorrectParamsWhenDelStudentThenNoChanges() {
            doReturn(false).when(mockDb).delete(any(Student.class));
            String[] params = new String[4];
            params[2] = null;
            params[3] = "Carter";
            String result = spyApp.delStudent(params);
            assertEquals(spyApp.getStringResult(false), result);
        }

        @Test
        public void givenCorrectParamWhenDelSubjectThenDeleted() {
            doReturn(true).when(mockDb).delete(any(Subject.class));
            String[] params = new String[3];
            params[2] = "PE";
            String result = spyApp.delSubject(params);
            assertEquals(spyApp.getStringResult(true), result);
        }

        @Test
        public void givenIncorrectParamWhenDelSubjectThenNoChanges() {
            doReturn(false).when(mockDb).delete(any(Subject.class));
            String[] params = new String[3];
            params[2] = null;
            String result = spyApp.delSubject(params);
            assertEquals(spyApp.getStringResult(false), result);
        }

        @Test
        public void givenCorrectParamsWhenAddGradeThenAdded() {
            doReturn(true).when(mockDb).addGrade(any(Student.class), any(Subject.class), anyInt());
            String result = spyApp.addGrade("Jotaro", "Nagasaki", "PE", "4");
            assertEquals(spyApp.getStringResult(true), result);
        }

        @Test
        public void givenIncorrectStudentNameWhenAddGradeThenNoChanges() {
            doReturn(false).when(mockDb).addGrade(any(Student.class), any(Subject.class), anyInt());
            String result = spyApp.addGrade(null, "Nagasaki", "PE", "4");
            assertEquals(spyApp.getStringResult(false), result);
        }

        @Test
        public void givenIncorrectStudentSurnameWhenAddGradeThenNoChanges() {
            doReturn(false).when(mockDb).addGrade(any(Student.class), any(Subject.class), anyInt());
            String result = spyApp.addGrade("Micke", null, "PE", "4");
            assertEquals(spyApp.getStringResult(false), result);
        }

        @Test
        public void givenIncorrectSubjectNameWhenAddGradeThenNoChanges() {
            doReturn(false).when(mockDb).addGrade(any(Student.class), any(Subject.class), anyInt());
            String result = spyApp.addGrade("Micke", "Nagasaki", null, "4");
            assertEquals(spyApp.getStringResult(false), result);
        }

        @Test
        public void givenIncorrectGradeNameWhenAddGradeThenNoChanges() {
            doReturn(false).when(mockDb).addGrade(any(Student.class), any(Subject.class), anyInt());
            String result = spyApp.addGrade("Micke", "Nagasaki", "PE", null);
            assertEquals(spyApp.getStringResult(false), result);
            String result2 = spyApp.addGrade("Micke", "Nagasaki", "PE", "dwa");
            assertEquals(spyApp.getStringResult(false), result2);
        }
    }

    @Nested
    class CountOperation {
        private GradesManagerDb dbManagerMock;

        //----------------------------------------------------------------------------------------------------------------//
        @BeforeEach
        public void initialize() {
            params = new String[2];
            dbManagerMock = mock(GradesManagerDb.class);
            doReturn(0L).when(dbManagerMock).countSubjects();
            doReturn(0L).when(dbManagerMock).countStudents();
            doReturn(dbManagerMock).when(spyApp).getDbManager();
        }

        @Test
        public void givenParamStudentWhenDoCountOperationThenCountStudentInvoked() {
            params[1] = "students";
            spyApp.doCountOperation(params);
            verify(dbManagerMock, times(1)).countStudents();
            verify(dbManagerMock, times(0)).countSubjects();
        }

        @Test
        public void givenParamSubjectWhenDoCountOperationThenCountSubjectInvoked() {
            params[1] = "subjects";
            spyApp.doCountOperation(params);
            verify(dbManagerMock, times(0)).countStudents();
            verify(dbManagerMock, times(1)).countSubjects();
        }

        @Test
        public void givenNullParamWhenDoCountOperationThenNothingInvoked() {
            params[1] = null;
            spyApp.doCountOperation(params);
            verify(dbManagerMock, times(0)).countStudents();
            verify(dbManagerMock, times(0)).countSubjects();
        }

        @Test
        public void givenIncorrectParamWhenDoAddOperationThenNothingInvoked() {
            params[1] = "student";
            spyApp.doCountOperation(params);
            verify(dbManagerMock, times(0)).countStudents();
            verify(dbManagerMock, times(0)).countSubjects();
        }
    }

    @Nested
    class SetOperation {
        @BeforeEach
        public void initialize() {
            params = new String[6];
            Arrays.fill(params, "");
            doReturn("").when(spyApp).addGrade(anyString(), anyString(), anyString(), anyString());
        }

        @Test
        public void givenParamGradeWhenDoSetGradeOperationThenAddGradeInvoked() {
            params[1] = "grade";
            spyApp.doSetOperation(params);
            verify(spyApp, times(1)).addGrade(anyString(), anyString(), anyString(), anyString());
        }

        @Test
        public void givenNullParamWhenDoSetGradeOperationThenAddGradeNotInvoked() {
            params[1] = null;
            spyApp.doSetOperation(params);
            verify(spyApp, times(0)).addGrade(anyString(), anyString(), anyString(), anyString());
        }

        @Test
        public void givenIncorrectParamWhenDoSetGradeOperationThenAddGradeNotInvoked() {
            params[1] = "grades";
            spyApp.doSetOperation(params);
            verify(spyApp, times(0)).addGrade(anyString(), anyString(), anyString(), anyString());
        }
    }

    @Nested
    class Others {
        @Test
        public void givenTrueWhenGetStringResultThenProperResult() {
            assertEquals(App.CORRECT, spyApp.getStringResult(true));
        }

        @Test
        public void givenFalseWhenGetStringResultThenProperResult() {
            assertEquals(App.INCORRECT, spyApp.getStringResult(false));
        }

        @Test
        public void givenDigitWhenParseNumberThenCorrectResult() {
            assertEquals(4, spyApp.parseGrade("4"));
            assertEquals(4, spyApp.parseGrade("4.0"));
            assertEquals(0, spyApp.parseGrade("0"));
            assertEquals(0, spyApp.parseGrade("0.0"));
            assertEquals(13, spyApp.parseGrade("13.0"));
            assertEquals(7, spyApp.parseGrade("7.00000"));
        }

        @Test
        public void givenCorrectParamsWhenAvgThenAvgCalcInvoked() {
            doReturn(4.7).when(mockDb).avgGrade(any(Student.class), any(Subject.class));
            String[] params = {"", "Ann", "Jotar", "PE"};
            String result = spyApp.calcAvg(params);
            assertEquals("4.7", result);
        }
    }
}
