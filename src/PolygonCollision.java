/**
 * Created by oneiros on 07/06/16.
 */
public class PolygonCollision {

    public boolean willIntersect; // Are the polygons going to intersect forward in time?
    public boolean intersect; // Are the polygons currently intersecting
    //public Vector2 minimumTranslationVector; // The translation to apply to polygon A to push the polygons appart.

    // Check if polygon A is going to collide with polygon B for the given velocity
    public static double polygonCollision(Polygon polygonA, Polygon polygonB, Vector2 velocityA, Vector2 velocityB) {
        PolygonCollision collision = new PolygonCollision();
        collision.intersect = true;
        collision.willIntersect = true;

        int edgeCountA = polygonA.getEdges().size();
        int edgeCountB = polygonB.getEdges().size();
        double firstCollision = Double.POSITIVE_INFINITY;
        //Vector2 translationAxis = new Vector2();
        Vector2 norm;

        // Loop through all the edges of both polygons
        for (int i = 0; i < edgeCountA + edgeCountB; i++) {
            if (i < edgeCountA) {
                norm = polygonA.getNormals().get(i);
            } else {
                norm = polygonB.getNormals().get(i - edgeCountA);
            }

            // Find the projection of the polygon on the current axis
            Vector2 projA = projectPolygon(polygonA, norm);
            Vector2 projB = projectPolygon(polygonB, norm);

            // Project the velocity on the current axis
            double velocityProjA = velocityA.dot(norm);
            double velocityProjB = velocityB.dot(norm);

            double t = intervalCollision(projA, projB, velocityProjA, velocityProjB);
            if (t == -1){
                return -1;
            } else if (t < firstCollision){
                firstCollision = t;
            }

        }
        return firstCollision;
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
                return -1d;
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

                double t = (i1.x - i2.x) / (v2 - v1);
                return (t > 0 && t <= 1) ? t : -1d;
            }
        }
    }

    // Calculate the projection of a polygon on an axis and returns it as a [min, max] interval
    private static Vector2 projectPolygon(Polygon polygon, Vector2 axis) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Vector2 point : polygon.getPoints()) {
            double dot = point.dot(axis); // To projectTo a point on an axis use the dot product
            min = Math.min(min, dot);
            max = Math.max(max, dot);
        }
        return new Vector2(min, max);
    }
}
