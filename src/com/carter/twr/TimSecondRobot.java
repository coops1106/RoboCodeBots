package com.carter.twr;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

/*
    Specialised to take down an early T70: beats Fire too
 */
public class TimSecondRobot extends AdvancedRobot {

    public void run() {
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);
        setTurnRadarRight(1000);
        execute();
        while (true) {
            if (getRadarTurnRemaining() == 0) {
                setTurnRadarRight(1);
            }
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {

        double firepower = 3.0;
        double currentEnemyBearing = e.getBearing();
        double currentEnemyHeading = e.getHeading();
        double turnGunRightDegrees = getHeading() - getGunHeading() + currentEnemyBearing;
        out.println("turnGunRightDegrees: " + turnGunRightDegrees);
        out.println("enemyBearing: " + currentEnemyBearing);
        out.println("currentEnemyHeading: " + currentEnemyHeading);
        double adjustForRotation = 0;
        setTurnGunRight(normalizeBearing(turnGunRightDegrees + adjustForRotation));
        setTurnRight(currentEnemyBearing);
        if (Math.abs(getTurnRemaining()) < 10) {
            if (e.getDistance() > 800) {
                setAhead(e.getDistance() / 5);
            }
            if (e.getDistance() < 700) {
                setBack(e.getDistance() / 2);
            }

        }
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10)  {
            setFire(firepower);
        }
        setTurnRadarRight(getHeading() - getRadarHeading() + currentEnemyBearing);
    }

    double normalizeBearing(double angle) {
        while (angle >  180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

}
