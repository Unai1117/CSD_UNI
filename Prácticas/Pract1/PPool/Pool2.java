// CSD feb 2015 Juansa Sendra

public class Pool2 extends Pool { // max kids/instructor
    private int kids = 0, instructors = 0;
    private int maxKids;

    public void init(int ki, int cap) {
        maxKids = ki;
    }

    public synchronized void kidSwims() throws InterruptedException {
        while (instructors == 0 || kids >= maxKids * instructors) {
            log.waitingToSwim();
            wait();
        }
        kids++;
        log.swimming();
    }

    public synchronized void kidRests() {
        log.resting();
        kids--;
        notifyAll();
    }

    public synchronized void instructorSwims() throws InterruptedException {
        log.swimming();
        instructors++;
        notifyAll();
    }

    public synchronized void instructorRests() throws InterruptedException {
        while (kids > maxKids * (instructors - 1)) {
            log.waitingToRest();
            wait();
        }
        instructors--;
        notifyAll();
        log.resting();
    }
}
