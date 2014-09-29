package br.cefet.vsged.util;

import java.awt.Color;
import java.awt.Graphics;

public class Box {


	private static int width;
	private static int height;
	private static int border = 10; // usado para afastar a caixa e os objetos da borda

	public static int getWidth() {
		return width;
	}

	public static void setWidth(int width) {
		Box.width = width;
	}

	public static int getHeight() {
		return height;
	}

	public static void setHeight(int height) {
		Box.height = height;
	}

	public static int getBorder() {
		return border;
	}

	public static void setBorder(int border) {
		Box.border = border;
	}

	public static void drawGrid(Graphics g) {
		for (int i = 20; i < height; i = i + 20) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(0 + border, i + border, width + border, i + border);
			g.setColor(Color.black);
		}
		for (int i = 20; i < width; i = i + 20) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(i + border, 0 + border, i + border, height + border);
			g.setColor(Color.black);
		}
	}

	public static void drawBox(Graphics g) {
		g.drawString("0", 2, 13);
		g.drawString("" + width, width - 3, border);
		g.drawString("" + height, 3, height + (border * 2));
		Arrow.drawArrow(g, border, (border / 2), (width + border), (border / 2));
		Arrow.drawArrow(g, (border / 2), border, (border / 2), (height + border));
		
		g.drawLine(0 + border, 0 + border, 0 + border, height + border);
		g.drawLine(0 + border, 0 + border, width + border, 0 + border);
		g.drawLine(0 + border, height + border, width + border, height + border);
		g.drawLine(width + border, height + border, width + border, 0 + border);
	}
}
