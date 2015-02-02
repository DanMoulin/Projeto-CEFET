package br.cefet.vsged.util;

import java.awt.Color;
import java.awt.Graphics;


public class Arrow {

	private static int al = 10;	// Arrow length
	private static int aw = 8;		// Arrow width
	private static int haw = aw/2;	// Half arrow width
	private static int xValues[] = new int[3];
	private static int yValues[] = new int[3];
	
	public static void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
		// ---------- Ponto médio ------------
		// -- x de PM = ((x1 + x2) / 2), () --
		// -- y de PM = ((y1 + y2) / 2), () --
		int xa = (x1 + ((x1 + x2) / 2)) / 2;
		int ya = (y1 + ((y1 + y2) / 2)) / 2;
		int xb = (((x1 + x2) / 2) + x2) / 2;
		int yb = (((y1 + y2) / 2) + y2) / 2;
		// -----------------------------------
		g.setColor(Color.black);
		g.drawLine(xa,ya,xb,yb);
		// Calculate x-y values for arrow head
		calcValues(xa,ya,xb,yb);
		g.fillPolygon(xValues,yValues,3);
	}
	
    /* CALC VALUES: Calculate x-y values. */
    public static void calcValues(int x1, int y1, int x2, int y2) {
		// North or south	
		if (x1 == x2) { 
			// North
			if (y2 < y1) arrowCoords(x2,y2,x2-haw,y2+al,x2+haw,y2+al);
			// South
			else arrowCoords(x2,y2,x2-haw,y2-al,x2+haw,y2-al);
			return;
		}	
		// East or West	
		if (y1 == y2) {
			// East
			if (x2 > x1) arrowCoords(x2,y2,x2-al,y2-haw,x2-al,y2+haw);
			// West
			else arrowCoords(x2,y2,x2+al,y2-haw,x2+al,y2+haw);
			return;
		}
		// Calculate quadrant
		calcValuesQuad(x1,y1,x2,y2);
	}
	
    /* CALCULATE VALUES QUADRANTS: Calculate x-y values where direction is not
    parallel to eith x or y axis. */
	public static void calcValuesQuad(int x1, int y1, int x2, int y2) { 
		double arrowAng = Math.toDegrees (Math.atan((double) haw/(double) al));
		double dist = Math.sqrt(al*al + aw);
		double lineAng = Math.toDegrees(Math.atan(((double) Math.abs(x1-x2))/((double) Math.abs(y1-y2))));
		
		// Adjust line angle for quadrant
		if (x1 > x2) {
			// South East
			if (y1 > y2) lineAng = 180.0-lineAng;
			}
		else {
			// South West
			if (y1 > y2) lineAng = 180.0+lineAng;
			// North West
			else lineAng = 360.0-lineAng;
			}
		
		// Calculate coords
		
		xValues[0] = x2;
		yValues[0] = y2;	
		calcCoords(1,x2,y2,dist,lineAng-arrowAng);
		calcCoords(2,x2,y2,dist,lineAng+arrowAng);
	}
    
    /* CALCULATE COORDINATES: Determine new x-y coords given a start x-y and
    a distance and direction */
    public static void calcCoords(int index, int x, int y, double dist, double dirn) {
		System.out.println("dirn = " + dirn);
		while(dirn < 0.0)   dirn = 360.0+dirn;
		while(dirn > 360.0) dirn = dirn-360.0;
		System.out.println("dirn = " + dirn);
			
		// North-East
		if (dirn <= 90.0) {
			xValues[index] = x + (int) (Math.sin(Math.toRadians(dirn))*dist);
			yValues[index] = y - (int) (Math.cos(Math.toRadians(dirn))*dist);
			return;
		}
		// South-East
		if (dirn <= 180.0) {
			xValues[index] = x + (int) (Math.cos(Math.toRadians(dirn-90))*dist);
			yValues[index] = y + (int) (Math.sin(Math.toRadians(dirn-90))*dist);
			return;
		}
		// South-West
		if (dirn <= 90.0) {
			xValues[index] = x - (int) (Math.sin(Math.toRadians(dirn-180))*dist);
			yValues[index] = y + (int) (Math.cos(Math.toRadians(dirn-180))*dist);
		}
		// Nort-West    
		else {
			xValues[index] = x - (int) (Math.cos(Math.toRadians(dirn-270))*dist);
			yValues[index] = y - (int) (Math.sin(Math.toRadians(dirn-270))*dist);
		}
	}      
    
    // ARROW COORDS: Load x-y value arrays */
    public static void arrowCoords(int x1, int y1, int x2, int y2, int x3, int y3) {
        xValues[0] = x1;
		yValues[0] = y1;
		xValues[1] = x2;
		yValues[1] = y2;
		xValues[2] = x3;
		yValues[2] = y3;
	}
}
