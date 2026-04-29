package Paralelo;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class BubbleSortParalelo {
    private int[] array;

    public BubbleSortParalelo(int[] array) {
        this.array = array;
    }

    public void sort() {
        int n = array.length;
        boolean swapped = true;

        while (swapped) {
            swapped = false;

            // Fase Ímpar (Comparações 1-2, 3-4, 5-6...)
            for (int i = 1; i < n - 1; i += 2) {
                if (array[i] > array[i + 1]) {
                    int aux = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = aux;
                    swapped = true;
                }
            }

            // Fase Par (Comparações 0-1, 2-3, 4-5...)
            for (int i = 0; i < n - 1; i += 2) {
                if (array[i] > array[i + 1]) {
                    int aux = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = aux;
                    swapped = true;
                }
            }
            
        }
    }
}