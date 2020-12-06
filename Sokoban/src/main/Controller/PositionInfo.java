package Controller;

import java.awt.*;

/**
 * The type Position info.
 */
public class PositionInfo {

    /**
     * The Delta of movement
     */
    private Point delta;
    /**
     * The Keeper position.
     */
    private final Point keeperPosition;
    /**
     * The Keeper symbol
     */
    private final char keeper;
    /**
     * if keeper is moved
     */
    private boolean keeperMoved;
    /**
     * The Target object point.
     */
    private final Point targetObjectPoint;
    /**
     * The target symbol
     */
    private final char keeperTarget;

    /**
     * Instantiates a new Position information object
     *
     * @param delta
     * @param keeperPosition
     * @param keeper
     * @param keeperMoved
     * @param targetObjectPoint
     * @param keeperTarget
     */
    PositionInfo(Point delta, Point keeperPosition, char keeper, boolean keeperMoved, Point targetObjectPoint, char keeperTarget) {
        this.delta = delta;
        this.keeperPosition = keeperPosition;
        this.keeper = keeper;
        this.keeperMoved = keeperMoved;
        this.targetObjectPoint = targetObjectPoint;
        this.keeperTarget = keeperTarget;
    }

    /**
     * @return the keeper position
     */
    public Point getKeeperPosition() {
        return keeperPosition;
    }

    /**
     * @return the keeper symbol
     */
    public char getKeeper() {
        return keeper;
    }

    /**
     * @return if keeper is moved
     */
    public boolean isKeeperMoved() {
        return keeperMoved;
    }

    /**
     * @param keeperMoved
     */
    public void setKeeperMoved(boolean keeperMoved) {
        this.keeperMoved = keeperMoved;
    }

    /**
     * get delta of movement
     *
     * @return the delta
     */
    public Point getDelta() {
        return delta;
    }

    /**
     * get target object point.
     *
     * @return the target object point
     */
    public Point getTargetObjectPoint() {
        return targetObjectPoint;
    }

    /**
     * get target of keeper
     *
     * @return the keeper target
     */
    public char getKeeperTarget() {
        return keeperTarget;
    }

    /**
     * set delta of moevement
     *
     * @param oldPos the old position
     * @param newPos the new position
     */
    public void setDelta(Point oldPos, Point newPos) {
        int x = newPos.x - oldPos.x;
        int y = newPos.y - oldPos.y;
        delta = new Point(x, y);
    }
}
