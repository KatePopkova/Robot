import java.util.ArrayList;

public class Robot implements LegCallback {

    private Object synchObj = new Object();
    private RobotCallback callback;
    private int steps = 0;
    private boolean flag = false;

    private ArrayList<Leg> legs;
    private double distance;

    public Robot(RobotCallback callback, int legsCount, double distance) {
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
                callback.resultGot("The robot moved with ".concat(Integer.toString(legs.indexOf(leg) + 1).concat(" leg.\n")));
                callback.resultGot(distancePassed());
            }
            else if (!flag) {
                callback.resultGot(distancePassed());
                callback.resultGot(Integer.toString(steps).concat(" steps have been done. \n"));
                flag = true;
            }
            else {
               callback.resultGot("");
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