package com.ukasz09.github;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
            String subjQuery = "{name: '#'}";
            getSubjectsCollection().remove(subjQuery, subject.getName());
            getStudentsCollection().remove("{grades: #}", subject.getName());
            return true;
        }
        return false;
    }

    public long countStudents() {
        return getStudentsCollection().count();
    }

    public long countSubjects() {
        return getSubjectsCollection().count();
    }

    public boolean addGrade(Student student, Subject subject, int grade) {
        if (existInDb(student) && existInDb(subject)) {
            String idQuery = "{name: '#', surname: '#'}";
            String pushQuery = "{$push: {grades: {#:#}}}";
            getStudentsCollection().update(idQuery, student.getName(), student.getSurname()).with(pushQuery, subject.getName(), grade);
            return true;
        }
        return false;
    }

    protected Iterable<Integer> getGrades(Student student, Subject subject) {
        if (existInDb(student) && existInDb(subject)) {
            String query = "{name: '#', surname: '#',grades:{#}}";
            return getStudentsCollection().find(query, student.getName(), student.getSurname(), subject.getName()).as(Integer.class);
        }
        return null;
    }

    public double avgGrade(Student student, Subject subject) {
        Iterable<Integer> grades = getGrades(student, subject);
        if (grades != null) {
            double sum = 0;
            int qty = 0;
            for (Integer grade : grades) {
                sum += grade;
                qty++;
            }

            return qty == 0 ? 0d : sum / qty;
        }
        return 0d;
    }
}