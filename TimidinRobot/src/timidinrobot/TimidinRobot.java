package timidinrobot;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;


/**
 * @author xavi
 * @author cris
 **/
public class TimidinRobot extends AdvancedRobot {
   private robotContext context;
   
   @Override
   public void run(){
       context = new robotContext(this);
       
       while(true){
           context.execute();
       }
   }
   
   @Override
   public void onScannedRobot(ScannedRobotEvent e){
       context.onScannedRobot(e);
   }
}
