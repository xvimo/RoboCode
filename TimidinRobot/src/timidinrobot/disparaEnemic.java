/*
*
*  FASE 03
*
 */
package timidinrobot;

import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class disparaEnemic implements State{

    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();
        
        // Fem que pel que queda de combat cerqui enemics (girant el cano també gira el radar)
        while(true){
            robot.setTurnGunRight(10);
            robot.execute();
        }
        
    }

    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        TimidinRobot robot = context.getRobot();
        
        // Obtenim la direccio en la que disparar
        double absoluteBearing = robot.getHeading() + e.getBearing();
        double bearingFromGun = Utils.normalRelativeAngleDegrees(absoluteBearing - robot.getGunHeading());    
        double enemyDistance = e.getDistance();
        robot.turnGunRight(bearingFromGun);
        
        // Calculem la força optima per disparar al enemic
        robot.setFire(Math.min(400/enemyDistance, 6));
        robot.execute();
    }
}
