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
public class detectaEnemic implements State {

    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();
        robot.setTurnRadarLeft(90);
        robot.execute();
    }

    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        TimidinRobot robot = context.getRobot();
        
        double enemyBearing = e.getBearing();
        double distance = e.getDistance();
        
        double myX = robot.getX(), myY = robot.getY();
        double myHeading = robot.getHeadingRadians();

        // Calculate the absolute bearing (convert to radians for trig functions)
        double absoluteBearing = myHeading + Math.toRadians(enemyBearing);

        // Calculate the enemy's position using trigonometry
        double enemyX = myX + distance * Math.sin(absoluteBearing);
        double enemyY = myY + distance * Math.cos(absoluteBearing);
        
        double height = robot.getBattleFieldHeight() - 10, width = robot.getBattleFieldWidth() - 10;
        
        double[] cornersX = {0, width, 0, height}, cornersY = {0, 0, height, width};
        
        double farthestDistance = -1;
        int farthestCorner = -1;
        
        // Find the farthest corner from the enemy
        for (int i = 0; i < 4; i++) {
            double cornerX = cornersX[i];
            double cornerY = cornersY[i];
            
            double dist = Math.sqrt(Math.pow(cornerX - enemyX, 2) + Math.pow(cornerY - enemyY, 2));
            
            if (dist > farthestDistance) {
                farthestDistance = dist;
                farthestCorner = i;
            }
        }
        
        // Calculate bearing to the farthest corner        
        context.setState(new anarCantonada(cornersX[farthestCorner], cornersY[farthestCorner]));
        
    }
    
}

