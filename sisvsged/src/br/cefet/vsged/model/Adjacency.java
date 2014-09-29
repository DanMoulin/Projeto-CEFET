package br.cefet.vsged.model;


public class Adjacency {

	private boolean[][] adjacency;
	private int size;

	public Adjacency(int n) {
		adjacency = new boolean[n][n];
		size = n;
	}
	public void setOneAdjacency(int i, int j, Boolean adjacency) {
		this.adjacency[i][j] = adjacency;
	}
	public boolean getOneAdjacency(int i, int j) {
		return adjacency[i][j];
	}
	public int getSize() {
		return size;
	}
}
