package campominado;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author Guilherme Cicarello Girata GRR20211616
 */
public class CampoMinado implements Runnable {
    CampoMinadoGUI gui;           
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Thread(new CampoMinado()).start();
    }

    public CampoMinado() {
        JOptionPane.showMessageDialog(new JFrame(), """
                                                    As regras do Campo Minado são simples:
                                                    
                                                    - Se você descobrir uma mina, o jogo acaba.
                                                    
                                                    - Se descobrir um quadrado vazio, o jogo continua.
                                                    
                                                    - Se aparecer um número, ele informará quantas minas estão escondidas nos oito quadrados que o cercam. 
                                                      Você usa essa informação para deduzir em que quadrados próximos é seguro clicar.
                                                    
                                                    - Para reiniciar o jogo, basta clicar na careta!""",
                "Instruções!", JOptionPane.INFORMATION_MESSAGE);
        this.gui = new CampoMinadoGUI(9, 9, 360, 455, 40, 1, 10);
    }   
    
    @Override
    public void run() {
        while (true) {
            gui.repaint();
        }
    }
}