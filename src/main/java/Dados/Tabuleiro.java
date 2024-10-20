package Dados;

import java.util.ArrayList;

import javafx.scene.image.ImageView;

public class Tabuleiro {
   private ArrayList<Jogador> inimigos;
   private int quantidade;

   public Tabuleiro() {
      inimigos = new ArrayList<>();
   }

   public void add(Jogador jogador) {
      inimigos.add(jogador);
   }

   public void remove(Jogador jogador) {
      inimigos.remove(jogador);
   }

   public Jogador getInimigoByImage(ImageView imageInimigo) {
      for (Jogador jogador : inimigos) {
         if (jogador.getJogadorView() == imageInimigo) {
            return jogador;
         }
      }
      return null;
   }

   public boolean contains(Jogador jogador){
      if (inimigos.contains(jogador)) {
         return true;
      }
      return false;
   }

   public boolean isEmpty(){
      if (inimigos.isEmpty()) {
         return true;
      }
      return false;
   }

   public int getQuantidade() {
      return quantidade;
   }
}
