package Engine;

import Business.GameObject;

import java.awt.*;

public class PositionInfo {
    private final Point delta;
    private final Point keeperPosition;
    private final GameObject keeper;
    private boolean keeperMoved;
    private final Point targetObjectPoint;
    private final GameObject keeperTarget;

    PositionInfo(Point delta, Point keeperPosition, GameObject keeper, boolean keeperMoved, Point targetObjectPoint, GameObject keeperTarget) {
        this.delta = delta;
        this.keeperPosition = keeperPosition;
        this.keeper = keeper;
        this.keeperMoved = keeperMoved;
        this.targetObjectPoint = targetObjectPoint;
        this.keeperTarget = keeperTarget;
    }

    public Point getKeeperPosition() {
        return keeperPosition;
    }

    public GameObject getKeeper() {
        return keeper;
    }

    public boolean isKeeperMoved() {
        return keeperMoved;
    }

    public void setKeeperMoved(boolean keeperMoved) {
        this.keeperMoved = keeperMoved;
    }

    public Point getDelta() {
        return delta;
    }

    public Point getTargetObjectPoint() {
        return targetObjectPoint;
    }

    public GameObject getKeeperTarget() {
        return keeperTarget;
    }
}
