public class Leg extends Thread {

    private LegCallback callback;

    public Leg(LegCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        if (callback != null) {
                callback.legMoved(this);
        }
    }
}