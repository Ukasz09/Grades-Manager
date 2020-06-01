package com.github.ukasz09.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppInteg {
    @Test
    public void whenInstantiatedThenConnectionEstablished() {
        assertNotNull(new App().getDbManager());
    }
}
