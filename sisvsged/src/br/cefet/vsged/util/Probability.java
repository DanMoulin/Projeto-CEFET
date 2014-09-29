package br.cefet.vsged.util;


public class Probability {

	public static boolean pointNotWorks() {
		boolean reply = false;
		double t1 = (Math.random() * 100) + 1;
		double t2 = (Math.random() * 100) + 1;
		double t3 = (t1 + t2) /2;
		if(t3 >= 97)//Min+(int)(Math.random()*((Max-Min)+1))
			reply = true;
		return reply;
	}
	
	public static boolean packageLost() {
		boolean reply = false;
		double t1 = (Math.random() * 100) + 1;
		double t2 = (Math.random() * 100) + 1;
		double t3 = (t1 + t2) /2;
		if(t3 >= 98)//Min+(int)(Math.random()*((Max-Min)+1))
			reply = true;
		return reply;
	}
}
