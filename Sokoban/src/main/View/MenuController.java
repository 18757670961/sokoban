package View;

import Main.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nuno on 06/02/2018.
 */
public class MenuController implements Initializable {

    @FXML private Button startButton;
    @FXML private Button loadButton;
    @FXML private Button infoButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        startButton.setOnAction(event -> {
            try {
                GameWindow.createGameWindow();
                GameWindow.reloadGrid();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        loadButton.setOnAction(event -> {
            try {
                GameWindow.createGameWindow();
                GameWindow.loadGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
