package tracage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * 
 * @author Julien 
 * Class permettant de tracer une courbe et de la sauvegarder
 *         sous forme jpg
 */
public class TracerCourbe {
	
	
	/**
	 * 
	 * @param Series
	 *            : nom de la serie
	 * @param X
	 *            : donnée en X
	 * @param Y
	 *            : donnée en Y
	 * @return dataset avec les données des deux ArrayList
	 */
	public XYSeriesCollection createDataset(String series, List<Float> x,List<Float> y) {
		XYSeriesCollection dataset= new XYSeriesCollection();
		XYSeries series1 = new XYSeries(series);

		for (int i = 0; i < x.size(); i++) {
			series1.add(x.get(i), y.get(i));
		}
		 
		dataset.addSeries(series1);

		return dataset;
	}

	/**
	 * Crée une serie XY à partir des données
	 * @param series
	 * @param X
	 * @param Y
	 * @return
	 */
	public XYSeries createSerie(String series, List<Float> x,List<Float> y) {
		XYSeries series1 = new XYSeries(series);

		for (int i = 0; i < x.size(); i++) {
			series1.add(x.get(i), y.get(i));
		}

		return series1;
	}
	/**
	 * 
	 * @param title
	 *            : titre de la courbe
	 * @param dataset
	 *            : set de données qui permettra de tracer la courbe
	 * @return chart: la courbe créée
	 */
	public JFreeChart createChart(String title,String xAxis,String yAxis, final XYDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart(title, // chart
																		// title
				xAxis, // x axis label
				yAxis, // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				false, // tooltips
				false // urls
				);
		return chart;
	}

	/**
	 * Permet la sauvegarde sous format JPG de la courbe
	 * 
	 * @param chart
	 *            : la courbe créée par createChart
	 * @param savePath
	 *            : le chemin où sauvegarder la courbe
	 */
	public void saveChart(JFreeChart chart, String savePath) throws IOException {
		final int height = 800;
		final int width = 400;
		ChartUtilities.saveChartAsJPEG(new File(savePath), chart, height, width);
	}
	
}
