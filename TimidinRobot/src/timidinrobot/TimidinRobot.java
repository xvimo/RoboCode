package timidinrobot;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

/**
 * Classe principal del robot Timidin, que utilitza un context per gestionar els seus estats segons el patró de disseny "State".
 * Controla el comportament del robot i els esdeveniments durant la batalla.
 * 
 * @author xavi
 * @author cris
 */
public class TimidinRobot extends AdvancedRobot {
    
    /** El context del robot, que gestiona els estats segons el patró "State". */
    private robotContext context;
   
    /**
     * Mètode principal que s'executa quan el robot comença la seva activitat.
     * Inicialitza el context i entra en un bucle d'execució continu.
     */
    @Override
    public void run() {
        context = new robotContext(this);  // Inicialitza el context amb aquest robot
        
        // Bucle d'execució principal que crida l'estat actual dins del context
        while (true) {
            context.execute();
        }
    }
   
    /**
     * Mètode que gestiona l'esdeveniment de detecció d'un robot enemic.
     * Quan es detecta un robot, es delega l'acció al context per determinar la resposta segons l'estat actual.
     *
     * @param e l'esdeveniment que conté informació sobre el robot escanejat
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        context.onScannedRobot(e);
    }
}
