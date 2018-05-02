package Pattern_Recognition;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class FastCollinearPoints {
    private final ArrayList<PointPair> pointPairs = new ArrayList<>();
    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Null Point[]");
        int length = points.length;
        Point[] clone = new Point[length];
        for (int i = 0; i < length; i++)
            if (points[i] == null) throw new IllegalArgumentException("Null Point");
            else clone[i] = points[i];

        FindPairs(clone);
        CreateLineSegment();
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
        StdDraw.setPenRadius(0.001);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println(collinear.segments().length);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    private void FindPairs(Point[] points) {
        //Sort all the points in natural order. First y-coordination Last x-coordination
        Arrays.sort(points);

        int length = points.length;
        for (int i = 0; i < length - 1; i++)
            if (points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException("Repeat Point");

        Point[] otherPoints = new Point[length - 1];
        for (int i = 0; i < length - 3; i++) {
            int num = 0;
            //Point[] otherPoints = Arrays.copyOfRange(points,i+1,length);
            for (int j = i + 1; j < length; j++)
                otherPoints[num++] = points[j];

            Arrays.sort(otherPoints, 0, num, points[i].slopeOrder());

            int start = 0;
            while (start < num - 2) {
                int end = start + 2;
                double SlopeStart = points[i].slopeTo(otherPoints[start]);
                double SlopeEnd = points[i].slopeTo(otherPoints[end]);

                //First of all, check whether the slope of Start equals to that of end of otherPoints, if true, add them to pointPairs,
                //then continue to next loop
                if (Double.compare(SlopeStart, points[i].slopeTo(otherPoints[num - 1])) == 0) {
                    pointPairs.add(new PointPair(points[i], otherPoints[num - 1]));
                    break;
                }

                if (Double.compare(SlopeStart, SlopeEnd) == 0) {
                    while (end < num - 1 && Double.compare(SlopeStart, points[i].slopeTo(otherPoints[end + 1])) == 0)
                        end++;
                    pointPairs.add(new PointPair(points[i], otherPoints[end]));
                    start = end + 1;
                    continue;
                }
                start++;
            }
        }
    }

    private void CreateLineSegment() {
        if (pointPairs.size() == 0) return;
        pointPairs.sort(PointPair.CompareByEndSlopeStart);

        PointPair prepair = pointPairs.get(0);
        lineSegments.add(new LineSegment(prepair.startPoint, prepair.endPoint));

        for (PointPair pair : pointPairs) {
            if (PointPair.CompareByEndSlope.compare(prepair, pair) != 0) {
                lineSegments.add(new LineSegment(pair.startPoint, pair.endPoint));
                prepair = pair;
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        int num = 0;
        LineSegment[] segments = new LineSegment[this.lineSegments.size()];
        for (LineSegment segment : lineSegments)
            segments[num++] = segment;
        return segments;
    }

    private static class PointPair {
        public final static Comparator<PointPair> CompareByEndSlope = (p1, p2) ->
        {
            int comparePoint = p1.endPoint.compareTo(p2.endPoint);
            if (comparePoint != 0) return comparePoint;
            return Double.compare(p1.slope, p2.slope);
        };
        public final static Comparator<PointPair> CompareByEndSlopeStart = (p1, p2) ->
        {
            int compareEndSlope = CompareByEndSlope.compare(p1, p2);
            if (compareEndSlope != 0) return compareEndSlope;
            return p1.startPoint.compareTo(p2.startPoint);
        };
        private final Point startPoint;
        private final Point endPoint;
        private final double slope;

        public PointPair(Point startPoint, Point endPoint) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.slope = startPoint.slopeTo(endPoint);
        }
    }
}


