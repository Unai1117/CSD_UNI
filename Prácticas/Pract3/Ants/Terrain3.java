import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

public class Terrain3 implements Terrain {
    Viewer v; 
    Lock l; 
    Condition[][] cond;

    public Terrain3 (int t, int ants, int movs, String msg){
        v = new Viewer(t, ants, movs, msg); 
        l = new ReentrantLock();
        cond = new Condition[t][t];
        for(int i = 0; i < t; i++){
            for(int j = 0; j < t; j++){
                cond[i][j] = l.newCondition();
            }
        }
    }

    public void hi (int a){
        l.lock();
        try{
            v.hi(a);
        } finally{
            l.unlock();
        }
    }

    public void bye (int a){
        Pos p = v.getPos(a);
        l.lock();
        try{
            bye(a);
            cond[p.x][p.y].signalAll();
        } finally {
            l.unlock();
        }
    }

    public void move (int a) throws InterruptedException {
        Pos p = v.getPos(a);
        l.lock();
        try{
            v.turn(a);
            Pos des = v.dest(a);
            while (v.occupied(des)) {
                if(cond[des.x][des.y].await(300, TimeUnit.MILLISECONDS)){
                    v.retry(a);
                } else {
                    v.chgDir(a);
                    des = v.dest(a);
                    v.retry(a);
                }
            }
            v.go(a);
            cond[p.x][p.y].signalAll();
        } finally {l.unlock();}
    }
}
