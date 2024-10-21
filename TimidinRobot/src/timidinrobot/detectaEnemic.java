package timidinrobot;

import robocode.ScannedRobotEvent;

/**
 * Aquesta classe detecta l'enemic i calcula la cantonada més llunyana per moure-s'hi.
 */
public class detectaEnemic implements State {

    /**
     * Fa que el robot giri el radar a l'esquerra buscant un enemic.
     *
     * @param context el context del robot que conté l'estat actual
     */
    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();
        robot.setTurnRadarLeft(90);
        robot.execute();
    }

    /**
     * Reacciona quan el robot detecta un altre robot, calcula la posició de l'enemic i la cantonada més llunyana d'aquest.
     * Posteriorment, envia les coordenades de la cantonada més llunyana a la segona fase.
     *
     * @param context el context del robot
     * @param e l'esdeveniment de robot escanejat
     */
    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        TimidinRobot robot = context.getRobot();
        
        double enemyBearing = e.getBearing();
        double distance = e.getDistance();
        
        double myX = robot.getX(), myY = robot.getY();
        double myHeading = robot.getHeadingRadians();

        // Calculem l'angle absolut respecte a l'enemic
        double absoluteBearing = myHeading + Math.toRadians(enemyBearing);

        // Calculem la posició de l'enemic utilitzant trigonometria
        double enemyX = myX + distance * Math.sin(absoluteBearing);
        double enemyY = myY + distance * Math.cos(absoluteBearing);
        
        double height = robot.getBattleFieldHeight(), width = robot.getBattleFieldWidth();
        
        double[] cornersX = {25, width - 25, 25, width - 25}; /** Coordenades X de les cantonades */
        double[] cornersY = {25, 25, height - 25, height - 25}; /** Coordenades Y de les cantonades */
        
        double farthestDistance = -1;
        int farthestCorner = -1;
        
        // Troba la cantonada més llunyana a l'enemic
        for (int i = 0; i < 4; i++) {
            double cornerX = cornersX[i];
            double cornerY = cornersY[i];
            
            double dist = Math.sqrt(Math.pow(cornerX - enemyX, 2) + Math.pow(cornerY - enemyY, 2));
            
            if (dist > farthestDistance) {
                farthestDistance = dist;
                farthestCorner = i;
            }
        }
        
        // Enviar les coordenades de la cantonada més llunyana a la segona fase
        context.setState(new anarCantonada(cornersX[farthestCorner], cornersY[farthestCorner]));
    }
}
