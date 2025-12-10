import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CircleButton extends JButton implements ActionListener {

    private JPanel drawingPanel;
    private View view;
    private UndoManager undoManager;
    private CircleCommand circleCommand;
    private MouseHandler mouseHandler;
    private DragHandler dragHandler;

    public CircleButton(UndoManager undoManager, View view, JPanel drawingPanel) {
        super("Circle");
        addActionListener(this);
        this.view = view;
        this.drawingPanel = drawingPanel;
        this.undoManager = undoManager;
        mouseHandler = new MouseHandler();
        dragHandler = new DragHandler();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        circleCommand = new CircleCommand(View.getModel());
        drawingPanel.addMouseListener(mouseHandler);
        drawingPanel.addMouseMotionListener(dragHandler);
    }

    private class MouseHandler extends MouseAdapter {

        private boolean gotCenter = false;

        @Override
        public void mouseClicked(MouseEvent event) {
            Point p = View.mapPoint(event.getPoint());

            if (!gotCenter) {
                // first click – store center
                circleCommand.setCenter(p);
                view.setPreviewCircle(new Circle(p, 0));  // create preview
                gotCenter = true;
                return;
            }

            // second click, finalize
            circleCommand.setRadius(p);

            undoManager.beginCommand(circleCommand);
            undoManager.endCommand(circleCommand);

            // remove preview
            view.setPreviewCircle(null);
            view.repaint();

            drawingPanel.removeMouseListener(this);
            drawingPanel.removeMouseMotionListener(dragHandler);
            gotCenter = false;
        }
    }

    private class DragHandler extends MouseMotionAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            updatePreview(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            updatePreview(e);
        }

        private void updatePreview(MouseEvent event) {
            if (circleCommand == null) return;

            Point radiusPoint = View.mapPoint(event.getPoint());
            Circle preview = view.getPreviewCircle();

            if (preview != null) {
                preview.setRadius(radiusPoint);
                view.repaint();
            }
        }
    }
}
