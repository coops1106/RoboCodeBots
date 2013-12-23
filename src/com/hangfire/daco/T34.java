package com.hangfire.daco;

import robocode.*;
import robocode.util.Utils;

import java.awt.*;

public class T34 extends AdvancedRobot {

    private static final Color RUSSIAN_GREEN = new Color(60, 105, 29);
    public static final int ENGAGEMENT_DISTANCE = 500;
    private double bulletPower = Rules.MIN_BULLET_POWER;
    private double velocity = Rules.MAX_VELOCITY;
    private int scannedX = Integer.MIN_VALUE;
    private int scannedY = Integer.MIN_VALUE;
    private int currentStep = 0;

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
            turnRadarRightRadians(Double.POSITIVE_INFINITY);
            scan();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        double angleToTarget = getHeadingRadians() + e.getBearingRadians();
        double radarTurn = angleToTarget - getRadarHeadingRadians();
        setTurnRadarRightRadians(1.9 * Utils.normalRelativeAngle(radarTurn));
        double gunTurn = angleToTarget - getGunHeadingRadians();
        setTurnGunRightRadians(1.9 * Utils.normalRelativeAngle(gunTurn));
        setFire(bulletPower);
        double robotTurn;
        if (e.getDistance() < ENGAGEMENT_DISTANCE) {
            robotTurn = e.getBearingRadians() + (Math.PI / 2);
        } else {
            robotTurn = e.getBearingRadians();
        }
        setTurnRightRadians(robotTurn);
        setAhead(velocity);
        scannedX = (int) (getX() + Math.sin(angleToTarget) * e.getDistance());
        scannedY = (int) (getY() + Math.cos(angleToTarget) * e.getDistance());
    }

    @Override
    public void onBulletHit(final BulletHitEvent event) {
        if (bulletPower <= Rules.MAX_BULLET_POWER) {
            bulletPower = bulletPower + 0.9;
        }
    }

    @Override
    public void onBulletMissed(final BulletMissedEvent event) {
        if (bulletPower >= Rules.MIN_BULLET_POWER) {
            bulletPower = bulletPower - 0.5;
        }
    }

    @Override
    public void onHitWall(final HitWallEvent event) {
        velocity *= -1;
    }

    @Override
    public void onHitByBullet(final HitByBulletEvent event) {
        velocity *= -1;
    }

    @Override
    public void onPaint(final Graphics2D g) {
        // Set the paint color to a red half transparent color
        g.setColor(new Color(0xff, 0x00, 0x00, 0x80));

        // Draw a line from our robot to the scanned robot
        g.drawLine(scannedX, scannedY, (int) getX(), (int) getY());

        // Draw a filled square on top of the scanned robot that covers it
        g.fillRect(scannedX - 20, scannedY - 20, 40, 40);
    }
}
