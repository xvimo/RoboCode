/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package followtheleader;
import java.awt.Color;
import java.awt.Graphics2D;
import robocode.MessageEvent;
import robocode.TeamRobot;

/**
 *
 * @author cris
 */
public class FollowTheLeader extends TeamRobot{
    private roboContext context;
    
    @Override
    public void run(){
        context = new roboContext(this);
        context.setState(new handShake(context));
        while(true){
            context.executeCurrentState();
            execute();
        }
    }
    
    @Override
    public void onMessageReceived(MessageEvent event){
        if(context.getCurrentState() instanceof handShake shake){
            shake.onMessageReceived(event);
        }
    }
    
    @Override
    public void onPaint(Graphics2D g){
        if(context.getCurrentState() instanceof handShake shake){
            shake.onPaint(g);
        }
    }
    

}
