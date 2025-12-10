import java.awt.Point;

public class CircleCommand extends Command {
    private Model model;
    private Circle circle;
    private Point centerPoint;
    private Point radiusPoint;

    public CircleCommand(Model model) {
        this.model = model;
    }

    public void setCenter(Point p) {
        centerPoint = p;
    }

    public void setRadius(Point p) {
        radiusPoint = p;
    }

    // the actual work happens in end method())
    @Override
    public void execute() {
    }

    @Override
    public boolean undo() {
        if (circle != null)
            model.removeItem(circle);
        return true;
    }

    @Override
    public boolean redo() {
        if (circle != null)
            model.addItem(circle);
        return true;
    }

    @Override
    public boolean end() {
        if (centerPoint == null || radiusPoint == null) {
            return false;   // user didnt do both clicks
        }

        // Create the circle only after 2 clicks
        circle = new Circle(centerPoint, 0);
        circle.setRadius(radiusPoint);

        model.addItem(circle);
        return true;
    }
}
