package com.carter.twr;

public class TankUtils {

    static double normalizeBearing(double angle) {
        while (angle >  180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    static double simpleRotationalTargeting(double currentEnemyBearing) {
        double adjustForRotation = 0;
        if (Math.abs(currentEnemyBearing) > 0.1) {
            int enemyMovingClockwise = -1;
            if (currentEnemyBearing < 0) {
                enemyMovingClockwise = 1;
            }
            adjustForRotation = -25.0 * enemyMovingClockwise;
        }
        return adjustForRotation;
    }
}

