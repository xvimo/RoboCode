package timidinrobot;

import robocode.ScannedRobotEvent;
import robocode.TurnCompleteCondition;
import robocode.util.Utils;
import robocode.HitWallEvent;
import robocode.HitRobotEvent;

/**
 * Aquesta classe implementa l'acció d'anar cap a una cantonada específica dins del camp de batalla.
 */
public class anarCantonada implements State {

    private final double cX; /** Coordenada X de la cantonada */
    private final double cY; /** Coordenada Y de la cantonada */
    private final double adjustmentAngle = Math.toRadians(40);  /** Ajust del gir per esquivar obstacles (40 graus) */

    /**
     * Constructor que assigna la coordenada de la cantonada.
     * 
     * @param cornerX coordenada X de la cantonada
     * @param cornerY coordenada Y de la cantonada
     */
    public anarCantonada(double cornerX, double cornerY) {
        this.cX = cornerX;
        this.cY = cornerY;
    }
    
    /**
     * Fa que el robot es mogui cap a la cantonada assignada, ajustant l'angle i la distància, esquivant obstacles.
     *
     * @param context el context del robot que conté l'estat actual
     */
    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();

        robot.stop();
        /** Obtenim la posició actual del robot */
        double robotX = robot.getX();
        double robotY = robot.getY();

        // Calcular l'angle i la distància fins a la cantonada
        double bearingAngle = Math.atan2(cX - robotX, cY - robotY);
        double distanceToCorner = Math.hypot(cX - robotX, cY - robotY);

        // Obtenim la direcció del robot
        double myHeading = robot.getHeadingRadians();
        double radarHeading = robot.getRadarHeadingRadians();

        // Calcular l'angle necessari per apuntar cap a la cantonada
        double angleToTurn = Utils.normalRelativeAngle(bearingAngle - myHeading);

        // El radar continua cercant mentre el robot es mou
        robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(myHeading - radarHeading));
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));

        // Girar cap a la cantonada i moure's en línia recta
        robot.setTurnRightRadians(angleToTurn);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));

        // Moure's cap a la cantonada
        robot.setAhead(distanceToCorner);
        robot.execute();
        
        // Continuar recalculant mentre no arribi a destí
        while (robot.getX() != cX && robot.getY() != cY) {
            robotX = robot.getX();
            robotY = robot.getY();
            
            // Recalcular l'angle i distància
            bearingAngle = Math.atan2(cX - robotX, cY - robotY);
            distanceToCorner = Math.hypot(cX - robotX, cY - robotY);
            
            // Recalcular l'angle de gir
            myHeading = robot.getHeadingRadians();
            angleToTurn = Utils.normalRelativeAngle(bearingAngle - myHeading);

            // Girar cap a la cantonada i moure's en línia recta
            robot.setTurnRightRadians(angleToTurn);
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));

            // Moure's cap a la cantonada
            robot.setAhead(distanceToCorner);
            robot.execute();
            
            // Comprovar que el robot no s'ha quedat enganxat
            if (robot.getVelocity() == 0) {
                robot.stop();
                // Retrocedir per desenganxar-se
                robot.setBack(50);
                robot.execute();
                robot.waitFor(new TurnCompleteCondition(robot));

                robot.setTurnRightRadians(adjustmentAngle);
                robot.execute();
                robot.waitFor(new TurnCompleteCondition(robot));
            }
        }
        context.setState(new disparaEnemic());
    }

    /**
     * Reacciona quan el robot escaneja un altre robot, ajustant l'angle i l'acció per esquivar-lo si és a prop.
     *
     * @param context el context del robot
     * @param e l'esdeveniment de robot escanejat
     */
    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        TimidinRobot robot = context.getRobot();

        // Si es detecta un robot proper, ajustem l'angle per esquivar
        if (e.getDistance() < 150) {

            double newAngle;

            // Decidim si girar cap a la dreta o esquerra per esquivar segons la posició de l'enemic
            if (e.getBearing() > 0) {
                newAngle = adjustmentAngle;  // Ajust cap a la dreta
            } else {
                newAngle = -adjustmentAngle;  // Ajust cap a l'esquerra
            }

            // Girar el robot segons el nou angle
            robot.setTurnRightRadians(newAngle);
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));

            // Moure's per esquivar
            if (e.getDistance() < 50) {
                robot.setBack(50);
            }
            
            robot.setAhead(150);  // Avançar per evitar l'obstacle
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));
        }

        // Continuar escanejant amb el radar mentre ens movem
        robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(robot.getHeadingRadians() - robot.getRadarHeadingRadians()));
        robot.setFireBullet(1);
        robot.execute();
    }

    /**
     * Es crida quan el robot col·lideix amb un altre robot i ajusta la trajectòria.
     *
     * @param context el context del robot
     * @param e l'esdeveniment de col·lisió amb un altre robot
     */
    public void onHitRobot(robotContext context, HitRobotEvent e) {
        TimidinRobot robot = context.getRobot();

        robot.stop();
        // Retrocedir per desenganxar-se
        robot.setBack(150);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));

        double newAngle;
        // Recalcular la direcció cap a la cantonada
        if (e.getBearing() > 0) {
            newAngle = adjustmentAngle;  // Ajust cap a la dreta
        } else {
            newAngle = -adjustmentAngle;  // Ajust cap a l'esquerra
        }
        robot.setTurnRightRadians(newAngle);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));
    }

    /**
     * Es crida quan el robot col·lideix amb una paret i ajusta la trajectòria.
     *
     * @param context el context del robot
     * @param e l'esdeveniment de col·lisió amb la paret
     */
    public void onHitWall(robotContext context, HitWallEvent e) {
        TimidinRobot robot = context.getRobot();
        if (e.getBearing() > -90 && e.getBearing() < 90) {

            robot.stop();
            // Retrocedir per desenganxar-se de la paret
            robot.setBack(100);
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));

            double newAngle;
            // Recalcular la direcció cap a la cantonada
            if (e.getBearing() > 0) {
                newAngle = adjustmentAngle;  // Ajust cap a la dreta
            } else {
                newAngle = -adjustmentAngle;  // Ajust cap a l'esquerra
            }
            robot.setTurnRightRadians(newAngle);
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));
        } else {
            robot.ahead(100);
        }
    }
}
