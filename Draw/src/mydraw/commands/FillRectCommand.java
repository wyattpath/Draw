package mydraw.commands;

import mydraw.DrawGUIs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Command to draw a filled rectangle.
 */
public class FillRectCommand extends RectangleCommand {

    public FillRectCommand(Point pressed, Point released, DrawGUIs window, Color color) {
        super(pressed, released, window, color);
    }

    @Override
    public void draw(Graphics g) {
        if (window.getShapeManager().getCurrentDrawer().getLastx() != -1)
        {
            // first undraw a rubber rect
            g.setXORMode(color);
            g.setColor(window.getDrawingPanel().getBackground());
            g.fillRect(x, y, width, height);

            window.getShapeManager().getCurrentDrawer().setLastx(-1);
            window.getShapeManager().getCurrentDrawer().setLasty(-1);
        }
        g.setPaintMode();
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }
}