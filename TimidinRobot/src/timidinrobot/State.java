/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package timidinrobot;
import robocode.ScannedRobotEvent;


public interface State {

    public void doAction(robotContext context);
    void onScannedRobot(robotContext context, ScannedRobotEvent e);
}
