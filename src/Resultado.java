public class Resultado {
    String algoritmo;
    int threads;
    double tempo;
    double speedup;
    double eficiencia;

    public Resultado(String algoritmo, int threads, double tempo, double speedup, double eficiencia) {
        this.algoritmo = algoritmo;
        this.threads = threads;
        this.tempo = tempo;
        this.speedup = speedup;
        this.eficiencia = eficiencia;
    }

    public String toCSV() {
        return String.format("%s;%d;%.2f;%.2f;%.2f", 
            algoritmo, threads, tempo, speedup, eficiencia);
    }
}