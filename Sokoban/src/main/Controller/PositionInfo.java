package Controller;

import Modal.GameObject;

import java.awt.*;

/**
 * The type Position info.
 */
public class PositionInfo {
    /**
     * The Delta.
     */
    private final Point delta;
    /**
     * The Keeper position.
     */
    private final Point keeperPosition;
    /**
     * The Keeper.
     */
    private final GameObject keeper;
    /**
     * The Keeper moved.
     */
    private boolean keeperMoved;
    /**
     * The Target object point.
     */
    private final Point targetObjectPoint;
    /**
     * The Keeper target.
     */
    private final GameObject keeperTarget;

    /**
     * Instantiates a new Position info.
     *
     * @param delta             the delta
     * @param keeperPosition    the keeper position
     * @param keeper            the keeper
     * @param keeperMoved       the keeper moved
     * @param targetObjectPoint the target object point
     * @param keeperTarget      the keeper target
     */
    PositionInfo(Point delta, Point keeperPosition, GameObject keeper, boolean keeperMoved, Point targetObjectPoint, GameObject keeperTarget) {
        this.delta = delta;
        this.keeperPosition = keeperPosition;
        this.keeper = keeper;
        this.keeperMoved = keeperMoved;
        this.targetObjectPoint = targetObjectPoint;
        this.keeperTarget = keeperTarget;
    }

    /**
     * Gets keeper position.
     *
     * @return the keeper position
     */
    public Point getKeeperPosition() {
        return keeperPosition;
    }

    /**
     * Gets keeper.
     *
     * @return the keeper
     */
    public GameObject getKeeper() {
        return keeper;
    }

    /**
     * Is keeper moved boolean.
     *
     * @return the boolean
     */
    public boolean isKeeperMoved() {
        return keeperMoved;
    }

    /**
     * Sets keeper moved.
     *
     * @param keeperMoved the keeper moved
     */
    public void setKeeperMoved(boolean keeperMoved) {
        this.keeperMoved = keeperMoved;
    }

    /**
     * Gets delta.
     *
     * @return the delta
     */
    public Point getDelta() {
        return delta;
    }

    /**
     * Gets target object point.
     *
     * @return the target object point
     */
    public Point getTargetObjectPoint() {
        return targetObjectPoint;
    }

    /**
     * Gets keeper target.
     *
     * @return the keeper target
     */
    public GameObject getKeeperTarget() {
        return keeperTarget;
    }
}
