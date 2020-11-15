package com.ae2dms;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class GraphicObject extends Rectangle {
    GraphicObject(GameObject obj) {
        Paint color = setColor(obj);

        this.setFill(color);
        this.setHeight(30);
        this.setWidth(30);

        if (obj != GameObject.WALL) {
            this.setArcHeight(50);
            this.setArcWidth(50);
        }

        if (GameEngine.isDebugActive()) {
            this.setStroke(Color.RED);
            this.setStrokeWidth(0.25);
        }
    }

    private Paint setColor(GameObject obj) {
        Paint color;

        // switch replaced with enhanced switch
        switch (obj) {
            case WALL -> color = Color.BLACK;
            case CRATE -> color = Color.ORANGE;
            case DIAMOND -> {
                color = Color.DEEPSKYBLUE;
                if (GameEngine.isDebugActive()) {
                    setTransition();
                }
            }
            case KEEPER -> color = Color.RED;
            case FLOOR -> color = Color.WHITE;
            case CRATE_ON_DIAMOND -> color = Color.DARKCYAN;
            default -> {
                String message = "Error in Level constructor. Object not recognized.";
                GameEngine.getLogger().severe(message);
                throw new AssertionError(message);
            }
        }
        return color;
    }

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
