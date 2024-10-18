/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package robottimid;
import robocode.AdvancedRobot;

/**
 *
 * @author cris
 */
public class RobotTimid extends AdvancedRobot{

    private String state="";
    public void mover(){
        
    }
    
    public void setState(String state){
        this.state = state;
    }
    public void doAction(){
        if(state.equalsIgnoreCase("detectaEnemic")){
            
        }else if(state.equalsIgnoreCase("anarCantonada")){
        
        }else if(state.equalsIgnoreCase("disparaEnemic")){
        
        }
    }
}

