package com.github.ukasz09.appInterface;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppInteg {
    @Test
    public void whenInstantiatedThenConnectionEstablished() {
        assertNotNull(new App().getDbManager());
    }
}
