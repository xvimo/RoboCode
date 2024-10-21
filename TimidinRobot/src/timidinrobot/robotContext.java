package timidinrobot;

import robocode.ScannedRobotEvent;

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
        currentState.onScannedRobot(this, e);  // Passem a la primera fase
    }

    public TimidinRobot getRobot() {
        return robot;
    }
}
