package EhNew.util;

/**
 * @author Abhishek
 */
public class TickManager {
    private final Thread ticker;//Notified whenever this thread goes to sleep (or Manager gets paused)
    private int tickDelay;
    private volatile int state;
    private final CallBack callback;//Wait() Called by this ticker thread
    
    public static final int STATE_NOT_STARTED = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_ENDED = 3;
    public static final int STATE_RUNNING_ONCE = 4;
    
    public TickManager(Runnable r, String name){
        callback  = new CallBack(r);
        ticker = new Thread(callback, name);
        tickDelay = 1000/60;
        state = 0;
    }

    public void setTickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
    }
    
    public void startPaused(){
        if(state != 0) return;
        state = 2;
        ticker.start();
    }
    public void start(){
        if(state != 0) return;
        state = 1;
        ticker.start();
    }
    public void pause(){
        if(state != 1 && state != 4) return;
        state = 2;
        System.out.println(Thread.currentThread().getName()+": State of "+ticker.getName()+" Changed to Pause");
    }
    public void stop(){
        if(state != 1) return;
        state = 3;
    }
    public void resume(){
        if(state != 2) return;
        if(Thread.currentThread().getName().equals(ticker.getName())){
            System.out.println(ticker.getName() + " Called for resuming itself, resuming in a safe way.");
            state = 1;
            return;
            //If Renderer Thread has come here from a callback Trip, it will return to the gameLoop in it's
            //TickManager.callback.run() function safely.
        }
        state = 1;
        System.out.println(Thread.currentThread().getName()+": State of "+ticker.getName()+" Resumed to 1");
        synchronized(callback){
            System.out.println(Thread.currentThread().getName() + ": Notifying Object " + callback);
            callback.notifyAll();
        }
    }
    
    public void runOnce(){
        System.out.println(Thread.currentThread().getName()+": Run Once Called");
        if(state == 3 || state == 1) return;
        if(state == 0){
            System.out.println(Thread.currentThread().getName()+": Initiating Run Once Machanic...");
            state = 4;
            ticker.start();
        }
        state = 4;
        synchronized (callback) {
            System.out.println(Thread.currentThread().getName() + ": Notifying Object For Run Once" + callback);
            callback.notifyAll();
        }
    }
    public void waitTillThreadPauses(){
        if(state == 2 || state == 3) return;
        System.out.println(Thread.currentThread().getName()+": Determined Thread "+ticker.getName()+" isn't Paused or Dead");
        try{
            synchronized(ticker){
                System.out.println(Thread.currentThread().getName()+": Initiating Wait on "+ticker);
                ticker.wait();
                System.out.println(Thread.currentThread().getName()+": Wait inturrupted On Object "+ticker);
            }
        } catch(InterruptedException ex){}
    }
    
    public Thread getTicker() {
        return ticker;
    }
    public void setCallback(Runnable r){
        callback.r = r;
    }
    public int getlTickDelay() {
        return tickDelay;
    }
    public int getState() {
        return state;
    }
    public CallBack getCallback(){
        return callback;
    }
    
    private class CallBack implements Runnable{
        Runnable r;
        
        CallBack(Runnable r){
            this.r = r;
        }
        @Override
        public void run() {
            while(state != 3)
            {
                if(state == 1){
                    r.run();
                }
                if(state == 2){
                    try {
                        synchronized(ticker){
                            System.out.println(Thread.currentThread().getName()+": Notifying Object: " + ticker+" before Pausing");
                            ticker.notifyAll();
                        }
                        synchronized(this){
                            System.out.println(Thread.currentThread().getName()+": Initiating Pause... On " + this);
                            this.wait();
                            System.out.println(Thread.currentThread().getName()+": Wait Inpurrupted On" + this + " with state " + state);
                        }
                    } catch (InterruptedException e) {}
                }
                if(state == 4){
                    System.out.println(Thread.currentThread().getName()+": Running Run Once Function");
                    r.run();
                    System.out.println(Thread.currentThread().getName()+": Run Once Function Successfully completed");
                    state = 2;
                }
                try{
                    if(state != 1) System.out.println(getTicker().getName()+": Thread Initiating Sleep with state " + state);
                    Thread.sleep(tickDelay);
                } catch (InterruptedException e) {}
            }
        }
    }
}