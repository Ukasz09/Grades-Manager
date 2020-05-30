package com.ukasz09.github;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Subject {
    public static final Set<Integer> AVAILABLE_GRADES = new HashSet<>(Arrays.asList(2, 3, 4, 5));
    private String name;

    //----------------------------------------------------------------------------------------------------------------//
    public Subject(String name) {
        this.name = name;
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
}
