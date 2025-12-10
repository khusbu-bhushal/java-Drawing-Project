import java.util.ArrayList;
import java.awt.*;

public class Polygon extends Item {

  ArrayList<Point> points;

  public Polygon(ArrayList<Point> vertices) {
    this.points = vertices;
  }

  public Polygon(Point point1) {
    this.points = new ArrayList<Point> ();

    this.points.add(point1);
  }

  public Polygon() {
	  this.points = null;
  }
  public boolean includes(Point testPoint) {
    for (Point point : points) {
        if (distance(point, testPoint ) < 20.0) {
            return true;
        }
        
    }

    return false;
  }
  public void render(UIContext uiContext) {

    Point lastPoint = points.get(0);
    for (int i = 1; i < points.size(); i++) {
        uiContext.drawLine(lastPoint, points.get(i));
        lastPoint = points.get(i);
    }

    uiContext.drawLine(lastPoint, points.get(0));
  }

  public void moveBy(int dx, int dy) {
    if (points == null) return;
    for (Point p : points) {
        p.translate(dx, dy);
    }
}


  public void setPoint(Point point, int i) {
    points.set (i, point);
  }
  public int addPoint(Point point) {
    points.add (point);

    return points.size() - 1;
  }
  public Point getPoint(int i) {
    return points.get (i);
  }
  public String toString() {
    String outString = "Polygon  from ";
    for (Point point : points) {
        outString += point.toString();
    }

    return outString;
  }
}


