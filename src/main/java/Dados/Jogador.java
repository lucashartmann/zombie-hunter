package Dados;
import java.util.Random;

import javafx.scene.image.ImageView;

public class Jogador {
    private int vidas;
    private int dano;
    private Tipo tipoJogador;
    private ImageView jogadorView;

    public Jogador(int vidas, Tipo tipoJogador, ImageView jogadorView) {
        this.vidas = vidas;
        this.tipoJogador = tipoJogador;
        this.jogadorView = jogadorView;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public int getDano() {
        return dano;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public ImageView getJogadorView() {
        return jogadorView;
    }

    public void setJogadorView(ImageView jogadorView) {
        this.jogadorView = jogadorView;
    }

    public Tipo getTipoJogador() {
        return tipoJogador;
    }

    public void setTipoJogador(Tipo tipoJogador) {
        this.tipoJogador = tipoJogador;
    }

    public int atacar(Jogador jogador){
        int dano = rolarDado();
        int vidas = jogador.getVidas() - dano;
        jogador.setVidas(vidas);
        return dano;
    }

    public static int rolarDado() {
        Random random = new Random();
        return random.nextInt(6) + 1; // Retorna um número entre 1 e 6
    }

}
