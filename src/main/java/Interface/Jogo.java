package Interface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Bounds;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Dados.Jogador;
import Dados.Tabuleiro;
import Dados.Tipo;
import javafx.application.Platform;

public class Jogo extends Application {

    private ImageView jogadorView;
    private ImageView tabuleiroView;
    private List<ImageView> inimigos;
    private Scene scene;
    private Pane root;
    private Jogador cacador;
    private Tabuleiro tabuleiro;
    private int passosDispo;
    private TextArea textArea;
    private boolean colidiu;
    private Jogador zumbiNew;
    private int dado;
    private int dadoJogador;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        jogadorView = new ImageView(new Image("/Imagens/jogador.png"));
        tabuleiroView = new ImageView(new Image("/Imagens/tabuleiro.png"));
        scene = new Scene(root, 600, 600);
        inimigos = new ArrayList<>();
        textArea = new TextArea();
        tabuleiroView.setFitWidth(600);
        tabuleiroView.setFitHeight(600);
        tabuleiroView.setPreserveRatio(true);
        tabuleiroView.setSmooth(true);
        root.getChildren().add(tabuleiroView);
        jogadorView.setFitWidth(50);
        jogadorView.setFitHeight(50);
        jogadorView.setX(100);
        jogadorView.setY(100);
        jogadorView.setScaleX(1);
        root.getChildren().add(jogadorView);
        tabuleiro = new Tabuleiro();
        cacador = new Jogador(18, Tipo.CAÇADOR, jogadorView);
        tabuleiro.add(cacador);
        for (int i = 0; i < 4; i++) {
            ImageView inimigo = new ImageView(new Image("/Imagens/zumbi.png"));
            inimigo.setFitWidth(50);
            inimigo.setFitHeight(50);
            inimigo.setX(50 + (i * 150));
            inimigo.setY(400 - (i * 100));
            root.getChildren().add(inimigo);
            inimigos.add(inimigo);
            Jogador zumbi = new Jogador(6, Tipo.ZUMBI, inimigo);
            tabuleiro.add(zumbi);
        }
        scene.setOnKeyPressed(event -> {
            if (passosDispo <= 0 && !colidiu) {
                passosDispo = rolarDado(); // Chama a função para rolar o dado e armazena o resultado
            }
            if (passosDispo > 0 && !colidiu) {
                if (event.getCode() == KeyCode.UP) {
                    jogadorView.setY(jogadorView.getY() - 10);
                    jogadorView.setRotate(0);
                    jogadorView.setScaleX(1);
                    jogadorView.setRotate(-270);
                    jogadorView.setScaleY(-1);
                    passosDispo--;
                } else if (event.getCode() == KeyCode.DOWN) {
                    jogadorView.setY(jogadorView.getY() + 10);
                    jogadorView.setRotate(0);
                    jogadorView.setScaleX(1);
                    jogadorView.setRotate(270);
                    jogadorView.setScaleY(1);
                    passosDispo--;
                } else if (event.getCode() == KeyCode.LEFT) {
                    jogadorView.setX(jogadorView.getX() - 10);
                    jogadorView.setRotate(0);
                    jogadorView.setScaleX(1);
                    jogadorView.setScaleY(1);
                    passosDispo--;
                } else if (event.getCode() == KeyCode.RIGHT) {
                    jogadorView.setX(jogadorView.getX() + 10);
                    jogadorView.setRotate(0);
                    jogadorView.setScaleX(-1);
                    jogadorView.setScaleY(1);
                    passosDispo--;
                }
                textArea.appendText("Passos restantes: " + passosDispo + "\n");
            }
            for (ImageView inimigo : new ArrayList<>(inimigos)) {
                if (verificaColisao(jogadorView, inimigo)) {
                    System.out.println("Colisão com zumbi!");
                    colidiu = true;
                    iniciarCombate();
                    break; // Sai do loop após iniciar o combate
                }
            }
        });
        primaryStage.setTitle("Jogo com JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public int rolarDado() {
        Random random = new Random();
        Stage diceStage = new Stage();
        diceStage.initModality(Modality.APPLICATION_MODAL);
        diceStage.setTitle("Rolagem de Dado");
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(300, 200);
        VBox layout = new VBox(10);
        layout.getChildren().add(textArea);
        layout.setStyle("-fx-padding: 10;");
        Scene diceScene = new Scene(layout);
        diceStage.setScene(diceScene);
        String mensagemDado = "Clique no botão para rolar o dado" + "\n";
        Platform.runLater(() -> textArea.appendText(mensagemDado));
        Button rolarButton = new Button("Rolar Dado");
        layout.getChildren().add(rolarButton);
        rolarButton.setOnAction(e -> {
            int dado = random.nextInt(6) + 1; // Rola o dado
            String mensagemDadoDois = "Você rolou o dado e obteve " + dado + "\n";
            Platform.runLater(() -> textArea.appendText(mensagemDadoDois)); // Atualiza a UI com a mensagem
            // Usar um novo thread para pausar e depois fechar a janela
            new Thread(() -> {
                pausar(2000); // Atraso de 2 segundos para mostrar a mensagem
                Platform.runLater(() -> diceStage.close()); // Fecha a janela na thread correta
            }).start();
            // Retorna o valor do dado rolado
            passosDispo = dado; // Atualiza passos disponíveis com o valor rolado
        });
        // Aguarda o fechamento da janela
        diceStage.showAndWait();
        return passosDispo; // Retorna o valor dos passos disponíveis
    }

