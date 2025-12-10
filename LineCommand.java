import java.awt.*;

public class LineCommand extends Command {
    private Model model;
    private Line line;
    private Point p1, p2;

    public LineCommand(Model model, Point startPoint) {
        this.model = model;
        this.p1 = startPoint;
        this.line = new Line(startPoint, startPoint); // temporary until 2nd click
    }

    public void setLinePoint(Point secondPoint) {
        this.p2 = secondPoint;
        line.setPoint2(secondPoint);
    }

    @Override
    public void execute() {
        model.addItem(line);
    }

    @Override
    public boolean undo() {
        model.removeItem(line);
        return true;
    }

    @Override
    public boolean redo() {
        execute();
        return true;
    }

    @Override
    public boolean end() {
        if (p1 == null || p2 == null) {
            undo();
            return false;
        }
        execute();
        return true;
    }
}
