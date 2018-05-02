import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;

public class Particle {
    private final double radius;    //radius;
    private final double mass;  //mass
    private final Color color;  //color
    private double rx, ry;   //position
    private double vx, vy;   //velocity
    private int count;  //number of collisions

    public Particle() {
        rx = StdRandom.uniform(0.0, 1.0);
        ry = StdRandom.uniform(0.0, 1.0);
        vx = StdRandom.uniform(-0.005, 0.005);
        vy = StdRandom.uniform(-0.005, 0.005);
        radius = 0.01;
        mass = 0.5;
        color = Color.BLACK;
    }

    public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color) {
        if (rx < 0 || ry < 0 || radius <= 0 || mass <= 0)
            throw new IllegalArgumentException("Negative or zero parameters");
        if (rx - radius < 0 || rx + radius > 1 || ry - radius < 0 || ry + radius > 1)
            throw new IllegalArgumentException("Particle is out of boundary");
        this.rx = rx;
        this.ry = ry;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.mass = mass;
        this.color = color;
    }

    /**
     * Moves this particle in a straight line (based on its velocity)
     * for the specified amount of time
     *
     * @param dt the amount of time
     */
    public void move(double dt) {
        /*
        if (rx+vx*dt < radius || rx+vx*dt > 1.0-radius) vx=-vx;
        if (ry+vy*dt < radius || ry+vy*dt < 1.0-radius) vy=-vy;
        */
        rx = rx + vx * dt;
        ry = ry + vy * dt;
    }

    public void draw() {
        StdDraw.filledCircle(rx, ry, radius);
    }

    /**
     * Returns the number of collisions involving this particle with
     * vertical walls, horizontal walls, or other particels.
     * This is equal to the number of calls to {@link #bounceOff(Particle)},
     * {@link #bounceOffVerticalWall()}, and
     * {@link #bounceOffHorizontalWall()}
     *
     * @return the number of collisions involving this particle with
     * vertical walls, horizontal walls, or other particles
     */
    public int count() {
        return count;
    }

    public double timeToHit(Particle that) {
        if (this == that) return Double.POSITIVE_INFINITY;
        double dx = that.rx - this.rx, dy = that.ry - this.ry,
                dvx = that.vx - this.vx, dvy = that.vy - this.vy,
                dvdr = dx * dvx + dy * dvy;
        if (dvdr >= 0) return Double.POSITIVE_INFINITY;
        double dvdv = dvx * dvx + dvy * dvy,
                drdr = dx * dx + dy * dy,
                sigma = this.radius + that.radius,
                d = dvdr * dvdr - dvdv * (drdr - sigma * sigma);
        if (d < 0) return Double.POSITIVE_INFINITY;
        return -(dvdr + Math.sqrt(d)) / dvdr;

    }

    public double timeToHitVerticalWall() {
        if (vx == 0) return Double.POSITIVE_INFINITY;
        else if (vx > 0) return (1.0 - radius - rx) / vx;
        else return (radius - rx) / vx;
    }

    public double timeToHitHorizontalWall() {
        if (vy == 0) return Double.POSITIVE_INFINITY;
        else if (vy > 0) return (1.0 - radius - ry) / vy;
        else return (radius - ry) / vy;
    }

    /**
     * Updates the velocities of this particles and the specified particle according
     * to the laws of elastic collision. Assumes that the particles are colliding
     * at the instant
     *
     * @param that the other particle
     */
    public void bounceOff(Particle that) {
        if (this == that) return;

        double dx = that.rx - this.rx, dy = that.ry - this.ry,
                dvx = that.vx - this.vx, dvy = that.vy - this.vy,
                dvdr = dx * dvx + dy * dvy,
                sigma = this.radius + that.radius;
        //magnitude of normal force
        double magnitude = 2 * this.mass * that.mass * (dvdr) / (sigma * (this.mass + that.mass)),
                //normal force, and in x and y directions
                Jx = magnitude * dx / sigma,
                Jy = magnitude * dy / sigma;

        //update velocities according to normal force
        this.vx += Jx / this.mass;
        this.vy += Jy / this.mass;
        that.vx -= Jx / that.mass;
        that.vy -= Jy / that.mass;

        //update collision counts
        this.count++;
        that.count++;
    }

    public void bounceOffVerticalWall() {
        vx = -vx;
        count++;
    }

    public void bounceOffHorizontalWall() {
        vy = -vy;
        count++;
    }

    public double kineticEnergy() {
        return 0.5 * mass * (vx * vx + vy * vy);
    }
}
