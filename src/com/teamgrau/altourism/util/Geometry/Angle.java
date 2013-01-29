package com.teamgrau.altourism.util.Geometry;

import android.location.Location;

/**
 * User: thomaseichinger
 * Date: 1/14/13
 * Time: 3:47 PM
 */
public class Angle {

    /**
     * Construct the vector specified by two points.
     *
     * @param p0, p1  Points the construct vector between [x,y].
     * @return v       Vector from p0 to p1 [x,y].
     */
    public static double[] createVector(Location p0, Location p1) {
        double[] v = {p1.getLatitude() - p0.getLatitude(), p1.getLongitude() - p0.getLongitude()};
        return v;
    }

    /**
     * Compute the dot product (a scalar) between two vectors.
     *
     * @param v0, v1  Vectors to compute dot product between [x,y,z].
     * @return Dot product of given vectors.
     */
    public static double computeDotProduct(double[] v0, double[] v1) {
        return v0[0] * v1[0] + v0[1] * v1[1];
    }

    /**
     * Return the length of a vector.
     *
     * @param v Vector to compute length of [x,y,z].
     * @return Length of vector.
     */
    public static double length(double[] v) {
        return Math.sqrt(v[0] * v[0] + v[1] * v[1]);
    }

    /**
     * Find the angle between three points. P1 is center point
     *
     * @param p0, p1, p2  Three points finding angle between [x,y].
     * @return Angle (in radians) between given points.
     */
    public static double computeAngle(Location p0, Location p1, Location p2) {
        double[] v0 = Angle.createVector(p1, p0);
        double[] v1 = Angle.createVector(p1, p2);

        double dotProduct = Angle.computeDotProduct(v0, v1);

        double length1 = Angle.length(v0);
        double length2 = Angle.length(v1);

        double denominator = length1 * length2;

        double product = denominator != 0.0 ? dotProduct / denominator : 0.0;

        double angle = Math.acos(product);

        return angle;
    }
}
