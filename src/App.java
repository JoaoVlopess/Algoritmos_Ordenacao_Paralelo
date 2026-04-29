import Paralelo.MergeSortParalelo;
import Sequencial.MergeSort;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class App {
    public static void main(String[] args) {
        // int tamanho = 1_000_000;
        // int[] original = gerarArray(tamanho);
        // int numAmostras = 5;

        // System.out.println("=== INICIANDO BENCHMARK DOS ALGORITMOS ===");
        // System.out.println("Tamanho do array: " + tamanho);
        // System.out.println("------------------------------------------");

        // //FASE DE AQUECIMENTO (WARM-UP)
        // System.out.println("Fase de Aquecimento (Warm-up)...");
        // for (int i = 0; i < 10; i++) {
        //     int[] temp = gerarArray(100_000);
        //     MergeSort.mergeSort(Arrays.copyOf(temp, temp.length));
        //     ForkJoinPool poolWarm = new ForkJoinPool();
        //     poolWarm.invoke(new MergeSortParalelo(Arrays.copyOf(temp, temp.length)));
        //     poolWarm.shutdown();
        // }
        // System.out.println("Aquecimento concluído!\n");

        // //TESTE SEQUENCIAL (COM 5 AMOSTRAS)
        // System.out.println("Testando modo Sequencial:");
        // double somaSeq = 0;
        // for (int i = 1; i <= numAmostras; i++) {
        //     int[] copiaSeq = Arrays.copyOf(original, original.length);
            
        //     long inicio = System.nanoTime();
        //     MergeSort.mergeSort(copiaSeq);
        //     long fim = System.nanoTime();
            
        //     double tempo = (fim - inicio) / 1_000_000.0;
        //     somaSeq += tempo;
        //     System.out.printf("  Amostra %d: %.2f ms%n", i, tempo);
        // }
        // double mediaSeq = somaSeq / numAmostras;
        // System.out.printf(">> MÉDIA SEQUENCIAL: %.2f ms%n%n", mediaSeq);

        // // TESTE PARALELO (VARIANDO THREADS COM 5 AMOSTRAS CADA)
        // int[] quantidadesThreads = {2, 4, 6, 8, 10, 12};

        // for (int qtd : quantidadesThreads) {
        //     double somaPar = 0;
        //     System.out.println("Testando com " + qtd + " threads:");
            
        //     for (int i = 1; i <= numAmostras; i++) {
        //         int[] copiaPar = Arrays.copyOf(original, original.length);
        //         ForkJoinPool pool = new ForkJoinPool(qtd);
        //         MergeSortParalelo tarefa = new MergeSortParalelo(copiaPar);
                
        //         long inicio = System.nanoTime();
        //         pool.invoke(tarefa);
        //         long fim = System.nanoTime();
        //         pool.shutdown();
                
        //         double tempo = (fim - inicio) / 1_000_000.0;
        //         somaPar += tempo;
        //         System.out.printf("  Amostra %d: %.2f ms%n", i, tempo);
        //     }
            
        //     double mediaPar = somaPar / numAmostras;
        //     double speedup = mediaSeq / mediaPar;
        //     System.out.printf(">> MÉDIA PARA %d THREADS: %.2f ms (Speedup: %.2fx)%n%n", qtd, mediaPar, speedup);
        // }

        // System.out.println("------------------------------------------");
        // System.out.println("Benchmark finalizado com sucesso!");




        int tamanhoBubble = 20_000; 
        int[] arrayBubble = gerarArray(tamanhoBubble);
        int[] copiaBubbleSeq = Arrays.copyOf(arrayBubble, tamanhoBubble);
        int[] copiaBubblePar = Arrays.copyOf(arrayBubble, tamanhoBubble);

        // Teste Sequencial Bubble
        long ini = System.nanoTime();
        new Sequencial.BubbleSort(copiaBubbleSeq).sort();
        long fim = System.nanoTime();
        System.out.printf("Bubble Sequencial: %.2f ms%n", (fim - ini) / 1_000_000.0);
    }

    public static int[] gerarArray(int n) {
        Random r = new Random();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt(1000000);
        return a;
    }
}