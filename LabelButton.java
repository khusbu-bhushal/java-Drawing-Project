import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LabelButton extends JButton implements ActionListener {

    private JPanel drawingPanel;
    private View view;
    private UndoManager undoManager;

    private MouseHandler mouseHandler;
    private KeyHandler keyHandler;
    private LabelCommand labelCommand;

    public LabelButton(UndoManager undoManager, View view, JPanel drawingPanel) {
        super("Label");
        this.undoManager = undoManager;
        this.view = view;
        this.drawingPanel = drawingPanel;

        addActionListener(this);
        keyHandler = new KeyHandler();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mouseHandler = new MouseHandler();
        drawingPanel.addMouseListener(mouseHandler);
        view.setCursor(new Cursor(Cursor.TEXT_CURSOR));
    }


    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent event) {

            Point p = View.mapPoint(event.getPoint());

            if (labelCommand != null) {
                undoManager.endCommand(labelCommand);
            }

            labelCommand = new LabelCommand(View.getModel(), p);
            undoManager.beginCommand(labelCommand);

            drawingPanel.addKeyListener(keyHandler);
            drawingPanel.addFocusListener(keyHandler);
            drawingPanel.requestFocusInWindow();
        }
    }


    private class KeyHandler extends KeyAdapter implements FocusListener {

        @Override
        public void keyTyped(KeyEvent event) {
            if (labelCommand == null) return;

            char c = event.getKeyChar();

            if (c >= 32 && c <= 126) {
                labelCommand.addCharacter(c);
            }
        }

        @Override
        public void keyPressed(KeyEvent event) {

            if (labelCommand == null) return;

            if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                finishLabel();
            }
            else if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                labelCommand.removeCharacter();
            }
        }

        private void finishLabel() {
            undoManager.endCommand(labelCommand);
            cleanup();
            labelCommand = null;

            view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            view.refresh();
        }

        @Override
        public void focusGained(FocusEvent e) {
            drawingPanel.addKeyListener(this);
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (labelCommand != null) {
                undoManager.endCommand(labelCommand);
            }
            cleanup();
            labelCommand = null;

            view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        private void cleanup() {
            drawingPanel.removeKeyListener(this);
            drawingPanel.removeMouseListener(mouseHandler);
            drawingPanel.removeFocusListener(this);
        }
    }
}
