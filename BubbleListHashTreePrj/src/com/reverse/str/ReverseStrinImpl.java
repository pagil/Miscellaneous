package com.reverse.str;

/**
 * Test task.
 * 
 * @author Victor
 * 
 */
public class ReverseStrinImpl {

    public static String reverseString(String str) {
        String result = null;
        if (str != null) {
            char[] array = str.toCharArray();
            int i = 0;
            int j = array.length - 1;
            while (i < j) {
                char tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
                i++;
                j--;
            }
            result = new String(array);
        }
        System.out.println(result);
        return result;
    }

    public static String reverseStringNoAdditionalVar(String str) {
        String result = null;
        if (str != null) {
            char[] array = str.toCharArray();
            int i = 0;
            int j = array.length - 1;
            while (i < j) {
                array[i] = (char) (array[i] ^ array[j]); // i -> i + j
                array[j] = (char) (array[i] ^ array[j]); // j -> i
                array[i] = (char) (array[i] ^ array[j]); // i -> j
                i++;
                j--;
            }
            result = new String(array);
        }
        System.out.println(result);
        return result;
    }
}
