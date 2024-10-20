/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timidinrobot;

import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 *
 * @author cris
 */
public class disparaEnemic implements State{

    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();
        while(true){
            robot.turnGunRight(10);
        }
        
    }

    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        TimidinRobot robot = context.getRobot();
        double absoluteBearing = robot.getHeading() + e.getBearing();
        double bearingFromGun = Utils.normalRelativeAngleDegrees(absoluteBearing - robot.getGunHeading());    
        double enemyDistance = e.getDistance();
        robot.turnGunRight(bearingFromGun);
        robot.setFire(Math.min(400/enemyDistance, 6));
    }
}
