package mydraw.drawables;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import mydraw.DrawGUIs;

/**
 * Command to draw ovals.
 */
public class OvalDrawable extends RectangleDrawable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OvalDrawable(Point pressed, Point released, DrawGUIs window, Color color) {
		super(pressed, released, window, color);
	}

	@Override
	public void draw(Graphics g) {
		if (window.getShapeManager().getCurrentDrawer().getLastx() != -1) {
			// first undraw a rubber rect
			g.setXORMode(color);
			g.setColor(window.getDrawingPanel().getBackground());
			g.drawOval(x, y, width, height);

			window.getShapeManager().getCurrentDrawer().setLastx(-1);
			window.getShapeManager().getCurrentDrawer().setLasty(-1);

		}

		g.setPaintMode();
		g.setColor(color);
		g.drawOval(x, y, width, height);
	}

	@Override
	public String toString() {
		final String name = "oval";
		final String pressed = this.pressed.x + "," + this.pressed.y;
		final String released = this.released.x + "," + this.released.y;
		final String color = this.color.toString();
		return name + ':' + pressed + ':' + released + ':' + color;
	}
}