import java.awt.*;

public class PolygonCommand extends Command {

    private Model model;
    private Polygon polygon;
    private boolean added = false;   // tracks if execute() was already called

    public PolygonCommand(Model model, Point firstPoint) {
        this.model = model;
        this.polygon = new Polygon(firstPoint);
    }

    public void addPoint(Point p) {
        polygon.addPoint(p);
        model.setChanged();  // refresh view as polygon grows
        execute();
    }

    public Polygon getPolygon() {
        return polygon;
    }

    @Override
    public void execute() {
        if (!added) {
            model.addItem(polygon);
            added = true;
        }
    }

    @Override
    public boolean undo() {
        model.removeItem(polygon);
        added = false;
        return true;
    }

    @Override
    public boolean redo() {
        execute();
        return true;
    }

    @Override
    public boolean end() {
        // Polygon must have at least 2 distinct points
        if (polygon.points.size() < 2) {
            undo();
            return false;
        }
        execute();
        return true;
    }
}
