
/**
 * Created by oneiros on 22/02/16.
 */
public class Collision {

    public double time;
    public Vector2 direction;


    private static Vector2 intersection1D(double p1, double p2, double q1, double q2) {
        double s = (p1 - q1) / ((q2 - q1)-(p2 - p1));
        if (s > 0 && s <= 1) return new Vector2(s, p1 + s * (p2 - p1));
        return null;
    }

    public static Collision checkCollisionInside(Rectangle r1, Rectangle n1, Rectangle r2, Rectangle n2){

        Vector2 leftIntersection    = intersection1D(r1.getMinX(), n1.getMinX(), r2.getMinX(), n2.getMinX());
        Vector2 rightIntersection   = intersection1D(r1.getMaxX(), n1.getMaxX(), r2.getMaxX(), n2.getMaxX());
        Vector2 topIntersection     = intersection1D(r1.getMinY(), n1.getMinY(), r2.getMinY(), n2.getMinY());
        Vector2 bottomIntersection  = intersection1D(r1.getMaxY(), n1.getMaxY(), r2.getMaxY(), n2.getMaxY());

        boolean coll1 = leftIntersection != null;
        boolean coll2 = rightIntersection != null;
        boolean coll3 = topIntersection != null;
        boolean coll4 = bottomIntersection != null;

        if (!coll1 && !coll2 && !coll3 && !coll4){
            return null;

        } else {

            Collision c = new Collision();

            c.time = Double.POSITIVE_INFINITY;
            if (coll1) c.time = Double.min(c.time, leftIntersection.x);
            if (coll2) c.time = Double.min(c.time, rightIntersection.x);
            if (coll3) c.time = Double.min(c.time, topIntersection.x);
            if (coll4) c.time = Double.min(c.time, bottomIntersection.x);

            //c.location = new Vector2();
            //c.location.x = r1.x + (n1.x - r1.x) * c.time * 0.9999999999;
            //c.location.y = r1.y + (n1.y - r1.y) * c.time * 0.9999999999;

            c.direction = new Vector2();
            if (coll1 && c.time == leftIntersection.x)   c.direction.x -= 1;
            if (coll2 && c.time == rightIntersection.x)  c.direction.x += 1;
            if (coll3 && c.time == topIntersection.x)    c.direction.y -= 1;
            if (coll4 && c.time == bottomIntersection.x) c.direction.y += 1;
            c.direction.normalize();

            return c;
        }
    }

    public static Collision polygonCollision(Polygon polygonA, Polygon polygonB, Vector2 velocityA, Vector2 velocityB) {


        // System.out.println(polygonA.getPoints());
        // System.out.println(polygonB.getPoints());
        // System.out.println();
        Collision collision = new Collision();
        collision.direction = Vector2.ZERO;
        collision.time = -1;

        int edgeCountA = polygonA.getEdges().size();
        int edgeCountB = polygonB.getEdges().size();
        Vector2 norm;

        // Loop through all the norms of both polygons
        for (int i = 0; i < edgeCountA + edgeCountB; i++) {
            if (i < edgeCountA) {
                norm = polygonA.getNormals().get(i);
            } else {
                norm = polygonB.getNormals().get(i - edgeCountA);
            }
            // System.out.print(i + ". " + norm + " : ");

            // Find the projection of the polygon on the current axis
            Vector2 projA = polygonA.projectTo(norm);
            Vector2 projB = polygonB.projectTo(norm);
            // System.out.print(" ----- " + projA + ", " + projB);

            // Project the velocity on the current axis
            double velocityProjA = velocityA.dot(norm);
            double velocityProjB = velocityB.dot(norm);
            // System.out.print(" ----- " + velocityProjA + ", " + velocityProjB);

            double t = intervalCollision(projA, projB, velocityProjA, velocityProjB);
            // System.out.print(" ---- " + t);

            if (t > collision.time) {
                collision.time = t;
                collision.direction = norm;
            }
            // System.out.println();
        }
        // System.out.println(collision);
        // System.out.println();
        return collision;
    }

    // Calculate the distance between [minA, maxA] and [minB, maxB]
    // The distance will be negative if the intervals overlap
    private static double intervalCollision(Vector2 i1, Vector2 i2, double v1, double v2) {

        // Overlap case (the intervals are already colliding)
        if (i1.y >= i2.x && i2.y >= i1.x){
            return 0d;
        }

        // Disjoint case
        else {

            // Same velocity case (the intervals won't collide)
            if (v1 == v2){
                return Double.POSITIVE_INFINITY;
            }

            else {

                // Swap intervals if i2 < i1
                if (i2.x < i1.x){
                    Vector2 temp = i1;
                    i1 = i2;
                    i2 = temp;
                    double temp2 = v1;
                    v1 = v2;
                    v2 = temp2;
                }

                double t = (i1.y - i2.x) / (v2 - v1);
                return (t > 0 && t <= 1) ? t : Double.POSITIVE_INFINITY;
            }
        }
    }

    public boolean exists(){
        return this.time != Double.POSITIVE_INFINITY;
    }

    @Override
    public String toString() {
        return String.format("%s[Elapsed Time: %.2f, Direction: %s]",
                this.getClass().getName(), this.time, this.direction);
    }
}
