import javax.swing.*;
public class DrawingProgram {
  public static void main(String[] args){
    JFrame.setDefaultLookAndFeelDecorated(true);
    //create model and undomanager
    Model model = new Model();
    UndoManager undoManager = new UndoManager();

    //register w the view
    View.setModel(model);
    View.setUndoManager(undoManager);

    View view = new View();
    Model.setView(view);

    
    Command.setModel(model);
    Command.setUndoManager(undoManager);

    view.setVisible(true);
  }
}