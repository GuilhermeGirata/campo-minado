package campominado;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author Guilherme Cicarello Girata GRR20211616
 */
public class CampoMinadoGUI extends JFrame {
    // variáveis do jogo
    private int bombas;
    private int linha;
    private int coluna;
    private int tamanhoLinha, tamanhoColuna;
    private int tamanhoBotao;
    private int espacamento;
    
    // probabilidade de bomba
    private double probabilidade;
    
    // variáveis de controle
    private boolean[][] minas;
    private int[][] camposVizinhos;
    private boolean[][] revelados;
    private boolean[][] marcados;
    
    // variáveis de estado de jogo
    private int dificuldade = 1;
    private boolean vitoria = false;
    private boolean derrota = false;
    private final String mensagemVencedor = "Parabéns! Você venceu!";
    
    // posição da bomba clicada
    private int linhaMina, colunaMina;
    
    // variaveis de mouse
    private int mouseX, mouseY;
    
    // careta
    private int caretaX, caretaY, caretaCentroX, caretaCentroY;
    private int careta = 1;
    
    // dropdown - opções de jogo
    final JMenuItem modoFacil; //fácil
    final JMenuItem modoMedio; //médio
    final JMenuItem modoDificil; //difícil
    
    public CampoMinadoGUI(int linhas, int colunas, int tamanhoLinhas, int tamanhoColunas, int tamanhoBotao, int espacamento, int bombas) {
        this.modoFacil = new JMenuItem("Fácil");
        this.modoMedio = new JMenuItem("Médio");
        this.modoDificil = new JMenuItem("Difícil");
        this.inicializar(linhas, colunas, tamanhoLinhas, tamanhoColunas, tamanhoBotao, espacamento, bombas, false);
    }
    
