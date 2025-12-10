import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LineButton extends JButton implements ActionListener {

    private JPanel drawingPanel;
    private View view;
    private UndoManager undoManager;

    public LineButton(UndoManager undoManager, View view, JPanel drawingPanel) {
        super("Line");
        this.undoManager = undoManager;
        this.view = view;
        this.drawingPanel = drawingPanel;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        drawingPanel.addMouseListener(new MouseHandler());
        drawingPanel.addMouseMotionListener(new DragHandler());
        view.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    private class MouseHandler extends MouseAdapter {
        private int clickCount = 0;
        private LineCommand lineCommand;

        @Override
        public void mouseClicked(MouseEvent event) {
            Point p = View.mapPoint(event.getPoint());

            if (clickCount == 0) {

                // first click, create command
                lineCommand = new LineCommand(View.getModel(), p);
                undoManager.beginCommand(lineCommand);

                // Create preview line so you see the first point
                view.setPreviewLine(new Line(p, p));

                clickCount = 1;

            } else {
                // 2nd click, finalize
                lineCommand.setLinePoint(p);

                undoManager.endCommand(lineCommand);

                // Remove preview
                view.setPreviewLine(null);

                drawingPanel.removeMouseListener(this);
                drawingPanel.removeMouseMotionListener(new DragHandler());

                view.setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    private class DragHandler extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent event) {
            updatePreview(event);
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            updatePreview(event);
        }

        private void updatePreview(MouseEvent event) {
            Line preview = view.getPreviewLine();
            if (preview == null) return;

            Point p = View.mapPoint(event.getPoint());
            preview.setPoint2(p);    // update endpoint of preview
            view.repaint();
        }
    }
}
