package timidinrobot;

import robocode.ScannedRobotEvent;

/**
 * Classe que representa el context del robot i emmagatzema l'estat actual.
 * Actua com a punt d'interacció entre el robot i els diferents estats del patró State.
 */
public class robotContext {
    
    /** L'estat actual en què es troba el robot. */
    private State currentState;
    
    /** Referència al robot Timidin. */
    private TimidinRobot robot;
   
    /**
     * Constructor per inicialitzar el context del robot amb el seu estat inicial.
     *
     * @param robot El robot que està sota control d'aquest context
     */
    public robotContext(TimidinRobot robot) {
        this.robot = robot;
        this.currentState = new detectaEnemic();  // Estableix l'estat inicial a "detectaEnemic"
    }
   
    /**
     * Actualitza l'estat actual del robot.
     *
     * @param state el nou estat al qual canviarà el robot
     */
    public void setState(State state) {
        this.currentState = state;
    }
   
    /**
     * Executa l'acció associada amb l'estat actual.
     */
    public void execute() {
        currentState.doAction(this);
    }
   
    /**
     * Gestió de l'esdeveniment on s'ha escanejat un robot enemic.
     * Aquesta funció crida el mètode adequat de l'estat actual per gestionar l'esdeveniment.
     *
     * @param e l'esdeveniment del robot escanejat
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        currentState.onScannedRobot(this, e);  // Passem a la primera fase
    }

    /**
     * Retorna la referència del robot actual.
     *
     * @return el robot Timidin controlat pel context
     */
    public TimidinRobot getRobot() {
        return robot;
    }
}
