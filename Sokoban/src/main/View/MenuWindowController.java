package View;

import Utils.GameIO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * MenuWindowController is a controller class that specify event handling and data binding for start page
 */
public class MenuWindowController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Start game.
     *
     * @param event the event
     */
    @FXML
    void startGame(MouseEvent event) {
        try {
            WindowFactory.createGameWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load game.
     *
     * @param event the event
     */
    @FXML
    void loadGame(MouseEvent event) {
        try {
            if (GameIO.chooseGameFile() != null) {
                WindowFactory.createGameWindow();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show user guide.
     *
     * @param event the event
     */
    @FXML
    void showUserGuide(MouseEvent event) {
        try {
            WindowFactory.createUserGuide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
