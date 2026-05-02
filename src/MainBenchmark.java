import java.io.File;

public class MainBenchmark {
    public static void main(String[] args) {
        // LIMPAR o arquivo antigo antes de começar
        File csv = new File("benchmarks.csv");
        if (csv.exists()) {
            csv.delete();
            System.out.println("Arquivo benchmarks.csv resetado para novo teste.");
        }

        System.out.println(">>> [PARTE 1] MERGE SORT E BUBBLE SORT");
        RunnerMergeBubble.main(args);

        System.out.println("\n>>> [PARTE 2] INSERTION SORT E SELECTION SORT");
        RunnerInsertionSelection.main(args);
        
        System.out.println("\nBENCHMARK COMPLETO FINALIZADO!");
    }
}