package com.github.ukasz09.app;

public abstract class Entity {
    protected GradesManagerDb dbManager = new GradesManagerDb();

    //----------------------------------------------------------------------------------------------------------------//
    public abstract boolean add();

    public abstract boolean delete();
}
