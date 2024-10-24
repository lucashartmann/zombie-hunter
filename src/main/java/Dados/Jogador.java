package Dados;

import javafx.scene.image.ImageView;

public class Jogador {
    private int vidas;
    private Tipo tipoJogador;
    private ImageView imagem;

    public Jogador(int vidas, Tipo tipoJogador, ImageView imagem) {
        this.vidas = vidas;
        this.tipoJogador = tipoJogador;
        this.imagem = imagem;
    }

    public void atacar(Jogador alvo, int dano) {
        alvo.vidas -= dano; // Subtrai o dano da vida do alvo
        if (alvo.vidas < 0) {
            alvo.vidas = 0; // Garante que a vida não fique negativa
        }
    }

    public int getVidas() {
        return vidas;
    }

    public String getTipoJogador() {
        return tipoJogador.toString();
    }

    public ImageView getImagem() {
        return imagem;
    }

}
