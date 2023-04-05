# Campo Minado
Campo Minado desenvolvido na disciplina de Linguagem de Programação Orientada a Objetos I - UFPR

Faça o jogo do campo minado em Java (desconsidere o contador de tempo). No menu jogo
deverá permitir escolher um dos três níveis, sendo o inicial o jogo padrão a ser criado quando o
programa foi carregado.

- Nível fácil: 9x9 com 10 minas
- Nível médio: 16x16 com 40 minas
- Nível avançado: 30x16 com 99 minas

O programa deve sortear as minas e na sequência para cada célula que não tiver mina deverá
preencher com valores de 1 a 8 correspondendo ao número de minas nas casas imediatamente ao redor
de cada célula do mapa.

O jogador poderá clicar com o botão direito para “abrir” a célula ou com o esquerdo para
marcar a célula como bomba. O número máximo de marcações corresponde ao número de minas e
deverá ser decrementado do display (lado direito da imagem). O botão em forma de “careta” pode ser
utilizado para reiniciar o jogo.

O jogo encerra quando todas as minas forem marcadas (botão direito) ou quando uma mina for
acionada (aberta).
