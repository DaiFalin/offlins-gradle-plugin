package com.java.test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Class1Test {
    @Test
    public void coveredShouldReturn1() {
        int res = new Class1().method( false);
        assertEquals(0, res);
    }
}
