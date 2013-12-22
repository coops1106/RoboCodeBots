package com.carter.twr;

import robocode.HitWallEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

/**
 * TimFirstRobot - a robot by Timboslice Carter
 */
public class TimFirstRobot extends Robot {

    public void run() {
        setAdjustRadarForGunTurn(true);

        do {
            turnRadarRight(360);
        } while (true);
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        double targetBearing = e.getBearing();
        double turnGunAmt = (getHeading() + e.getBearing()) - getGunHeading();
        turnGunRight(turnGunAmt);

        if (e.getDistance() < 140) {
            if (getGunHeat() == 0) {
                fire(2.5);
            }
        } else {
            turnRight(targetBearing);
            ahead(e.getDistance() - 120);
        }
        scan();
    }

    public void onHitWall(HitWallEvent e) {
        back(10);
        turnLeft(180);
        ahead(100);
    }
}
