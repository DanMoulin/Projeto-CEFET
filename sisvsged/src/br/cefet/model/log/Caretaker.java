package br.cefet.model.log;

import java.util.ArrayList;

public class Caretaker {

	private ArrayList<Text> state;

	public Caretaker(ArrayList<Text> state) {
		this.state = state;
	}
	
	public void insert(Text text) {
		state.add(text);
	}
	
	public Text get(int i) {
		if(state.size() <= i)
			return null;
		return state.get(i);
	}
}
