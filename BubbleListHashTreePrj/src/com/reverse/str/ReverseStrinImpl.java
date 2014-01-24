package com.reverse.str;

public class ReverseStrinImpl {

	public static String reverseString(String str) {
		String result = null;
		if (str != null) {
			char[] array = str.toCharArray();
			int i = 0;
			int j = array.length-1;
			while(i < j) {
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
}
