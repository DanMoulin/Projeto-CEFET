package br.cefet.vsged.util;

import java.awt.Label;
import java.util.ArrayList;

import br.cefet.vsged.model.Vertex;

public class ToolTip {

	private static Label sensor = new Label();
	private static Label state = new Label();
	private static Label bathery = new Label();
	private static Label position = new Label();
	private static Label send = new Label();
	private static Label received = new Label();
	private static int width;
	
	public static Label getSensor() {
		return sensor;
	}

	public static Label getState() {
		return state;
	}

	public static Label getBathery() {
		return bathery;
	}

	public static Label getPosition() {
		return position;
	}

	public static Label getSend() {
		return send;
	}

	public static Label getReceived() {
		return received;
	}
	
	private static int verifyWidth() {
		if (ToolTip.sensor.getText().length() > ToolTip.state.getText().length()
				&& ToolTip.sensor.getText().length() > ToolTip.bathery.getText().length()
				&& ToolTip.sensor.getText().length() > ToolTip.position.getText().length()
				&& ToolTip.sensor.getText().length() > ToolTip.send.getText().length()
				&& ToolTip.sensor.getText().length() > ToolTip.received.getText().length())
			return ToolTip.sensor.getText().length();
		else if (ToolTip.state.getText().length() > ToolTip.sensor.getText().length()
				&& ToolTip.state.getText().length() > ToolTip.bathery.getText().length()
				&& ToolTip.state.getText().length() > ToolTip.position.getText().length()
				&& ToolTip.state.getText().length() > ToolTip.send.getText().length()
				&& ToolTip.state.getText().length() > ToolTip.received.getText().length())
			return ToolTip.state.getText().length();
		else if (ToolTip.bathery.getText().length() > ToolTip.sensor.getText().length()
				&& ToolTip.bathery.getText().length() > ToolTip.state.getText().length()
				&& ToolTip.bathery.getText().length() > ToolTip.position.getText().length()
				&& ToolTip.bathery.getText().length() > ToolTip.send.getText().length()
				&& ToolTip.bathery.getText().length() > ToolTip.received.getText().length())
			return ToolTip.bathery.getText().length();
		else if (ToolTip.position.getText().length() > ToolTip.sensor.getText().length()
				&& ToolTip.position.getText().length() > ToolTip.state.getText().length()
				&& ToolTip.position.getText().length() > ToolTip.bathery.getText().length()
				&& ToolTip.position.getText().length() > ToolTip.send.getText().length()
				&& ToolTip.position.getText().length() > ToolTip.received.getText().length())
			return ToolTip.position.getText().length();
		else if (ToolTip.send.getText().length() > ToolTip.sensor.getText().length()
				&& ToolTip.send.getText().length() > ToolTip.state.getText().length()
				&& ToolTip.send.getText().length() > ToolTip.bathery.getText().length()
				&& ToolTip.send.getText().length() > ToolTip.position.getText().length()
				&& ToolTip.send.getText().length() > ToolTip.received.getText().length())
			return ToolTip.send.getText().length();
		else
			return ToolTip.received.getText().length();
	}

	public static void setAll(int sensor, String state, int Bathery, int x, int y, int send, int received) {
		ToolTip.sensor.setText("Sensor: " + sensor);
		ToolTip.state.setText("Estado: " + state);
		ToolTip.bathery.setText("Bateria: " + Bathery);
		ToolTip.position.setText("Posição: x: " + x + ", y: " + y);
		ToolTip.send.setText("Enviados: " + send);
		ToolTip.received.setText("Recebidos: " + received);
		
		ToolTip.width = verifyWidth() * 6;
		
		ToolTip.sensor.setSize(width, 20);
		ToolTip.state.setSize(width, 20);
		ToolTip.bathery.setSize(width, 20);
		ToolTip.position.setSize(width, 20);
		ToolTip.send.setSize(width, 20);
		ToolTip.received.setSize(width, 20);
	}
	
	private static void setLocation(int x, int y) {
		ToolTip.sensor.setLocation(x, y);
		ToolTip.state.setLocation(x, y + 20);
		ToolTip.bathery.setLocation(x, y + 40);
		ToolTip.position.setLocation(x, y + 60);
		ToolTip.send.setLocation(x, y + 80);
		ToolTip.received.setLocation(x, y + 100);
	}
	
