// CSD feb 2013 Juansa Sendra

public class Pool4 extends Pool { // kids cannot enter if there are instructors waiting to exit
    private int kids = 0, instructors = 0;
    private int maxKids, maxCap;
    private int insWaiting = 0;

    public void init(int ki, int cap) {
        maxKids = ki;
        maxCap = cap;
    }

    public synchronized void kidSwims() throws InterruptedException {
        while (instructors == 0 || kids >= maxKids * instructors || (kids + instructors) >= maxCap || insWaiting >= 1) {
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
        insWaiting++;
        while (kids > maxKids * (instructors - 1)) {
            log.waitingToRest();
            wait();
        }
        instructors--;
        insWaiting--;
        log.resting();
    }
}
