package br.cefet.control;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import br.cefet.model.Adjacency;


public class BreadthFirstSearch {

	private static ArrayList<Integer> scored = new ArrayList<Integer>();
	private static Queue<Integer> queue = new LinkedList<Integer>();
	private static int noName = 0;
	
	public static int getNoName() {
		return noName;
	}

	public static boolean getResponse(int root, Adjacency adjacency) {
		scored.add(root);
		queue.offer(root);
		while (!queue.isEmpty()) {
			int removed = queue.poll();
			for (int i = 0; i < adjacency.getSize(); i++) {
				if (adjacency.getOneAdjacency(removed, i)) {
					boolean verify = false;
					for (int j = 0; j < scored.size(); j++)
						if (scored.get(j) == i)
							verify = true;
					if (!verify) {
						scored.add(i);
						queue.add(i);
					}
				}
			}
			if (queue.size() > 1)
				noName = removed;
		}
		int size = scored.size();
		scored.clear();
		queue.clear();
		return size == adjacency.getSize();
	}
}
