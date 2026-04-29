import pandas as pd
import matplotlib.pyplot as plt
import os

# Verifica se o arquivo existe para não dar erro
if not os.path.exists('benchmarks.csv'):
    print("Arquivo benchmarks.csv não encontrado!")
    exit()

# Lendo o CSV (ajustado para o padrão que seu Java gera)
df = pd.read_csv('benchmarks.csv', sep=';', decimal=',')

algoritmos = df['Algoritmo'].unique()

for algo in algoritmos:
    data = df[df['Algoritmo'] == algo].sort_values('Threads')
    
    # Criar uma figura com 3 subgráficos (lado a lado)
    fig, (ax1, ax2, ax3) = plt.subplots(1, 3, figsize=(18, 5))
    fig.suptitle(f'Análise de Desempenho Paralelo: {algo}', fontsize=16)

    # Gráfico 1: Tempo
    ax1.plot(data['Threads'], data['TempoMS'], marker='o', color='blue')
    ax1.set_title('Tempo de Execução')
    ax1.set_xlabel('Threads')
    ax1.set_ylabel('ms')
    ax1.grid(True)

    # Gráfico 2: Speedup
    ax2.plot(data['Threads'], data['Speedup'], marker='s', color='green', label='Real')
    ax2.plot(data['Threads'], data['Threads'], '--', color='red', label='Ideal') # Linha ideal
    ax2.set_title('Speedup')
    ax2.set_xlabel('Threads')
    ax2.set_ylabel('Aceleração (x)')
    ax2.legend()
    ax2.grid(True)

    # Gráfico 3: Eficiência
    ax3.bar(data['Threads'].astype(str), data['Eficiencia'], color='orange')
    ax3.set_title('Eficiência por Core')
    ax3.set_xlabel('Threads')
    ax3.set_ylabel('%')
    ax3.set_ylim(0, 110)

    plt.tight_layout(rect=[0, 0.03, 1, 0.95])
    plt.savefig(f'Graficos_{algo}.png')
    print(f"Gráfico de {algo} gerado.")