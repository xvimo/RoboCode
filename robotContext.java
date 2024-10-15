/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package robottimid;

/**
 *
 * @author cris
 */
public class robotContext implements State{
    private State robotState;
    public void setState(State state){
        this.robotState = state;
    }
    public State getState(){
        return this.robotState;
    }
    @Override
    public void doAction() {
        this.robotState.doAction();
    }
    
}
