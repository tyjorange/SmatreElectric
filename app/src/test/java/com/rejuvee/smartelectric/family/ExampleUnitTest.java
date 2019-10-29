package com.rejuvee.smartelectric.family;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String s = Integer.toBinaryString(0);
        System.out.println(s);
        String s1 = Integer.valueOf("11", 2).toString();
        System.out.println(s1);
        assertEquals(4, 2 + 2);
    }
}