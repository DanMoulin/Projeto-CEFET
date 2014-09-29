package br.cefet.vsged.servlet;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ServletGraphic extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String[] graphicData = null;
	private static String title = "";
	private static String rowKey = "";

	public ServletGraphic() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String act = request.getParameter("act");
		if (act.equals("1")) {
			graphicData = null;
			graphicData = request.getParameter("gd").split("-");
			title = request.getParameter("title").replace("-", " ");
			rowKey = request.getParameter("rk").replace("-", " ");
			try {
				Desktop.getDesktop().browse(new URI("http://localhost:8080/sisvsged/graphic.jsp"));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else if (act.equals("0")) {
			DefaultCategoryDataset data = new DefaultCategoryDataset();
			/*
			 * data.addValue(30, "bateria", "1"); data.addValue(15, "bateria",
			 * "2"); data.addValue(17, "bateria", "3"); data.addValue(10,
			 * "bateria", "4");
			 */
			for (int i = 0; i < graphicData.length; i++) {
				data.addValue(Integer.parseInt(graphicData[i]), rowKey, "" + i);
			}
			JFreeChart chart = ChartFactory.createBarChart(title, "Tempo",
					"Valores", data, PlotOrientation.VERTICAL, true, true,
					false);

			chart.setBorderVisible(true);

			if (chart != null) {
				int width = 950;
				int height = 600;
				response.setContentType("image/jpeg");
				OutputStream out = response.getOutputStream();
				ChartUtilities.writeChartAsJPEG(out, chart, width, height);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
