package br.cefet.vsged.view;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import br.cefet.vsged.util.Resume;
/* Mudança pra teste Git */

@SuppressWarnings("serial")
public class Menu extends Applet implements Runnable, TextListener, FocusListener {

	private Thread t;
	private static Checkbox drawArrow = null;
	private static Checkbox drawcomray = null;
	private static Checkbox drawsenray = null;
	private static Checkbox drawnumpoints = null;
	private static Button start = null;
	private static TextField txtTime = null;
	private static int time = 1;
	private Label lblIndex = null;
	private Label lblQuantity = null;
	private Label lblStartEnergy = null;
	private Label lblComunicationRay = null;
	private Label lblSensingRay = null;
	private Label lblPercMin = null;
	private Label lblPercAchieved = null;
	private Label lblFinalTime = null;
	private Label lblFinalBathery = null;
	private static boolean startedSimulation = false;
	private static Button batheryLevel = null;
	private static Button sensorsLive = null;
	private static Button coverage = null;
	private static Button pause = null;
	private static boolean paused = false;
	private static Button init = null;
	private static Button previw = null;
	private static Button next = null;
	private static Button end = null;
	private static Button print = null;
	private boolean test = true;
	private static Button go = null;

	public void init() {
		super.init();
		t = new Thread(this);
		t.start();

		drawArrow = new Checkbox("exibir arvore de roteamento");
		drawcomray = new Checkbox("exibir raio de comunicacao");
		drawsenray = new Checkbox("exibir raio de cobertura");
		drawnumpoints = new Checkbox("exibir pontos numerados", true);
		start = new Button(" |> ");
		txtTime = new TextField();
		txtTime.addTextListener(this);
		txtTime.addFocusListener(this);
		lblIndex = new Label();
		lblQuantity = new Label();
		lblStartEnergy = new Label();
		lblComunicationRay = new Label();
		lblSensingRay = new Label();
		lblPercMin = new Label();
		lblPercAchieved = new Label();
		lblFinalTime = new Label();
		lblFinalBathery = new Label();
		batheryLevel = new Button("Gráfico de nivel de bateria");
		sensorsLive = new Button("Gráfico de vida útil de sensores");
		coverage = new Button("Gráfico de cobertura");
		pause = new Button(" || ");
		init = new Button(" << ");
		previw = new Button(" < ");
		next = new Button(" > ");
		end = new Button(" >> ");
		print = new Button("Print");
		go = new Button("Ok");

		add(drawArrow);
		add(drawcomray);
		add(drawsenray);
		add(drawnumpoints);
		add(start);
		add(txtTime);
		add(lblIndex);
		add(lblQuantity);
		add(lblStartEnergy);
		add(lblComunicationRay);
		add(lblSensingRay);
		add(lblPercMin);
		add(lblPercAchieved);
		add(lblFinalTime);
		add(lblFinalBathery);
		add(batheryLevel);
		add(sensorsLive);
		add(coverage);
		add(pause);
		add(init);
		add(previw);
		add(next);
		add(end);
		add(print);
		add(go);
	}

	public void run() {
		while (test) {
			repaint();
		}
	}

	public void update(Graphics g) {
		super.update(g);
		paint(g);
	}

	public void paint(Graphics g) {
		super.paint(g);
		drawArrow.setBounds(10, 20, 220, 15);
		drawcomray.setBounds(10, 40, 220, 15);
		drawsenray.setBounds(10, 60, 220, 15);
		drawnumpoints.setBounds(10, 80, 220, 15);
		
		init.setBounds(10, 100, 40, 20);
		previw.setBounds(52, 100, 40, 20);
		next.setBounds(94, 100, 40, 20);
		end.setBounds(136, 100, 40, 20);
		print.setBounds(178, 100, 40, 20);
		
		txtTime.setBounds(10, 122, 82, 20);
		go.setBounds(94, 122, 40, 20);
		start.setBounds(136, 122, 40, 20);
		pause.setBounds(178, 122, 40, 20);
		
		lblIndex.setBounds(10, 160, 220, 20);
		lblQuantity.setBounds(10, 180, 220, 20);
		lblStartEnergy.setBounds(10, 200, 220, 20);
		lblComunicationRay.setBounds(10, 220, 220, 20);
		lblSensingRay.setBounds(10, 240, 220, 20);
		lblPercMin.setBounds(10, 260, 220, 20);
		lblPercAchieved.setBounds(10, 280, 220, 20);
		lblFinalTime.setBounds(10, 300, 220, 20);
		lblFinalBathery.setBounds(10, 320, 220, 20);
		
		batheryLevel.setBounds(10, 340, 180, 20);
		sensorsLive.setBounds(10, 362, 180, 20);
		coverage.setBounds(10, 384, 180, 20);
		
		lblIndex.setText(Resume.getStrIndex());
		lblQuantity.setText(Resume.getStrQuantity());
		lblStartEnergy.setText(Resume.getStrStartEnergy());
		lblComunicationRay.setText(Resume.getStrComunicationRay());
		lblSensingRay.setText(Resume.getStrSensingRay());
		lblPercMin.setText(Resume.getStrPercMin());
		lblPercAchieved.setText(Resume.getStrPercAchieved());
		lblFinalTime.setText(Resume.getStrFinalTime());
		lblFinalBathery.setText(Resume.getStrFinalBathery());

		if (Menu.time != 0) {
			txtTime.setText("" + Menu.time);
		} else {
			txtTime.setText("");
		}
		//repaint();
	}

