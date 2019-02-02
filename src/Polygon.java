import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneiros on 07/06/16.
 */
public class Polygon {

    private List<Vector2> points = new ArrayList<Vector2>();
    private List<Vector2> edges = new ArrayList<Vector2>();
    private List<Vector2> normals = new ArrayList<Vector2>();

    public void calculateEdgesAndNormals(boolean left) {
        edges.clear();
        normals.clear();

        for (int i = 0; i < points.size(); i++) {

            Vector2 p1 = points.get(i);
            Vector2 p2 = getPoint(i + 1 < points.size() ? i + 1 : 0);

            // Calculate edge
            Vector2 edge = p2.sub(p1);
            edges.add(edge);

            // Calculate normal
            Vector2 norm = left ? new Vector2(-edge.y, edge.x) : new Vector2(edge.y, edge.x);
            normals.add(norm.normalize());
        }
    }

    public List<Vector2> getPoints() {
        return points;
    }

    public Vector2 getPoint(int i){
        return this.points.get(i);
    }

    public Polygon addPoint(Vector2 point){
        this.points.add(point);
        return this;
    }

    public List<Vector2> getEdges() {
        return edges;
    }

    public List<Vector2> getNormals() {
        return normals;
    }

    public Vector2 getCenter(){
        double totalX = 0;
        double totalY = 0;
        for (int i = 0; i < points.size(); i++) {
            totalX += getPoint(i).x;
            totalY += getPoint(i).y;
        }
        return new Vector2(totalX / (double)points.size(), totalY / (double)points.size());
    }

    public void offset(Vector2 v) {
        offset(v.x, v.y);
    }

    public Vector2 projectTo(Vector2 axis) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Vector2 point : this.getPoints()) {
            double dot = point.dot(axis); // To projectTo a point on an axis use the dot product
            min = Math.min(min, dot);
            max = Math.max(max, dot);
        }
        return new Vector2(min, max);
    }

    public void offset(double x, double y) {
        for (int i = 0; i < points.size(); i++) {
            points.set(i, getPoint(i).sum(x, y));
        }
    }

    @Override
    public String toString() {
        return this.points.toString();
    }
}