package com.carter.twr;
import robocode.*;

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
		if (turnGunAmt >= 0) {
			turnGunRight(turnGunAmt);
		} else {
			turnGunLeft(-turnGunAmt);	
		}		
		
		if (e.getDistance() < 140) {
			if (getGunHeat() == 0) {
				fire(2.5);		
			}
		} else {
			if (targetBearing >= 0) {
				turnRight(targetBearing);
			} else {	
				turnLeft(targetBearing);
			}
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
