import java.util.Enumeration;

public class MoveCommand extends Command {

    private Model model;
    private int totalDx;
    private int totalDy;

    public MoveCommand(Model model) {
        this.model = model;
        this.totalDx = 0;
        this.totalDy = 0;
    }

    // Called continuously while dragging
    public void moveItems(int dx, int dy) {
        if (dx == 0 && dy == 0) return;

        Enumeration items = model.getSelectedItems();
        while (items.hasMoreElements()) {
            Item item = (Item) items.nextElement();
            item.moveBy(dx, dy);   // we'll add this to Item subclasses
        }

        totalDx += dx;
        totalDy += dy;
        model.setChanged();
    }

    @Override
    public void execute() {
        // Nothing to do at the beginning cuz movement happens during drag
    }

    @Override
    public boolean undo() {
        if (totalDx == 0 && totalDy == 0) return false;

        Enumeration items = model.getSelectedItems();
        while (items.hasMoreElements()) {
            Item item = (Item) items.nextElement();
            item.moveBy(-totalDx, -totalDy);
        }

        model.setChanged();
        return true;
    }

    @Override
    public boolean redo() {
        if (totalDx == 0 && totalDy == 0) return false;

        Enumeration items = model.getSelectedItems();
        while (items.hasMoreElements()) {
            Item item = (Item) items.nextElement();
            item.moveBy(totalDx, totalDy);
        }

        model.setChanged();
        return true;
    }

    @Override
    public boolean end() {
        // If nothing actually moved, don't keep this command on stack
        return !(totalDx == 0 && totalDy == 0);
    }
}
