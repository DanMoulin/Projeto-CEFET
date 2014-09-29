package br.cefet.vsged.model;

import java.awt.Color;

public class Ray {

	private int ray;
	private Color color;

	public Ray(int ray, Color color) {
		super();
		this.ray = ray;
		this.color = color;
	}


	public int getRay() {
		return ray;
	}

	public void setRay(int ray) {
		this.ray = ray;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
