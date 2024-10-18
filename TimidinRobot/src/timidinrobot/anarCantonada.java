/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timidinrobot;

import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class anarCantonada implements State {
    private double bearingAngle;
    private double distanceToCorner;

    public anarCantonada(double angle, double distance) {
        this.bearingAngle = angle;
        this.distanceToCorner = distance;
    }


    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();
        
        double myHeading = robot.getHeadingRadians();
        double radarHeading = robot.getRadarHeadingRadians();
        
        double angleToTurn = Utils.normalRelativeAngle(bearingAngle - myHeading);
        double angleToRadar = Utils.normalRelativeAngle(bearingAngle - radarHeading);
        
        // Turn the robot to face the farthest corner and move towards it
        robot.stop();
        robot.setTurnRadarRightRadians(angleToRadar);
        robot.setTurnRightRadians(angleToTurn);
        robot.setAhead(distanceToCorner);
        robot.execute();
        
        // After moving, transition back to scanning state
        context.setState(new disparaEnemic());
    }

    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        // Ignore scanning events in the moving state
    }
}


