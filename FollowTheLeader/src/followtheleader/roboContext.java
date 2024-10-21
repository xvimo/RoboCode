/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package followtheleader;


/**
 *
 * @author cris
 */
public class roboContext{
    private State currentState;
    private FollowTheLeader robot;
    private long time;

   
    public roboContext(FollowTheLeader robot){
        this.robot = robot;
        time = 0;
    }
    public void setState(State state){
        this.currentState = state;
    }
    
    public void executeCurrentState(){
        if(currentState != null){
            currentState.doAction();
        }
    }
    
    public FollowTheLeader getRobot(){
        return robot;
    }
    
    public State getCurrentState(){
        return currentState;
    }

}
