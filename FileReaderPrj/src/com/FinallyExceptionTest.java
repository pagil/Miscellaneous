package com;

public class FinallyExceptionTest {

    public static void main(String[] args) {
        System.out.println(test());
        System.out.println(test2());
    }

    public static boolean test() {
        try {
            return true;
        } finally {
            return false;
        }

    }

    public static boolean test2() {
        try {
            boolean result = true;
            if (result) {
                throw new RuntimeException("Test!");
            }
            return result;
        } finally {
            return false;
        }
    }

}