    private void inicializar(int linhas, int colunas, int tamanhoLinhas, int tamanhoColunas, int tamanhoBotao, int espacamento, int bombas, boolean reset) {
        this.linha = linhas;
        this.coluna = colunas;
        this.tamanhoLinha = tamanhoLinhas;
        this.tamanhoColuna = tamanhoColunas;
        this.tamanhoBotao = tamanhoBotao;
        this.espacamento = espacamento;
        this.bombas = bombas;
        this.probabilidade = (double) bombas / (linhas * colunas);
        this.minas = new boolean[linhas][colunas];
        this.camposVizinhos = new int[linhas][colunas];
        this.revelados = new boolean[linhas][colunas];
        this.marcados = new boolean[linhas][colunas];
        this.mouseX = -100;
        this.mouseY = -100;
        this.caretaX = 310;
        this.caretaY = 5;
        this.caretaCentroX = this.caretaX + tamanhoBotao/2;
        this.caretaCentroY = this.caretaY + tamanhoBotao/2;
        this.careta = 1;
        this.vitoria = false;
        this.derrota = false;
        
        boolean repeating = false;
        while (bombas > 0) {
            for (int col = 0; col < colunas; col++) {
                for (int row = 0; row < linhas; row++) {
                    if (bombas > 0 && Math.random() < probabilidade && !minas[row][col]) {
                        minas[row][col] = true;
                        bombas--;
                    } else if (!repeating) {
                        minas[row][col] = false;
                    }
                    revelados[row][col] = false;
                    marcados[row][col] = false;
                }
            }
            repeating = true;
        }
        
        // camposVizinhos
        for (int col = 0; col < colunas; col++) {
            for (int row = 0; row < linhas; row++) {
                int count = 0;
                
                // cima
                if (row > 0 && minas[row - 1][col]) {
                    count++;
                }
                                
                // baixo
                if (row < linhas - 1 && minas[row + 1][col]) {
                    count++;
                }
                
                // esquerda
                if (col > 0 && minas[row][col - 1]) {
                    count++;
                }
                
                // direita
                if (col < colunas - 1 && minas[row][col + 1]) {
                    count++;
                }
                
                // esquerda superior
                if (row > 0 && col > 0 && minas[row - 1][col - 1]) {
                    count++;
                }
                
                // direita superios
                if (row > 0 && col < colunas - 1 && minas[row - 1][col + 1]) {
                    count++;
                }
                
                // esquerda inferior
                if (row < linhas - 1 && col > 0 && minas[row + 1][col - 1]) {
                    count++;
                }
                
                // direita inferior
                if (row < linhas - 1 && col < colunas - 1 && minas[row + 1][col + 1]) {
                    count++;
                }
                
                camposVizinhos[row][col] = count;
            }
        }
        
        this.setSize(tamanhoLinha, tamanhoColuna);
        this.setLocationRelativeTo(null);

        // configurações da grade
        if (!reset) {
             // menu bar
            JMenuBar menuBar = new JMenuBar();

            this.setJMenuBar(menuBar);

            // opções - menu dropdown
            JMenu gameMenu = new JMenu("Opções");
            menuBar.add(gameMenu);

            // ações do menu
            Acoes menuActions = new Acoes();
            this.modoFacil.addActionListener(menuActions);
            this.modoMedio.addActionListener(menuActions);
            this.modoDificil.addActionListener(menuActions);
            
            gameMenu.add(modoFacil);
            gameMenu.add(modoMedio);
            gameMenu.add(modoDificil);
            
            this.setTitle("Campo Minado");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);
            this.setVisible(true);
            this.setBounds(800,250,376,500);

            personalizacao board = new personalizacao();
            this.setContentPane(board);

            Move move = new Move();
            this.addMouseMotionListener(move);

            Click click = new Click();
            this.addMouseListener(click);
        }
    }
    
    private void revelarCelula(int row, int col) {
        revelados[row][col] = true;
        if (marcados[row][col])
            bombas++;
        marcados[row][col] = false;
        if (!minas[row][col] && camposVizinhos[row][col] == 0) {   
            // cima
            if (row > 0 && !revelados[row - 1][col]) {
                revelarCelula(row -1, col);
            }

            // baixo
            if (row < linha - 1 && !revelados[row + 1][col]) {
                revelarCelula(row + 1, col);
            }

            // esquerda
            if (col > 0 && !revelados[row][col - 1]) {
                revelarCelula(row, col - 1);
            }

            // direita
            if (col < this.coluna - 1 && !revelados[row][col + 1]) {
                revelarCelula(row, col + 1);
            }
                
            // esquerda superior
            if (row > 0 && col > 0 && !revelados[row - 1][col - 1]) {
                revelarCelula(row - 1, col - 1);
            }

            // direita superior
            if (row > 0 && col < this.coluna - 1 && !revelados[row - 1][col + 1]) {
                revelarCelula(row - 1, col + 1);
            }

            // esquerda inferior
            if (row < linha - 1 && col > 0 && !revelados[row + 1][col - 1]) {
                revelarCelula(row + 1, col - 1);
            }

            // direita inferior
            if (row < linha - 1 && col < this.coluna - 1 && !revelados[row + 1][col + 1]) {
                revelarCelula(row + 1, col + 1);
            }
        }
    }
    
    private boolean derrota() {
        for (int col = 0; col < this.coluna; col++) {
            for (int row = 0; row < linha; row++) {
                if (revelados[row][col] && minas[row][col])
                    return true;
            }
        }
        return false;
    }
    
    private boolean vitoria() {
        for (int col = 0; col < this.coluna; col++) {
            for (int row = 0; row < linha; row++) {
                if ((!revelados[row][col] && !marcados[row][col]) || minas[row][col] != marcados[row][col])
                    return false;
            }
        }
        return true;
    }
    
    private int statusJogo() {
        derrota = derrota();
        vitoria = vitoria();
        if (vitoria) {              // mensagem vitória
            JOptionPane.showMessageDialog(new JFrame(), mensagemVencedor, "Você venceu!", JOptionPane.INFORMATION_MESSAGE);
        } else if (derrota) {        // revela bombas
            for (int col = 0; col < this.coluna; col++) {
                for (int row = 0; row < linha; row++) {
                    if (minas[row][col])
                        revelados[row][col] = true;
                }
            }
            JOptionPane.showMessageDialog(new JFrame(), "Você Perdeu! Clique na careta para tentar novamente! ", "Explodiu!", JOptionPane.INFORMATION_MESSAGE);
        }
        return derrota ? 2 : 1;
    }
    
    private void restart() {
        this.mudarDificuldade();
    }
    
    private void mudarDificuldade() {
        switch (dificuldade) {

            case 1 -> {
                this.inicializar(9, 9, 360, 455, 40, 1, 10, true);
                this.setBounds(800,250,376,500);
            }
            case 2 -> {
                this.inicializar(16, 16, 530, 620, 530/16, 1, 40, true);
                this.setBounds(700,250,545,650);
                caretaX = 485;
            }
            default -> {
                this.inicializar(16, 30, 990, 620, 530/16, 1, 99, true);
                this.setBounds(500,250,1005,650);
                caretaX = 935;
            }
        }
    }
    
    private boolean careta() {
        double mouseDistanceToCenter = Math.sqrt(Math.pow(mouseX - caretaCentroX, 2) + Math.pow(mouseY - caretaCentroY, 2));
        return mouseDistanceToCenter < tamanhoBotao/2;
    }
    
    private int[] insideBox() {
        int X, Y, ladoBotao;
        
        for (int col = 0; col < this.coluna; col++) {
            for (int row = 0; row < linha; row++) {
                X = espacamento + col * tamanhoBotao;
                Y = 10 + espacamento + (row + 1) * tamanhoBotao;
                ladoBotao = tamanhoBotao - 2 * espacamento;
                if (mouseX >= X && mouseX <= X + ladoBotao && mouseY >= Y - espacamento && mouseY <= Y + ladoBotao) {
                    return new int[]{ row, col };
                }
            }
        }
        return new int[]{ -1 };
    }
    
    public class personalizacao extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            // background
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, tamanhoLinha, tamanhoColuna);
            
            int positionX, positionY, buttonSide;
            for (int col = 0; col < CampoMinadoGUI.this.coluna; col++) {
                for (int row = 0; row < linha; row++) {
                    positionX = espacamento + col * tamanhoBotao;
                    positionY = 10 + espacamento + (row + 1) * tamanhoBotao;
                    buttonSide = tamanhoBotao - 2 * espacamento;
                    
                    // botao default
                    g.setColor(Color.WHITE);

                    if (revelados[row][col]) {
                        g.setColor(Color.GRAY);
                        if (minas[row][col] && row == linhaMina && col == colunaMina)
                            g.setColor(Color.RED);
                    } else if (mouseX >= positionX && mouseX <= positionX + buttonSide && mouseY >= positionY - espacamento  && mouseY <= positionY + buttonSide) {
                        g.setColor(Color.LIGHT_GRAY);
                    }
                                        
                    g.fillRect(positionX, positionY, buttonSide, buttonSide);
                    
                    if (revelados[row][col]) {
                        if (minas[row][col]) {
                            g.setColor(Color.BLACK);
                            g.fillOval(positionX - espacamento - 3 + tamanhoBotao/9 + tamanhoBotao/8, positionY - espacamento - 3 + tamanhoBotao/9 + tamanhoBotao/8, tamanhoBotao/2, tamanhoBotao/2);
                        } else {
                            g.setColor(Color.GRAY);
                            switch (camposVizinhos[row][col]) {
                                case 1 -> g.setColor(Color.BLUE);
                                case 2 -> g.setColor(Color.GREEN);
                                case 3 -> g.setColor(Color.RED);
                                case 4 -> g.setColor(new Color(0, 0, 128));
                                case 5 -> g.setColor(new Color(178, 34, 34));
                                case 6 -> g.setColor(new Color(72, 209, 204));
                                case 7 -> g.setColor(Color.PINK);
                                case 8 -> g.setColor(Color.BLACK);
                                default -> {
                                }
                            }
                            g.setFont(new Font("Tahoma", Font.BOLD, tamanhoBotao * 2/3));
                            g.drawString(Integer.toString(camposVizinhos[row][col]), positionX + tamanhoBotao/6, positionY + tamanhoBotao * 2/3);
                        }
                    } else if (marcados[row][col]) {   
                        g.setColor(Color.BLACK);
                        g.fillRect(positionX + tamanhoBotao/3, positionY + tamanhoBotao/7, tamanhoBotao/8, tamanhoBotao/2);
                        g.fillRect(positionX + tamanhoBotao/7, positionY + tamanhoBotao/7, tamanhoBotao/3, tamanhoBotao/5);
                        g.setColor(Color.RED);
                        g.fillRect(positionX + tamanhoBotao/7 + 2, positionY + tamanhoBotao/7 + 2, tamanhoBotao/3 - 4, tamanhoBotao/5 - 4);
                    }
                }
            }
            
            // contador marcadores
            g.setColor(Color.GRAY);
            g.fillRect(0, 5, tamanhoBotao * 2, tamanhoBotao);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Digital-7", Font.PLAIN, tamanhoBotao));
            g.drawString(String.format("%03d", bombas), 0, 5 + tamanhoBotao - espacamento);
            
            // careta
            g.setColor(Color.YELLOW);
            g.fillOval(caretaX, caretaY, tamanhoBotao, tamanhoBotao);
            g.setColor(Color.BLACK);
            switch (careta) {
                case 1 -> {
                    g.setFont(new Font("Tahoma", Font.BOLD, tamanhoBotao * 1/3));
                    g.drawString(">", caretaX + tamanhoBotao/5, caretaY + tamanhoBotao/2);
                    g.drawString("<", caretaX + tamanhoBotao - 2 * espacamento - 5 - tamanhoBotao/5, caretaY + tamanhoBotao/2);
                    g.fillRect(caretaX + tamanhoBotao/5, caretaY + tamanhoBotao * 5/7, tamanhoBotao * 5/8, tamanhoBotao/10);
                    g.fillRect(caretaX + tamanhoBotao/7, caretaY + tamanhoBotao * 5/8, tamanhoBotao * 1/8, tamanhoBotao/8);
                    g.fillRect(caretaX + tamanhoBotao - espacamento - 3 - tamanhoBotao/7, caretaY + tamanhoBotao * 5/8, tamanhoBotao * 1/8, tamanhoBotao/8);
                }
                case 2 -> {
                    g.setFont(new Font("Tahoma", Font.BOLD, tamanhoBotao * 1/3));
                    g.drawString("X", caretaX + tamanhoBotao/5, caretaY + tamanhoBotao/2);
                    g.drawString("X", caretaX + tamanhoBotao - 2 * espacamento - 5 - tamanhoBotao/5, caretaY + tamanhoBotao/2);
                    g.fillRect(caretaX + tamanhoBotao/5, caretaY + tamanhoBotao * 4/7, tamanhoBotao * 5/8, tamanhoBotao/10);
                    g.fillRect(caretaX + tamanhoBotao/7, caretaY + tamanhoBotao * 5/8, tamanhoBotao * 1/8, tamanhoBotao/8);
                    g.fillRect(caretaX + tamanhoBotao - espacamento - 3 - tamanhoBotao/7, caretaY + tamanhoBotao * 5/8, tamanhoBotao * 1/8, tamanhoBotao/8);  
                }
                default -> {
                    g.fillOval(caretaX + tamanhoBotao/5, caretaY + tamanhoBotao/4, tamanhoBotao/4, tamanhoBotao/4);
                    g.fillOval(caretaX + tamanhoBotao - 2 * espacamento - tamanhoBotao/5, caretaY + tamanhoBotao/4, tamanhoBotao/4, tamanhoBotao/4);
                    g.fillOval(caretaX + tamanhoBotao/3, caretaY + tamanhoBotao * 5/8, tamanhoBotao/3, tamanhoBotao/3);
                }
            }
        }
    }
    
    public class Move implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent arg0) {
            // Not implemented yet
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY() - 50;
        }
    }
    
    public class Click implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Not implemented yet
        }

        @Override
        public void mousePressed(MouseEvent arg0) {
            // Not implemented yet
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int[] buttonXY = insideBox();
            
            if (e.getButton() == MouseEvent.BUTTON1) {                  // botão esquerdo
                if (!derrota && !vitoria && buttonXY[0] != -1) {
                    if (minas[buttonXY[0]][buttonXY[1]]) {
                        linhaMina = buttonXY[0];
                        colunaMina = buttonXY[1];
                    }
                    revelarCelula(buttonXY[0], buttonXY[1]);
                }

                if (careta()) {
                    restart();
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {           // botão direito
                if (!derrota && !vitoria && buttonXY[0] != -1) {
                    if (marcados[buttonXY[0]][buttonXY[1]]) {
                        marcados[buttonXY[0]][buttonXY[1]] = false;
                        bombas++;
                    } else if (!marcados[buttonXY[0]][buttonXY[1]] && bombas > 0) {
                        marcados[buttonXY[0]][buttonXY[1]] = true;
                        bombas--;
                    }
                }
            }
            careta = statusJogo();
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
            // Not implemented yet
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
            // Not implemented yet
        }
    }
    
    public class Acoes implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(modoFacil)) {
                dificuldade = 1;
            } else if (e.getSource().equals(modoMedio)) {
                dificuldade = 2;
            } else if (e.getSource().equals(modoDificil)) {
                dificuldade = 3;
            }
            mudarDificuldade();
        }
    }
}