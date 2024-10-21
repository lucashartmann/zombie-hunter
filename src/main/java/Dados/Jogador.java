package Dados;

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

    public void atacar(Jogador jogador, int dado){
        int vidas = jogador.getVidas() - dano;
        jogador.setVidas(vidas);
    }


}
