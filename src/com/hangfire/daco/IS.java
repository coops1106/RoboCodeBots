package com.hangfire.daco;

import robocode.*;
import robocode.util.Utils;

import java.awt.*;

public class IS extends AdvancedRobot {

    private static final Color RUSSIAN_GREEN = new Color(60, 105, 29);
    private static final int ENGAGEMENT_DISTANCE = 500;
    private static final double BULLET_POWER = 2.0;
    private static final double BULLET_SPEED = (20-3*BULLET_POWER);
    private double velocity = Rules.MAX_VELOCITY;
    private int predictedTargetX = Integer.MIN_VALUE;
    private int predictedTargetY = Integer.MIN_VALUE;
    private boolean hitByBullet = false;
    private double bearingToTarget = Double.MAX_VALUE;
    private double distanceToTarget = Double.MAX_VALUE;
    private double gunTurn = Double.MAX_VALUE;
    private double radarTurn = Double.MAX_VALUE;
    private double enemyEnergy =  Double.MIN_VALUE;
    private boolean energyDropDetected;
    private int pauseLength = 8;
    private int pause = 0;

    @Override
    public void run() {

        setBodyColor(RUSSIAN_GREEN);
        setGunColor(RUSSIAN_GREEN);
        setRadarColor(RUSSIAN_GREEN);
        setScanColor(Color.red);
        setBulletColor(Color.red);

        setAdjustRadarForGunTurn(true);


        while (true) {
            System.out.println("Main loop");
            //turnRadarRightRadians(Double.POSITIVE_INFINITY);
            //scan();

            setTurnRadarRightRadians(1.9 * Utils.normalRelativeAngle(radarTurn));
            setTurnGunRightRadians(Utils.normalRelativeAngle(gunTurn));

            setFire(BULLET_POWER);

            double robotTurn = bearingToTarget;
            if (distanceToTarget < ENGAGEMENT_DISTANCE) {
                robotTurn = bearingToTarget + (Math.PI / 2);
            }

            setTurnRightRadians(robotTurn);
            if (energyDropDetected && (pause < pauseLength)) {
                setAhead(0);
                velocity *= -1;
                pause++;
            } else {
                setAhead(velocity);
                pause = 0;
                energyDropDetected = false;
            }

            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        bearingToTarget = e.getBearingRadians();
        final double angleToTarget = getHeadingRadians() + bearingToTarget;
        final double targetHeading = e.getHeadingRadians();
        final double targetVelocity = e.getVelocity();
        distanceToTarget = e.getDistance();
        final double targetX = getX() + Math.sin(angleToTarget) * distanceToTarget;
        final double targetY = getY() + Math.cos(angleToTarget) * distanceToTarget;
        final double bulletTimeToTarget = distanceToTarget /BULLET_SPEED;

        predictedTargetX = (int) (targetX + Math.sin(targetHeading) * (targetVelocity) * bulletTimeToTarget);
        predictedTargetY = (int) (targetY + Math.cos(targetHeading) * (targetVelocity) * bulletTimeToTarget);

        double angleToPredictedTarget = Math.atan2(predictedTargetX-getX(), predictedTargetY-getY());

        radarTurn = angleToTarget - getRadarHeadingRadians();
        gunTurn = angleToPredictedTarget - getGunHeadingRadians();
        if (e.getEnergy() < enemyEnergy) {
            energyDropDetected = true;
        }
        enemyEnergy = e.getEnergy();
    }

    @Override
    public void onHitWall(final HitWallEvent event) {
        velocity *= -1;
    }

    @Override
    public void onHitByBullet(final HitByBulletEvent event) {
        hitByBullet = true;
    }

    @Override
    public void onPaint(final Graphics2D g) {
        // Set the paint color to a red half transparent color
        g.setColor(new Color(0xff, 0x00, 0x00, 0x80));

        // Draw a line from our robot to the scanned robot
        g.drawLine(predictedTargetX, predictedTargetY, (int) getX(), (int) getY());

        // Draw a filled square on top of the scanned robot that covers it
        g.fillRect(predictedTargetX - 20, predictedTargetY - 20, 40, 40);
    }
}
