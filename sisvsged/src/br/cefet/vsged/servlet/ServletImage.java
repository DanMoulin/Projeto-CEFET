package br.cefet.vsged.servlet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.cefet.vsged.model.Package;
import br.cefet.vsged.model.Vertex;
import br.cefet.vsged.util.Arrow;
import br.cefet.vsged.util.Box;
import br.cefet.vsged.view.Main;

public class ServletImage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ArrayList<Vertex> vertex = null;
    
    public ServletImage() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String text = request.getParameter("tx");
		String[] t = text.split("-");
		String a = request.getParameter("xpo");
		String b = request.getParameter("ypo");
		int[] x = Main.returnsArrayOfIntegers(a.split("-"));
		int[] y = Main.returnsArrayOfIntegers(b.split("-"));
		int[] houteringTree = Main.returnsArrayOfIntegers(request.getParameter("ht").split("-"));
		int comuRay = Integer.valueOf(request.getParameter("comu"));
		int sensRay = Integer.valueOf(request.getParameter("sens"));
		boolean printSinkPackage = Boolean.valueOf(request.getParameter("psp"));
		boolean printRouteringThree = Boolean.valueOf(request.getParameter("prt"));
		boolean printComuRay = Boolean.valueOf(request.getParameter("pcr"));
		boolean printSensRay = Boolean.valueOf(request.getParameter("psr"));
		boolean printNumPoint = Boolean.valueOf(request.getParameter("pnp"));
		
		if(text != null){
			vertex = new ArrayList<Vertex>();
			vertex.add(new Vertex(x[0], y[0]));
			vertex.get(0).setPackages(new LinkedList<Package>());
			while(vertex.get(0).getPackages().size() < Integer.valueOf(t[3]))
				vertex.get(0).getPackages().add(new Package());
			int j = 1;
			for (int i = 4; i < t.length; i+=4){
				vertex.add(new Vertex(x[j] + Box.getBorder(), y[j] + Box.getBorder(), comuRay, sensRay, 0));
				vertex.get(Integer.valueOf(t[i])).setActivate(Boolean.valueOf(t[i+1]));
				vertex.get(Integer.valueOf(t[i])).resetBathery(Integer.valueOf(t[i+2]));
				vertex.get(Integer.valueOf(t[i])).setPackages(new LinkedList<Package>());
				while(vertex.get(Integer.valueOf(t[i])).getPackages().size() < Integer.valueOf(t[i+3]))
					vertex.get(Integer.valueOf(t[i])).getPackages().add(new Package());
				j++;
			}
		}
		
		int width=484, height=364;  
        BufferedImage buffer = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );  
        Graphics g = buffer.createGraphics(); 
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setColor( Color.black );
        Box.setWidth(width - 24);
        Box.setHeight(height - 24);
		Box.drawGrid(g);
		Box.drawBox(g);
		//-------
		if (vertex.get(0).getPackages().size() != 0 && printSinkPackage) {
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
		//-------
		for (int i = 0; i < vertex.size(); i++) {
			//--------
			if (printComuRay && vertex.get(i).isActivate()) {
				g.setColor(vertex.get(i).getComunication().getColor());
				g.fillOval(vertex.get(i).getX() - vertex.get(i).getComunicationRay() - 4, vertex.get(i).getY()
						- vertex.get(i).getComunicationRay() - 4, vertex.get(i).getComunicationRay() * 2 + 8,
						vertex.get(i).getComunicationRay() * 2 + 8);
			}
			//--------
			if (printSensRay && vertex.get(i).isActivate()) {
				g.setColor(vertex.get(i).getSensing().getColor());
				g.fillOval(vertex.get(i).getX() - vertex.get(i).getSensingRay() - 4,
						vertex.get(i).getY() - vertex.get(i).getSensingRay() - 4,
						vertex.get(i).getSensingRay() * 2 + 8, vertex.get(i).getSensingRay() * 2 + 8);
			}
			//--------
			if (!vertex.get(i).isSink()) {
				if (vertex.get(i).isActivate())
					g.setColor(Color.black);
				else
					g.setColor(Color.red);
				// pintar ponto numerado
				if (printNumPoint) {
					if (vertex.get(i).isActivate()) {
						g.setColor(Color.lightGray);
						g.fillOval(vertex.get(i).getX() - 11, vertex.get(i).getY() - 11, 22, 22);
						g.setColor(Color.black);
						g.drawOval(vertex.get(i).getX() - 11, vertex.get(i).getY() - 11, 22, 22);
					} else {
						g.setColor(Color.white);
						g.fillOval(vertex.get(i).getX() - 11, vertex.get(i).getY() - 11, 22, 22);
						g.setColor(Color.red);
						g.drawOval(vertex.get(i).getX() - 11, vertex.get(i).getY() - 11, 22, 22);
					}
					if (i < 10)
						g.drawString("" + i + "", vertex.get(i).getX() - 3, vertex.get(i).getY() + 5);
					else if (i < 100)
						g.drawString("" + i + "", vertex.get(i).getX() - 7, vertex.get(i).getY() + 5);
					else if (i < 1000)
						g.drawString("" + i + "", vertex.get(i).getX() - 10, vertex.get(i).getY() + 5);
				} else
					// pintar ponto normal
					g.fillOval(vertex.get(i).getX() - 4, vertex.get(i).getY() - 4, 8, 8);
			} else {
				g.setColor(Color.blue);
				g.fillRect(vertex.get(i).getX() + Box.getBorder(), vertex.get(i).getY() + Box.getBorder(), 10, 10);
			}
			//-------
			if (vertex.get(i).getPackages().size() != 0 && printSinkPackage && vertex.get(i).isActivate()) {
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
			}
			//-------
			if (!vertex.get(i).isSink()) {
				if (vertex.get(i).getBathery() < 10)
					g.drawString("" + vertex.get(i).getBathery(), vertex.get(i).getX() - 3, vertex.get(i).getY() - 13);
				else if (vertex.get(i).getBathery() < 100)
					g.drawString("" + vertex.get(i).getBathery(), vertex.get(i).getX() - 7, vertex.get(i).getY() - 13);
				else if (vertex.get(i).getBathery() < 1000)
					g.drawString("" + vertex.get(i).getBathery(), vertex.get(i).getX() - 10, vertex.get(i).getY() - 13);
				else if (vertex.get(i).getBathery() < 10000)
					g.drawString("" + vertex.get(i).getBathery(), vertex.get(i).getX() - 14, vertex.get(i).getY() - 13);
			}
			//-------
		}
		if (printRouteringThree)
			for (int i = 0; i < houteringTree.length; i += 2) {
				Arrow.drawArrow(g, x[houteringTree[i]] + Box.getBorder(),
						y[houteringTree[i]] + Box.getBorder(),
						x[houteringTree[i + 1]] + Box.getBorder(),
						y[houteringTree[i + 1]] + Box.getBorder());
			}
		
		OutputStream out = null;

		response.setContentType("image/png");
        try {
            out = response.getOutputStream();
            // Escreve a imagem no outputstream da response no formato png
    		response.setHeader("Content-Disposition", "attachment; filename=imagem.png");
            ImageIO.write(buffer, "png", out);
        }catch(Exception e){
        	e.printStackTrace();
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	public static void retrieveLog(String text, ArrayList<Vertex> vertex) {
		String[] a = text.split("-");
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

}
