import java.util.Stack;

public class UndoManager {

    private static Model model;
    private Stack<Command> history;
    private Stack<Command> redoStack;
    // private Command currentCommand;

    public UndoManager() {
        history = new Stack<>();
        redoStack = new Stack<>();
    }

    public static void setModel(Model m) {
        model = m;
    }

    public void beginCommand(Command command) {
        // currentCommand = command;
        redoStack.clear();
    }

    public void endCommand(Command command) {
        if (command.end()) {        // Only accept valid completed commands
            history.push(command);  // Put into undo history
            if (model != null) {
                model.setChanged(); // Causes view to repaint
            }
        }
        // currentCommand = null;
    }

    public void cancelCommand() {
        // currentCommand = null;
        if (model != null) {
            model.setChanged();
        }
    }

    public void undo() {
        if (!history.empty()) {
            Command command = history.pop();
            if (command.undo()) {
                redoStack.push(command);
            }
            if (model != null) {
                model.setChanged();
            }
        }
    }

    public void redo() {
        if (!redoStack.empty()) {
            Command command = redoStack.pop();
            if (command.redo()) {
                history.push(command);
            }
            if (model != null) {
                model.setChanged();
            }
        }
    }
}
