import java.awt.*;

/**
 * Created by oneiros on 11/02/16.
 */
class Sprite extends Rectangle {

    public Color color;
    public Vector2 speed = new Vector2();
    public Polygon collisionBox = new Polygon();
    public Image image;

    public void setCollisionBox(boolean inside){
        this.collisionBox.addPoint(new Vector2(x, y));
        this.collisionBox.addPoint(new Vector2(x, y+height));
        this.collisionBox.addPoint(new Vector2(x + width, y + height));
        this.collisionBox.addPoint(new Vector2(x + width, y));
        this.collisionBox.calculateEdgesAndNormals(inside);
    }

    public void animate(double elapsed) {
        this.setFrame(this.x + speed.x * elapsed, this.y + speed.y * elapsed, width, height);
        this.collisionBox.offset(this.speed.multiply(elapsed));
    }

    public Collision checkCollisionInside(Sprite s, double elapsed){
        /*Rectangle next1 = this.nextFrame(elapsed);
        Rectangle next2 = s.nextFrame(elapsed);
        return Collision.checkCollisionInside(this, next1, s, next2);*/
        return Collision.polygonCollision(this.collisionBox, s.collisionBox, this.speed.multiply(elapsed), s.speed.multiply(elapsed));
    }

    public void collide(Vector2 direction){
        this.speed = this.speed.reflect(direction);
        this.animate(1);
    }

    public void draw(Graphics g) {
        if (color != null){
            Color old = g.getColor();
            g.setColor(this.color);
            g.fillRect((int)x, (int)y, (int)width, (int)height);
            g.setColor(old);
        }
        if (image != null){
            g.drawImage(image, (int)x, (int)y, (int)width, (int)height, null);
        }
    }

    @Override
    public String toString() {
        return String.format("%s[Frame: [x=%.1f,y=%.1f,w=%.1f,h=%.1f], Speed: [x=%.1f,y=%.1f]]",
                this.getClass().getName(), x, y, width, height, speed.x, speed.y);

    }
}
