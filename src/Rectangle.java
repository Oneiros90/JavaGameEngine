/**
 * Created by oneiros on 22/02/16.
 */
public class Rectangle extends java.awt.Rectangle.Double {

    Rectangle(){
        super();
    }

    public Rectangle(double x, double y, double w, double h){
        super(x, y, w, h);
    }

    public void setLocation(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setLocation(Vector2 v){
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2 getLocation(){
        return new Vector2(x, y);
    }
}
