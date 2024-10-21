/*
*
*  FASE 01
*
*/
package timidinrobot;

import robocode.ScannedRobotEvent;

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

        // Calculem l'angle absolut sobre l'enemic
        double absoluteBearing = myHeading + Math.toRadians(enemyBearing);

        // CCalculem la posicio del enemic mitjançant trigonometria
        double enemyX = myX + distance * Math.sin(absoluteBearing);
        double enemyY = myY + distance * Math.cos(absoluteBearing);
        
        double height = robot.getBattleFieldHeight(), width = robot.getBattleFieldWidth();
        
        double[] cornersX = {25, width-25, 25, width-25}, cornersY = {25, 25, height-25, height-25};
        
        double farthestDistance = -1;
        int farthestCorner = -1;
        
        // Trobem la cantonada més llunyana al enemic
        for (int i = 0; i < 4; i++) {
            double cornerX = cornersX[i];
            double cornerY = cornersY[i];
            
            double dist = Math.sqrt(Math.pow(cornerX - enemyX, 2) + Math.pow(cornerY - enemyY, 2));
            
            if (dist > farthestDistance) {
                farthestDistance = dist;
                farthestCorner = i;
            }
        }
        
        // Enviem les coordenades de la cantonada més llunyana a la segona fase
        context.setState(new anarCantonada(cornersX[farthestCorner], cornersY[farthestCorner]));
        
    }
    
}

