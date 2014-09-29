package br.cefet.vsged.model;

import java.awt.Color;
import java.awt.Graphics;

import br.cefet.vsged.util.Box;
import br.cefet.vsged.view.Menu;


public class Vertex {

	private int x;
	private int y;
	private int bathery;
	private Ray comunication;
	private Ray sensing;
	private boolean activate;
	private boolean isSink = false;

	public Vertex(int x, int y, int comuRay, int sensRay, int bathery) {
		super();
		this.x = x;
		this.y = y;
		if (!isSink) {
			this.bathery = bathery;
			comunication = new Ray(comuRay, new Color(.7f, 0f, 0f, .12f));
			sensing = new Ray(sensRay, new Color(0f, .5f, 0f, .12f));
			activate = true;
		} else {
			this.bathery = 0;
			comunication = null;
			sensing = null;
			activate = false;
			isSink = true;
		}
	}

	public Vertex(int x, int y) {
		super();
		this.bathery = 0;
		comunication = null;
		sensing = null;
		bathery = 0;
		activate = false;
		isSink = true;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getComunicationRay() {
		return comunication.getRay();
	}

	public int getSensingRay() {
		return sensing.getRay();
	}

	public void drawComunicationRay(Graphics g) {
		if (Menu.getDrawcomray().getState() && activate) {
			g.setColor(comunication.getColor());
			g.fillOval(x - comunication.getRay() - 4, y
					- comunication.getRay() - 4, comunication.getRay() * 2 + 8,
					comunication.getRay() * 2 + 8);
		}
	}

	public void drawSensingRay(Graphics g) {
		if (Menu.getDrawsenray().getState() && activate) {
			g.setColor(sensing.getColor());
			g.fillOval(x - sensing.getRay() - 4,
					y - sensing.getRay() - 4,
					sensing.getRay() * 2 + 8, sensing.getRay() * 2 + 8);
		}
	}

	public void drawPoint(Graphics g, int i) {
		if (!isSink) {
			if (activate)
				g.setColor(Color.black);
			else
				g.setColor(Color.red);
			// pintar ponto numerado
			if (Menu.getDrawnumpoints().getState()) {
				if (activate) {
					g.setColor(Color.lightGray);
					g.fillOval(this.x - 11, this.y - 11, 22, 22);
					g.setColor(Color.black);
					g.drawOval(this.x - 11, this.y - 11, 22, 22);
				} else {
					g.setColor(Color.white);
					g.fillOval(this.x - 11, this.y - 11, 22, 22);
					g.setColor(Color.red);
					g.drawOval(this.x - 11, this.y - 11, 22, 22);
				}
				if (i < 10)
					g.drawString("" + i + "", this.x - 3, this.y + 5);
				else if (i < 100)
					g.drawString("" + i + "", this.x - 7, this.y + 5);
				else if (i < 1000)
					g.drawString("" + i + "", this.x - 10, this.y + 5);
			} else
				// pintar ponto normal
				g.fillOval(this.x - 4, this.y - 4, 8, 8);
		} else {
			g.setColor(Color.blue);
			g.fillRect(this.x + Box.getBorder(), this.y + Box.getBorder(), 10, 10);
		}
	}

	public int getBathery() {
		return bathery;
	}

	public void setBathery(int bathery) {
		this.bathery -= bathery;
	}

	public void resetBathery(int bathery) {
		this.bathery = bathery;
	}

	public void drawBathery(Graphics g) {
		// pintar bateria
		if (!isSink) {
			if (bathery < 10)
				g.drawString("" + bathery, x - 3, y - 13);
			else if (bathery < 100)
				g.drawString("" + bathery, x - 7, y - 13);
			else if (bathery < 1000)
				g.drawString("" + bathery, x - 10, y - 13);
			else if (bathery < 10000)
				g.drawString("" + bathery, x - 14, y - 13);
		}
	}

	public boolean isActivate() {
		return activate;
	}

	public void setActivate(boolean activate) {
		this.activate = activate;
	}

	public boolean isSink() {
		return isSink;
	}
}
