/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package timidinrobot;

import robocode.ScannedRobotEvent;

/**
 *
 * @author cris
 */
public class robotContext{
   private State currentState;
   private TimidinRobot robot;
   
   public robotContext(TimidinRobot robot){
       this.robot = robot;
       this.currentState = new detectaEnemic();
   }
   
   public void setState(State state){
       this.currentState = state;
   }
   
   public void execute(){
       currentState.doAction(this);
   }
   
    public void onScannedRobot(ScannedRobotEvent e) {
        currentState.onScannedRobot(this, e);  // Delegate to the current state's scanning logic
    }

    public TimidinRobot getRobot() {
        return robot;
    }
}
