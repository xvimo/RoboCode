package followtheleader;

import robocode.DeathEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import robocode.TurnCompleteCondition;
import robocode.util.Utils;

public class congaState implements State {
    private roboContext context;
    private String teamLeader;
    private long time;
    private final int [][] corners = {{100,100},{100,720},{900,720},{900,80}};
    private final double adjustmentAngle = Math.PI / 6;
    boolean dead;
    private int currentCorner = 0;
    
    public congaState(roboContext context, String teamLeader){
        this.context = context;
        this.teamLeader = teamLeader;
        this.dead = false;
        this.time = context.getRobot().getTime();
    }
    
    @Override
    public void doAction() {
        FollowTheLeader robot = context.getRobot();
        if(dead){
            leaderDeath();
            return;
        }

        // Calcular el ángulo y la distancia a la esquina más cercana
        double[][] r = calculateCorner();
        double angleToTurn = r[0][1];
        double distanceToCorner = r[0][0];
        
        // Girar el robot hacia la esquina y moverse en línea recta hacia ella
        robot.setTurnRightRadians(angleToTurn);
        robot.execute();
        robot.waitFor(new TurnCompleteCondition(robot));

        // Mover hacia la esquina en línea recta
        robot.setAhead(distanceToCorner);
        robot.execute();
        
        if(robot.getDistanceRemaining() == 0){
            currentCorner = (currentCorner + 1 ) % corners.length;
        }
    }
    
    public void onScannedRobot(ScannedRobotEvent e) {
        FollowTheLeader robot = context.getRobot();

        // Si detectamos un robot en el camino, ajustamos el ángulo de aproximación para esquivar
        if (e.getDistance() < 125) {
            double newAngle;
            if (e.getBearing() < 0) {
                newAngle = adjustmentAngle;  // Ajuste de ángulo hacia la derecha
            } else {
                newAngle = -adjustmentAngle;  // Ajuste de ángulo hacia la izquierda
            }

            robot.setTurnRightRadians(newAngle);
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));

            // Mover el robot en línea recta con el nuevo ángulo
            if(e.getDistance() < 50) robot.setBack(50);
            robot.setAhead(125);
            robot.execute();
            robot.waitFor(new TurnCompleteCondition(robot));
        }

        // Continuamos escaneando con el radar mientras nos movemos
        robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(robot.getHeadingRadians() - robot.getRadarHeadingRadians()));
        robot.setFireBullet(1);
        robot.execute();
        
        // Aquí podrías enviar mensajes al equipo si es necesario
    }
    
    public double[][] calculateCorner(){
        FollowTheLeader robot = context.getRobot();
        double myX = robot.getX();
        double myY = robot.getY();
      
        double farthestDistance = -1;
        
        // Encontrar la esquina más lejana
        for (int i = 0; i < 4; i++) {
            double cornerX = corners[i][0];  // Corregido el acceso a las coordenadas
            double cornerY = corners[i][1];
            
            double dist = Math.sqrt(Math.pow(cornerX - myX, 2) + Math.pow(cornerY - myY, 2));
            
            if (dist > farthestDistance) {
                farthestDistance = dist;
                currentCorner = i;
            }
        }

        double bearingAngle = Math.atan2(corners[currentCorner][1] - myY, corners[currentCorner][0] - myX);
        double distanceToCorner = Math.hypot(corners[currentCorner][0] - myX, corners[currentCorner][1] - myY);

        double myHeading = robot.getHeadingRadians();
        double angleToTurn = Utils.normalRelativeAngle(bearingAngle - myHeading);

        double r [][] = {{distanceToCorner, angleToTurn}};
        return r;
    }

    public void onMessageReceived(MessageEvent event) {
        if (event.getMessage() instanceof String) {
            String message = (String) event.getMessage();
            if (message.startsWith("Muerte: ")) {
                String deadRobotName = message.split(": ")[1];
                
                if (deadRobotName.equals(teamLeader)) {
                    leaderDeath();
                }
            }
        }
    }

    private void leaderDeath() {
        FollowTheLeader robot = context.getRobot();
        String[] teammates = robot.getTeammates();

        if (teammates != null && teammates.length > 0) {
            for (String teammate : teammates) {
                if (!teammate.equals(teamLeader)) {
                    teamLeader = teammate; // Designar un nuevo TL
                    context.setState(new congaState(context, teamLeader)); // Actualizar el estado
                    break;
                }
            }
        }
    }

    public void onDeath(DeathEvent e){
        dead = true;
        leaderDeath();  // Llamamos a leaderDeath cuando muere
    }
}
