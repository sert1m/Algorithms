package kdtrees;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree implements PointStorage {

    private final BST2D bst;

    public KdTree() {
       bst = new BST2D();
    }
    public boolean isEmpty() {
        return bst.isEmpty();
    }
    public int size() {
        return bst.size();
    }
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        
        bst.insert(p);
    }
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        
        return bst.contains(p);
    }
    public void draw() {
        bst.draw();
    }
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        return bst.range(rect);
    }
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return bst.nearest(p);
    }

    private class BST2D {
        private Node root;
        private final RectHV field = new RectHV(0.0, 0.0, 1.0, 1.0);
        
        private class Node {
            private Point2D point;
            private Node left, right;

            private int size;
            private final boolean isVerticalSplit;

            Node(Point2D p, boolean isVertical) {
                point = p;
                size = 1;
                isVerticalSplit = isVertical;
            }
            
            int compareWithPoint(Point2D p) {
                Comparator<Point2D> comparator = isVerticalSplit ? Point2D.X_ORDER : Point2D.Y_ORDER;
                
                int cmp = comparator.compare(p, point);
                if (cmp == 0 && !p.equals(point))
                    cmp = +1;

                return cmp;
            }
            
            // first mins return left or upper rect depending on node state
            RectHV splitRect(RectHV rect, boolean first) {
                double xmin = rect.xmin();
                double ymin = rect.ymin();
                double xmax = rect.xmax();
                double ymax = rect.ymax();
                
                if (isVerticalSplit) {
                    if (first)
                        xmax = point.x();
                    else
                        xmin = point.x();
                }
                else {
                    if (first)
                        ymax = point.y();
                    else
                        ymin = point.y();
                }
                return new RectHV(xmin, ymin, xmax, ymax);
            }

            void updateSize() {
                size = 1 + size(left) + size(right);
            }
        }
        
        private class NearestPoint {
            private Point2D point;
            // Square distance
            private double distance;
            
            NearestPoint(Point2D p, double d) {
                point = p;
                distance = d;
            }
        }
        
        public boolean isEmpty() {
            return root == null;
        }
        
        public int size() {
            return size(root);
        }

        public void insert(Point2D p) {
            root = put(root, true, p);
        }
        
        public boolean contains(Point2D p) {
            return p.equals(get(root, p));
        }
        
        public void draw() {
            draw(root, field);
        }
        
        public Iterable<Point2D> range(RectHV rect) {
            List<Point2D> list = new LinkedList<>();
            range(root, field, rect, list);
            return list;
        }
        
        public Point2D nearest(Point2D p) {
            return nearest(root, field, new NearestPoint(null, Double.MAX_VALUE), p).point;
        }
        
        private int size(Node node) {
            if (node == null)
                return 0;
            
            return node.size;
        }
        
        private Node put(Node node, boolean isVertical, Point2D p) {
            if (node == null)
                return new Node(p, isVertical);
            
            int cmp = node.compareWithPoint(p);
            if (cmp < 0) 
                node.left = put(node.left, !isVertical, p);
            else if (cmp > 0)
                node.right = put(node.right, !isVertical, p);
            else
                node.point = p;
            
            node.updateSize();
            
            return node;
        }
        
        private Point2D get(Node node, Point2D p) {
            if (node == null)
                return null;
            
            int cmp = node.compareWithPoint(p);
            if (cmp < 0)
                return get(node.left, p);
            else if (cmp > 0)
                return get(node.right, p);
            
            return node.point;
        }
        
        private void range(Node node, RectHV nodeRect, RectHV rect, List<Point2D> list) {
            if (node == null)
                return;
            
            if (!nodeRect.intersects(rect))
                return;
            
            if (rect.contains(node.point))
                list.add(node.point);
            
            RectHV tempRect = node.splitRect(nodeRect, true);
            if (nodeRect.intersects(rect))
                range(node.left, tempRect, rect, list);
            
            tempRect = node.splitRect(nodeRect, false);
            if (nodeRect.intersects(rect))
                range(node.right, tempRect, rect, list);
        }
        
        private NearestPoint nearest(Node node, RectHV rect, NearestPoint nearest, Point2D p) {
            if (node == null)
                return nearest;
            
            // count distance to current point
            double currentDistance = node.point.distanceSquaredTo(p);
            if (currentDistance < nearest.distance) {
                nearest.point = node.point;
                nearest.distance = currentDistance;
            }
            
            Node first = node.left, second = node.right;
            if (node.compareWithPoint(p) >= 0) {
                first = node.right;
                second = node.left;
            }
            
            RectHV temp = node.splitRect(rect, first == node.left);
            if (isIntersects(temp, p, nearest.distance))
                nearest = nearest(first, temp, nearest, p);
            
            temp = node.splitRect(rect, second == node.left);
            if (isIntersects(temp, p, nearest.distance))
                nearest = nearest(second, temp, nearest, p);
            
            return nearest;
        }
        
        // Check if circle created by a point and its r2 intersects the rectangle created by a node splitting line
        private boolean isIntersects(RectHV rect, Point2D p, double r2) {
            double deltaX = p.x() - Math.max(rect.xmin(), Math.min(p.x(), rect.xmin() + rect.width()));
            double deltaY = p.y() - Math.max(rect.ymin(), Math.min(p.y(), rect.ymin() + rect.height()));
            
            return (deltaX * deltaX + deltaY * deltaY) < r2;
        }
        
        private void draw(Node node, RectHV rect) {
            if (node == null)
                return;

            StdDraw.setPenColor(StdDraw.BLACK);
            node.point.draw();
            drawSplitter(node, rect);
            
            draw(node.left, node.splitRect(rect, true));
            draw(node.right, node.splitRect(rect, false));
        }
        
        private void drawSplitter(Node node, RectHV rect) {
            double xmin = rect.xmin();
            double ymin = rect.ymin();
            double xmax = rect.xmax();
            double ymax = rect.ymax();
            if (node.isVerticalSplit) {
                StdDraw.setPenColor(StdDraw.RED);
                xmin = node.point.x();
                xmax = node.point.x();
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                ymin = node.point.y();
                ymax = node.point.y();
            }
            StdDraw.line(xmin, ymin, xmax, ymax);
        }
    }
}
