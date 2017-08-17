package kdtrees;

import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree implements PointStorage {

    private BST2D bst;

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
        return null;
    }

    private class BST2D {
        private Node root;
        private final RectHV field = new RectHV(0.0, 0.0, 1.0, 1.0);
        
        private class Node {
            Point2D point;
            Node left, right;

            int count;
            boolean isVerticalSplit;

            Node(Point2D p, boolean isVertical) {
                point = p;
                count = 1;
                isVerticalSplit = isVertical;
            }
            
            int compareWithPoint(Point2D p) {
                if (isVerticalSplit) 
                    return Point2D.X_ORDER.compare(p, point);
                else
                    return Point2D.Y_ORDER.compare(p, point);
            }
            
            void updateSize() {
                count = 1 + size(left) + size(right);
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
            
            private int size(Node node) {
                if (node == null)
                    return 0;
                
                return node.count;
            }
        }
        
        public boolean isEmpty() {
            return root == null;
        }
        
        public int size() {
            if (isEmpty())
                return 0;
            return root.count;
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
            
            if (nodeRect == null)
                nodeRect = field;
            
            if (rect.contains(node.point))
                list.add(node.point);
            
            RectHV tempRect = node.splitRect(nodeRect, true);
            if (nodeRect.intersects(rect))
                range(node.left, tempRect, rect, list);
            
            tempRect = node.splitRect(nodeRect, false);
            if (nodeRect.intersects(rect))
                range(node.right, tempRect, rect, list);
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
                    xmin = xmax = node.point.x();
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                ymin = ymax = node.point.y();
            }
            StdDraw.line(xmin, ymin, xmax, ymax);
        }
    }
}
