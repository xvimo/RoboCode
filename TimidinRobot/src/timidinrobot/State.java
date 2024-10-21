package timidinrobot;

import robocode.ScannedRobotEvent;

/**
 * Interfície que defineix el comportament dels diferents estats del robot segons el patró de disseny "State".
 * Cada estat ha d'implementar les accions que el robot ha de dur a terme i la resposta davant d'esdeveniments d'escaneig de robots enemics.
 */
public interface State {

    /**
     * Executa l'acció associada a l'estat actual del robot.
     * 
     * @param context El context actual del robot que permet canviar d'estat i accedir a les funcionalitats del robot.
     */
    public void doAction(robotContext context);
    
    /**
     * Gestiona l'esdeveniment quan el radar del robot detecta un altre robot.
     * 
     * @param context El context actual del robot que permet canviar d'estat i accedir a les funcionalitats del robot.
     * @param e L'esdeveniment que conté informació sobre el robot detectat (distància, angle, etc.).
     */
    void onScannedRobot(robotContext context, ScannedRobotEvent e);
}
