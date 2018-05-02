package Kd_Tress;

import edu.princeton.cs.algs4.*;

import java.util.ArrayList;

public class KdTree {
    private static final boolean horizon = false;
    private static final RectHV CANVAS = new RectHV(0, 0, 1, 1);
    private Node root;
    private int N;


    public KdTree() {
        root = null;
        N = 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        KdTree kdTree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble(),
                    y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
        }
        kdTree.draw();
        Point2D p = new Point2D(0.75, 0.5);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BOOK_RED);
        StdDraw.point(p.x(), p.y());
        StdDraw.show();
        System.out.println(kdTree.nearest(p));
        //System.out.println(kdTree.contains(p));
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    private int compare(Point2D p, Point2D q, boolean ori) {
        if (p.compareTo(q) == 0) return 0;
        if (ori)    //Horizon: false, Vertical: true
            return Double.compare(p.x(), q.x());
        else return Double.compare(p.y(), q.y());
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to insert() is null");
        root = insert(root, p, CANVAS.xmin(), CANVAS.ymin(), CANVAS.xmax(), CANVAS.ymax(), !horizon);
    }

    private Node insert(Node x, Point2D p, double xmin, double ymin,
                        double xmax, double ymax, boolean ori) {
        if (x == null) {
            N++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }
        if (p.compareTo(x.p) == 0) return x;
        int cmp = compare(p, x.p, ori);
        if (cmp >= 0) {
            if (ori) xmin = x.p.x();
            else ymin = x.p.y();
            x.rt = insert(x.rt, p, xmin, ymin, xmax, ymax, !ori);
        } else {
            if (ori) xmax = x.p.x();
            else ymax = x.p.y();
            x.lb = insert(x.lb, p, xmin, ymin, xmax, ymax, !ori);
        }
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to contains() is null");
        return contains(root, p, !horizon);
    }

    private boolean contains(Node x, Point2D p, boolean ori) {
        if (x == null) return false;
        if (p.compareTo(x.p) == 0) return true;
        int cmp = compare(p, x.p, ori);
        if (cmp >= 0) return contains(x.rt, p, !ori);
        else return contains(x.lb, p, !ori);
    }

    public void draw() {
        if (isEmpty()) return;
        StdDraw.clear();
        draw(root, !horizon);
    }

    private void draw(Node x, boolean ori) {
        if (x == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        //Draw point
        StdDraw.point(x.p.x(), x.p.y());
        //Draw splitting line
        StdDraw.setPenRadius();
        if (ori) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }

        draw(x.rt, !ori);
        draw(x.lb, !ori);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Argument to range() is null");
        ArrayList<Point2D> list = new ArrayList<>();
        if (!CANVAS.intersects(rect) || isEmpty()) return list;
        range(root, list, rect, !horizon);
        //list.sort(Point2D.Y_ORDER);
        //list.sort(Point2D.X_ORDER);
        return list;
    }

    private void range(Node x, ArrayList<Point2D> list, RectHV rect, boolean ori) {
        if (x == null) return;
        else if (rect.contains(x.p)) list.add(x.p);

        if ((ori && x.p.x() <= rect.xmin()) || !ori && x.p.y() <= rect.ymin()) {
            range(x.rt, list, rect, !ori);
            return;
        } else if ((ori && x.p.x() > rect.xmax()) || !ori && x.p.y() > rect.ymax()) {
            range(x.lb, list, rect, !ori);
            return;
        }
        range(x.lb, list, rect, !ori);
        range(x.rt, list, rect, !ori);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Argument to nearest() is null");
        if (isEmpty()) return null;

        //return nearest(root,root,Double.MAX_VALUE, p,!horizon).p;


        double min = Double.MAX_VALUE;
        boolean ori = !horizon;
        Point2D retp = null;
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node x = stack.pop();
            if (x == null) continue;
            double distance = p.distanceSquaredTo(x.p);
            if (distance < min) {
                retp = x.p;
                min = distance;
            }
            if (compare(p, x.p, ori) >= 0) {
                if (x.lb != null && x.lb.rect.distanceSquaredTo(p) < min) stack.push(x.lb);
                stack.push(x.rt);
            } else {
                if (x.rt != null && x.rt.rect.distanceSquaredTo(p) < min) stack.push(x.rt);
                stack.push(x.lb);
            }
            ori = !ori;
        }
        return retp;

    }

    private Node nearest(Node x, Node minNode, Double min, Point2D p, boolean ori) {
        if (x == null) return minNode;
        double distance = p.distanceSquaredTo(x.p);
        if (distance < min) {
            minNode = x;
            min = distance;
        }
        if (compare(p, x.p, ori) >= 0) {
            minNode = nearest(x.rt, minNode, min, p, !ori);
            if (x.lb != null && x.lb.rect.distanceSquaredTo(p) < min)
                minNode = nearest(x.lb, minNode, min, p, !ori);
        } else {
            minNode = nearest(x.lb, minNode, min, p, !ori);
            if (x.rt != null && x.rt.rect.distanceSquaredTo(p) < min)
                minNode = nearest(x.rt, minNode, min, p, !ori);
        }
        return minNode;
    }

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        Node(Point2D p, RectHV rect) {
            if (p == null || rect == null)
                throw new IllegalArgumentException("Lack of argument when initialing Node object");
            if (!CANVAS.contains(p)) throw new IllegalArgumentException("Point coordination over range");
            if (rect.xmin() < CANVAS.xmin() || rect.xmax() > CANVAS.xmax() ||
                    rect.ymin() < CANVAS.ymin() || rect.ymax() > CANVAS.ymax())
                throw new IllegalArgumentException("Rect over range");
            this.p = p;
            this.rect = rect;
            lb = null;
            rt = null;
        }
    }
}