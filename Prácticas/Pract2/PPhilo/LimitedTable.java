// CSD Mar 2013 Juansa Sendra

public class LimitedTable extends RegularTable { // max 4 in dinning-room
    public LimitedTable(StateManager state) {
        super(state);
    }

    private int nfil = 0;

    public synchronized void enter(int id) throws InterruptedException {
        while (nfil >= 4) {
            state.wenter(id);
            wait();
        }

        nfil++;
        state.enter(id);
        notifyAll();
    }

    public synchronized void exit(int id) {
        nfil--;
        state.exit(id);
        notifyAll();
    }
}
