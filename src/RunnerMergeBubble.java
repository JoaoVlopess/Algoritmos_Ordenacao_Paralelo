import Paralelo.MergeSortParalelo;
import Sequencial.MergeSort;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class RunnerMergeBubble {

    public static void main(String[] args) {
        // --- CONFIGURAÇÕES GERAIS ---
        int numAmostras = 5;
        int[] quantidadesThreads = {2, 4, 8, 12, 24};
        List<Resultado> logGlobal = new ArrayList<>();

        System.out.println("=== INICIANDO BENCHMARK: MERGE SORT VS BUBBLE SORT ===");

        // ==========================================================
        // 1. BLOCO MERGE SORT (Tamanho Grande: 1.000.000)
        // ==========================================================
        int tamanhoMerge = 1_000_000;
        int[] originalMerge = gerarArray(tamanhoMerge);

        System.out.println("\n>>> EXECUTANDO MERGE SORT (" + tamanhoMerge + " elementos)");
        
        // Warm-up Merge
        System.out.println("Aquecendo JVM para Merge Sort...");
        for (int i = 0; i < 5; i++) {
            MergeSort.mergeSort(Arrays.copyOf(originalMerge, 100_000));
        }

        // Sequencial Merge
        double mediaSeqMerge = rodarSequencialMerge(originalMerge, numAmostras);
        logGlobal.add(new Resultado("MergeSort", 1, mediaSeqMerge, 1.0, 100.0));

        
        // Paralelo Merge
        for (int qtd : quantidadesThreads) {
            // 1. Chama o método e guarda o retorno na variável 'mediaObtida'
            double mediaObtida = rodarParaleloMerge(originalMerge, qtd, numAmostras, mediaSeqMerge);
            
            // 2. Calcula Speedup e Eficiência para salvar no objeto
            double speedup = mediaSeqMerge / mediaObtida;
            double eficiencia = (speedup / qtd) * 100;

            // 3. Adiciona na sua lista global (que criamos para o CSV)
            logGlobal.add(new Resultado("MergeSort", qtd, mediaObtida, speedup, eficiencia));
        }

        // ==========================================================
        // 2. BLOCO BUBBLE SORT (Tamanho Médio: 20.000)
        // ==========================================================
        int tamanhoBubble = 20_000;
        int[] originalBubble = gerarArray(tamanhoBubble);

        System.out.println("\n>>> EXECUTANDO BUBBLE SORT (" + tamanhoBubble + " elementos)");

        // Sequencial Bubble
        double mediaSeqBubble = rodarSequencialBubble(originalBubble, numAmostras);
        logGlobal.add(new Resultado("BubbleSort", 1, mediaSeqBubble, 1.0, 100.0));

        // Paralelo Bubble
        for (int qtd : quantidadesThreads) {
            double mediaObtida = rodarParaleloBubble(originalBubble, qtd, numAmostras, mediaSeqBubble);
            double speedup = mediaSeqBubble / mediaObtida;
            double eficiencia = (speedup / qtd) * 100;
            logGlobal.add(new Resultado("BubbleSort", qtd, mediaObtida, speedup, eficiencia));
        }



        salvarArquivoCSV(logGlobal);

        chamarPythonParaGraficos();

        System.out.println("\n=== BENCHMARK FINALIZADO COM SUCESSO ===");
    }

    // --- MÉTODOS AUXILIARES PARA ORGANIZAÇÃO ---

            private static void salvarArquivoCSV(List<Resultado> lista) {
        try (PrintWriter writer = new PrintWriter(new File("benchmarks.csv"))) {
            writer.println("Algoritmo;Threads;TempoMS;Speedup;Eficiencia");
            for (Resultado r : lista) {
                writer.println(r.toCSV());
            }
            System.out.println("\n>>> Arquivo 'benchmarks.csv' gerado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao salvar CSV: " + e.getMessage());
        }
    }
    
    private static double rodarSequencialMerge(int[] original, int amostras) {
        double soma = 0;
        System.out.println("Testando Merge Sequencial:");
        for (int i = 1; i <= amostras; i++) {
            int[] copia = Arrays.copyOf(original, original.length);
            long ini = System.nanoTime();
            MergeSort.mergeSort(copia);
            long fim = System.nanoTime();
            double tempo = (fim - ini) / 1_000_000.0;
            soma += tempo;
            System.out.printf("  Amostra %d: %.2f ms%n", i, tempo);
        }
        double media = soma / amostras;
        System.out.printf(">> MÉDIA SEQUENCIAL: %.2f ms%n%n", media);
        return media;
    }

    private static double rodarParaleloMerge(int[] original, int threads, int amostras, double mediaSeq) {
        double soma = 0;
        System.out.println("Testando Merge Paralelo (" + threads + " threads):");
        for (int i = 1; i <= amostras; i++) {
            int[] copia = Arrays.copyOf(original, original.length);
            ForkJoinPool pool = new ForkJoinPool(threads);
            long ini = System.nanoTime();
            pool.invoke(new MergeSortParalelo(copia));
            long fim = System.nanoTime();
            pool.shutdown();
            double tempo = (fim - ini) / 1_000_000.0;
            soma += tempo;
        }
        double media = soma / amostras;
            double speedup = mediaSeq / media;
            double eficiencia = (speedup / threads) * 100;

            System.out.printf(">> MÉDIA %d THREADS: %.2f ms (Speedup: %.2fx) | Eficiência: %.2f%%%n%n", 
                threads, media, speedup, eficiencia);
            
            // A MÁGICA ESTÁ AQUI: retornamos a média para quem chamou o método
            return media;
    }

    private static double rodarSequencialBubble(int[] original, int amostras) {
        double soma = 0;
        System.out.println("Testando Bubble Sequencial:");
        for (int i = 1; i <= amostras; i++) {
            int[] copia = Arrays.copyOf(original, original.length);
            long ini = System.nanoTime();
            new Sequencial.BubbleSort(copia).sort();
            long fim = System.nanoTime();
            double tempo = (fim - ini) / 1_000_000.0;
            soma += tempo;
            System.out.printf("  Amostra %d: %.2f ms%n", i, tempo);
        }
        double media = soma / amostras;
        System.out.printf(">> MÉDIA SEQUENCIAL: %.2f ms%n%n", media);
        return media;
    }

    private static double rodarParaleloBubble(int[] original, int threads, int amostras, double mediaSeq) {
        // Nota: O Bubble Paralelo usa o pool interno do Java via parallelStream
        // Mas definimos a propriedade para respeitar o número de threads do teste
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(threads));
        
        double soma = 0;
        System.out.println("Testando Bubble Paralelo (" + threads + " threads):");
        for (int i = 1; i <= amostras; i++) {
            int[] copia = Arrays.copyOf(original, original.length);
            long ini = System.nanoTime();
            new Paralelo.BubbleSortParalelo(copia).sort();
            long fim = System.nanoTime();
            double tempo = (fim - ini) / 1_000_000.0;
            soma += tempo;
        }
        double media = soma / amostras;
        double speedup = mediaSeq / media;
        double eficiencia = (speedup / threads) * 100;

        System.out.printf(">> MÉDIA %d THREADS: %.2f ms (Speedup: %.2fx) | Eficiência: %.2f%%%n%n", 
                threads, media, speedup, eficiencia);
            
        // A MÁGICA ESTÁ AQUI: retornamos a média para quem chamou o método
        return media;
        }

    public static int[] gerarArray(int n) {
        Random r = new Random();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt(1000000);
        return a;
    }

    private static void chamarPythonParaGraficos() {
    System.out.println("\nTentando gerar gráficos via Python...");
    try {
        // Comando para rodar o script (pode ser python ou python3 dependendo do SO)
        ProcessBuilder pb = new ProcessBuilder("python3", "gerar_graficos.py");
        // Isso faz com que as mensagens do Python apareçam no console do Java
        pb.inheritIO(); 
        Process p = pb.start();
        p.waitFor(); // Espera o Python terminar para seguir
    } catch (Exception e) {
        System.out.println("Erro ao chamar Python. Certifique-se de que o python3 e pandas/matplotlib estão instalados.");
    }
}
}