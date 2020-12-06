package View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * ImageFactory is a factory class for generating image view according to object type
 */
public class ImageFactory {

    /**
     * Create wall image view.
     *
     * @return the image view
     */
    public static ImageView createWall() {
        return new ImageView(new Image("file:src/main/resources/image/wall.bmp"));
    }

    /**
     * Create floor image view.
     *
     * @return the image view
     */
    public static ImageView createFloor() {
        return new ImageView(new Image("file:src/main/resources/image/floor.bmp"));
    }

    /**
     * Create crate image view.
     *
     * @return the image view
     */
    public static ImageView createCrate() {
        return new ImageView(new Image("file:src/main/resources/image/box.png"));
    }

    /**
     * Create goal image view.
     *
     * @return the image view
     */
    public static ImageView createGoal() {
        return new ImageView(new Image("file:src/main/resources/image/goal.png"));
    }

    /**
     * Create player image view.
     *
     * @return the image view
     */
    public static ImageView createPlayer() {
        return new ImageView(new Image("file:src/main/resources/image/playerD.png"));
    }

    /**
     * Create player up image view.
     *
     * @return the image view
     */
    public static ImageView createPlayerUp() {
        return new ImageView(new Image("file:src/main/resources/image/playerU.png"));
    }

    /**
     * Create player left image view.
     *
     * @return the image view
     */
    public static ImageView createPlayerLeft() {
        return new ImageView(new Image("file:src/main/resources/image/playerL.png"));
    }

    /**
     * Create player right image view.
     *
     * @return the image view
     */
    public static ImageView createPlayerRight() {
        return new ImageView(new Image("file:src/main/resources/image/playerR.png"));
    }

    /**
     * Create crate on dest image view.
     *
     * @return the image view
     */
    public static ImageView createCrateOnDest() {
        return new ImageView(new Image("file:src/main/resources/image/boxO.png"));
    }

    /**
     * Create pipe up image view.
     *
     * @return the image view
     */
    public static ImageView createPipeUp() {
        return new ImageView(new Image("file:src/main/resources/image/pipeU.png"));
    }

    /**
     * Create pipe down image view.
     *
     * @return the image view
     */
    public static ImageView createPipeDown() {
        return new ImageView(new Image("file:src/main/resources/image/pipeD.png"));
    }

    /**
     * Create pipe left image view.
     *
     * @return the image view
     */
    public static ImageView createPipeLeft() {
        return new ImageView(new Image("file:src/main/resources/image/pipeL.png"));
    }

    /**
     * Create pipe right image view.
     *
     * @return the image view
     */
    public static ImageView createPipeRight() {
        return new ImageView(new Image("file:src/main/resources/image/pipeR.png"));
    }

    /**
     * Create gate image view.
     *
     * @return the image view
     */
    public static ImageView createGate() {
        return new ImageView(new Image("file:src/main/resources/image/gate.bmp"));
    }

    /**
     * Create box on gate image view.
     *
     * @return the image view
     */
    public static ImageView createBoxOnGate() {
        return new ImageView(new Image("file:src/main/resources/image/box&gate.png"));
    }

    /**
     * Create pad image view.
     *
     * @return the image view
     */
    public static ImageView createPad() {
        return new ImageView(new Image("file:src/main/resources/image/pressure_pad.png"));
    }

    /**
     * Create crate on pad image view.
     *
     * @return the image view
     */
    public static ImageView createCrateOnPad() {
        return new ImageView(new Image("file:src/main/resources/image/boxP.png"));
    }

    /**
     * select image view.
     *
     * @param obj the obj
     * @return the image view
     */
    public static ImageView chooseImage(char obj) {
        switch (obj) {
            case 'W':
                return createWall();
            case ' ':
                return createFloor();
            case 'C':
                return createCrate();
            case 'G':
                return createGoal();
            case 'S':
                return createPlayer();
            case 'T':
                return createPlayerUp();
            case 'F':
                return createPlayerLeft();
            case 'H':
                return createPlayerRight();
            case 'O':
                return createCrateOnDest();
            case 'U':
                return createPipeUp();
            case 'D':
                return createPipeDown();
            case 'L':
                return createPipeLeft();
            case 'R':
                return createPipeRight();
            case '$':
                return createGate();
            case '#':
                return createBoxOnGate();
            case '&':
                return createPad();
            case 'P':
                return createCrateOnPad();
            default:
                return null;
        }
    }
}
