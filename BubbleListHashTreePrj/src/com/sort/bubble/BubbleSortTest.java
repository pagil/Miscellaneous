package com.sort.bubble;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class BubbleSortTest {

	@Test
	public void testPositive() {
		int[] sorted = {5, 10, 300, 500, 1000};
		int[] unsorted = {10, 300, 1000, 500, 5};
		BubbleSortImpl.sort(unsorted);
		assertTrue(Arrays.equals(sorted, unsorted));
	}

	@Test
	public void testNegative() {
		int[] toBeSorted =  {10, 500, 300, 1000,  5};
		int[] unsorted =    {10, 500, 300, 1000, 5};
		BubbleSortImpl.sort(unsorted);
		assertFalse(Arrays.equals(toBeSorted, unsorted));
	}

}
