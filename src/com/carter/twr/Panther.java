package com.carter.twr;

import robocode.AdvancedRobot;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;

/*
    T34, T70 and Fire buster
 */
public class Panther extends AdvancedRobot {

    private int moveDirection = 1;
    private int toggleAngleOffset = 1;
    private double lastEnemyHeading = 0.0;

    public void run() {
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);
        setTurnRadarRight(1000);
        execute();
        while (true) {
            if (getRadarTurnRemaining() == 0) {
                setTurnRadarRight(90);
            }
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        double firepower = Math.min(400 / e.getDistance(), 3);
        double currentEnemyBearing = e.getBearing();
        double currentEnemyHeading = e.getHeading();
        double turnGunRightDegrees = getHeading() - getGunHeading() + currentEnemyBearing;
        out.println("turnGunRightDegrees: " + turnGunRightDegrees);
        out.println("enemyBearing: " + currentEnemyBearing);
        out.println("currentEnemyHeading: " + currentEnemyHeading);
        double adjustForRotation = 0.0 * toggleAngleOffset;
        if (Math.abs(lastEnemyHeading - currentEnemyHeading) > 0.2) {
            adjustForRotation = 15.0 * toggleAngleOffset;
        }
        lastEnemyHeading = currentEnemyHeading;
        setTurnGunRight(normalizeBearing(turnGunRightDegrees + adjustForRotation));
        setTurnRight(currentEnemyBearing);
        if (getVelocity() == 0)  {
            moveDirection *= -1;
        }
        setTurnRight(normalizeBearing(e.getBearing() + 90 - (15 * moveDirection)));
        setAhead(1000 * moveDirection);
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10)  {
            setFire(firepower);
            toggleAngleOffset *= -1;
        }
        setTurnRadarRight(getHeading() - getRadarHeading() + currentEnemyBearing);
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        moveDirection *= -1;
        setAhead(200 * moveDirection);
        execute();
    }

    double normalizeBearing(double angle) {
        while (angle >  180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

}
