import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PolygonButton extends JButton implements ActionListener {

    private JPanel drawingPanel;
    private View view;
    private UndoManager undoManager;

    public PolygonButton(UndoManager undoManager, View view, JPanel drawingPanel) {
        super("Polygon");
        this.undoManager = undoManager;
        this.view = view;
        this.drawingPanel = drawingPanel;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MouseHandler mh = new MouseHandler();
        drawingPanel.addMouseListener(mh);
        drawingPanel.addMouseMotionListener(mh);

        view.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    private class MouseHandler extends MouseAdapter implements MouseMotionListener {

        private PolygonCommand polygonCommand = null;

        @Override
        public void mouseClicked(MouseEvent event) {
            Point p = View.mapPoint(event.getPoint());

            // Right click =finish polygon
            if (SwingUtilities.isRightMouseButton(event)) {
                if (polygonCommand != null) {
                    undoManager.endCommand(polygonCommand);
                }
                cleanup();
                return;
            }

            // Left click = Add a vertex
            if (polygonCommand == null) {
                polygonCommand = new PolygonCommand(View.getModel(), p);
                undoManager.beginCommand(polygonCommand);

                // Preview starts at first vertex → zero-length segment
                view.setPolygonPreview(p, p);

            } else {
                polygonCommand.addPoint(p);

                // Update preview start (last vertex)
                Point last = polygonCommand.getPolygon().getPoint(
                        polygonCommand.getPolygon().points.size() - 1);

                view.setPolygonPreview(last, last);
            }
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            updatePreview(event);
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            updatePreview(event);
        }

        private void updatePreview(MouseEvent event) {
            if (polygonCommand == null) return;

            Point mouse = View.mapPoint(event.getPoint());
            Point last = polygonCommand.getPolygon().getPoint(
                    polygonCommand.getPolygon().points.size() - 1
            );

            view.setPolygonPreview(last, mouse);
        }

        private void cleanup() {
            view.clearPolygonPreview();
            drawingPanel.removeMouseListener(this);
            drawingPanel.removeMouseMotionListener(this);
            view.setCursor(Cursor.getDefaultCursor());
        }
    }
}