    public void iniciarCombate() {
        Stage combateStage = new Stage();
        combateStage.initModality(Modality.APPLICATION_MODAL);
        combateStage.setTitle("Combate");
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(300, 200);
        VBox layout = new VBox(10);
        layout.getChildren().add(textArea);
        layout.setStyle("-fx-padding: 10;");
        Scene combateScene = new Scene(layout);
        combateStage.setScene(combateScene);
        // Encontre o zumbi que colidiu com o jogador
        ImageView inimigoColidido = null;
        for (ImageView inimigo : inimigos) {
            if (verificaColisao(jogadorView, inimigo)) {
                inimigoColidido = inimigo;
                break;
            }
        }
        final ImageView inimigoFinal = inimigoColidido;
        if (inimigoFinal != null) {
            zumbiNew = tabuleiro.getInimigoByImage(inimigoFinal);
            while (cacador.getVidas() > 0 && zumbiNew.getVidas() > 0) {
                // Rola o dado para o caçador
                dadoJogador = rolarDado(); // Aqui você deve usar o valor retornado
                // Rola o dado para o zumbi
                dado = new Random().nextInt(6) + 1; // Rola o dado para o zumbi
                // Aplica o dano
                cacador.atacar(zumbiNew, dadoJogador);
                zumbiNew.atacar(cacador, dado);
                // Atualiza a interface com os resultados
                String mensagemCacador = cacador.getTipoJogador() + " causou " + dadoJogador + " de dano em "
                        + zumbiNew.getTipoJogador() + "\n";
                String mensagemZumbi = zumbiNew.getTipoJogador() + " causou " + dado + " de dano em "
                        + cacador.getTipoJogador() + "\n";
                String mensagem = "Vida do Caçador: " + cacador.getVidas() + "\n" + "Vida do zumbi: "
                        + zumbiNew.getVidas() + "\n";
                Platform.runLater(() -> {
                    textArea.appendText(mensagemCacador);
                    textArea.appendText(mensagemZumbi);
                    textArea.appendText(mensagem);
                });
                pausar(3000); // Pausa para visualização
                // Verifica se o caçador ou o zumbi foram derrotados
                if (cacador.getVidas() <= 0) {
                    Platform.runLater(() -> textArea.appendText("O Caçador foi derrotado!\n"));
                    root.getChildren().remove(jogadorView);
                    break; // Sai do loop
                } else if (zumbiNew.getVidas() <= 0) {
                    Platform.runLater(() -> textArea.appendText("O Zumbi foi eliminado!\n"));
                    root.getChildren().remove(inimigoFinal);
                    break; // Sai do loop
                }
            }
        }
        // Feche a janela após um curto atraso
        Platform.runLater(() -> {
            pausar(3000); // Espera 3 segundos antes de fechar
            combateStage.close();
        });
        combateStage.showAndWait();
    }

    public boolean verificaColisao(ImageView jogador, ImageView inimigo) {
        Bounds boundsJogador = jogador.getBoundsInParent();
        Bounds boundsInimigo = inimigo.getBoundsInParent();
        return boundsJogador.intersects(boundsInimigo);
    }

    public void pausar(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void executar(String[] args) {
        launch(args);
    }
}
