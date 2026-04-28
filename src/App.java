import Paralelo.InsertionSortParalelo;
import Paralelo.MergeSortParalelo;
import Paralelo.SelectionSortParalelo;
import Sequencial.InsertionSort;
import Sequencial.MergeSort;
import Sequencial.SelectionSort;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class App {
    public static void main(String[] args) {
        int tamanho = 100_000;
        int[] original = gerarArray(tamanho);
        int numAmostras = 5;

        System.out.println("=== INICIANDO BENCHMARK DOS ALGORITMOS ===");
        System.out.println("Tamanho do array: " + tamanho);
        System.out.println("------------------------------------------");

        //FASE DE AQUECIMENTO (WARM-UP)
        System.out.println("Fase de Aquecimento (Warm-up)...");
        for (int i = 0; i < 10; i++) {
            int[] temp = gerarArray(100_000);
            MergeSort.mergeSort(Arrays.copyOf(temp, temp.length));
            ForkJoinPool poolWarm = new ForkJoinPool();
            poolWarm.invoke(new MergeSortParalelo(Arrays.copyOf(temp, temp.length)));
            poolWarm.shutdown();
        }
        System.out.println("Aquecimento concluído!\n");

        //TESTE SEQUENCIAL (COM 5 AMOSTRAS)
        System.out.println("Testando modo Sequencial (Merge Sort):");
        double somaSeq = 0;
        for (int i = 1; i <= numAmostras; i++) {
            int[] copiaSeq = Arrays.copyOf(original, original.length);
            
            long inicio = System.nanoTime();
            MergeSort.mergeSort(copiaSeq);
            long fim = System.nanoTime();
            
            double tempo = (fim - inicio) / 1_000_000.0;
            somaSeq += tempo;
            System.out.printf("  Amostra %d: %.2f ms%n", i, tempo);
        }
        double mediaSeq = somaSeq / numAmostras;
        System.out.printf(">> MÉDIA SEQUENCIAL: %.2f ms%n%n", mediaSeq);

        // TESTE PARALELO (VARIANDO THREADS COM 5 AMOSTRAS CADA)
        int[] quantidadesThreads = {2, 4, 6, 8, 10, 12};

        for (int qtd : quantidadesThreads) {
            double somaPar = 0;
            System.out.println("Testando com " + qtd + " threads (Merge Sort):");
            
            for (int i = 1; i <= numAmostras; i++) {
                int[] copiaPar = Arrays.copyOf(original, original.length);
                ForkJoinPool pool = new ForkJoinPool(qtd);
                MergeSortParalelo tarefa = new MergeSortParalelo(copiaPar);
                
                long inicio = System.nanoTime();
                pool.invoke(tarefa);
                long fim = System.nanoTime();
                pool.shutdown();
                
                double tempo = (fim - inicio) / 1_000_000.0;
                somaPar += tempo;
                System.out.printf("  Amostra %d: %.2f ms%n", i, tempo);
            }
            
            double mediaPar = somaPar / numAmostras;
            double speedup = mediaSeq / mediaPar;
            System.out.printf(">> MÉDIA PARA %d THREADS: %.2f ms (Speedup: %.2fx)%n%n", qtd, mediaPar, speedup);
        }

        // ==========================================
        // ADIÇÃO DOS TESTES: INSERTION SORT
        // ==========================================
        System.out.println("------------------------------------------");
        System.out.println("Testando modo Sequencial (Insertion Sort):");
        double somaSeqIns = 0;
        for (int i = 1; i <= numAmostras; i++) {
            int[] copiaSeq = Arrays.copyOf(original, original.length);
            
            long inicio = System.nanoTime();
            InsertionSort.sort(copiaSeq);
            long fim = System.nanoTime();
            
            double tempo = (fim - inicio) / 1_000_000.0;
            somaSeqIns += tempo;
            System.out.printf("  Amostra %d: %.2f ms%n", i, tempo);
        }
        double mediaSeqIns = somaSeqIns / numAmostras;
        System.out.printf(">> MÉDIA SEQUENCIAL INSERTION SORT: %.2f ms%n%n", mediaSeqIns);

        for (int qtd : quantidadesThreads) {
            double somaParIns = 0;
            System.out.println("Testando Insertion Sort Paralelo com " + qtd + " threads:");
            
            for (int i = 1; i <= numAmostras; i++) {
                int[] copiaPar = Arrays.copyOf(original, original.length);
                ForkJoinPool pool = new ForkJoinPool(qtd);
                InsertionSortParalelo tarefa = new InsertionSortParalelo(copiaPar);
                
                long inicio = System.nanoTime();
                pool.invoke(tarefa);
                long fim = System.nanoTime();
                pool.shutdown();
                
                double tempo = (fim - inicio) / 1_000_000.0;
                somaParIns += tempo;
                System.out.printf("  Amostra %d: %.2f ms%n", i, tempo);
            }
            
            double mediaParIns = somaParIns / numAmostras;
            double speedupIns = mediaSeqIns / mediaParIns;
            System.out.printf(">> MÉDIA PARA %d THREADS (Insertion Sort): %.2f ms (Speedup: %.2fx)%n%n", qtd, mediaParIns, speedupIns);
        }

        // ==========================================
        // ADIÇÃO DOS TESTES: SELECTION SORT
        // ==========================================
        System.out.println("------------------------------------------");
        System.out.println("Testando modo Sequencial (Selection Sort):");
        double somaSeqSel = 0;
        for (int i = 1; i <= numAmostras; i++) {
            int[] copiaSeq = Arrays.copyOf(original, original.length);
            
            long inicio = System.nanoTime();
            SelectionSort.sort(copiaSeq);
            long fim = System.nanoTime();
            
            double tempo = (fim - inicio) / 1_000_000.0;
            somaSeqSel += tempo;
            System.out.printf("  Amostra %d: %.2f ms%n", i, tempo);
        }
        double mediaSeqSel = somaSeqSel / numAmostras;
        System.out.printf(">> MÉDIA SEQUENCIAL SELECTION SORT: %.2f ms%n%n", mediaSeqSel);

        for (int qtd : quantidadesThreads) {
            double somaParSel = 0;
            System.out.println("Testando Selection Sort Paralelo com " + qtd + " threads:");
            
            for (int i = 1; i <= numAmostras; i++) {
                int[] copiaPar = Arrays.copyOf(original, original.length);
                ForkJoinPool pool = new ForkJoinPool(qtd);
                SelectionSortParalelo tarefa = new SelectionSortParalelo(copiaPar);
                
                long inicio = System.nanoTime();
                pool.invoke(tarefa);
                long fim = System.nanoTime();
                pool.shutdown();
                
                double tempo = (fim - inicio) / 1_000_000.0;
                somaParSel += tempo;
                System.out.printf("  Amostra %d: %.2f ms%n", i, tempo);
            }
            
            double mediaParSel = somaParSel / numAmostras;
            double speedupSel = mediaSeqSel / mediaParSel;
            System.out.printf(">> MÉDIA PARA %d THREADS (Selection Sort): %.2f ms (Speedup: %.2fx)%n%n", qtd, mediaParSel, speedupSel);
        }

        System.out.println("------------------------------------------");
        System.out.println("Benchmark finalizado com sucesso!");
    }

    public static int[] gerarArray(int n) {
        Random r = new Random();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt(1000000);
        return a;
    }
}