# 📊 Benchmark de Algoritmos de Ordenação: Sequencial vs. Paralelo

Este repositório contém um framework de testes desenvolvido em Java para analisar e comparar o desempenho de algoritmos de ordenação clássicos executados de forma **sequencial** e **paralela**. O objetivo principal é demonstrar na prática os conceitos de computação concorrente, speedup, eficiência e a Lei de Amdahl em ambientes multicore.

## 🚀 Funcionalidades e Algoritmos Analisados

O projeto implementa versões de thread única (serial) e múltiplas threads (utilizando o framework *Fork/Join* e *Parallel Streams* do Java) para os seguintes métodos:

*   **Merge Sort:** Abordagem de divisão e conquista (O(n log n)).
*   **Bubble Sort:** Abordagem quadrática clássica com paralelização baseada em *Odd-Even Transposition* (O(n^2)).
*   **Insertion Sort:** Implementação híbrida particionada via Fork/Join.
*   **Selection Sort:** Implementação híbrida particionada via Fork/Join.

Além do código Java, o projeto conta com um script em **Python** automatizado para ler os resultados extraídos e gerar gráficos comparativos de Tempo de Execução, Aceleração (Speedup) e Eficiência por Core.

## 📁 Estrutura do Projeto

*   src/Sequencial/: Contém as classes com a lógica de ordenação em thread única.
*   src/Paralelo/: Contém as classes com a lógica de ordenação distribuída (concorrente).
*   src/MainBenchmark.java: Classe orquestradora que executa toda a suíte de testes em lote.
*   src/gerar_graficos.py: Script Python responsável por ler os dados de saída e plotar os gráficos.

## ⚙️ Pré-requisitos

Para rodar este projeto na sua máquina, você precisará ter instalado:
1.  **Java Development Kit (JDK):** Versão 8 ou superior.
2.  **Python 3:** Para a geração dos gráficos.
3.  **Bibliotecas Python:** Instale as dependências executando o comando abaixo no seu terminal:
    pip install matplotlib pandas

## 🛠️ Como Executar o Projeto

1. Clone este repositório para a sua máquina:
   git clone https://github.com/JoaoVlopess/Algoritmos_Ordenacao_Paralelo.git

2. Navegue até o diretório do código-fonte:
   cd Algoritmos_Ordenacao_Paralelo/src

3. Compile todos os arquivos Java simultaneamente:
   javac Resultado.java Sequencial/*.java Paralelo/*.java RunnerMergeBubble.java RunnerInsertionSelection.java MainBenchmark.java

4. Execute o orquestrador principal de testes:
   java MainBenchmark

## 📈 Resultados Esperados

Ao finalizar a execução, o programa fará duas coisas automaticamente:
1.  Gerar um arquivo benchmarks.csv contendo todas as métricas de tempo, speedup e eficiência.
2.  Acionar o script Python para criar 4 imagens PNG (uma para cada algoritmo) ilustrando o comportamento do paralelismo conforme o número de threads aumenta.

## 👥 Autores
*   **João Pedro Amorim**
*   **João Victor Lopes**

Desenvolvido como parte dos estudos de Ciência da Computação - Centro de Ciências Tecnológicas (Universidade de Fortaleza).
