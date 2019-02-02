import java.awt.*;

/**
 * Created by oneiros on 09/02/16.
 */
public abstract class GameEngine implements Runnable {

    public static int DEFAULT_FPS = 60;

    private Thread thread;              // the game thread
    private GameTime timer;             // the time manager
    private GameWindow window;          // the game window
    private long sleep;                 // the nanoseconds between each frame

    public GameEngine(){
        this.setDesiredFramesPerSecond(DEFAULT_FPS);
        this.timer = new GameTime();
        this.window = new GameWindow(this);
    }

    public abstract void init();
    public abstract void input();
    public abstract void update();
    public abstract void draw(Graphics g);

    @Override
    public void run() {
        try {

            // the game loop
            while (GameEngine.this.isRunning()){

                // tick and update fps
                this.timer.tick();

                // input, update and draw
                this.input();
                this.update();
                this.window.draw();

                // sleep
                long timeToWakeUp = this.timer.tock() + this.sleep;
                do {Thread.sleep(0);} while (System.nanoTime() < timeToWakeUp);
            }

        } catch (InterruptedException e) {
            // the game has been stopped
        }
    }

    public void start(){
        this.window.setup();
        this.init();
        this.timer.start();
        this.resume();
        this.window.setVisible(true);
    }

    public void stop(){
        this.pause();
        this.window.dispose();
    }

    public void pause(){
        if (this.thread != null)
            this.thread.interrupt();
        this.thread = null;
        this.timer.stop();
    }

    public void resume(){
        if (!this.isRunning()){
            this.thread = new Thread(this);
            this.thread.setName("Game Thread");
            this.thread.start();
            this.timer.start();
        }
    }

    public void setDesiredFramesPerSecond(double fps){
        this.sleep = (long)(GameTime.ONE_SECOND/fps);
    }

    public GameTime getTimer(){
        return this.timer;
    }

    public GameWindow getWindow() {
        return this.window;
    }

    public boolean isRunning(){
        return this.thread != null;
    }

}
