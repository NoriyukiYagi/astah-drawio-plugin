package astah_drawio_plugin.internal.utils;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TextGeometryCalculator {

	public static int getTextWidth(String text, String fontName, int fontSize, int fontStyle) {
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_BINARY);
		Graphics graphics = image.getGraphics();
		Font font = new Font(fontName, fontStyle, fontSize);
		FontMetrics fontMetrics = graphics.getFontMetrics(font);
		return fontMetrics.stringWidth(text);
	}

	public static int getTextWidth(String text, String fontName, int fontSize) {
		return getTextWidth(text, fontName, fontSize, Font.PLAIN);
	}

	public static int getTextWidth(String text, int fontSize) {
		return getTextWidth(text, "Helvetica", fontSize, Font.PLAIN);
	}

	public static int getTextWidth(String text) {
		return getTextWidth(text, "Helvetica", 12, Font.PLAIN);
	}

	public static int getTextHeight(String fontName, int fontSize, int fontStyle) {
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_BINARY);
		Graphics graphics = image.getGraphics();
		Font font = new Font(fontName, fontStyle, fontSize);
		FontMetrics fontMetrics = graphics.getFontMetrics(font);
		return fontMetrics.getAscent() + fontMetrics.getDescent();
	}

	public static int getTextHeight(String fontName, int fontSize) {
		return getTextHeight(fontName, fontSize, Font.PLAIN);
	}

	public static int getTextHeight(int fontSize) {
		return getTextHeight("Helvetica", fontSize, Font.PLAIN);
	}

	public static int getTextHeight() {
		return getTextHeight("Helvetica", 12, Font.PLAIN);
	}
}
