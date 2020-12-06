package View;

import Model.GameStatus;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * The type Game window.
 */
// GUI class extracted
public class GameWindow {
    /**
     * The Primary stage.
     */
    private static Stage primaryStage;

    public static void createStartMenu() throws IOException {
        Parent parent = FXMLLoader.load(new URL("file:src/main/View/MenuWindow.fxml"));
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sokoban");
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:src/main/resources/image/box.png"));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }


    public static void createGameWindow() throws IOException {
        Parent parent = FXMLLoader.load(new URL("file:src/main/View/GameWindow.fxml"));
        Scene scene = new Scene(parent);
        primaryStage.setTitle(GameStatus.getGameStatus().getGameName());
        primaryStage.setScene(scene);
//        primaryStage.setScene(new Scene(root));
//        primaryStage.setResizable(false);
//        primaryStage.setFullScreen(true);
//        primaryStage.setWidth(1920);
//        primaryStage.setHeight(1080);
        primaryStage.centerOnScreen();
        //primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.getIcons().add(new Image("file:src/main/resources/image/box.png"));
    }

    public static void createUserGuide() throws IOException {
        Stage stage = new Stage();
        Parent parent = FXMLLoader.load(new URL("file:src/main/View/UserGuide.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("User Guide");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image("file:src/main/resources/image/box.png"));
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
}
