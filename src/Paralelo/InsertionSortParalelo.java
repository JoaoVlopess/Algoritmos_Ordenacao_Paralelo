package Paralelo;

import Sequencial.InsertionSort;
import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class InsertionSortParalelo extends RecursiveAction {
    private int[] array;
    private final int THRESHOLD = 1000; 

    public InsertionSortParalelo(int[] array) {
        this.array = array;
    }

    @Override
    protected void compute() {
        // SE o array for pequeno, resolve de forma sequencial usando seu InsertionSort
        if (array.length < THRESHOLD) {
            InsertionSort.sort(array);
            return;
        }

        // CASO CONTRÁRIO: Divide para conquistar em paralelo
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);

        // Criamos duas novas tarefas de InsertionSort
        InsertionSortParalelo leftTask = new InsertionSortParalelo(left);
        InsertionSortParalelo rightTask = new InsertionSortParalelo(right);

        // Dispara as tarefas em paralelo
        invokeAll(leftTask, rightTask);

        // Faz o merge dos resultados
        merge(array, left, right);
    }

    // Função auxiliar para mesclar os arrays já ordenados
    private void merge(int[] array, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }
        while (i < left.length) {
            array[k++] = left[i++];
        }
        while (j < right.length) {
            array[k++] = right[j++];
        }
    }
}