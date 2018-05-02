package Kd_Tress;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class PointSET {

    private final java.util.TreeSet<Point2D> points;

    public PointSET() {
        points = new java.util.TreeSet<>();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        PointSET set = new PointSET();
        while (!in.isEmpty()) {
            Point2D point = new Point2D(in.readDouble(), in.readDouble());
            set.insert(point);
        }
        if (set.isEmpty()) return;
        set.draw();

    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to insert() is null");
        if (p.x() < 0 || p.x() > 1 || p.y() < 0 || p.y() > 1)
            throw new IllegalArgumentException("Point coordination over range");
        if (!contains(p)) points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to contains() is null");
        return points.contains(p);
    }

    public void draw() {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D point : points)
            StdDraw.point(point.x(), point.y());
        StdDraw.show();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Argument to range() is null");
        ArrayList<Point2D> list = new ArrayList<>();
        for (Point2D point : points)
            if (rect.contains(point))
                list.add(point);
        list.sort(Point2D.Y_ORDER);
        list.sort(Point2D.X_ORDER);
        return list;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to nearest() is null");
        if (p.x() < 0 || p.x() > 1 || p.y() < 0 || p.y() > 1)
            throw new IllegalArgumentException("Point coordination over range");
        if (isEmpty()) return null;
        Point2D nearestSofar = null;
        double minDistance = Double.MAX_VALUE;
        for (Point2D point : points) {
            double distance = p.distanceSquaredTo(point);
            if (distance < minDistance) {
                nearestSofar = point;
                minDistance = distance;
            }
        }
        return nearestSofar;
    }
}
