import java.awt.*;
import java.io.Serializable;

public class Circle extends Item implements Serializable {
    private Point center;
    private int radius;

    public Circle(Point center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public void setCenter(Point p) {
        this.center = p;
    }

    public void setRadius(Point p) {
        int dx = p.x - center.x;
        int dy = p.y - center.y;
        this.radius = (int) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
public boolean includes(Point p) {
    int dx = p.x - center.x;
    int dy = p.y - center.y;
    return (dx * dx + dy * dy) <= (radius * radius);
}


    @Override
    public void render(UIContext uiContext) {
        uiContext.drawCircle(center.x, center.y, radius);
    }

    @Override
    public void moveBy(int dx, int dy) {
    center.translate(dx, dy);   
    }


    public void translate(int dx, int dy) {
    center = new Point(center.x + dx, center.y + dy);
}

}
