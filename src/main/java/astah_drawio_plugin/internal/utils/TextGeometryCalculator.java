package astah_drawio_plugin.internal.utils;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TextGeometryCalculator {

	private static BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

	private static Graphics graphics = dummyImage.getGraphics();

	public static int getTextWidth(String text, String fontName, int fontSize, int fontStyle) {
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

	private static int countTextLine(String text) {
		return text.split("\n").length;
	}

	public static int getTextHeight(String text, String fontName, int fontSize, int fontStyle) {
		Font font = new Font(fontName, fontStyle, fontSize);
		FontMetrics fontMetrics = graphics.getFontMetrics(font);
		return (fontMetrics.getAscent() + fontMetrics.getDescent()) * countTextLine(text);
	}

	public static int getTextHeight(String text, String fontName, int fontSize) {
		return getTextHeight(text, fontName, fontSize, Font.PLAIN);
	}

	public static int getTextHeight(String text, int fontSize) {
		return getTextHeight(text, "Helvetica", fontSize, Font.PLAIN);
	}

	public static int getTextHeight(String text) {
		return getTextHeight(text, "Helvetica", 12, Font.PLAIN);
	}

	public static int getTextHeight() {
		return getTextHeight("", "Helvetica", 12, Font.PLAIN);
	}
}
