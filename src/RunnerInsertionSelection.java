import Paralelo.InsertionSortParalelo;
import Paralelo.SelectionSortParalelo;
import Sequencial.InsertionSort;
import Sequencial.SelectionSort;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class RunnerInsertionSelection {

    public static void main(String[] args) {
        int numAmostras = 5;
        int[] quantidadesThreads = {2, 4, 8, 12, 24};
        List<Resultado> logGlobal = new ArrayList<>();

        // Tamanho reduzido devido à complexidade O(n^2) desses algoritmos
        int tamanho = 30_000; 
        int[] original = gerarArray(tamanho);

        System.out.println("=== BENCHMARK: INSERTION VS SELECTION (FORK/JOIN) ===");

        // --- BLOCO INSERTION SORT ---
        System.out.println("\n>>> INSERTION SORT (" + tamanho + " itens)");
        double mediaSeqIns = rodarSequencialInsertion(original, numAmostras);
        logGlobal.add(new Resultado("InsertionSort", 1, mediaSeqIns, 1.0, 100.0));

        for (int qtd : quantidadesThreads) {
            double mediaPar = rodarParaleloInsertion(original, qtd, numAmostras, mediaSeqIns);
            logGlobal.add(new Resultado("InsertionSort", qtd, mediaPar, mediaSeqIns/mediaPar, (mediaSeqIns/mediaPar/qtd)*100));
        }

        // --- BLOCO SELECTION SORT ---
        System.out.println("\n>>> SELECTION SORT (" + tamanho + " itens)");
        double mediaSeqSel = rodarSequencialSelection(original, numAmostras);
        logGlobal.add(new Resultado("SelectionSort", 1, mediaSeqSel, 1.0, 100.0));

        for (int qtd : quantidadesThreads) {
            double mediaPar = rodarParaleloSelection(original, qtd, numAmostras, mediaSeqSel);
            logGlobal.add(new Resultado("SelectionSort", qtd, mediaPar, mediaSeqSel/mediaPar, (mediaSeqSel/mediaPar/qtd)*100));
        }

        salvarArquivoCSV(logGlobal);
        chamarPythonParaGraficos();
    }

    private static double rodarParaleloInsertion(int[] original, int threads, int amostras, double mediaSeq) {
        double soma = 0;
        System.out.println("Testando Insertion Paralelo (" + threads + " threads):");
        for (int i = 1; i <= amostras; i++) {
            int[] copia = Arrays.copyOf(original, original.length);
            ForkJoinPool pool = new ForkJoinPool(threads); // Define o número de threads
            long ini = System.nanoTime();
            pool.invoke(new InsertionSortParalelo(copia)); // Usa o RecursiveAction do seu amigo
            long fim = System.nanoTime();
            pool.shutdown();
            soma += (fim - ini) / 1_000_000.0;
        }
        double media = soma / amostras;
        System.out.printf(">> MEDIA %d THREADS: %.2f ms%n", threads, media);
        return media;
    }

    private static double rodarParaleloSelection(int[] original, int threads, int amostras, double mediaSeq) {
        double soma = 0;
        System.out.println("Testando Selection Paralelo (" + threads + " threads):");
        for (int i = 1; i <= amostras; i++) {
            int[] copia = Arrays.copyOf(original, original.length);
            ForkJoinPool pool = new ForkJoinPool(threads);
            long ini = System.nanoTime();
            pool.invoke(new SelectionSortParalelo(copia)); // Usa o RecursiveAction do seu amigo
            long fim = System.nanoTime();
            pool.shutdown();
            soma += (fim - ini) / 1_000_000.0;
        }
        double media = soma / amostras;
        System.out.printf(">> MEDIA %d THREADS: %.2f ms%n", threads, media);
        return media;
    }

    // Métodos Sequenciais (Idênticos aos anteriores, mas chamando os métodos do seu amigo)
    private static double rodarSequencialInsertion(int[] original, int amostras) {
        double soma = 0;
        for (int i = 0; i < amostras; i++) {
            int[] copia = Arrays.copyOf(original, original.length);
            long ini = System.nanoTime();
            InsertionSort.sort(copia);
            long fim = System.nanoTime();
            soma += (fim - ini) / 1_000_000.0;
        }
        double media = soma / amostras;
        System.out.printf(">> MEDIA SEQUENCIAL: %.2f ms%n", media);
        return media;
    }

    private static double rodarSequencialSelection(int[] original, int amostras) {
        double soma = 0;
        for (int i = 0; i < amostras; i++) {
            int[] copia = Arrays.copyOf(original, original.length);
            long ini = System.nanoTime();
            SelectionSort.sort(copia);
            long fim = System.nanoTime();
            soma += (fim - ini) / 1_000_000.0;
        }
        double media = soma / amostras;
        System.out.printf(">> MEDIA SEQUENCIAL: %.2f ms%n", media);
        return media;
    }

    private static void salvarArquivoCSV(List<Resultado> lista) {
    File arquivo = new File("benchmarks.csv");
    boolean existe = arquivo.exists();

    try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo, true))) {
        if (!existe) {
            writer.println("Algoritmo;Threads;TempoMS;Speedup;Eficiencia");
        }
        for (Resultado r : lista) {
            writer.println(r.toCSV());
        }
    } catch (Exception e) {
        System.err.println("Erro ao salvar CSV: " + e.getMessage());
    }
}

    private static void chamarPythonParaGraficos() {
        try { new ProcessBuilder("python", "gerar_graficos.py").inheritIO().start().waitFor();
        } catch (Exception e) { System.out.println("Erro Python."); }
    }

    public static int[] gerarArray(int n) {
        Random r = new Random();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt(1000000);
        return a;
    }
}