import java.util.Scanner;

public class JogoWumpus {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int[][] ambiente = new int[4][4];
        int[] posicaoAgente = {0, 0};
        int[] posicaoWumpus = {0, 0};
        int[] posicaoOuro = {0, 0};
        int[][] posicoesPocos = new int[3][2];
        int[] direcao = {0, 1}; // Direção inicial do agente (direita)

        // Entrada das posições pelo usuário
        System.out.println("Digite a posição de 3 poços (linha coluna):");
        for (int i = 0; i < 3; i++) {
            posicoesPocos[i][0] = scanner.nextInt();
            posicoesPocos[i][1] = scanner.nextInt();
        }

        System.out.println("Digite a posição do Wumpus (linha coluna):");
        posicaoWumpus[0] = scanner.nextInt();
        posicaoWumpus[1] = scanner.nextInt();

        System.out.println("Digite a posição do ouro (linha coluna):");
        posicaoOuro[0] = scanner.nextInt();
        posicaoOuro[1] = scanner.nextInt();

        // Inicializar ambiente
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ambiente[i][j] = 0; // 0 representa célula vazia
            }
        }

        for (int i = 0; i < 3; i++) {
            ambiente[posicoesPocos[i][0]][posicoesPocos[i][1]] = 1; // 1 representa poço
        }

        ambiente[posicaoWumpus[0]][posicaoWumpus[1]] = 2; // 2 representa Wumpus
        ambiente[posicaoOuro[0]][posicaoOuro[1]] = 3; // 3 representa ouro

        // Loop do jogo
        boolean jogoTerminado = false;
        boolean pegouOuro = false;
        boolean wumpusVivo = true;

        while (!jogoTerminado) {
            int linhaAtual = posicaoAgente[0];
            int colunaAtual = posicaoAgente[1];

            // Verificar percepções na posição atual
            boolean cheiro = temCheiro(linhaAtual, colunaAtual, posicaoWumpus);
            boolean brisa = temBrisa(linhaAtual, colunaAtual, posicoesPocos);
            boolean brilho = ambiente[linhaAtual][colunaAtual] == 3;

            // Exibir percepções
            if (cheiro) {
                System.out.println("Sinto um cheiro ruim.");
            }
            if (brisa) {
                System.out.println("Sinto uma brisa.");
            }
            if (brilho) {
                System.out.println("Vejo um brilho.");
            }

            // Executar ação
            System.out.print("Digite a ação (mover/virar/atirar/pegar): ");
            String acao = scanner.next();

            if (acao.equals("mover")) {
                int novaLinha = linhaAtual + direcao[0];
                int novaColuna = colunaAtual + direcao[1];

                if (posicaoValida(novaLinha, novaColuna)) {
                    posicaoAgente[0] = novaLinha;
                    posicaoAgente[1] = novaColuna;

                    if (ambiente[novaLinha][novaColuna] == 1 || ambiente[novaLinha][novaColuna] == 2) {
                        jogoTerminado = true;
                    }
                } else {
                    System.out.println("Bati em uma parede.");
                }
            } else if (acao.equals("virar")) {
                System.out.print("Digite a direção para virar (esquerda/direita): ");
                String direcaoVirar = scanner.next();
                if (direcaoVirar.equals("esquerda")) {
                    // Girar no sentido anti-horário
                    int temp = direcao[0];
                    direcao[0] = direcao[1];
                    direcao[1] = -temp;
                } else if (direcaoVirar.equals("direita")) {
                    // Girar no sentido horário
                    int temp = direcao[0];
                    direcao[0] = -direcao[1];
                    direcao[1] = temp;
                }
            } else if (acao.equals("atirar")) {
                if (wumpusVivo) {
                    System.out.println("Ouvi um grito distante.");
                    wumpusVivo = false;
                } else {
                    System.out.println("Já matei o Wumpus.");
                }
            } else if (acao.equals("pegar")) {
                if (brilho) {
                    pegouOuro = true;
                    System.out.println("Peguei o ouro!");
                } else {
                    System.out.println("Aqui não tem ouro.");
                }
            }

            // Verificar condições de fim de jogo
            if (posicaoAgente[0] == posicaoOuro[0] && posicaoAgente[1] == posicaoOuro[1] && pegouOuro) {
                System.out.println("Parabéns! Você encontrou o ouro e venceu!");
                jogoTerminado = true;
            }
            if (ambiente[posicaoAgente[0]][posicaoAgente[1]] == 1 || ambiente[posicaoAgente[0]][posicaoAgente[1]] == 2) {
                System.out.println("Fim de jogo! Você caiu em um poço ou foi pego pelo Wumpus.");
                jogoTerminado = true;
            }
        }

        scanner.close();
    }

    // Verificar se uma posição é válida (dentro dos limites do ambiente)
    private static boolean posicaoValida(int linha, int coluna) {
        return linha >= 0 && linha < 4 && coluna >= 0 && coluna < 4;
    }

    // Verificar se há cheiro na posição atual
    private static boolean temCheiro(int linha, int coluna, int[] posicaoWumpus) {
        int[][] celulasAdjacentes = {{linha - 1, coluna}, {linha + 1, coluna}, {linha, coluna - 1}, {linha, coluna + 1}};
        for (int[] celula : celulasAdjacentes) {
            if (posicaoValida(celula[0], celula[1]) && (celula[0] == posicaoWumpus[0] || celula[1] == posicaoWumpus[1])) {
                return true;
            }
        }
        return false;
    }
 
    // Verificar se há brisa na posição atual
    private static boolean temBrisa(int linha, int coluna, int[][] posicoesPocos) {
        int[][] celulasAdjacentes = {{linha - 1, coluna}, {linha + 1, coluna}, {linha, coluna - 1}, {linha, coluna + 1}};
        for (int[] celula : celulasAdjacentes) {
            for (int[] poco : posicoesPocos) {
                if (posicaoValida(celula[0], celula[1]) && celula[0] == poco[0] && celula[1] == poco[1]) {
                    return true;
                }
            }
        }
        return false;
    }
}
