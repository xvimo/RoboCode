/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tecnoBot;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.*;
import robocode.MessageEvent;

/**
 *
 * @author cris
 */
public class handShake implements State{
    private roboContext context;
    private Map<String,double[]> teamPositions = new HashMap<>();
    private String teamLeader;

    public handShake(roboContext context){
        this.context = context;
    }
    @Override
    public void doAction() {
        tecnoBot robot = context.getRobot();
        sentPosition();
        if(teamPositions.size() >= robot.getTeammates().length){
            calculateHierarchy();
        }
        System.out.println("Fin");
        context.setState(new congaState(context,teamLeader));
    }
    public void sentPosition(){
        tecnoBot robot = context.getRobot();
        try {
            robot.broadcastMessage(new double[] {robot.getX(),robot.getY()});
        } catch (IOException ex) {
        }
    }
    
    public void onMessageReceived(MessageEvent e){
        tecnoBot robot = context.getRobot();
        if(e.getMessage() instanceof double[]){
            double[] position = (double []) e.getMessage();
            teamPositions.put(e.getSender(),position);
            
            if(teamPositions.size() >= robot.getTeammates().length){
                calculateHierarchy();
            }
        }
    }

    public void calculateHierarchy() {
        tecnoBot robot = context.getRobot();
        List<String> robots = new ArrayList <>(teamPositions.keySet());
        teamLeader = robots.get((int) ((Math.random() * robots.size())));
        
        List<robotInfo> info = new ArrayList <>();
        double[] leaderPos = teamPositions.get(teamLeader);
        
        for(String robotName : teamPositions.keySet()){
            if(!robotName.equals(teamLeader)){
                double[] position = teamPositions.get(robotName);
                double distanceL = Math.sqrt(Math.pow(leaderPos[0] - position[0], 2) + Math.pow(leaderPos[1] - position[1], 2));
                info.add(new robotInfo(robotName,distanceL));
            }
            
            info.sort(Comparator.comparingDouble(robotInfo::getDistance));
        }
        System.out.println("Teamleader: " + teamLeader + " " +robot.getName());
        
    }
    public void onPaint(Graphics2D g){
        tecnoBot robot = context.getRobot();
        if(teamLeader != null && teamLeader.equals(robot.getName())){
            g.setColor(Color.YELLOW);
            g.drawOval((int)(robot.getX()-50), (int)(robot.getY()-50), 100, 100);
        }
    }


    private class robotInfo {
        private String robotName;
        private double distance;
        
        public robotInfo(String robotName,double distance) {
            this.robotName = robotName;
            this.distance = distance;
            
        }
        
        public String getRobotName(){
            return this.robotName;
        }
        
        public Double getDistance(){
            return this.distance;
        }
    }
    
}

