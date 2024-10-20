/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timidinrobot;

import java.awt.Color;
import robocode.ScannedRobotEvent;
import robocode.TurnCompleteCondition;
import robocode.util.Utils;

public class anarCantonada implements State {

    private double bearingToCorner, distanceToCorner;
    private double cornerX, cornerY;
    private double offset;

    public anarCantonada(double X, double Y) {
        this.cornerX = X;
        this.cornerY = Y;
    }

    @Override
    public void doAction(robotContext context) {
        TimidinRobot robot = context.getRobot();

        robot.stop();

        double myHeading = robot.getHeadingRadians();
        double radarHeading = robot.getRadarHeadingRadians();
        double angleToRadar = Utils.normalRelativeAngle(myHeading - radarHeading);

        robot.setTurnRadarRightRadians(angleToRadar);
        robot.execute();  // Start radar turn
        robot.waitFor(new TurnCompleteCondition(robot));

        double myX = robot.getX(), myY = robot.getY();
        bearingToCorner = Math.atan2(cornerX - myX, cornerY - myY);
        distanceToCorner = Math.hypot(cornerX - myX, cornerY - myY);

        myHeading = robot.getHeadingRadians();
        double angleToTurn = Utils.normalRelativeAngle(bearingToCorner - myHeading);

        // Turn the robot to face the farthest corner and move towards it
        robot.setTurnRightRadians(angleToTurn);
        robot.execute();  // Start turning
        // Wait for the robot to complete its turn
        robot.waitFor(new TurnCompleteCondition(robot));

        // Turn the radar and execute it, then wait for it to complete
        // Move ahead by the specified distance, then wait for it to complete
        robot.setAhead(distanceToCorner);
        robot.execute();  // Start moving forward

        while (robot.getDistanceRemaining() > 0) {
            myX = robot.getX();
            myY = robot.getY();
            bearingToCorner = Math.atan2(cornerX - myX, cornerY - myY);
            distanceToCorner = Math.hypot(cornerX - myX, cornerY - myY);

            myHeading = robot.getHeadingRadians();
            angleToTurn = Utils.normalRelativeAngle(bearingToCorner - myHeading);

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

        // After moving, transition back to scanning state
        context.setState(new disparaEnemic());
    }

    @Override
    public void onScannedRobot(robotContext context, ScannedRobotEvent e) {
        // Ignore scanning events in the moving state
        TimidinRobot robot = context.getRobot();
        double eDist = e.getDistance();
        double eBearing = e.getBearing();

        if (eDist < 150 && eBearing < 45) {
            robot.setFire(15);

            if (eBearing < 0) {
                offset = -40;
            } else if (eBearing > 0) {
                offset = 40;
            } else {
                offset = 40;
            }
            robot.setTurnRight(offset);
            //robot.setAhead(100);

            double myX = robot.getX(), myY = robot.getY();
            bearingToCorner = Math.atan2(cornerX - myX, cornerY - myY);
            distanceToCorner = Math.hypot(cornerX - myX, cornerY - myY);

            while (robot.getTurnRemainingRadians() != 0) {
                robot.execute();
            }

            /*if (robot.getX() < 50 || robot.getX() > robot.getBattleFieldWidth() - 50 || robot.getY() < 50 || robot.getY() > robot.getBattleFieldHeight() - 50) {
                // Mensaje de depuración
                System.out.println("Cerca de la pared, cambiando dirección");
                // Retrocede
                robot.setBack(50);
                // Gira 90 grados
                robot.setTurnRight(90);
            } else {
                // Lógica de movimiento hacia el enemigo
                robot.setTurnRight(e.getBearing());
                robot.setAhead(e.getDistance());
            }*/
        } else {
            offset = 0;
        }

        robot.execute();
    }
}
