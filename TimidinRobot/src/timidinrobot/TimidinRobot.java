/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package timidinrobot;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 *
 * @author xavi
 * @author cris
 */
public class TimidinRobot extends AdvancedRobot {
    
    @Override
    public void run(){
        //setTurnLeft(getHeading());
        while(true){
            setTurnRadarLeft(90);
            execute();
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        double enemyBearing = e.getBearing();
        double distance = e.getDistance();
        
        double myX = getX(), myY = getY();
        double myHeading = getHeadingRadians();

        // Calculate the absolute bearing (convert to radians for trig functions)
        double absoluteBearing = myHeading + Math.toRadians(enemyBearing);

        // Calculate the enemy's position using trigonometry
        double enemyX = myX + distance * Math.sin(absoluteBearing);
        double enemyY = myY + distance * Math.cos(absoluteBearing);
        
        double height = getBattleFieldHeight() - 10, width = getBattleFieldWidth() - 10;
        
        double[] cornersX = {0,width,0,height}, cornersY = {0,0,height,width};
        
        double farthestDistance =  -1;
        int farthestCorner = -1;
        
        for(int i = 0; i<4; i++) {
            double cornerX = cornersX[i];
            double cornerY = cornersY[i];
            
            double dist = Math.sqrt(Math.pow(cornerX - enemyX, 2)+Math.pow(cornerY - enemyY, 2));
            
            if(dist>farthestDistance){
                farthestDistance = dist;
                farthestCorner = i;
            }
        }
        
        double bearingToCorner = Math.atan2(cornersX[farthestCorner] - myX, cornersY[farthestCorner] - myY);

        // Convert the bearing to degrees and adjust relative to the current heading
        double angleToTurn = Utils.normalRelativeAngle(bearingToCorner - myHeading);

        // Turn the robot to face the farthest corner
        setTurnRightRadians(angleToTurn);

        // Move toward the farthest corner
        double distanceToCorner = Math.hypot(cornersX[farthestCorner] - myX, cornersY[farthestCorner] - myY);  // Calculate the distance
        setAhead(distanceToCorner); 
        execute();
    }
    
    
}
