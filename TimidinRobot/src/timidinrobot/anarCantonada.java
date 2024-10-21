/*
*
*  FASE 02
*
*/
package timidinrobot;

import robocode.ScannedRobotEvent;
import robocode.TurnCompleteCondition;
import robocode.util.Utils;
import robocode.HitWallEvent;
import robocode.HitRobotEvent;

public class anarCantonada implements State {

    private final double cX; // Coordenada X de la esquina
    private final double cY; // Coordenada Y de la esquina
    private final double adjustmentAngle = Math.toRadians(40);  // Ajuste del ángulo para esquivar obstáculos (15 grados)

    public anarCantonada(double cornerX, double cornerY) {
        this.cX = cornerX;
        this.cY = cornerY;
    }
    
    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();

        robot.stop();
        // Obtener la posición actual del robot
        double robotX = robot.getX();
        double robotY = robot.getY();

        // Calcular el ángulo y distancia hacia la esquina
        double bearingAngle = Math.atan2(cX - robotX, cY - robotY);
        double distanceToCorner = Math.hypot(cX - robotX, cY - robotY);

        // Obtener el encabezado actual del robot
        double myHeading = robot.getHeadingRadians();
        double radarHeading = robot.getRadarHeadingRadians();

        // Calcular el ángulo de giro necesario para apuntar a la esquina
        double angleToTurn = Utils.normalRelativeAngle(bearingAngle - myHeading);

        // El radar debe seguir buscando mientras el robot se mueve
        robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(myHeading - radarHeading));
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));

        // Girar el robot hacia la esquina y moverse en línea recta hacia ella
        robot.setTurnRightRadians(angleToTurn);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));

        // Mover hacia la esquina en línea recta
        robot.setAhead(distanceToCorner);
        robot.execute();
        
        // Fins que no arribi al desti seguim calculant la trajectoria
        while (robot.getX() != cX && robot.getY() != cY) {
            robotX = robot.getX();
            robotY = robot.getY();
            
            // Realcular el ángulo y distancia hacia la esquina
            bearingAngle = Math.atan2(cX - robotX, cY - robotY);
            distanceToCorner = Math.hypot(cX - robotX, cY - robotY);
            
            // Realcular el ángulo de giro necesario para apuntar a la esquina
            myHeading = robot.getHeadingRadians();
            angleToTurn = Utils.normalRelativeAngle(bearingAngle - myHeading);

            // Girar el robot hacia la esquina y moverse en línea recta hacia ella
            robot.setTurnRightRadians(angleToTurn);
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));

            // Mover hacia la esquina en línea recta
            robot.setAhead(distanceToCorner);
            robot.execute();  // Start moving forward
            
            // Assegurem-nos que no s'ha quedat enganxat a cap lloc
            if (robot.getVelocity() == 0) {
                robot.stop();
                // Retroceder para evitar quedarnos pegados
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

    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        TimidinRobot robot = context.getRobot();

        // Si detectamos un robot en el camino, ajustamos el ángulo de aproximación para esquivar
        if (e.getDistance() < 150) {

            double newAngle;

            // Decidimos si giramos a la derecha o izquierda para esquivar, según la posición del enemigo
            if (e.getBearing() > 0) {
                newAngle = adjustmentAngle;  // Ajuste de ángulo hacia la derecha
            } else {
                newAngle = -adjustmentAngle;  // Ajuste de ángulo hacia la izquierda
            }

            // Girar el robot según el ajuste calculado
            robot.setTurnRightRadians(newAngle);
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));

            // Mover el robot en línea recta con el nuevo ángulo
            if (e.getDistance() < 50) {
                robot.setBack(50);
            }
            
            robot.setAhead(150);  // Avanzamos una pequeña distancia para evitar el obstáculo
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));
        }

        // Continuamos escaneando con el radar mientras nos movemos
        robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(robot.getHeadingRadians() - robot.getRadarHeadingRadians()));
        robot.setFireBullet(1);
        robot.execute();
    }

    public void onHitRobot(robotContext context, HitRobotEvent e) {
        TimidinRobot robot = context.getRobot();

        robot.stop();
        // Retroceder para evitar quedarnos pegados
        robot.setBack(150);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));

        double newAngle;
        // Recalcular dirección a la esquina objetivo
        if (e.getBearing() > 0) {
            newAngle = adjustmentAngle;  // Ajuste de ángulo hacia la derecha
        } else {
            newAngle = -adjustmentAngle;  // Ajuste de ángulo hacia la izquierda
        }
        robot.setTurnRightRadians(newAngle);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));
    }

    // Manejo de colisión con la pared
    public void onHitWall(robotContext context, HitWallEvent e) {
        TimidinRobot robot = context.getRobot();
        if (e.getBearing() > -90 && e.getBearing() < 90) {

            robot.stop();
            // Retroceder para evitar quedarnos pegados
            robot.setBack(100);
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));

            double newAngle;
            // Recalcular dirección a la esquina objetivo
            if (e.getBearing() > 0) {
                newAngle = adjustmentAngle;  // Ajuste de ángulo hacia la derecha
            } else {
                newAngle = -adjustmentAngle;  // Ajuste de ángulo hacia la izquierda
            }
            robot.setTurnRightRadians(newAngle);
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));
        } else {
            robot.ahead(100);
        }
    }
}
