import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MoveButton extends JButton implements ActionListener {

    private JPanel drawingPanel;
    private View view;
    private UndoManager undoManager;

    private DragHandler activeDragHandler = null; // <--- IMPORTANT!!!!!!

    public MoveButton(UndoManager undoManager, View view, JPanel drawingPanel) {
        super("Move");
        this.undoManager = undoManager;
        this.view = view;
        this.drawingPanel = drawingPanel;

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        drawingPanel.addMouseListener(new MouseHandler());
        view.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            Point startPoint = View.mapPoint(event.getPoint());

            // right click, cancels move mode
            if (SwingUtilities.isRightMouseButton(event)) {
                if (activeDragHandler != null) {
                    drawingPanel.removeMouseMotionListener(activeDragHandler);
                    activeDragHandler = null;
                }

                drawingPanel.removeMouseListener(this);
                view.setCursor(Cursor.getDefaultCursor());
                return;
            }

            //left click = start move cmd
            MoveCommand moveCommand = new MoveCommand(View.getModel());
            undoManager.beginCommand(moveCommand);

            activeDragHandler = new DragHandler(moveCommand, this, startPoint);

            drawingPanel.addMouseMotionListener(activeDragHandler);
        }
    }

    private class DragHandler extends MouseAdapter {

        private MoveCommand moveCommand;
        private MouseAdapter mouseHandler;
        private Point lastPoint;

        public DragHandler(MoveCommand moveCommand, MouseAdapter mouseHandler, Point startPoint) {
            this.moveCommand = moveCommand;
            this.mouseHandler = mouseHandler;
            this.lastPoint = startPoint;
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            Point current = View.mapPoint(event.getPoint());

            int dx = current.x - lastPoint.x;
            int dy = current.y - lastPoint.y;

            moveCommand.moveItems(dx, dy);
            lastPoint = current;
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            undoManager.endCommand(moveCommand);

            drawingPanel.removeMouseMotionListener(this);
            drawingPanel.removeMouseListener(mouseHandler);

            activeDragHandler = null;

            view.setCursor(Cursor.getDefaultCursor());
        }
    }
}
