import java.awt.*;
public class Label extends Item {
  private Point startingPoint;
  private String text = "";
  public Label(Point point) {
    startingPoint = point;
  }
  public void addCharacter(char character) {
    text += character;
  }
  public void removeCharacter() {
    if (text.length() > 0) {
      text = text.substring(0, text.length() - 1);
    }
  }
  public boolean includes(Point point) {
    return distance(point, startingPoint) < 20.0;
  }
 public void render(UIContext uiContext) {
   uiContext.drawLabel(text, startingPoint);
  }
  public void moveBy(int dx, int dy) {
    if (startingPoint != null) {
        startingPoint.translate(dx, dy);
    }
  }

  public String getText() {
    return text;
  }
  public Point getStartingPoint() {
    return startingPoint;
  }
}
