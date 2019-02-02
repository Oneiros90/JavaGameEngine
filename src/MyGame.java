import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by oneiros on 09/02/16.
 */
public class MyGame extends GameEngine {

    private Sprite background = new Sprite();
    private Sprite[] rectangles;

    @Override
    public void init(){
        Dimension size = new Dimension(800, 600);
        this.getWindow().setSize(size);
        this.getWindow().setCentered();
        this.background.setFrame(0, 0, size.width, size.height);

        this.rectangles = new Sprite[5000];
        for (int i = 0; i < rectangles.length; i++){
            this.rectangles[i] = new Sprite();
            //this.rectangles[i].color = Color.getHSBColor((float) Math.random(), 1.0f, 1.0f);
            this.rectangles[i].image = Resources.Images.get("pokemon-" + ((i%4)+1));
            this.rectangles[i].setFrame(random(0, size.width-100), random(0, size.height-100), 100, 100);
            this.rectangles[i].setCollisionBox(false);
            this.rectangles[i].speed.set(random(-200, 200), random(-200, 200));
        }
    }

    @Override
    public void input() {
    }

    @Override
    public void update() {

        GameTime timer = getTimer();
        double time = timer.getTime(TimeUnit.SECONDS);
        double elapsed = timer.tockDiff(TimeUnit.SECONDS);

        /*for (int i = 0; i < rectangles.length; i++){
            double spriteTime = elapsed;
            Collision collision;
            for (int j = 0; j < rectangles.length; j++) {
                if (j == i) continue;
                Sprite collideWith = this.rectangles[j];
                do {
                    collision = this.rectangles[i].checkCollisionInside(collideWith, spriteTime);
                    if (collision.exists()) {
                        this.rectangles[i].animate(collision.time * spriteTime);
                        this.rectangles[i].collide(collision.direction);
                        collideWith.animate(collision.time * spriteTime);
                        collideWith.collide(collision.direction.inverse());
                        spriteTime -= collision.time * spriteTime;
                    }
                } while (collision.exists());
                this.rectangles[i].animate(spriteTime);
            }
        }*/

        for (int i = 0; i < rectangles.length; i++){
            this.rectangles[i].animate(elapsed);
        }

        /*if ((int) time == a){
            a+=1;
            int width = (int) random(400, 1000);
            int height = (int) random(200, 600);
            this.background.setFrame(0, 0, width, height);
            this.getWindow().setSize(new Dimension(width, height));
        }*/

        float randomHue = (float)((time/1.0)%1);
        this.background.color = Color.getHSBColor(randomHue, 0.5f, 0.2f);
        this.getWindow().setTitle("GameEngine (FPS: " + timer.getFPS() + ")");
    }

    @Override
    public void draw(Graphics g) {
        this.background.draw(g);
        for (Sprite rectangle : this.rectangles) {
            rectangle.draw(g);
        }
    }

    private static double random(double min, double max){
        return Math.random() * (max - min) + min;
    }

    public static void main(String[] args){
        MyGame myGame = new MyGame();
        myGame.setDesiredFramesPerSecond(120);

        Resources.Logger = System.out;
        Resources.Images.prepare("res/bulbasaur.png", "pokemon-1");
        Resources.Images.prepare("res/charmander.png", "pokemon-2");
        Resources.Images.prepare("res/squirtle.png", "pokemon-3");
        Resources.Images.prepare("res/pikachu.png", "pokemon-4");
        Resources.Images.loadEverything();

        myGame.start();
    }
}
