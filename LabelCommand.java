import java.awt.*;

public class LabelCommand extends Command {
    private Label label;

    public LabelCommand(Model model, Point point) {
        this.model = model;
        label = new Label(point);

                System.out.println("Label created at: " + label.getStartingPoint());

        execute(); //add it to the model
    }

    public void addCharacter(char character) {
        label.addCharacter(character);
        model.setChanged();
    }

    public void removeCharacter() {
        label.removeCharacter();
        model.setChanged();
    }

    @Override
    public void execute() {
        model.addItem(label);
    }

    @Override
    public boolean undo() {
        model.removeItem(label);
        return true;
    }

    @Override
    public boolean redo() {
        execute();
        return true;
    }

    @Override
    public boolean end() {
        if (label.getText().isEmpty()) {
            undo();
            return false;
        }
        return true;
    }
}
