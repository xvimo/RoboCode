/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timidinrobot;

import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class anarCantonada implements State {
    private double angleToTurn;
    private double distanceToCorner;

    public anarCantonada(double angle, double distance) {
        this.angleToTurn = angle;
        this.distanceToCorner = distance;
    }


    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();
        
        
        // Turn the robot to face the farthest corner and move towards it
        robot.stop();
        robot.setTurnRightRadians(angleToTurn);
        robot.setTurnRadarRightRadians(angleToTurn);
        robot.setAhead(distanceToCorner);
        robot.resume();
        
        // After moving, transition back to scanning state
        context.setState(new disparaEnemic());
    }

    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        // Ignore scanning events in the moving state
    }
}


