import pandas as pd
import matplotlib.pyplot as plt
import os

# --- Criar diretório de resultados ---
NOME_PASTA = 'resultados'
if not os.path.exists(NOME_PASTA):
    os.makedirs(NOME_PASTA)
    print(f"Pasta '{NOME_PASTA}' criada.")

# Verifica se o arquivo CSV existe
if not os.path.exists('benchmarks.csv'):
    print("Arquivo benchmarks.csv não encontrado!")
    exit()

# Lendo o CSV
df = pd.read_csv('benchmarks.csv', sep=';', decimal=',')

algoritmos = df['Algoritmo'].unique()

for algo in algoritmos:
    data = df[df['Algoritmo'] == algo].sort_values('Threads')
    
    # Criar uma figura com 3 subgráficos
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
    
    # --- AJUSTADO: Salvar dentro da pasta 'resultados' ---
    caminho_arquivo = os.path.join(NOME_PASTA, f'Graficos_{algo}.png')
    plt.savefig(caminho_arquivo)
    print(f"Gráfico de {algo} salvo em: {caminho_arquivo}")