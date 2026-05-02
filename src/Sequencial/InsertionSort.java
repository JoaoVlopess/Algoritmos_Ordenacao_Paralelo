package Sequencial;

import java.util.Arrays;

public class InsertionSort {
    private static final int THRESHOLD = 1000;

    public static void sort(int[] array) {
        if (array.length < THRESHOLD) {
            sortSequencialPuro(array);
            return;
        }

        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);

        sort(left);  // Chamada recursiva sequencial
        sort(right); // Chamada recursiva sequencial

        merge(array, left, right);
    }

    private static void sortSequencialPuro(int[] array) {
        int n = array.length;
        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;
        }
    }

    private static void merge(int[] array, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) array[k++] = left[i++];
            else array[k++] = right[j++];
        }
        while (i < left.length) array[k++] = left[i++];
        while (j < right.length) array[k++] = right[j++];
    }
}