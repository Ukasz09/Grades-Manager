package com.github.ukasz09.api;

import com.github.ukasz09.app.GradesManagerDb;
import com.github.ukasz09.app.Logger;
import com.github.ukasz09.app.Student;
import com.github.ukasz09.app.Subject;

import java.util.Scanner;
import java.util.logging.Level;

public class App {
    private GradesManagerDb dbManager = new GradesManagerDb();
    protected static final String CORRECT = "OK";
    protected static final String INCORRECT = "ERROR";

    //----------------------------------------------------------------------------------------------------------------//
    public static void main(String[] params) {
        String result = new App().doOperation(params);
        Logger.logMsg(result, Logger.consoleOut);
    }

    protected String doOperation(String[] params) {
        if (params == null || params.length == 0 || params[0] == null)
            return INCORRECT;
        switch (params[0]) {
            case "add":
                return doAddOperation(params);
            case "del":
                return doDelOperation(params);
            case "count":
                return doCountOperation(params);
            case "set":
                return doSetOperation(params);
            case "average":
                return calcAvg(params);
            default:
                return INCORRECT;
        }
    }

    protected String doAddOperation(String[] params) {
        if (params[1] == null)
            return INCORRECT;
        if (params[1].equals("student"))
            return addStudent(params);
        if (params[1].equals("subject"))
            return addSubject(params);
        return INCORRECT;
    }

    protected String addStudent(String[] params) {
        if (params[2] == null || params[3] == null)
            return INCORRECT;
        String name = params[2];
        String surname = params[3];
        boolean result = new Student(name, surname, getDbManager()).add();
        return getStringResult(result);
    }

    protected String addSubject(String[] params) {
        if (params[2] == null)
            return INCORRECT;
        String name = params[2];
        boolean result = new Subject(name, getDbManager()).add();
        return getStringResult(result);
    }

    protected String getStringResult(boolean correct) {
        return correct ? CORRECT : INCORRECT;
    }

    protected String doDelOperation(String[] params) {
        if (params[1] == null)
            return INCORRECT;
        if (params[1].equals("student"))
            return delStudent(params);
        if (params[1].equals("subject"))
            return delSubject(params);
        return INCORRECT;
    }

    protected String delStudent(String[] params) {
        if (params[2] == null || params[3] == null)
            return INCORRECT;
        String name = params[2];
        String surname = params[3];
        boolean result = new Student(name, surname, getDbManager()).delete();
        return getStringResult(result);
    }

    protected String delSubject(String[] params) {
        if (params[2] == null)
            return INCORRECT;
        String name = params[2];
        boolean result = new Subject(name, getDbManager()).delete();
        return getStringResult(result);
    }

    protected String doCountOperation(String[] params) {
        if (params[1] == null)
            return INCORRECT;
        if (params[1].equals("students"))
            return String.valueOf(getDbManager().countStudents());
        if (params[1].equals("subjects"))
            return String.valueOf(getDbManager().countSubjects());
        return INCORRECT;
    }

    protected String doSetOperation(String[] params) {
        if (params[1] != null && params[1].equals("grade")) {
            String studName = params[2];
            String studSurname = params[3];
            String subjName = params[4];
            String strGrade = params[5];
            return addGrade(studName, studSurname, subjName, strGrade);
        }
        return INCORRECT;
    }

    protected String addGrade(String studName, String studSurname, String subjName, String strGrade) {
        if (studName != null && studSurname != null && subjName != null && strGrade != null) {
            try {
                int grade = parseGrade(strGrade);
                Subject subject = new Subject(subjName, getDbManager());
                boolean result = new Student(studName, studSurname, getDbManager()).addGrade(subject, grade);
                return getStringResult(result);
            } catch (NumberFormatException e) {
                return INCORRECT;
            }
        }
        return INCORRECT;
    }

    protected int parseGrade(String grade) {
        String[] gradeParts = grade.split("\\.");
        if (gradeParts.length == 1)
            return Integer.parseInt(grade);
        return Integer.parseInt(gradeParts[1]) == 0 ? Integer.parseInt(gradeParts[0]) : 0;
    }

    protected String calcAvg(String[] params) {
        String studName = params[1];
        String studSurname = params[2];
        String subjName = params[3];
        if (studName != null && studSurname != null && subjName != null) {
            Subject subject = new Subject(subjName, getDbManager());
            return String.valueOf(new Student(studName, studSurname, getDbManager()).avgGrade(subject));
        }
        return String.valueOf(0d);
    }

    protected GradesManagerDb getDbManager() {
        return dbManager;
    }
}
