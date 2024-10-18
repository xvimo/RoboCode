/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package timidinrobot;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;


/**
 *
 * @author xavi
 * @author cris
 */
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
