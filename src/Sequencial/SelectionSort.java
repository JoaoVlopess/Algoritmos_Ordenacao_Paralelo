package Sequencial;

public class SelectionSort {
    
    public static void sort(int[] array) {
        int n = array.length;
        
        // Avança o limite do subarray não ordenado
        for (int i = 0; i < n - 1; i++) {
            // Encontra o menor elemento no subarray não ordenado
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[min_idx]) {
                    min_idx = j;
                }
            }

            // Troca o menor elemento encontrado com o primeiro elemento
            int temp = array[min_idx];
            array[min_idx] = array[i];
            array[i] = temp;
        }
    }
}