/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timidinrobot;

import robocode.ScannedRobotEvent;
import robocode.TurnCompleteCondition;
import robocode.util.Utils;
import robocode.HitWallEvent;
import robocode.HitRobotEvent;
import robocode.MoveCompleteCondition;

public class anarCantonada implements State {
    private final double cX; // Coordenada X de la esquina
    private final double cY; // Coordenada Y de la esquina
    private final double adjustmentAngle = Math.PI / 6;  // Ajuste del ángulo para esquivar obstáculos (15 grados)

    public anarCantonada(double cornerX, double cornerY) {
        this.cX = cornerX;
        this.cY = cornerY;
    }

    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();

        // Obtener la posición actual del robot
        double robotX = robot.getX();
        double robotY = robot.getY();

        // Calcular el ángulo hacia la esquina
        double bearingAngle = Math.atan2(cX - robotX, cY - robotY);
        double distanceToCorner = Math.hypot(cX-robotX, cY - robotY);

        // Obtener el encabezado actual del robot
        double myHeading = robot.getHeadingRadians();
        double radarHeading = robot.getRadarHeadingRadians();

        // Calcular el ángulo de giro necesario para apuntar a la esquina
        double angleToTurn = Utils.normalRelativeAngle(bearingAngle - myHeading);

        // Girar el robot hacia la esquina y moverse en línea recta hacia ella
        robot.setTurnRightRadians(angleToTurn);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));

        // Mover hacia la esquina en línea recta
        robot.setAhead(distanceToCorner);
        robot.execute();

        // El radar debe seguir buscando mientras el robot se mueve
        robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(bearingAngle - radarHeading));
        robot.execute();
        while (robot.getDistanceRemaining() > 0) {
            robotX = robot.getX();
            robotY = robot.getY();
            bearingAngle = Math.atan2(cX - robotX, cY - robotY);
            distanceToCorner = Math.hypot(cX - robotX, cY - robotY);

            myHeading = robot.getHeadingRadians();
            angleToTurn = Utils.normalRelativeAngle(bearingAngle - myHeading);

            // Turn the robot to face the farthest corner and move towards it
            robot.setTurnRightRadians(angleToTurn);
            robot.execute();  // Start turning
            // Wait for the robot to complete its turn
            robot.waitFor(new TurnCompleteCondition(robot));

            // Turn the radar and execute it, then wait for it to complete
            // Move ahead by the specified distance, then wait for it to complete
            robot.setAhead(distanceToCorner);
            robot.execute();  // Start moving forward
            //robot.waitFor(new MoveCompleteCondition(robot));
        }
        context.setState(new disparaEnemic());
    }

    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        TimidinRobot robot = context.getRobot();

        // Si detectamos un robot en el camino, ajustamos el ángulo de aproximación para esquivar
        if (e.getDistance() < 125) {
            
            double newAngle;

            // Decidimos si giramos a la derecha o izquierda para esquivar, según la posición del enemigo
            if (e.getBearing() < 0) {
                newAngle = adjustmentAngle;  // Ajuste de ángulo hacia la derecha
            } else {
                newAngle = -adjustmentAngle;  // Ajuste de ángulo hacia la izquierda
            }

            // Girar el robot según el ajuste calculado
            robot.setTurnRightRadians(newAngle);
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));

            // Mover el robot en línea recta con el nuevo ángulo
            if(e.getDistance()< 50 ) robot.setBack(50);
            robot.setAhead(125);  // Avanzamos una pequeña distancia para evitar el obstáculo
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

        // Retroceder para evitar quedarnos pegados
        robot.setBack(100);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));

        // Recalcular dirección a la esquina objetivo
        double bearingAngle = Math.atan2(cX - robot.getX(), cY - robot.getY());
        double angleToTurn = Utils.normalRelativeAngle(bearingAngle - robot.getHeadingRadians());
        robot.setTurnRightRadians(angleToTurn);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));
        robot.setAhead(100);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));
        
        
    }

    // Manejo de colisión con la pared
   
    public void onHitWall(robotContext context, HitWallEvent e) {
        TimidinRobot robot = context.getRobot();
        if(e.getBearing() > -90 && e.getBearing() < 90){
        
        // Retroceder para evitar quedarnos atascados en la pared
        robot.setBack(100);
        robot.execute();
        robot.waitFor(new MoveCompleteCondition(robot));

        // Recalcular dirección a la esquina objetivo
        double bearingAngle = Math.atan2(cX - robot.getX(), cY - robot.getY());
        double angleToTurn = Utils.normalRelativeAngle(bearingAngle - robot.getHeadingRadians());
        robot.setTurnRightRadians(angleToTurn);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));
        robot.setAhead(100);
        robot.execute();
        robot.waitFor(new MoveCompleteCondition(robot));
        }else{
            robot.ahead(100);
        }
    }
}
