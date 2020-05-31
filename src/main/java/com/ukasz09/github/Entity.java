package com.ukasz09.github;

public abstract class Entity {
    protected GradesManagerDb dbManager = new GradesManagerDb();

    //----------------------------------------------------------------------------------------------------------------//
    public abstract boolean add();

    public abstract boolean delete();
}
