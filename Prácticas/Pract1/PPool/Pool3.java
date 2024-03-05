// CSD feb 2015 Juansa Sendra

public class Pool3 extends Pool { // max capacity
    private int kids = 0, instructors = 0;
    private int maxKids, maxCap;

    public void init(int ki, int cap) {
        maxKids = ki;
        maxCap = cap;
    }

    public synchronized void kidSwims() throws InterruptedException {
        while (instructors == 0 || kids >= maxKids * instructors || (kids + 1 + instructors) > maxCap) {
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
        while ((kids + 1 + instructors) > maxCap) {
            log.waitingToSwim();
            wait();
        }
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
