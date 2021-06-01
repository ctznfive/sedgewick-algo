import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;

    public KdTree() {
        root = null;
        size = 0;
    }

    private class Node {
        private final Point2D point;
        private final RectHV rect;
        private Node left;
        private Node right;
        private final boolean vertical;

        public Node(Point2D point, RectHV rect, Node left, Node right, boolean vertical) {
            this.point = point;
            this.rect = rect;
            this.left = left;
            this.right = right;
            this.vertical = vertical;
        }

        public Point2D getPoint() {
            return point;
        }

        public RectHV getRect() {
            return rect;
        }

        public boolean isX() {
            return vertical;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (root == null) {
            RectHV rect = new RectHV(0, 0, 1, 1);
            root = new Node(p, rect, null, null, true);
            size++;
            return;
        }

        Node node = root;
        double xMin, yMin;
        double xMax, yMax;
        boolean isVertical = true;

        while (true) {
            isVertical = node.isX();
            Point2D point = node.getPoint();

            if (point.equals(p))
                return;

            if (isVertical && p.x() < point.x()) {
                if (node.left == null) {
                    xMin = node.getRect().xmin();
                    yMin = node.getRect().ymin();
                    xMax = point.x();
                    yMax = node.getRect().ymax();
                    RectHV rect = new RectHV(xMin, yMin, xMax, yMax);

                    node.left = new Node(p, rect, null, null, !isVertical);
                    size++;
                    return;
                }
                node = node.left;

            } else if (isVertical && p.x() >= point.x()) {
                if (node.right == null) {
                    xMin = point.x();
                    yMin = node.getRect().ymin();
                    xMax = node.getRect().xmax();
                    yMax = node.getRect().ymax();
                    RectHV rect = new RectHV(xMin, yMin, xMax, yMax);

                    node.right = new Node(p, rect, null, null, !isVertical);
                    size++;
                    return;
                }
                node = node.right;

            } else if (p.y() < point.y()) {
                if (node.left == null) {
                    xMin = node.getRect().xmin();
                    yMin = node.getRect().ymin();
                    xMax = node.getRect().xmax();
                    yMax = point.y();
                    RectHV rect = new RectHV(xMin, yMin, xMax, yMax);

                    node.left = new Node(p, rect, null, null, !isVertical);
                    size++;
                    return;
                }
                node = node.left;

            } else {
                if (node.right == null) {
                    xMin = node.getRect().xmin();
                    yMin = point.y();
                    xMax = node.getRect().xmax();
                    yMax = node.getRect().ymax();
                    RectHV rect = new RectHV(xMin, yMin, xMax, yMax);

                    node.right = new Node(p, rect, null, null, !isVertical);
                    size++;
                    return;
                }
                node = node.right;
            }
        }
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        Node node = root;
        while (node != null) {
            boolean isVertical = node.isX();
            Point2D point = node.getPoint();

            if (point.equals(p))
                return true;
            else if (isVertical && p.x() < point.x())
                node = node.left;
            else if (isVertical && p.x() >= point.x())
                node = node.right;
            else if (p.y() < point.y())
                node = node.left;
            else node = node.right;
        }
        return false;
    }

    public void draw() {
        if (isEmpty())
            return;

        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            Node node = queue.dequeue();

            if (node.right != null)
                queue.enqueue(node.right);
            if (node.left != null)
                queue.enqueue(node.left);

            boolean isVertical = node.isX();
            Point2D point = node.getPoint();
            Point2D pointHead;
            Point2D pointTail;

            if (isVertical) {
                pointHead = new Point2D(point.x(), node.getRect().ymin());
                pointTail = new Point2D(point.x(), node.getRect().ymax());
                StdDraw.setPenColor(StdDraw.BLUE);
            } else {
                pointHead = new Point2D(node.getRect().xmin(), point.y());
                pointTail = new Point2D(node.getRect().xmax(), point.y());
                StdDraw.setPenColor(StdDraw.RED);
            }

            pointHead.drawTo(pointTail);
            StdDraw.setPenColor(StdDraw.BLACK);
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        Queue<Point2D> queue = new Queue<Point2D>();
        if (isEmpty())
            return queue;
        search(queue, rect, root);
        return queue;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;

        double distanceNearest = Double.POSITIVE_INFINITY;
        Stack<Node> nodes = new Stack<Node>();
        Node nodeNearest = root;
        nodes.push(root);

        while (!nodes.isEmpty()) {
            Node node = nodes.pop();
            if (distanceNearest < node.getRect().distanceSquaredTo(p))
                continue;

            double distanceTemp = node.getPoint().distanceSquaredTo(p);
            if (distanceNearest > distanceTemp) {
                nodeNearest = node;
                distanceNearest = distanceTemp;
            }

            if (node.left != null && node.right == null)
                nodes.push(node.left);
            else if (node.left == null && node.right != null)
                nodes.push(node.right);
            else if (node.left != null && node.right != null) {
                double distanceLeftBottom = p.distanceSquaredTo(node.left.getPoint());
                double distanceRightTop = p.distanceSquaredTo(node.right.getPoint());

                if (distanceLeftBottom < distanceRightTop) {
                    nodes.push(node.right);
                    nodes.push(node.left);
                } else {
                    nodes.push(node.left);
                    nodes.push(node.right);
                }
            }
        }
        return nodeNearest.getPoint();
    }

    private void search(Queue<Point2D> queue, RectHV rect, Node node) {
        if (node == null)
            return;
        if (!rect.intersects(node.getRect()))
            return;

        if (rect.contains(node.getPoint()))
            queue.enqueue(node.getPoint());

        search(queue, rect, node.left);
        search(queue, rect, node.right);
    }
}
