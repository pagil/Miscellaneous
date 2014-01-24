package com.sort.bubble;
public class BubbleSortImpl {

  public static void sort(int[] array) {
    boolean swapDetected = false;
    int iteration = 0;
    do {
      swapDetected = false;
      iteration++;
      for(int i = 0; i < (array.length - iteration); i++) {
        if (array[i] > array[i+1]) {
          swapDetected = true;
          array[i] = array[i]^array[i+1];
          array[i+1] = array[i]^array[i+1];
          array[i] = array[i]^array[i+1];
        }
      }
    } while (swapDetected);
  }
}
