package View;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageFactory {
//    private static ImageView wall = new ImageView(new Image("file:src/main/resources/image/wall.bmp"));
//    private static ImageView floor = new ImageView(new Image("file:src/main/resources/image/floor.bmp"));
//    private static ImageView crate = new ImageView(new Image("file:src/main/resources/image/box.png"));
//    private static ImageView goal = new ImageView(new Image("file:src/main/resources/image/goal.png"));
//    private static ImageView player = new ImageView(new Image("file:src/main/resources/image/playerD.png"));
//    private static ImageView crateOnDest = new ImageView(new Image("file:src/main/resources/image/boxO.png"));
//    private static ImageView pipeUp = new ImageView(new Image("file:src/main/resources/image/pipeU.png"));
//    private static ImageView pipeDown = new ImageView(new Image("file:src/main/resources/image/pipeD.png"));
//    private static ImageView pipeLeft = new ImageView(new Image("file:src/main/resources/image/pipeL.png"));
//    private static ImageView pipeRight = new ImageView(new Image("file:src/main/resources/image/pipeR.png"));
//    private static ImageView gate = new ImageView(new Image("file:src/main/resources/image/gate.bmp"));
//    private static ImageView pad = new ImageView(new Image("file:src/main/resources/image/pressure_pad.bmp"));
//    private static ImageView crateOnPad = new ImageView(new Image("file:src/main/resources/image/boxP.bmp"));

    public static ImageView createWall() {
        return new ImageView(new Image("file:src/main/resources/image/wall.bmp"));
    }

    public static ImageView createFloor() {
        return new ImageView(new Image("file:src/main/resources/image/floor.bmp"));
    }

    public static ImageView createCrate() {
        return new ImageView(new Image("file:src/main/resources/image/box.png"));
    }

    public static ImageView createGoal() {
        return new ImageView(new Image("file:src/main/resources/image/goal.png"));
    }

    public static ImageView createPlayer() {
        return new ImageView(new Image("file:src/main/resources/image/playerD.png"));
    }

    public static ImageView createPlayerUp() {
        return new ImageView(new Image("file:src/main/resources/image/playerU.png"));
    }

    public static ImageView createPlayerLeft() {
        return new ImageView(new Image("file:src/main/resources/image/playerL.png"));
    }

    public static ImageView createPlayerRight() {
        return new ImageView(new Image("file:src/main/resources/image/playerR.png"));
    }

    public static ImageView createCrateOnDest() {
        return new ImageView(new Image("file:src/main/resources/image/boxO.png"));
    }

    public static ImageView createPipeUp() {
        return new ImageView(new Image("file:src/main/resources/image/pipeU.png"));
    }

    public static ImageView createPipeDown() {
        return new ImageView(new Image("file:src/main/resources/image/pipeD.png"));
    }

    public static ImageView createPipeLeft() {
        return new ImageView(new Image("file:src/main/resources/image/pipeL.png"));
    }

    public static ImageView createPipeRight() {
        return new ImageView(new Image("file:src/main/resources/image/pipeR.png"));
    }

    public static ImageView createGate() {
        return new ImageView(new Image("file:src/main/resources/image/gate.bmp"));
    }

    public static ImageView createBoxOnGate() {
        return new ImageView(new Image("file:src/main/resources/image/box&gate.png"));
    }

    public static ImageView createPad() {
        return new ImageView(new Image("file:src/main/resources/image/pressure_pad.png"));
    }

    public static ImageView createCrateOnPad() {
        return new ImageView(new Image("file:src/main/resources/image/boxP.png"));
    }

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
