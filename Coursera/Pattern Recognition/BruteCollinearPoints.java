import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private final ArrayList<Point[]> list;
    private final Comparator<Point[]> CompareByFirstEnd = (p1, p2) ->
    {
        int compareStart = p1[0].compareTo(p2[0]);
        if (compareStart != 0) return compareStart;
        return p1[1].compareTo(p2[1]);
    };

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Null Point[]");
        int length = points.length;

        Point[] clone = new Point[length];
        list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            if (points[i] == null) throw new IllegalArgumentException("Null Point");
            clone[i] = points[i];
        }

        FindPairs(clone);
        list.sort(CompareByFirstEnd);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.01);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

        for (int i = 0; i < 100; i++)
            collinear.segments();
    }

    private void FindPairs(Point[] points) {
        Arrays.sort(points);

        int length = points.length;
        for (int i = 0; i < length - 1; i++)
            if (points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException("Repeat Point");

        for (int p = 0; p < length - 3; p++) {
            Comparator<Point> comparator = points[p].slopeOrder();
            for (int q = p + 1; q < length - 2; q++) {
                if (points[p].compareTo(points[q]) == 0) continue;
                for (int r = q + 1; r < length - 1; r++) {
                    if (points[q].compareTo(points[r]) == 0) continue;
                    if (comparator.compare(points[q], points[r]) == 0) {
                        for (int s = r + 1; s < length; s++) {
                            if (points[r].compareTo(points[s]) == 0) continue;
                            if (comparator.compare(points[q], points[s]) == 0)
                                list.add(new Point[]{points[p], points[s]});
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() { return list.size(); }

    public LineSegment[] segments() {
        LineSegment[] lineSegments = new LineSegment[numberOfSegments()];
        for (int i = 0; i < list.size(); i++) {
            Point[] points = list.get(i);
            lineSegments[i] = new LineSegment(points[0], points[1]);
        }
        return lineSegments;
    }
}
