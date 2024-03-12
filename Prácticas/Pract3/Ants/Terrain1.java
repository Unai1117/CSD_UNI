import java.util.concurrent.locks.*;

public class Terrain1 implements Terrain {
    Viewer v;
    Lock l;
    Condition cond; 

    public Terrain1(int t, int ants, int movs, String msg){
        v = new Viewer(t, ants, movs, msg);
        l = new ReentrantLock();
        cond = l.newCondition();
    }

    public void hi (int a){
        l.lock();
        try {
            v.hi(a);
        } catch (Exception e) {}
        finally{
            l.unlock();
        }
    }

    public void bye(int a){
        l.lock();
        try{
            cond.signalAll();
            v.bye(a);
        }
        finally{
            l.unlock();
        }
    }

    public void move (int a) throws InterruptedException {
        l.lock();
        try{
            v.turn(a); 
            Pos des = v.dest(a);
            while (v.occupied(des)){
                cond.await();
                v.retry(a);
            }
            v.go(a);
            cond.signalAll();
        } finally {
            l.unlock();
        }
    }
}