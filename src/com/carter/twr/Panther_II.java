package com.carter.twr;

import robocode.*;

import java.util.HashMap;
import java.util.Map;

/*
    T34, T70 and Fire buster
 */
public class Panther_II extends AdvancedRobot {

    private int moveDirection = 1;
    private double lastEnemyHeading = 0.0;
    private int toggleFactor = -1;
    private int evasiveActionCounter = 1;

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
        double firepower = 3.0;
        double currentEnemyBearing = e.getBearing();
        double currentEnemyHeading = e.getHeading();
        double turnGunRightDegrees = getHeading() - getGunHeading() + currentEnemyBearing;
        out.println("turnGunRightDegrees: " + turnGunRightDegrees);
        out.println("enemyBearing: " + currentEnemyBearing);
        out.println("currentEnemyHeading: " + currentEnemyHeading);
        double adjustment = TankUtils.simpleRotationalTargeting(currentEnemyBearing);
        if (Math.abs(lastEnemyHeading - currentEnemyHeading) < 0.2) {
            adjustment = 0;
        }
        lastEnemyHeading = currentEnemyHeading;
        setTurnGunRight(TankUtils.normalizeBearing(turnGunRightDegrees + (adjustment * toggleFactor)));
        setTurnRight(currentEnemyBearing);
        if (getVelocity() == 0)  {
            moveDirection *= -1;
        }
        setTurnRight(TankUtils.normalizeBearing(e.getBearing() + 90));
        if (getTime() % 20 == 0) {
            moveDirection *= -1;
            setAhead(150 * moveDirection);
        }
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10)  {
            Bullet bullet = setFireBullet(firepower);
            toggleFactor *= -1;
        }
        setTurnRadarRight(getHeading() - getRadarHeading() + currentEnemyBearing);
    }

    @Override
    public void onHitWall(HitWallEvent e) {
        takeEvasiveAction();
    }

    private void takeEvasiveAction() {
        out.println("evasiveActionCounter: " + evasiveActionCounter);
        if (evasiveActionCounter <= 0) {
            moveDirection *= -1;
            setAhead(250 * moveDirection);
            execute();
            evasiveActionCounter = 2;
            out.println("taking evasive action");
        } else {
            evasiveActionCounter--;
        }
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        takeEvasiveAction();
    }

    @Override
    public void onBulletHit(BulletHitEvent e) {
        out.println("hit scored");
    }


}
