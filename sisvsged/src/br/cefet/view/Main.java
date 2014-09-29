package br.cefet.view;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import br.cefet.control.Arrow;
import br.cefet.control.Box;
import br.cefet.control.BreadthFirstSearch;
import br.cefet.control.Probability;
import br.cefet.control.Resume;
import br.cefet.control.ToolTip;
import br.cefet.model.Adjacency;
import br.cefet.model.Distance;
import br.cefet.model.Vertex;
import br.cefet.model.log.Caretaker;
import br.cefet.model.log.Text;


@SuppressWarnings("serial")
public class Main extends Applet implements Runnable, ItemListener,
		ActionListener {
//teste
	private Thread t;
	private ArrayList<Vertex> vertex = new ArrayList<Vertex>();
	private int[] xPositions;
	private int[] yPositions;
	private int quantity;
	private int[] houteringTree;
	private int[] packages; // dados sensoriados e recebidos dos pontos
	private int finalTime = 0;
	private int finalBathery = 0;
	private Distance matrizDistance;
	private Adjacency matrizAdjacency;
	private String batheryData = "";
	private String liveSensorData = "";
	private String coverageData = "";
	private Caretaker log;
	private boolean check = false;

	public void init() {
		super.init();
		t = new Thread(this);
		t.start();

		xPositions = returnsArrayOfIntegers(getParameter("x").split("-"));
		yPositions = returnsArrayOfIntegers(getParameter("y").split("-"));
		
		quantity = Integer.parseInt(getParameter("quant"));
		Box.setWidth(Integer.parseInt(getParameter("wtela")));
		Box.setHeight(Integer.parseInt(getParameter("htela")));
		
		houteringTree = returnsArrayOfIntegers(getParameter("arrows").split("-"));
		
		packages = new int[quantity + 1];
		for (int i = 0; i < packages.length; i++) {
			packages[i] = 0;
		}
		Menu.setTime(0);
		// inicializa ponto--talvez metodo
		Vertex s = new Vertex(xPositions[0] + Box.getBorder(), yPositions[0] + Box.getBorder());
		vertex.add(s);
		for (int i = 1; i <= quantity; i++) {
			Vertex v = new Vertex(xPositions[i] + Box.getBorder(), yPositions[i] + Box.getBorder(),
					Integer.parseInt(getParameter("comuray")),
					Integer.parseInt(getParameter("sensray")), 3000);
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
		
		Resume.atualizaDados(quantity,
				Integer.parseInt(getParameter("comuray")),
				Integer.parseInt(getParameter("sensray")), finalTime,
				finalBathery);

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

	private int[] returnsArrayOfIntegers(String[] split) {
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
			
			for (int k = 1; k < packages.length; k++) {
				//verifica se o sensor foi desativado
				if (Probability.pointNotWorks())
					vertex.get(k).setActivate(false);
				//verifica se o vertice possui pacotes a enviar e se tem bateria--possivel metodo
				if (packages[k] != 0 && vertex.get(k).getBathery() > 13
						&& vertex.get(k).isActivate())
					for (int j = 0; j < houteringTree.length; j += 2) {
						if (houteringTree[j] == k) {//localiza o vertice no array da arvore
							//verifica se o vertice de destino não é o sink e se esta ativado--possivel metodo
							if (houteringTree[j + 1] != 0
									&& vertex.get(houteringTree[j + 1]).isActivate()) {
								// *** deve ser (13 * packages[k])
								while (true) {
									//se o vertive de horigem não tiver pacotes ou bateria
									//e se o vertice de destino não tiver bateria, sai do loop
									if (packages[k] == 0
											|| vertex.get(k).getBathery() <= 13
											|| vertex.get(houteringTree[j + 1]).getBathery() <= 2)
										break;
									//se o pacote for perdido o vertice de horigem mantem a bateria
									//e a quantidade de pacotes atual
									else if (Probability.packageLost()) {
										vertex.get(k).setBathery(13);
										packages[k] -= 1;
									} else {//se estiver tudo ok o procedimento e normal
										vertex.get(k).setBathery(13);
										vertex.get(houteringTree[j + 1]).setBathery(2);
										packages[houteringTree[j + 1]] += 1;
										packages[k] -= 1;
									}
								}
							} else {//se o vertice de destino for o sink ou estiver desativado entra aqui
								if (houteringTree[j + 1] == 0) {
									//este loop é muito parecido com o da condição anterior
									//a unica diferença é que não é necessario diminuir a bateria
									//do vertice de destino pois ele é o sink
									while (true) {
										if (packages[k] == 0
												|| vertex.get(k)
														.getBathery() <= 13)
											break;
										else if (Probability.packageLost()) {
											vertex.get(k).setBathery(13);
											packages[k] -= 1;
										} else {
											vertex.get(k).setBathery(13);
											packages[houteringTree[j + 1]] += 1;
											packages[k] -= 1;
										}
									}
								}
							}
						}
					}
			}

			//insertLog();
			
			// ------------------------------
			//aqui reune-se os dados que vão ser enviados ao servlet -- metodo
			int bt = 0;
			int ls = 0;
			for (int i = 1; i < vertex.size(); i++) {
				bt += vertex.get(i).getBathery();
				if(vertex.get(i).isActivate())
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
			/*if (((vertex.get(0).getBathery() > 13 && vertex.get(0).isActivate()) && (vertex
					.get(1).getBathery() > 13 && vertex.get(1).isActivate()))
					&& performBreadthFirstSearch()) {*/
			//vertex.get(10).setActivate(false);
			//vertex.get(7).setActivate(false);
			//vertex.get(9).setActivate(false);
			
			//verifica se a simulação está em condições de prosseguir -- metodo
			if (!verifyBatheryAndActivate && performBreadthFirstSearch() && getAchievedPercentage() >= 90) {
				Menu.setTime(Menu.getTime() + 1);
				
				//insere novos pacotes
				for (int i = 1; i < packages.length; i++) {
					packages[i] += 1;
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
					if(vertex.get(i).isActivate())
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
						finalBathery);
				Resume.setAchieved(getAchievedPercentage());
				
				//verifica se algum evento interrompeu a simulação
				//----talvez metodo
				if (!performBreadthFirstSearch() || getAchievedPercentage() < 90) {
					String string = "Simulação interrompida!\n";
					for (int i = 1; i < vertex.size(); i++)
						if (!vertex.get(i).isActivate())
							string += " - O ponto nº " + i + " apresentou uma falha inesperada;\n";
					if (getAchievedPercentage() < 90)
						string += "Percentual de cobertura abaixo do mínimo!";
					else
						string += "Conexão com a rede perdida!";
					JOptionPane.showMessageDialog(null, string);
				}
				//----
				// exibe caixa de mensagem
				JOptionPane.showMessageDialog(null,
						"Simulação terminada!\nDados recebidos pelo sink: "
								+ packages[0] + "\nEnergia residual: " + temp
								+ "\nTempo: " + Menu.getTime());
				// setar pacotes -- metodo
				for (int i = 0; i < packages.length; i++) {
					packages[i] = 0;
				}
				// setar vertices -- metodo
				for (int i = 1; i < vertex.size(); i++) {
					vertex.get(i).resetBathery(3000);
					vertex.get(i).setActivate(true);
				}
				Menu.setTime(0);
				/*try {
			         AppletContext a = getAppletContext();
			         URL url = new URL("http://localhost:8080/sisvsged/ServletGraphic?batherydata="+batheryData);
			         a.showDocument(url,"_blank");
			      }
			      catch (MalformedURLException e){
			         System.out.println(e.getMessage());
			      }*/
				/*try {
			        URL servlet = new URL("http://localhost:8080/sisvsged/ServletGraphic?batherydata="+batheryData);
			        URLConnection servletConnection = servlet.openConnection();
			        servletConnection.getInputStream();
				}catch (Exception e) {
			        System.out.println(e.toString());
			    }*/
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
			text += packages[i];
			if(i!= quantity)
				text += "-";
		}
		Text t = new Text(text);
		log.insert(t);
	}
	private void retrieveLog(int index) {
		String[] a = log.get(index).getText().split("-");
		packages[Integer.valueOf(a[0])] = Integer.valueOf(a[3]);
		//-----
		for (int i = 4; i < a.length; i+=4) {
			vertex.get(Integer.valueOf(a[i])).setActivate(Boolean.valueOf(a[i+1]));
			vertex.get(Integer.valueOf(a[i])).resetBathery(Integer.valueOf(a[i+2]));
			packages[Integer.valueOf(a[i])] = Integer.valueOf(a[i+3]);
		}
		//-----
	}

	private boolean performBreadthFirstSearch() {
		// --------- matriz de distancias --------- metodo
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
		// ---------------------------------------
		// -------- matriz de adjacência --------- metodo
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
		// ---------------------------------------

		return BreadthFirstSearch.getResponse(0, matrizAdjacency);
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
		//if(check) {
		//	retrieveLog(0);
			/*String[] a = log.get(0).getText().split("-");
			packages[Integer.valueOf(a[0])] = Integer.valueOf(a[3]);
			//-----
			for (int i = 4; i < a.length; i+=4) {
				vertex.get(Integer.valueOf(a[i])).setActivate(Boolean.valueOf(a[i+1]));
				vertex.get(Integer.valueOf(a[i])).resetBathery(Integer.valueOf(a[i+2]));
				packages[Integer.valueOf(a[i])] = Integer.valueOf(a[i+3]);
			}*/
			//-----
		//}
		//if(!check) {
			// sink
			//g.setColor(Color.blue);
			//g.fillRect(xPositions[0] + Box.getBorder(), yPositions[0] + Box.getBorder(), 10, 10);
			// dados recebidos pelo sink
		//---- metodo
			if (packages[0] != 0 && (Menu.isStartedSimulation() || check)) {
				g.setColor(Color.orange);
				g.fillRect(11 + Box.getBorder(), 11 + Box.getBorder(), 20, 10);
				g.setColor(Color.black);
				g.drawRect(11 + Box.getBorder(), 11 + Box.getBorder(), 20, 10);
				
				g.drawString("" + packages[0] + "", 11 + Box.getBorder(), 21 + Box.getBorder());
			}
		//----
			for (int i = 0; i < vertex.size(); i++) {
				vertex.get(i).drawPoint(g, i);
				drawPackages(g, i);
				vertex.get(i).drawBathery(g);
			}
			drawRouteringTree(g);
			//for (int i = 0; i < vertex.size(); i++)
			for (int i = vertex.size() - 1; i >= 1; i--) {
				vertex.get(i).drawComunicationRay(g);
				vertex.get(i).drawSensingRay(g);
				vertex.get(i).drawPoint(g, i);
				drawPackages(g, i);
				vertex.get(i).drawBathery(g);
			}
			drawRouteringTree(g);
			Resume.setAchieved(getAchievedPercentage());
		/*}
		else {
			//JOptionPane.showMessageDialog(null, log.get(0).getText());
			//JOptionPane.showMessageDialog(null, log.get(1).getText());
			//JOptionPane.showMessageDialog(null, log.get(2).getText());
			//int[] a = returnsArrayOfIntegers(log.get(0).getText().split("-"));
			String[] a = log.get(0).getText().split("-");
			packages[Integer.valueOf(a[0])] = Integer.valueOf(a[3]);
			//-----
			for (int i = 4; i < a.length; i+=4) {
				vertex.get(Integer.valueOf(a[i])).setActivate(Boolean.valueOf(a[i+1]));
				vertex.get(Integer.valueOf(a[i])).resetBathery(Integer.valueOf(a[i+2]));
				packages[Integer.valueOf(a[i])] = Integer.valueOf(a[i+3]);
			}
			//-----
			for (int i = 0; i < vertex.size(); i++) {
				vertex.get(i).drawPoint(g, i);
				drawPackages(g, i);
				vertex.get(i).drawBathery(g);
			}
			/*String[] a = log.get(0).getText().split("-"); 
			ArrayList<Vertex> v2 = vertex;
			int[] p2 = packages;
			p2[Integer.valueOf(a[0])] = Integer.valueOf(a[3]);
			for (int i = 4; i <= a.length; i+=4) {
				v2.get(Integer.valueOf(a[i])).setActivate(Boolean.valueOf(a[i+1]));
				v2.get(Integer.valueOf(a[i])).resetBathery(Integer.valueOf(a[i+2]));
				p2[Integer.valueOf(a[i])] = Integer.valueOf(a[i+3]);
			}
			for (int i = 0; i < v2.size(); i++) {
				v2.get(i).drawComunicationRay(g);
				v2.get(i).drawSensingRay(g);
				v2.get(i).drawPoint(g, i);
				//---
				if (p2[i] != 0 && v2.get(i).isActivate()) {
					int xp = v2.get(i).getX() + 4;
					int yp = v2.get(i).getY() + 4;
					for (int j = 1; j <= p2[i]; j++) {

						g.setColor(Color.orange);
						g.fillRect(xp, yp, 13, 10);
						g.setColor(Color.black);
						g.drawRect(xp, yp, 13, 10);
						xp += 2;
						yp += 2;
					}
				}
				//---
				v2.get(i).drawBathery(g);
			}
		}*/
	}

	public float getAchievedPercentage() {
		int t = 0;
		int q = 0;
		double temp;
		for (int i = 10 + Box.getBorder(); i < Box.getWidth() + Box.getBorder(); i += 20) {
			for (int j = 10 + Box.getBorder(); j < Box.getHeight() + Box.getBorder(); j += 20) {
				q++;
				for (int j2 = 1; j2 < vertex.size(); j2++) {
					if (vertex.get(j2).isActivate()) {
						temp = Math.sqrt(Math.pow(j
								- vertex.get(j2).getY(), 2)
								+ Math.pow(vertex.get(j2).getX()
										- i, 2));
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
		if (packages[i] != 0 && (Menu.isStartedSimulation() || check)
				&& vertex.get(i).isActivate()) {
			int xp = vertex.get(i).getX() + 4;
			int yp = vertex.get(i).getY() + 4;
			for (int j = 1; j <= packages[i]; j++) {

				g.setColor(Color.orange);
				g.fillRect(xp, yp, 13, 10);
				g.setColor(Color.black);
				g.drawRect(xp, yp, 13, 10);
				xp += 2;
				yp += 2;
			}
		}
	}

	public void itemStateChanged(ItemEvent e) {
		repaint();
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand() == " |> ") {
			Menu.setStartedSimulation(true);
			check = false;
			
			//insere novos pacotes
			//---- metodo -- com alterações
			packages[0] = 0;
			for (int i = 1; i < packages.length; i++) {
				packages[i] = 1;
			}
			//----
			for (int i = 1; i < vertex.size(); i++) {
				vertex.get(i).resetBathery(3000);
				vertex.get(i).setActivate(true);
			}
			Menu.setTime(1);
			//----
			log = new Caretaker(new ArrayList<Text>());
			insertLog();
			
			if(Menu.getPause().isEnabled()) {
				JOptionPane.showMessageDialog(null, "Simulação iniciada !");
				// ----grafico de nivel de bateria
				//---- metodo
				int bt = 0;
				int ls = 0;
				for (int i = 1; i < vertex.size(); i++) {
					bt += vertex.get(i).getBathery();
					if(vertex.get(i).isActivate())
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
				index = Menu.getTime();
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
				index = Menu.getTime();
			}
			retrieveLog(index);
			repaint();
		}
		else if(ae.getActionCommand() == "Gráfico de nivel de bateria") {
			//---- metodo
			try {
		        URL servlet = new URL("http://localhost:8080/sisvsged/ServletGraphic?act=1&title=Gráfico-de-nível-de-bateria&rk=bateria-total&gd="+batheryData);
		        URLConnection servletConnection = servlet.openConnection();
		        servletConnection.getInputStream();
			}catch (Exception e) {
		        System.out.println(e.toString());
		    }
			//----
		}
		else if(ae.getActionCommand() == "Gráfico de vida útil de sensores") {
			try {
		        URL servlet = new URL("http://localhost:8080/sisvsged/ServletGraphic?act=1&title=Gráfico-de-vida-útil-de-sensores&rk=sensores-ativos&gd="+liveSensorData);
		        URLConnection servletConnection = servlet.openConnection();
		        servletConnection.getInputStream();
			}catch (Exception e) {
		        System.out.println(e.toString());
		    }
		}
		else if(ae.getActionCommand() == "Gráfico de cobertura") {
			try {
		        URL servlet = new URL("http://localhost:8080/sisvsged/ServletGraphic?act=1&title=Gráfico-de-cobertura&rk=cobertura-atingida&gd="+coverageData);
		        URLConnection servletConnection = servlet.openConnection();
		        servletConnection.getInputStream();
			}catch (Exception e) {
		        System.out.println(e.toString());
		    }
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
