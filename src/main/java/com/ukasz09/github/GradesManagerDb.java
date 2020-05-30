package com.ukasz09.github;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

public class GradesManagerDb {
    protected static final String DATABASE_NAME = "GradesManager";
    protected static final String STUDENTS_COLLECTION_NAME = "Students";
    protected static final String SUBJECTS_COLLECTION_NAME = "Subjects";
    private MongoCollection studentsCollection;
    private MongoCollection subjectsCollection;

    //----------------------------------------------------------------------------------------------------------------//
    public GradesManagerDb() {
        DB db = new MongoClient().getDB(DATABASE_NAME);
        studentsCollection = new Jongo(db).getCollection(STUDENTS_COLLECTION_NAME);
        subjectsCollection = new Jongo(db).getCollection(SUBJECTS_COLLECTION_NAME);
    }

    //----------------------------------------------------------------------------------------------------------------//
    protected MongoCollection getStudentsCollection() {
        return studentsCollection;
    }

    protected MongoCollection getSubjectsCollection() {
        return subjectsCollection;

    }

    // TODO: 30.05.2020 : merge ?
    public boolean add(Student student) {
        try {
            if (!existInDb(student)) {
                getStudentsCollection().insert(student);
                return true;
            }
            return false;
        } catch (MongoException e) {
            Logger.logError(getClass(), e);
            return false;
        }
    }

    public boolean add(Subject subject) {
        try {
            if (!existInDb(subject)) {
                getSubjectsCollection().insert(subject);
                return true;
            }
            return false;
        } catch (MongoException e) {
            Logger.logError(getClass(), e);
            return false;
        }
    }

    // TODO: 30.05.2020 integration test
    protected boolean existInDb(Student student) {
        String query = "{name: '#', surname: '#'}";
        Student result = getStudentsCollection().findOne(query, student.getName(), student.getSurname()).as(Student.class);
        return result != null;
    }

    protected boolean existInDb(Subject subject) {
        String query = "{name: '#'}";
        Subject result = getSubjectsCollection().findOne(query, subject.getName()).as(Subject.class);
        return result != null;
    }

    public boolean delete(Student student) {
        if (existInDb(student)) {
            String query = "{name: '#', surname: '#'}";
            getStudentsCollection().remove(query, student.getName(), student.getSurname());
            return true;
        }
        return false;
    }

    public boolean delete(Subject subject) {
        if (existInDb(subject)) {
            String query = "{name: '#'}";
            getSubjectsCollection().remove(query, subject.getName());
            return true;
        }
        return false;
    }
}