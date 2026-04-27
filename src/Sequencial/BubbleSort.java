package Sequencial;

public class BubbleSort {
    public int[] array;

    public BubbleSort(int[] array) {
        this.array = array;
    }

    public void sort() {
        int n = array.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
                swapped = false;
            for (int j = 0; j < n - i - 1; j++){
                if (array[j] > array[j + 1]) {
                    int aux = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = aux;
                    swapped = true;
                }
            }
            if(!swapped) {
                break;
            }
    }
}
}
