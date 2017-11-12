public interface RobotEventListener {
    void stepDone(int leg);
    void robotStopped(int steps);
}