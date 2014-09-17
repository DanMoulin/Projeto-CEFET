package br.cefet.control;


public class Resume {

	private static int quantity;
	private static int comunicationRay;
	private static int sensingRay;
	private static int finalTime;
	private static int finalBathery;
	private static float achieved = 0;
	
	private static String strIndex;
	private static String strQuantity;
	private static String strStartEnergy;
	private static String strComunicationRay;
	private static String strSensingRay;
	private static String strPercMin;
	private static String strPercAchieved;
	private static String strFinalTime;
	private static String strFinalBathery;

	public static void atualizaDados(int quantity, int comunicationRay,
			int sensingRay, int finalTime, int finalBathery) {
		Resume.quantity = quantity;
		Resume.comunicationRay = comunicationRay;
		Resume.sensingRay = sensingRay;
		Resume.finalTime = finalTime;
		Resume.finalBathery = finalBathery;
		setStrings();
	}
	
	

	public static void setAchieved(float achieved) {
		Resume.achieved = achieved;
		setStrings();
	}
	
	public static void setStrings() {
		strIndex = "Quadro de resumo";
		strQuantity = "Quantidade de sensores: " + quantity;
		strStartEnergy = "Energia inicial: 3000";
		strComunicationRay = "Raio de Comunicacao: " + comunicationRay;
		strSensingRay = "Raio de Sensoriamento: " + sensingRay;
		strPercMin = "Percentual de Cobertura minimo: 90";
		strPercAchieved = "Percentual de Cobertura atingido: " + achieved;
		strFinalTime = "Tempo de vida util da rede: " + finalTime;
		strFinalBathery = "Energia residual total da rede: " + finalBathery;
	}

	public static String getStrIndex() {
		return strIndex;
	}

	public static String getStrQuantity() {
		return strQuantity;
	}

	public static String getStrStartEnergy() {
		return strStartEnergy;
	}

	public static String getStrComunicationRay() {
		return strComunicationRay;
	}

	public static String getStrSensingRay() {
		return strSensingRay;
	}

	public static String getStrPercMin() {
		return strPercMin;
	}

	public static String getStrPercAchieved() {
		return strPercAchieved;
	}

	public static String getStrFinalTime() {
		return strFinalTime;
	}

	public static String getStrFinalBathery() {
		return strFinalBathery;
	}
}
