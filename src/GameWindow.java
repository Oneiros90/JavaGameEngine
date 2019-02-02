import javax.swing.*;
import java.awt.*;

/**
 * Created by oneiros on 09/08/16.
 */
public class GameWindow {

    static { System.setProperty("sun.java2d.opengl", "true"); }
    public static Dimension DEFAULT_FRAME_SIZE = new Dimension(800, 600);

    private GameEngine engine;
    private JFrame window;
    private JPanel canvas;
    
    protected GameWindow(GameEngine engine){
        this.engine = engine;
        this.window = new JFrame();
    }

    protected void setup(){
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.window.setResizable(false);
        this.window.pack();
        this.setTitle("Game Engine");

        this.canvas = new JPanel(null){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2D = (Graphics2D) g;
                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GameWindow.this.engine.draw(g);
            }
        };
        this.canvas.setDoubleBuffered(true);
        this.canvas.setIgnoreRepaint(true);
        this.window.add(this.canvas);
        this.setSize(DEFAULT_FRAME_SIZE);
        this.setCentered();
    }

    public void draw(){
        if (this.canvas != null) {
            this.canvas.paintImmediately(this.canvas.getBounds());
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

    public void setSize(int width, int height){
        Insets i = this.window.getInsets();
        this.window.setSize(width + i.left + i.right, height + i.top + i.bottom);
        this.canvas.setSize(width, height);
    }

    public void setSize(Dimension size){
        this.setSize(size.width, size.height);
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
