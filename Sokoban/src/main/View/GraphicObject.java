package View;

import Controller.GameEngine;
import Debug.GameLogger;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * The type Graphic object.
 */
public class GraphicObject extends Rectangle {
    /**
     * Instantiates a new Graphic object.
     *
     * @param obj the obj
     */
    public GraphicObject(char obj) {
        Paint color = setColor(obj);

        this.setFill(color);
        this.setHeight(30);
        this.setWidth(30);

        if (obj != 'W') {
            this.setArcHeight(50);
            this.setArcWidth(50);
        }

        if (GameLogger.isDebugActive()) {
            this.setStroke(Color.RED);
            this.setStrokeWidth(0.25);
        }
    }

    /**
     * Sets color.
     *
     * @param obj the obj
     * @return the color
     */
    private Paint setColor(char obj) {
        Paint color;

        // switch replaced with enhanced switch
        switch (obj) {
            case 'W' -> color = Color.BLACK;
            case 'C' -> color = Color.ORANGE;
            case 'D' -> {
                color = Color.DEEPSKYBLUE;
                if (GameLogger.isDebugActive()) {
                    setTransition();
                }
            }
            case 'S' -> color = Color.RED;
            case ' ' -> color = Color.WHITE;
            case 'O' -> color = Color.DARKCYAN;
            default -> {
                String message = "Error in Level constructor. Object not recognized.";
                GameLogger.showSevere(message);
                throw new AssertionError(message);
            }
        }
        return color;
    }

    /**
     * Sets transition.
     */
// method extracted
    private void setTransition() {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), this); // variable name changed
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.2);
        fadeTransition.setCycleCount(Timeline.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }
}
