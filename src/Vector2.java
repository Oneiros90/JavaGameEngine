import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by oneiros on 11/02/16.
 */
class Vector2 extends Point2D.Double {

    public static Vector2 ZERO = new Vector2(0,0);
    public static Vector2 ONE = new Vector2(1,1);

    public Vector2(){
        super();
    }

    public Vector2(double x, double y) {
        super(x, y);
    }

    public Vector2 set(double x, double y) {
        this.setLocation(x, y);
        return this;
    }

    public Vector2 set(Vector2 v) {
        return this.set(v.x, v.y);
    }

    public Vector2 sum(double x, double y) {
        return new Vector2(this.x + x, this.y + y);
    }

    public Vector2 sum(Vector2 v) {
        return this.sum(v.x, v.y);
    }

    public Vector2 sub(double x, double y) {
        return new Vector2(this.x - x, this.y - y);
    }

    public Vector2 sub(Vector2 v) {
        return this.sub(v.x, v.y);
    }

    public Vector2 multiply(double m) {
        return new Vector2(x * m, y * m);
    }

    public Vector2 multiply(Vector2 v) {
        return new Vector2(x * v.x, y * v.y);
    }

    public double dot(Vector2 v){
        return this.x * v.x + this.y * v.y;
    }

    public double length(){
        return Math.sqrt(this.dot(this));
    }

    public Vector2 normalize(){
        double length = this.length();
        return new Vector2(x/length, y/length);
    }

    public Vector2 reflect(Vector2 n){
        double dot = this.dot(n);
        return new Vector2(x - 2*dot*n.x, y - 2*dot*n.y);
    }

    public Vector2 inverse() {
        return new Vector2(-x, -y);
    }

    public Vector2 inverseX() {
        return new Vector2(-x, y);
    }

    public Vector2 inverseY() {
        return new Vector2(x, -y);
    }

    public boolean isZero(){
        return this.equals(ZERO);
    }

    public Point toPoint(){
        return new Point((int)x, (int)y);
    }

    @Override
    public String toString() {
        return String.format("%s[%.2f;%.2f]", this.getClass().getName(), x, y);
    }
}
