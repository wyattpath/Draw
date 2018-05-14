package mydraw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class Draw {
    private final DrawGUIs window;

    public Draw() {
        window = new DrawGUIs(this);
    }

    public static void main(String[] args) {
        new Draw();
    }

    /**
     * Returns current drawing color.
     *
     * @return current drawing color
     */
    public String getFGColor() {
        return getKey(window.getColor());
    }

    /**
     * Sets foreground color.
     *
     * @param new_color new color to set
     * @throws ColorException if color is not in Choice
     */
    public void setFGColor(String new_color) throws ColorException {
        final Color color = window.getColorMap().get(new_color.toLowerCase());
        if (color != null)
            window.setColor(color);
        else
            throw new ColorException();
    }

    /**
     * Returns the width of the window.
     *
     * @return width
     */
    public int getWidth() {
        return window.getSize().width;
    }

    /**
     * Returns the height of the window.
     *
     * @return height
     */
    public int getHeight() {
        return window.getSize().height;
    }

    /**
     * Sets the height of the window.
     *
     * @param height new height
     */
    public void setHeight(int height) {
        window.setSize(window.getSize().width, height);
    }

    /**
     * Sets the width of the window.
     *
     * @param width new width
     */
    public void setWidth(int width) {
        window.setSize(width, window.getSize().height);
    }

    /**
     * Sets background color
     *
     * @param new_color new background color
     * @throws ColorException invalid color
     */
    public void setBGColor(String new_color) throws ColorException {
        final Color color = window.getColorMap()
                .get(new_color.toLowerCase());
        if (color != null){

            window.getDrawingPanel().setBackground(color);
        }
        else
            throw new ColorException();

        final Graphics g = window.getDrawingPanel()
                .getGraphics();
        g.setColor(window.getDrawingPanel().getBackground());

        final Graphics gb = window.getBufferedImage()
                .createGraphics();
        gb.setColor(window.getDrawingPanel().getBackground());
    }

    /**
     * Returns the background color of the window.
     *
     * @return background color
     */
    public String getBGColor() {

        return getKey(window.getDrawingPanel().getBackground());
    }

    /**
     * Draws a rectangle.
     *
     * @param upper_left  top left corner
     * @param lower_right bottom right corner
     */
    public void drawRectangle(Point upper_left, Point lower_right) {
        final RectangleCommand cmd = new RectangleCommand(upper_left, lower_right, window.getColor());
        cmd.draw(window.getDrawingPanel().getGraphics());
        cmd.draw(window.getBufferedImage().createGraphics());
        CommandQueue.add(cmd);
    }

	/**
	 * Draws an oval.
	 *
	 * @param upper_left
	 *            top left corner
	 * @param lower_right
	 *            bottom right corner
	 */
	public void drawOval(Point upper_left, Point lower_right) {
		final OvalCommand cmd = new OvalCommand(upper_left, lower_right, window.getColor());
		cmd.draw(window.getDrawingPanel().getGraphics());
        cmd.draw(window.getBufferedImage().createGraphics());
		CommandQueue.add(cmd);
	}

	/**
	 * Draws a polyline.
	 *
	 * @param points
	 *            list of points
	 */
	public void drawPolyLine(java.util.List<Point> points) {
		final ScribbleCommand cmd = new ScribbleCommand(points, window.getColor());
		cmd.draw(window.getDrawingPanel().getGraphics());
        cmd.draw(window.getBufferedImage().createGraphics());
        CommandQueue.add(cmd);
	}

    /**
     * Returns the current drawing.
     *
     * @return drawing
     */
    public Image getDrawing() {
        return window.getBufferedImage();
    }

	/**
	 * Clears drawing panel.
	 */
	public void clear() {
		final FillRectCommand cmd = new FillRectCommand(new Point(0, 0), new Point(window.getDrawingPanel().getWidth(), window.getDrawingPanel().getHeight()),
				window.getDrawingPanel().getBackground());
		cmd.draw(window.getDrawingPanel().getGraphics());
        cmd.draw(window.getBufferedImage().createGraphics());
		CommandQueue.add(cmd);
	}

    /**
     * Draws a predefined image.
     */
    public void autoDraw() {

        try {
            setBGColor("blue");
            clear();
            drawOval(new Point(50, 50), new Point(200, 200));
            setFGColor("red");
            drawRectangle(new Point(100, 100), new Point(300, 300));
            setFGColor("Green");
            final List<Point> list = Arrays.asList(new Point(50, 50), new Point(100, 150), new Point(80, 80));
            drawPolyLine(list);
        } catch (final ColorException e) {
            e.printStackTrace();
        }

    }

    /**
     * Exports an image.
     *
     * @param img      image
     * @param filename filename
     * @throws IOException
     */
    public void writeImage(Image img, String filename) throws IOException {
        ImageIO.write((RenderedImage) img, "PNG", new File(filename));
    }

    /**
     * Reads an image.
     *
     * @param filename filename
     * @return Image image
     * @throws IOException
     */
    public Image readImage(String filename) throws IOException {
        return ImageIO.read(new File(filename));
    }

    /**
     *
     * @param name filename
     * @throws TxtIOException
     */
    public void writeText(String name) throws TxtIOException {
        // TODO writeText
        JFileChooser jfc = new JFileChooser();
        int retVal = jfc.showSaveDialog(null);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            name = f.getAbsolutePath();

        }
        try (FileWriter writer = new FileWriter(name)) {
            for (Drawable str : CommandQueue.getQueue()) {
                writer.write(str.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readText(String name) throws TxtIOException {
        // TODO readText
    }

    /**
     * Removes the last drawn element.
     */
    public void undo() {
        CommandQueue.undo(window.getDrawingPanel().getGraphics());
    }

    /**
     * Inserts the last undone element.
     */
    public void redo() {
        CommandQueue.redo(window.getDrawingPanel().getGraphics());
    }

    private String getKey(Color color) {
        for (final String key : window.getColorMap().keySet()) {
            if (window.getColorMap().get(key).equals(color))
                return key;
        }
        return null;
    }
}