	public static Checkbox getDrawArrow() {
		return drawArrow;
	}

	public static void setDrawArrow(Checkbox drawArrow) {
		Menu.drawArrow = drawArrow;
	}

	public static Checkbox getDrawcomray() {
		return drawcomray;
	}

	public static void setDrawcomray(Checkbox drawcomray) {
		Menu.drawcomray = drawcomray;
	}

	public static Checkbox getDrawsenray() {
		return drawsenray;
	}

	public static void setDrawsenray(Checkbox drawsenray) {
		Menu.drawsenray = drawsenray;
	}

	public static Checkbox getDrawnumpoints() {
		return drawnumpoints;
	}

	public static void setDrawnumpoints(Checkbox drawnumpoints) {
		Menu.drawnumpoints = drawnumpoints;
	}

	public static Button getStart() {
		return start;
	}

	public static void setStart(Button start) {
		Menu.start = start;
	}

	public static int getTime() {
		return time;
	}

	public static void setTime(int time) {
		Menu.time = time;
	}

	public static TextField getTxtTime() {
		return txtTime;
	}

	public static boolean isStartedSimulation() {
		return startedSimulation;
	}

	public static void setStartedSimulation(boolean startSimulation) {
		Menu.startedSimulation = startSimulation;
	}

	public static Button getBatheryLevel() {
		return batheryLevel;
	}

	public static void setBatheryLevel(Button batheryLevel) {
		Menu.batheryLevel = batheryLevel;
	}

	public static Button getSensorsLive() {
		return sensorsLive;
	}

	public static void setSensorsLive(Button sensorsLive) {
		Menu.sensorsLive = sensorsLive;
	}

	public static Button getCoverage() {
		return coverage;
	}

	public static void setCoverage(Button coverage) {
		Menu.coverage = coverage;
	}

	public static Button getPause() {
		return pause;
	}

	public static void setPause(Button pause) {
		Menu.pause = pause;
	}

	public static boolean isPaused() {
		return paused;
	}

	public static void setPaused(boolean paused) {
		Menu.paused = paused;
	}

	public static Button getInit() {
		return init;
	}

	public static void setInit(Button init) {
		Menu.init = init;
	}

	public static Button getPreviw() {
		return previw;
	}

	public static void setPreviw(Button previw) {
		Menu.previw = previw;
	}

	public static Button getNext() {
		return next;
	}

	public static void setNext(Button next) {
		Menu.next = next;
	}

	public static Button getEnd() {
		return end;
	}

	public static void setEnd(Button end) {
		Menu.end = end;
	}

	public static Button getPrint() {
		return print;
	}

	public static void setPrint(Button print) {
		Menu.print = print;
	}

	public static Button getGo() {
		return go;
	}

	public static void setGo(Button go) {
		Menu.go = go;
	}

	@Override
	public void textValueChanged(TextEvent e) {
		if(Menu.isStartedSimulation() || test)
			repaint();
		if(!test)
			time = Integer.valueOf(Menu.getTxtTime().getText());
	}

	@Override
	public void focusGained(FocusEvent e) {
		if(!Menu.isStartedSimulation())
			test = false;
	}

	@Override
	public void focusLost(FocusEvent e) {
		test = true;
		repaint();
	}
}
