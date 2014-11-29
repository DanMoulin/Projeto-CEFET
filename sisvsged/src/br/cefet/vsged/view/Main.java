package br.cefet.vsged.view;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import br.cefet.vsged.model.Adjacency;
import br.cefet.vsged.model.Distance;
import br.cefet.vsged.model.Package;
import br.cefet.vsged.model.Vertex;
import br.cefet.vsged.model.log.Caretaker;
import br.cefet.vsged.model.log.Text;
import br.cefet.vsged.util.Arrow;
import br.cefet.vsged.util.Box;
import br.cefet.vsged.util.BreadthFirstSearch;
import br.cefet.vsged.util.Resume;
import br.cefet.vsged.util.Simulation;
import br.cefet.vsged.util.ToolTip;


@SuppressWarnings("serial")
public class Main extends Applet implements Runnable, ItemListener,
		ActionListener {

	private Thread t;
	private ArrayList<Vertex> vertex = new ArrayList<Vertex>();
	private int[] xPositions;
	private int[] yPositions;
	private int quantity;
	private int[] houteringTree;
	private int finalTime = 0;
	private int finalBathery = 0;
	private Distance matrizDistance;
	private Adjacency matrizAdjacency;
	private String batheryData = "";
	private String liveSensorData = "";
	private String coverageData = "";
	private Caretaker log;
	private boolean check = false;
	private float minimumPercentage  = 0;

	public void init() {
		super.init();
		t = new Thread(this);
		t.start();

		xPositions = returnsArrayOfIntegers(getParameter("x").split("-"));
		yPositions = returnsArrayOfIntegers(getParameter("y").split("-"));
		
		quantity = Integer.parseInt(getParameter("quant"));
		Box.setWidth(Integer.parseInt(getParameter("wtela")));
		Box.setHeight(Integer.valueOf(getParameter("htela")));
		
		minimumPercentage = Float.parseFloat(getParameter("minprc"));
		Simulation.setBatheryToSend(Integer.valueOf(getParameter("btosend")));
		Simulation.setBatheryToReceive(Integer.valueOf(getParameter("btoreceive")));
		
		houteringTree = returnsArrayOfIntegers(getParameter("arrows").split("-"));
		
		Menu.setTime(0);
		// inicializa ponto--talvez metodo
		Vertex s = new Vertex(xPositions[0] + Box.getBorder(), yPositions[0] + Box.getBorder());
		vertex.add(s);
		for (int i = 1; i <= quantity; i++) {
			Vertex v = new Vertex(xPositions[i] + Box.getBorder(), yPositions[i] + Box.getBorder(),
					Integer.parseInt(getParameter("comuray")),
					Integer.parseInt(getParameter("sensray")), Integer.valueOf(getParameter("maxbathery")));
			// inclui ponto no array de pontos
			vertex.add(v);
		}

		Menu.getDrawArrow().addItemListener(this);
		Menu.getDrawcomray().addItemListener(this);
		Menu.getDrawsenray().addItemListener(this);
		Menu.getDrawnumpoints().addItemListener(this);
		Menu.getStart().addActionListener(this);
		
		Menu.getBatheryLevel().addActionListener(this);
		Menu.getSensorsLive().addActionListener(this);
		Menu.getCoverage().addActionListener(this);
		Menu.getPause().addActionListener(this);
		Menu.getInit().addActionListener(this);
		Menu.getPreviw().addActionListener(this);
		Menu.getNext().addActionListener(this);
		Menu.getEnd().addActionListener(this);
		Menu.getPrint().addActionListener(this);
		Menu.getGo().addActionListener(this);
		
		Resume.atualizaDados(quantity,
				Integer.parseInt(getParameter("comuray")),
				Integer.parseInt(getParameter("sensray")), finalTime,
				finalBathery, minimumPercentage);

		performBreadthFirstSearch();
		
		log = new Caretaker(new ArrayList<Text>());
		
		add(ToolTip.getSensor());
		add(ToolTip.getState());
		add(ToolTip.getBathery());
		add(ToolTip.getPosition());
		add(ToolTip.getSend());
		add(ToolTip.getReceived());
		
		ToolTip.setVisible(false);
	}

	public static int[] returnsArrayOfIntegers(String[] split) {
		int[] temp = new int[split.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = Integer.parseInt(split[i]);
		}
		return temp;
	}

	public void run() {
		repaint();
	}

	public void update(Graphics g) {
		super.update(g);
		if (Menu.isStartedSimulation() && !Menu.isPaused()) {

			repaint();
			
			Simulation.packageTransference(vertex, houteringTree);
			
			// ------------------------------
			//aqui reune-se os dados que vão ser enviados ao servlet -- metodo
			int bt = 0;
			int ls = 0;
			for (int i = 1; i < vertex.size(); i++) {
				bt += vertex.get(i).getBathery();
				if(vertex.get(i).isActivate() && vertex.get(i).getBathery() >= Integer.valueOf(getParameter("btosend")))
					ls++;
			}
			batheryData += "" + bt;
			liveSensorData += "" + ls;
			coverageData += "" + (int) getAchievedPercentage();
			// ------------------------------
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//----possivel metodo
			boolean verifyBatheryAndActivate = false;
			for (int i = 1; i < BreadthFirstSearch.getNoName(); i++) {
				//aqui se verifica a bateria dos primeiros vertices e se estão ativos
				if (vertex.get(i).getBathery() < 13 || !vertex.get(i).isActivate())
					verifyBatheryAndActivate = true;
			}
			//----
			
			//verifica se a simulação está em condições de prosseguir -- metodo
			if (!verifyBatheryAndActivate && performBreadthFirstSearch() && getAchievedPercentage() >= minimumPercentage) {
				Menu.setTime(Menu.getTime() + 1);
				
				//insere novos pacotes
				for (int i = 1; i < vertex.size(); i++) {
					vertex.get(i).getPackages().add(new Package());
				}
				insertLog();
				
				batheryData += "-";
				liveSensorData += "-";
				coverageData += "-";
			} else {//se a simulação for encerrada, entra aqui
				insertLog();
				Menu.setStartedSimulation(false);
				Menu.setTime(Menu.getTime() + 1);
				//-------------------------------- metodo
				int temp = 0;
				int temp2 = 0;
				for (int i = 1; i < vertex.size(); i++) {
					temp += vertex.get(i).getBathery();
					if(vertex.get(i).isActivate() && vertex.get(i).getBathery() >= Integer.valueOf(getParameter("btosend")))
						temp2++;
				}
				finalTime = Menu.getTime();
				finalBathery = temp;
				batheryData += "-" + temp;
				liveSensorData += "-" + temp2;
				coverageData += "-" + (int) getAchievedPercentage();
				//--------------------------------

				Resume.atualizaDados(quantity,
						Integer.parseInt(getParameter("comuray")),
						Integer.parseInt(getParameter("sensray")), finalTime,
						finalBathery, minimumPercentage);
				Resume.setAchieved(getAchievedPercentage());
				
				//verifica se algum evento interrompeu a simulação
				//----talvez metodo
				if (!performBreadthFirstSearch() || getAchievedPercentage() < minimumPercentage) {
					String string = "Simulação interrompida!\n";
					for (int i = 1; i < vertex.size(); i++)
						if (!vertex.get(i).isActivate())
							string += " - O ponto nº " + i + " apresentou uma falha inesperada;\n";
					if (getAchievedPercentage() < minimumPercentage)
						string += "Percentual de cobertura abaixo do mínimo!";
					else
						string += "Conexão com a rede perdida!";
					JOptionPane.showMessageDialog(null, string);
				}
				//----
				// exibe caixa de mensagem
				JOptionPane.showMessageDialog(null,
						"Simulação terminada!\nDados recebidos pelo sink: "
								+ vertex.get(0).getPackages().size() + "\nEnergia residual: " + temp
								+ "\nTempo: " + Menu.getTime());
				// setar pacotes -- metodo
				for (int i = 0; i < vertex.size(); i++) {
					vertex.get(i).setPackages(new LinkedList<Package>());
				}
				// setar vertices -- metodo
				for (int i = 1; i < vertex.size(); i++) {
					vertex.get(i).resetBathery(Integer.valueOf(getParameter("maxbathery")));
					vertex.get(i).setActivate(true);
				}
				Menu.setTime(0);
				
				repaint();
			}
		}
		else if(Menu.isPaused())
			repaint(0, 0, 1, 1);
	}

	private void insertLog() {
		String text = "";
		for (int i = 0; i < vertex.size(); i++) {
			text += i + "-";
			text += vertex.get(i).isActivate() + "-";
			text += vertex.get(i).getBathery() + "-";
			text += vertex.get(i).getPackages().size();
			if(i!= quantity)
				text += "-";
		}
		Text t = new Text(text);
		log.insert(t);
	}
	private void retrieveLog(int index) {
		String[] a = log.get(index).getText().split("-");
		vertex.get(Integer.valueOf(a[0])).setPackages(new LinkedList<Package>());
		while(vertex.get(Integer.valueOf(a[0])).getPackages().size() < Integer.valueOf(a[3]))
			vertex.get(Integer.valueOf(a[0])).getPackages().add(new Package());
		//-----
		for (int i = 4; i < a.length; i+=4) {
			vertex.get(Integer.valueOf(a[i])).setActivate(Boolean.valueOf(a[i+1]));
			vertex.get(Integer.valueOf(a[i])).resetBathery(Integer.valueOf(a[i+2]));
			vertex.get(Integer.valueOf(a[i])).setPackages(new LinkedList<Package>());
			while(vertex.get(Integer.valueOf(a[i])).getPackages().size() < Integer.valueOf(a[i+3]))
				vertex.get(Integer.valueOf(a[i])).getPackages().add(new Package());
		}
		//-----
	}

	private boolean performBreadthFirstSearch() {
		createMatrizDistance();
		createMatrizAdjacency();

		return BreadthFirstSearch.getResponse(0, matrizAdjacency);
	}

	private void createMatrizAdjacency() {
		matrizAdjacency = new Adjacency(matrizDistance.getSize());
		for (int i = 0; i < matrizAdjacency.getSize(); i++) {
			for (int j = 0; j < matrizAdjacency.getSize(); j++) {
				boolean adjacency;
				if (matrizDistance.getOneDistance(i, j) != 0
						&& matrizDistance.getOneDistance(i, j) <= Integer
								.parseInt(getParameter("comuray")))
					adjacency = true;
				else
					adjacency = false;
				matrizAdjacency.setOneAdjacency(i, j, adjacency);
			}
		}
	}

	private void createMatrizDistance() {
		matrizDistance = new Distance(quantity + 1);
		for (int i = 0; i < matrizDistance.getSize(); i++) {
			int x1;
			int x2;
			int y1;
			int y2;
			boolean firstActivate = false;
			boolean secondActivate = false;
			if (i == 0) {
				x1 = Box.getBorder();
				y1 = Box.getBorder();
				firstActivate = true;
			} else {
				x1 = vertex.get(i).getX();
				y1 = vertex.get(i).getY();
				if(vertex.get(i).isActivate())
					firstActivate = true;
			}

			for (int j = 0; j < matrizDistance.getSize(); j++) {
				if (j == 0) {
					x2 = Box.getBorder();
					y2 = Box.getBorder();
					secondActivate = true;
				} else {
					x2 = vertex.get(j).getX();
					y2 = vertex.get(j).getY();
					if(vertex.get(j).isActivate())
						secondActivate = true;
				}
				int distance = 0;
				if (firstActivate && secondActivate)
					distance = (int) Math.sqrt(Math.pow(x1 - x2, 2)
							+ Math.pow(y1 - y2, 2));
				
				matrizDistance.setOneDistance(i, j, distance);
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		try {
			ToolTip.show(getMousePosition().x, getMousePosition().y, vertex,
					Box.getWidth(), Box.getHeight());
		} catch (Exception e) {
			ToolTip.setVisible(false);
		}

		Box.drawGrid(g);
		Box.drawBox(g);
		
			// dados recebidos pelo sink
		//---- metodo
			if (vertex.get(0).getPackages().size() != 0 && (Menu.isStartedSimulation() || check)) {
				g.setColor(Color.orange);
				if (vertex.get(0).getPackages().size() < 10){
					g.fillRect(Box.getBorder(), 11 + Box.getBorder(), 8, 12);
					g.setColor(Color.black);
					g.drawRect(Box.getBorder(), 11 + Box.getBorder(), 8, 12);
				}
				else if (vertex.get(0).getPackages().size() < 100){
					g.fillRect(Box.getBorder(), 11 + Box.getBorder(), 15, 12);
					g.setColor(Color.black);
					g.drawRect(Box.getBorder(), 11 + Box.getBorder(), 15, 12);
				}
				else if (vertex.get(0).getPackages().size() < 1000){
					g.fillRect(Box.getBorder(), 11 + Box.getBorder(), 22, 12);
					g.setColor(Color.black);
					g.drawRect(Box.getBorder(), 11 + Box.getBorder(), 22, 12);
				}
				
				g.drawString("" + vertex.get(0).getPackages().size() + "", 1 + Box.getBorder(), 22 + Box.getBorder());
			}
		//----
			for (int i = 0; i < vertex.size(); i++) {
				vertex.get(i).drawPoint(g, i);
				drawPackages(g, i);
				vertex.get(i).drawBathery(g);
			}
			drawRouteringTree(g);
			//for (int i = 0; i < vertex.size(); i++)
			for (int i = vertex.size() - 1; i >= 0; i--) {
				vertex.get(i).drawComunicationRay(g);
				vertex.get(i).drawSensingRay(g);
				vertex.get(i).drawPoint(g, i);
				drawPackages(g, i);
				vertex.get(i).drawBathery(g);
			}
			drawRouteringTree(g);
			Resume.setAchieved(getAchievedPercentage());
		
	}

	public float getAchievedPercentage() {
		int t = 0;
		int q = 0;
		double temp;
		for (int i = 10 + Box.getBorder(); i < Box.getWidth() + Box.getBorder(); i += 20) {
			for (int j = 10 + Box.getBorder(); j < Box.getHeight() + Box.getBorder(); j += 20) {
				q++;
				for (int j2 = 1; j2 < vertex.size(); j2++) {
					if (vertex.get(j2).isActivate() && vertex.get(j2).getBathery() >= Integer.valueOf(getParameter("btosend"))) {
						temp = Math.sqrt(Math.pow(j - vertex.get(j2).getY(), 2) + Math.pow(vertex.get(j2).getX() - i, 2));
						if (temp <= (Integer.parseInt(getParameter("comuray")) + 8)) {
							t++;
							break;
						}
					}
				}
			}
		}

		return (100 * t) / q;
	}

	public void drawRouteringTree(Graphics g) {
		// pintar arvore de roteamento
		if (Menu.getDrawArrow().getState())
			for (int i = 0; i < houteringTree.length; i += 2) {
				Arrow.drawArrow(g, xPositions[houteringTree[i]] + Box.getBorder(),
						yPositions[houteringTree[i]] + Box.getBorder(),
						xPositions[houteringTree[i + 1]] + Box.getBorder(),
						yPositions[houteringTree[i + 1]] + Box.getBorder());
			}
	}

	public void drawPackages(Graphics g, int i) {
		// pintar pacotes
		if (vertex.get(i).getPackages().size() != 0 && (Menu.isStartedSimulation() || check)
				&& vertex.get(i).isActivate()) {
			int xp = vertex.get(i).getX() - 10;
			int yp = vertex.get(i).getY() + 10;
			
			g.setColor(Color.orange);
			if (vertex.get(i).getPackages().size() < 10){
				g.fillRect(xp, yp, 8, 12);
				g.setColor(Color.black);
				g.drawRect(xp, yp, 8, 12);
			}
			else if (vertex.get(i).getPackages().size() < 100){
				g.fillRect(xp, yp, 15, 12);
				g.setColor(Color.black);
				g.drawRect(xp, yp, 15, 12);
			}
			else if (vertex.get(i).getPackages().size() < 1000){
				g.fillRect(xp, yp, 22, 12);
				g.setColor(Color.black);
				g.drawRect(xp, yp, 22, 12);
			}
			
			g.drawString("" + vertex.get(i).getPackages().size() + "", 1 + xp, 11 + yp);
			/*
			for (int j = 1; j <= packages[i]; j++) {

				g.setColor(Color.orange);
				g.fillRect(xp, yp, 13, 10);
				g.setColor(Color.black);
				g.drawRect(xp, yp, 13, 10);
				xp += 2;
				yp += 2;
			}*/
		}
	}
	
	public void callServlet(int act, String title, String rowKey, String info) {
		title = title.replace(" ", "-");
		rowKey = rowKey.replace(" ", "-");
		try {
	        URL servlet = new URL("http://localhost:8080/sisvsged/ServletGraphic?act="+act+"&title="+title+"&rk="+rowKey+"&gd="+info);
	        URLConnection servletConnection = servlet.openConnection();
	        servletConnection.getInputStream();
		}catch (Exception e) {
	        System.out.println(e.toString());
	    }

		String link = "http://localhost:8080/sisvsged/graphic.jsp";
		try {
			AppletContext a = getAppletContext();
			URL url = new URL(link);
			a.showDocument(url, "_blank");
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void itemStateChanged(ItemEvent e) {
		repaint();
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand() == " |> ") {
			if(Menu.getPause().isEnabled()) {
				Menu.setStartedSimulation(true);
				check = false;

				// insere novos pacotes
				// ---- metodo -- com alterações
				vertex.get(0).setPackages(new LinkedList<Package>());
				for (int i = 1; i < vertex.size(); i++) {
					vertex.get(i).setPackages(new LinkedList<Package>());
					vertex.get(i).getPackages().add(new Package());
				}
				// ----
				for (int i = 1; i < vertex.size(); i++) {
					vertex.get(i).resetBathery(Integer.valueOf(getParameter("maxbathery")));
					vertex.get(i).setActivate(true);
				}
				Menu.setTime(1);
				// ----
				log = new Caretaker(new ArrayList<Text>());
				insertLog();

				//JOptionPane.showMessageDialog(null, "Simulação iniciada !");
				// ----grafico de nivel de bateria
				//---- metodo
				int bt = 0;
				int ls = 0;
				for (int i = 1; i < vertex.size(); i++) {
					bt += vertex.get(i).getBathery();
					if(vertex.get(i).isActivate() && vertex.get(i).getBathery() >= Integer.valueOf(getParameter("btosend")))
						ls++;
				}
				batheryData = "" + bt + "-";
				liveSensorData = "" + ls + "-";
				coverageData = "" + (int) getAchievedPercentage() + "-";
				//----
			}
			else {
				Menu.getPause().setEnabled(true);
				Menu.setPaused(false);
			}
			// ------------------------------
			repaint();
		}
		else if (ae.getActionCommand() == " || ") {
			Menu.getPause().setEnabled(false);
			Menu.setPaused(true);
		}
		else if (ae.getActionCommand() == " << ") {
			check = true;
			Menu.setTime(1);
			retrieveLog(0);
			repaint();
		}
		else if (ae.getActionCommand() == " >> ") {
			check = true;
			Menu.setTime(finalTime);
			retrieveLog(finalTime - 1);
			repaint();
		}
		else if (ae.getActionCommand() == " < ") {
			//---- metodo
			check = true;
			int index;
			if(Menu.getTime() <= 1)
				index = 0;
			else {
				Menu.setTime(Menu.getTime() - 1);
				index = Menu.getTime() - 1;
			}
			retrieveLog(index);
			repaint();
			//----
		}
		else if (ae.getActionCommand() == " > ") {
			check = true;
			int index;
			if(Menu.getTime() >= (finalTime))
				index = finalTime - 1;
			else {
				Menu.setTime(Menu.getTime() + 1);
				index = Menu.getTime() - 1;
			}
			retrieveLog(index);
			repaint();
		}
		else if (ae.getActionCommand() == "Ok") {
			check = true;
			int index;
			if(Menu.getTime() <= 1){
				index = 0;
				Menu.setTime(index + 1);
			}
			else if(Menu.getTime() >= (finalTime)){
				index = finalTime - 1;
				Menu.setTime(index + 1);
			}
			else {
				index = Menu.getTime() - 1;
			}
			retrieveLog(index);
			repaint();
		}
		else if(ae.getActionCommand() == "Print"){
			String text = "";
			for (int i = 0; i < vertex.size(); i++) {
				text += i + "-";
				text += vertex.get(i).isActivate() + "-";
				text += vertex.get(i).getBathery() + "-";
				text += vertex.get(i).getPackages().size();
				if(i!= quantity)
					text += "-";
			}

			boolean psp = false;
			if(Menu.isStartedSimulation() || check)
				psp = true;
			
			String link = "http://localhost:8080/sisvsged/ServletImage?tx="
					+ text + "&xpo=" + getParameter("x") + "&ypo="
					+ getParameter("y") + "&ht=" + getParameter("arrows")
					+ "&comu=" + getParameter("comuray") + "&sens="
					+ getParameter("sensray") + "&psp=" + psp + "&prt="
					+ Menu.getDrawArrow().getState() + "&pcr="
					+ Menu.getDrawcomray().getState() + "&psr="
					+ Menu.getDrawsenray().getState() + "&pnp="
					+ Menu.getDrawnumpoints().getState();
			try {
				AppletContext a = getAppletContext();
				URL url = new URL(link);
				a.showDocument(url, "_blank");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		else if(ae.getActionCommand() == "Gráfico de nivel de bateria") {
			callServlet(1, "Gráfico de nível de bateria", "bateria total", batheryData);
		}
		else if(ae.getActionCommand() == "Gráfico de vida útil de sensores") {
			callServlet(1, "Gráfico de vida útil de sensores", "sensores ativos", liveSensorData);
		}
		else if(ae.getActionCommand() == "Gráfico de cobertura") {
			callServlet(1, "Gráfico de cobertura", "cobertira atingida", coverageData);
		}
	}

	@Override
	@Deprecated
	public boolean mouseMove(Event e, int x, int y) {
		if(!Menu.isStartedSimulation())
			ToolTip.show(x, y, vertex, Box.getWidth(), Box.getHeight());
		return super.mouseMove(e, x, y);
	}
}
