package Interface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Bounds;
import java.util.ArrayList;
import java.util.List;
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
    private Text text;
    private int passosDispo;
    private TextArea textArea;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        jogadorView = new ImageView(new Image("/Imagens/jogador.png"));
        tabuleiroView = new ImageView(new Image("/Imagens/tabuleiro.png"));
        scene = new Scene(root, 600, 600);
        inimigos = new ArrayList<>();
        text = new Text();
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
            if (passosDispo <= 0) {
                passosDispo = Jogador.rolarDado();
                textArea.appendText("Você rolou o dado e pode mover " + passosDispo + " passos.\n");
            }

            if (passosDispo > 0) {
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
                    if (!root.getChildren().contains(text)) {
                        text.setText("Colisao");
                        root.getChildren().add(text);
                    }
                    System.out.println("Colisão com zumbi!");
                    iniciarCombate();
                    break; // Sai do loop após iniciar o combate
                }
            }
        });

        primaryStage.setTitle("Jogo com JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
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

        // Inicie o combate em uma thread separada
        Thread combateThread = new Thread(() -> {
            if (inimigoFinal != null) {
                Jogador zumbiNew = tabuleiro.getInimigoByImage(inimigoFinal);
                while (cacador.getVidas() > 0 && zumbiNew.getVidas() > 0) {
                    int danoCacador = cacador.atacar(zumbiNew);
                    int danoZumbi = zumbiNew.atacar(cacador);
                    
                    String mensagemCacador = cacador.getTipoJogador() + " rolou " + danoCacador + " e causou " + danoCacador + " de dano em " + zumbiNew.getTipoJogador();
                    String mensagemZumbi = zumbiNew.getTipoJogador() + " rolou " + danoZumbi + " e causou " + danoZumbi + " de dano em " + cacador.getTipoJogador();
                    
                    String mensagem = "Vida do Caçador: " + cacador.getVidas() +
                            "\nVida do zumbi: " + zumbiNew.getVidas() +
                            "\n" + mensagemCacador +
                            "\n" + mensagemZumbi + "\n";
                    
                    Platform.runLater(() -> textArea.appendText(mensagem));
                    pausar(500);
                }
                Platform.runLater(() -> {
                    if (cacador.getVidas() <= 0) {
                        textArea.appendText("O Caçador foi derrotado!\n");
                        root.getChildren().remove(jogadorView);
                        if (tabuleiro != null && (!tabuleiro.isEmpty()) && tabuleiro.contains(cacador)) {
                            tabuleiro.remove(cacador);
                        }
                    } else if (zumbiNew.getVidas() <= 0) {
                        textArea.appendText("O Zumbi foi eliminado!\n");
                        root.getChildren().remove(inimigoFinal);
                        if (tabuleiro != null && (!tabuleiro.isEmpty()) && tabuleiro.contains(zumbiNew)) {
                            tabuleiro.remove(zumbiNew);
                        }
                        if (inimigos != null && (!inimigos.isEmpty()) && inimigos.contains(inimigoFinal)) {
                            inimigos.remove(inimigoFinal);
                        }
                    }
                });
            }
            
            // Feche a janela após um curto atraso
            Platform.runLater(() -> {
                pausar(3000); // Espera 3 segundos antes de fechar
                combateStage.close();
            });
        });

        combateThread.start();
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
        Platform.runLater(() -> {}); // Isso garante que a UI seja atualizada
    }

    public void executar(String[] args) {
        launch(args);
    }
}