	public static void setVisible(boolean visible) {
		ToolTip.sensor.setVisible(visible);
		ToolTip.state.setVisible(visible);
		ToolTip.bathery.setVisible(visible);
		ToolTip.position.setVisible(visible);
		ToolTip.send.setVisible(visible);
		ToolTip.received.setVisible(visible);
	}

	public static void show(int x, int y, ArrayList<Vertex> vertex, int widthBox, int heightBox) {
		int xp;
		int yp;
		boolean check = false;
		boolean check2 = false;
		int i;
		for (i = 0; i < vertex.size(); i++) {
			xp = vertex.get(i).getX();
			yp = vertex.get(i).getY();
			if (vertex.get(i).isSink()) {
				if (x >= xp + Box.getBorder() && x <= Box.getBorder() *2 && y >= yp + Box.getBorder() && y <= Box.getBorder() * 2) {
					check2  = true;
					break;
				}
			}
			else if (x >= xp - 8 && x <= xp + 8  && y >= yp - 8 && y <= yp + 8) {
				check = true;
				break;
			}
		}
		if (check) {
			String stateStr;
			if(vertex.get(i).isActivate())
				stateStr = "ativado";
			else
				stateStr = "desativado";
			ToolTip.setAll(i, stateStr, vertex.get(i).getBathery(), vertex.get(i).getX() - Box.getBorder(), vertex.get(i).getY() - Box.getBorder(), 0, 0);
			if (x + width <= widthBox && y + 120 <= heightBox) {
				//label dentro da caixa
				ToolTip.setLocation(x + 10, y + 10);
			}
			else {
				//label fora da caixa
				if(x + width > widthBox && y + 120 < heightBox)// largura
					ToolTip.setLocation(x - ((x + ToolTip.width) - widthBox - 20), y + 10);
				else if(x + width < widthBox && y + 120 > heightBox)// altura
					ToolTip.setLocation(x + 10, y - ((y + 120) - heightBox - 20));
				else// largura e altura
					ToolTip.setLocation(x - ((x + ToolTip.width) - widthBox - 20), y - ((y + 120) - heightBox - 20));
			}
			ToolTip.setVisible(true);
		}
		else if(check2) {
			ToolTip.setAll(0, "sink", 0, vertex.get(i).getX(), vertex.get(i).getY(), 0, 0);
			if (x + width <= widthBox && y + 60 <= heightBox) {
				// label dentro da caixa
				//ToolTip.setLocation(x + 10, y + 10);
				ToolTip.sensor.setLocation(x + 10, y + 10);
				ToolTip.position.setLocation(x + 10, y + 10 + 20);
				ToolTip.received.setLocation(x + 10, y + 10 + 40);
			} else {
				// label fora da caixa
				if (x + width > widthBox) {// largura
					//ToolTip.setLocation(x - ((x + ToolTip.width) - widthBox - 20), y + 10);
					ToolTip.sensor.setLocation(x - ((x + ToolTip.width) - widthBox - 20), y + 10);
					ToolTip.position.setLocation(x - ((x + ToolTip.width) - widthBox - 20), y + 10 + 20);
					ToolTip.received.setLocation(x - ((x + ToolTip.width) - widthBox - 20), y + 10 + 40);
				} else if (y + 60 > heightBox) {// altura
					//ToolTip.setLocation(x + 10, y - ((y + 60) - heightBox - 20));
					ToolTip.sensor.setLocation(x + 10, y - ((y + 60) - heightBox - 20));
					ToolTip.position.setLocation(x + 10, y - ((y + 60) - heightBox - 20) + 20);
					ToolTip.received.setLocation(x + 10, y - ((y + 60) - heightBox - 20) + 40);
				} else {
					// largura e altura
					//ToolTip.setLocation(x - ((x + ToolTip.width) - widthBox - 20), y - ((y + 60) - heightBox - 20));
					ToolTip.sensor.setLocation(x - ((x + ToolTip.width) - widthBox - 20), y - ((y + 60) - heightBox - 20));
					ToolTip.position.setLocation(x - ((x + ToolTip.width) - widthBox - 20), y - ((y + 60) - heightBox - 20) + 20);
					ToolTip.received.setLocation(x - ((x + ToolTip.width) - widthBox - 20), y - ((y + 60) - heightBox - 20) + 40);
				}
			}
			ToolTip.sensor.setVisible(true);
			ToolTip.position.setVisible(true);
			ToolTip.received.setVisible(true);
		}
		else {
			//this.setCursor(Cursor.getDefaultCursor());
			ToolTip.setVisible(false);
		}
	}
}
