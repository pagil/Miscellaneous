package com.reverse.str;

import static org.junit.Assert.*;
import org.junit.Test;

public class ReverseStringTest {

	@Test
	public void testReverseStringPositive() {
		assertTrue("cba".equals(ReverseStrinImpl.reverseString("abc")));;
		assertTrue("12345".equals(ReverseStrinImpl.reverseString("54321")));;
		assertTrue("tset".equals(ReverseStrinImpl.reverseString("test")));;
	}

	@Test
	public void testReverseStringNegative() {
		assertFalse("test".equals(ReverseStrinImpl.reverseString("test")));;
	}

}
