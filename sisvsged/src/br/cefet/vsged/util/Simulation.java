package br.cefet.vsged.util;

import java.util.ArrayList;

import br.cefet.vsged.model.Vertex;

public class Simulation {

	public static void packageTransference(ArrayList<Vertex> vertex, int[] houteringTree) {
		for (int k = 1; k < vertex.size(); k++) {
			sensorOff(vertex.get(k));
			//verifica se o vertice possui pacotes a enviar e se tem bateria--possivel metodo
			if (vertex.get(k).getPackages().size() != 0 && vertex.get(k).getBathery() > 13 && vertex.get(k).isActivate())
				for (int j = 0; j < houteringTree.length; j += 2) {
					if (houteringTree[j] == k) {//localiza o vertice no array da arvore
						send(vertex.get(k), vertex.get(houteringTree[j + 1]), houteringTree[j + 1]);
					}
				}
		}
	}

	private static void sensorOff(Vertex v) {
		//verifica se o sensor será desativado
		if (Probability.pointNotWorks())
			v.setActivate(false);
	}

	private static void send(Vertex v1, Vertex v2, int destiny) {
		if (destiny != 0 && v2.isActivate()) {//verifica se o vertice de destino não é o sink e se esta ativado
			sendToVertex(v1, v2);
		} else {//se o vertice de destino for o sink ou estiver desativado entra aqui
			if (destiny == 0) {
				sendToSink(v1, v2);
			}
		}
	}

	private static void sendToSink(Vertex v1, Vertex v2) {
		while (true) {
			if (v1.getPackages().size() == 0 || v1.getBathery() <= 13)
				break;
			else if (Probability.packageLost()) {
				v1.getPackages().poll();
				v1.setBathery(13);
			} else {
				v2.getPackages().add(v1.getPackages().poll());
				v1.setBathery(13);
			}
		}
	}

	private static void sendToVertex(Vertex v1, Vertex v2) {
		while (true) {
			//se o vertive de horigem não tiver pacotes ou bateria e se o vertice de destino não tiver bateria, sai do loop
			if (v1.getPackages().size() == 0 || v1.getBathery() <= 13 || v2.getBathery() <= 2)
				break;
			//se o pacote for perdido o vertice de destino mantem a bateria e a quantidade de pacotes atual
			else if (Probability.packageLost()) {
				v1.getPackages().poll();
				v1.setBathery(13);
			} else {//se estiver tudo ok o procedimento e normal
				v2.getPackages().add(v1.getPackages().poll());
				v1.setBathery(13);
				v2.setBathery(2);
			}
		}
	}
}
