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
        String s1 = Integer.valueOf("12", 2).toString();
        System.out.println(s1);
        assertEquals(4, 2 + 2);
    }

    @Test
    public void add_isCorrect() {
        double x = Double.parseDouble("1.1");
        double y = Double.parseDouble("2.2");
        for (Operation op : Operation.values())
            System.out.println(x + " " + op + " " + y +
                    " = " + op.eval(x, y));
    }

    enum Operation {
        PLUS {
            double eval(double x, double y) {
                return x + y;
            }
        },
        MINUS {
            double eval(double x, double y) {
                return x - y;
            }
        },
        TIMES {
            double eval(double x, double y) {
                return x * y;
            }
        },
        DIVIDED_BY {
            double eval(double x, double y) {
                return x / y;
            }
        };

        // Each constant supports an arithmetic operation
        abstract double eval(double x, double y);

//        public static void main(String args[]) {
//            double x = Double.parseDouble(args[0]);
//            double y = Double.parseDouble(args[1]);
//            for (Operation op : Operation.values())
//                System.out.println(x + " " + op + " " + y +
//                        " = " + op.eval(x, y));
//        }
    }
}