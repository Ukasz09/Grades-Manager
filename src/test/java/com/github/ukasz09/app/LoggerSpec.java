package com.github.ukasz09.app;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerSpec {
    @Test
    public void whenLogMsgThenMsgInOutput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        Logger.logMsg("msg", new PrintStream(outContent));
        assertEquals("msg\n", outContent.toString());
    }

    @Test
    public void whenLogErrorThenMsgInErr() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        Logger.logError(LoggerSpec.class, new Exception("err_msg"), new PrintStream(errContent));
        assertFalse(errContent.toString().isEmpty());
    }
}
