/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
        context.setState(new anarCantonada(e));
    }
    
}

