package br.cefet.model;


public class Distance {

	private int[][] distance;
	private int size;

	public Distance(int n) {
		distance = new int[n][n];
		size = n;
	}
	public void setOneDistance(int i, int j, int distance) {
		this.distance[i][j] = distance;
	}
	public int getOneDistance(int i, int j) {
		return distance[i][j];
	}
	public int getSize() {
		return size;
	}
}
