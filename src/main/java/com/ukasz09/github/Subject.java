package com.ukasz09.github;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class Subject {
    public static final Set<Integer> AVAILABLE_GRADES = new HashSet<>(Arrays.asList(2, 3, 4, 5));
    private static final Pattern PATTERN = Pattern.compile("[A-Z][a-zA-Z]+");

    private String name;
    private GradesManagerDb managerDb;

    //----------------------------------------------------------------------------------------------------------------//
    public Subject(String name, GradesManagerDb dbManager) {
        this.name = name;
        this.managerDb = dbManager;
    }

    //----------------------------------------------------------------------------------------------------------------//
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return name.equals(subject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public boolean add() {
        if (PATTERN.matcher(name).matches())
            return managerDb.add(this);
        return false;
    }
}
