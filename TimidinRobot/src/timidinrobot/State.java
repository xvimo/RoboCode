package timidinrobot;
import robocode.ScannedRobotEvent;


public interface State {

    public void doAction(robotContext context);
    void onScannedRobot(robotContext context, ScannedRobotEvent e);
}
