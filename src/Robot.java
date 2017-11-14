import java.util.ArrayList;

public class Robot implements LegCallback {

    private Object synchObj = new Object();
    private RobotEventListener callback;
    private int steps = 0;

    private ArrayList<Leg> legs;
    private double distance;

    public Robot(RobotEventListener callback, int legsCount, double distance) {
        this.callback = callback;
        this.distance = distance;
        legs = new ArrayList<>(legsCount);
        createLeg(legsCount);
    }

    private void createLeg(int legsCount) {
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
        if (distance > (double) 0) {
            legs.clear();
            createLeg(legsCount);
        }
        else {
            callback.robotStopped(steps);
        }
    }

    @Override
    public void legMoved(Leg leg) {
        synchronized (synchObj) {
            System.out.println(Thread.currentThread().toString() + "\n");
            System.out.print(distance + "\n");
            if (callback != null & distance > (double) 0) {
                callback.stepDone(legs.indexOf(leg) + 1);
                distancePassed();
            }
            synchObj.notify();
        }
    }

    private void distancePassed() {
        distance = distance - (0.5 + Math.random() * 1);
        steps++;
    }
}