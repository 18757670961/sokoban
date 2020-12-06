package View;

import Utils.GameIO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Nuno on 06/02/2018.
 */
public class MenuWindowController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    void startGame(MouseEvent event) {
        try {
            GameWindow.createGameWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadGame(MouseEvent event) {
        try {
            if (GameIO.chooseGameFile() != null) {
                GameWindow.createGameWindow();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showUserGuide(MouseEvent event) {
        try {
            GameWindow.createUserGuide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
