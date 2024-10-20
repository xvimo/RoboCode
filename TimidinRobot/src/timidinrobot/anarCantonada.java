/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timidinrobot;

import robocode.ScannedRobotEvent;
import robocode.TurnCompleteCondition;
import robocode.util.Utils;

public class anarCantonada implements State {
    private double cX;
    private double cY;
    private double nAngle;
    private double distance,angle;

    public anarCantonada(double conerX, double cornerY) {
        this.cX = conerX;
        this.cY = cornerY;
    }


    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();
        double bearingAngle = Math.atan2(cX - robot.getX(), cY - robot.getY());
        double distanceToCorner = Math.hypot(cX - robot.getX(), cY - robot.getY());
        
        double myHeading = robot.getHeadingRadians();
        double radarHeading = robot.getRadarHeadingRadians();
        
        double angleToTurn = Utils.normalRelativeAngle(bearingAngle - myHeading);
        double angleToRadar = Utils.normalRelativeAngle(myHeading - radarHeading);
        robot.stop();
        
        // Turn the robot to face the farthest corner and move towards it
        robot.setTurnRightRadians(angleToTurn);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));
        robot.setTurnRadarRightRadians(angleToRadar);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));
        
        robot.setAhead(distanceToCorner);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));
        
        // After moving, transition back to scanning state
        context.setState(new disparaEnemic());
    }

    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        // Ignore scanning events in the moving state

    }
}


