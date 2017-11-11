import java.util.ArrayList;

public class Robot implements LegCallback {

    private Object synchObj = new Object();
    private RobotEventListener callback;
    private int steps = 0;
    private boolean isAllStepsDone = false;

    private ArrayList<Leg> legs;
    private double distance;

    public Robot(RobotEventListener callback, int legsCount, double distance) {
        this.callback = callback;
        this.distance = distance;
        System.out.println(distance);
        legs = new ArrayList<>(legsCount);
        for (int i = 1; i <= legsCount; i++) {
            Leg leg = new Leg(this);
            legs.add(leg);
            leg.start();
            synchronized (synchObj) {
                try {
                    synchObj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void legMoved(Leg leg) {
        synchronized (synchObj) {
            if ((callback != null) && (distance > 0)) {
                callback.stepDone("The robot moved with ".concat(Integer.toString(legs.indexOf(leg) + 1).concat(" leg.\n")));
                callback.stepDone(distancePassed());
            }
            else {
                if(!isAllStepsDone) {
                    callback.robotStopped(distancePassed());
                    callback.robotStopped(Integer.toString(steps).concat(" steps have been done. \n"));
                    isAllStepsDone = true;
                }
            }
            synchObj.notify();
        }
    }

    private String distancePassed() {
        if(distance > 0) {
            distance = distance - (0.5 + Math.random() * 1);
            System.out.println(distance);
            steps++;
            return "";
        }
        else {
            return "Distance has been passed.\n";
        }
    }
}