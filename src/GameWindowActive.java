import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 * Created by oneiros on 09/08/16.
 */
public class GameWindowActive {

    static { System.setProperty("sun.java2d.opengl", "true"); }
    public static Dimension DEFAULT_FRAME_SIZE = new Dimension(800, 600);

    private GameEngine engine;
    private JFrame window;
    private Canvas canvas;

    private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private GraphicsDevice gd = ge.getDefaultScreenDevice();
    private GraphicsConfiguration gc = gd.getDefaultConfiguration();
    private BufferedImage image;
    private BufferStrategy buffer;
    
    protected GameWindowActive(GameEngine engine){
        this.engine = engine;
        this.window = new JFrame();
    }

    protected void setup(){
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.window.setIgnoreRepaint(true);
        this.window.setResizable(false);
        this.setTitle("Game Engine");

        this.canvas = new Canvas();
        this.canvas.setIgnoreRepaint(true);

        this.window.add(this.canvas);
        this.window.pack();
        this.window.setVisible(true);

        this.canvas.createBufferStrategy(2);
        this.buffer = this.canvas.getBufferStrategy();
        this.setSize(DEFAULT_FRAME_SIZE);
        this.setCentered();
    }

    public void draw(){
        if (this.canvas != null) {
            Graphics2D g2D = (Graphics2D) this.image.getGraphics();
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2D.setClip(0, 0, this.getSize().width, this.getSize().height);
            this.engine.draw(g2D);
            Graphics graphics = this.buffer.getDrawGraphics();
            graphics.drawImage(this.image, 0, 0, null);
            if(!this.buffer.contentsLost()) this.buffer.show();
            Thread.yield();
            graphics.dispose();
            g2D.dispose();
        }
    }

    public void setVisible(boolean b){
        this.window.setVisible(b);
    }

    public boolean isVisible(){
        return this.window.isVisible();
    }

    public void dispose(){
        this.window.dispose();
    }

    public void setSize(Dimension size){
        Insets i = this.window.getInsets();
        this.window.setSize(size.width + i.left + i.right, size.height + i.top + i.bottom);
        this.canvas.setSize(size);
        this.image = gc.createCompatibleImage(size.width, size.height, Transparency.TRANSLUCENT);
    }

    public Dimension getSize() {
        return this.canvas.getSize();
    }

    public void setCentered(){
        this.setLocation(null);
    }

    public void setLocation(Point point){
        if (point == null){
            this.window.setLocationRelativeTo(null);
        } else {
            this.window.setLocation(point);
        }
    }

    public void setTitle(String s){
        this.window.setTitle(s);
    }
}
