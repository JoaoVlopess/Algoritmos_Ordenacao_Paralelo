package Paralelo;

import Sequencial.SelectionSort;
import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class SelectionSortParalelo extends RecursiveAction {
    private int[] array;
    private final int THRESHOLD = 1000; 

    public SelectionSortParalelo(int[] array) {
        this.array = array;
    }

    @Override
    protected void compute() {
        // SE o array for pequeno, resolve de forma sequencial (mais rápido)
        if (array.length < THRESHOLD) {
            SelectionSort.sort(array);
            return;
        }

        // CASO CONTRÁRIO: Divide para conquistar em paralelo
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);

        // Criamos duas novas tarefas
        SelectionSortParalelo leftTask = new SelectionSortParalelo(left);
        SelectionSortParalelo rightTask = new SelectionSortParalelo(right);

        // O invokeAll dispara as tarefas em paralelo e espera elas terminarem
        invokeAll(leftTask, rightTask);

        // Faz o merge dos resultados
        merge(array, left, right);
    }

    // Método auxiliar de mesclagem (idêntico ao do MergeSort)
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