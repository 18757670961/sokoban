package View;

import Model.GameStatus;
import Model.HighScore;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * GameDialog is a factory class that show specific type of dialog when needed
 */
public class GameDialog {

    /**
     * The Dialog window
     */
    private final Stage dialog;
    /**
     * The Dialog title.
     */
    private String dialogTitle;
    /**
     * The Dialog message.
     */
    private String dialogMessage;
    /**
     * The Dialog message effect.
     */
    private Effect dialogMessageEffect;

    /**
     * Instantiates a new Game dialog.
     *
     * @param primaryStage        the primary stage
     * @param dialogTitle         the dialog title
     * @param dialogMessage       the dialog message
     * @param dialogMessageEffect the dialog message effect
     */
    public GameDialog(final Stage primaryStage, final String dialogTitle, final String dialogMessage, final Effect dialogMessageEffect) {
        dialog = setDialogTitle(primaryStage, dialogTitle);
        Text text1 = setDialogMessage(dialogMessage, dialogMessageEffect);
        setDialogScene(text1);
    }

    /**
     * Show victory message.
     */
    public static void showVictoryMessage() {
        String dialogTitle = "Game Over !";
        String dialogMessage = "You completed " + GameStatus.getGameStatus().getMapSetName() + " in " + GameStatus.getGameStatus().getMovesCount() + " moves!\n" +
                "High score in history: " + HighScore.getHighScore(0) + " moves";
        GameDialog dialog = new GameDialog(WindowFactory.getPrimaryStage(), dialogTitle, dialogMessage, null);
    }

    /**
     * Show about.
     */
    public static void showAbout() {
        String title = "Sokoban 2.0";
        String message = "Game refactored and maintained by Shuguang LYU (Desmond)\n";
        GameDialog dialog = new GameDialog(WindowFactory.getPrimaryStage(), title, message, null);
    }

    /**
     * Show high score.
     */
    public static void showHighScore() {
        String title = "Good Job !";
        String message = "Level completed: " + GameStatus.getGameStatus().getCurrentLevel().getName() +
                "\n\n" + "High score: " + HighScore.getHighScore(GameStatus.getGameStatus().getCurrentLevel().getIndex()) + " moves\n\n"
                + "Your score: " + GameStatus.getGameStatus().getMovesCountLevel() + " moves";
        GameDialog dialog = new GameDialog(WindowFactory.getPrimaryStage(), title, message, null);
    }

    /**
     * Sets dialog scene.
     *
     * @param text the text
     */
    private void setDialogScene(Text text) {
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setBackground(Background.EMPTY);
        dialogVbox.getChildren().add(text);

        Scene dialogScene = new Scene(dialogVbox, 350, 150);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Sets dialog message.
     *
     * @param dialogMessage       the dialog message
     * @param dialogMessageEffect the dialog message effect
     * @return the dialog message
     */
    private Text setDialogMessage(String dialogMessage, Effect dialogMessageEffect) {
        Text text1 = new Text(dialogMessage);
        text1.setTextAlignment(TextAlignment.CENTER);
        text1.setFont(javafx.scene.text.Font.font(14));
        text1.setWrappingWidth(300);

        if (dialogMessageEffect != null) {
            text1.setEffect(dialogMessageEffect);
        }
        return text1;
    }

    /**
     * Sets dialog title.
     *
     * @param primaryStage the primary stage
     * @param dialogTitle  the dialog title
     * @return the dialog title
     */
    private Stage setDialogTitle(Stage primaryStage, String dialogTitle) {
        final Stage dialog;
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setResizable(false);
        dialog.setTitle(dialogTitle);
        return dialog;
    }
}
