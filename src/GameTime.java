import java.util.concurrent.TimeUnit;

/**
 * Created by oneiros on 07/03/16.
 */
public class GameTime {

    public static double ONE_SECOND = TimeUnit.SECONDS.toNanos(1);

    private long start;
    private long pause;
    private long[] ticks = new long[1000];
    private int first, last;
    private int fps;

    public GameTime(){}

    public void start(){
        if (this.pause > 0 && this.start > 0){
            this.start = System.nanoTime() - (this.pause - this.start);
            this.pause = 0;
        } else {
            this.start = System.nanoTime();
        }
    }

    public void stop(){
        this.pause = System.nanoTime();
    }

    public void tick(){
        ticks[last] = System.nanoTime();
        while (ticks[last] - ticks[first] > ONE_SECOND){
            first = circular(first + 1);
            fps--;
        }
        last = circular(last + 1);
        fps++;
    }

    public long tock(){
        return ticks[circular(last-1)];
    }

    public long tockDiff(){
        long prev = ticks[circular(last-2)];
        return (prev == 0) ? 0 : tock() - prev;
    }

    public double tockDiff(TimeUnit timeUnit){
        return (double) tockDiff() / timeUnit.toNanos(1);
    }

    public long getTime(){
        return System.nanoTime() - this.start;
    }

    public double getTime(TimeUnit timeUnit){
        return (double) getTime() / timeUnit.toNanos(1);
    }

    public int getFPS(){
        return this.fps;
    }

    private int circular(int n){
        return Math.floorMod(n, ticks.length);
    }
}
