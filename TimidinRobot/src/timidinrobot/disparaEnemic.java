package timidinrobot;

import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 * Aquesta classe gestiona el comportament del robot quan ha de disparar a un enemic.
 */
public class disparaEnemic implements State {

    /**
     * Fa que el robot giri el canó constantment per buscar enemics durant la resta del combat.
     *
     * @param context el context del robot que conté l'estat actual
     */
    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();
        
        // Fem que pel que queda de combat cerqui enemics (girant el canó també gira el radar)
        while (true) {
            robot.setTurnGunRight(10);
            robot.execute();
        }
    }

    /**
     * Reacciona quan es detecta un robot enemic. Apunta i dispara amb la força adequada segons la distància.
     *
     * @param context el context del robot
     * @param e l'esdeveniment de robot escanejat
     */
    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        TimidinRobot robot = context.getRobot();
        
        // Obtenim la direcció en la que hem de disparar
        double absoluteBearing = robot.getHeading() + e.getBearing();
        double bearingFromGun = Utils.normalRelativeAngleDegrees(absoluteBearing - robot.getGunHeading());
        double enemyDistance = e.getDistance();
        
        // Girem el canó cap a l'enemic
        robot.turnGunRight(bearingFromGun);
        
        // Calculem la força òptima per disparar a l'enemic basat en la distància
        robot.setFire(Math.min(400 / enemyDistance, 6));
        robot.execute();
    }
}
