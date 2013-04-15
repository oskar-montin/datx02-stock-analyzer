package frontend;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import data.Curve;
import data.DailyData;
import data.Result;
import data.SimpleData;


public class GraphPanel extends JPanel {

	private String title;
	private ChartPanel chartPanel;
	private XYSeries stockSerie;
	final XYSeriesCollection dataset = new XYSeriesCollection();
	ArrayList<SimpleData> ddList;

	public GraphPanel(PriorityQueue<? extends SimpleData> dailyDataQueue){
		ddList = new ArrayList<SimpleData>(dailyDataQueue);
		title = ddList.get(0).getStock().getName();

		stockSerie = createSeries(ddList, title);
		dataset.addSeries(stockSerie);
		final JFreeChart chart = createChart(dataset);

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(600, 400));
	}

	public GraphPanel(PriorityQueue<? extends SimpleData> dailyDataQueue, String title){
		ddList = new ArrayList<SimpleData>(dailyDataQueue);
		this.title = title;

		stockSerie = createSeries(ddList, title);
		dataset.addSeries(stockSerie);
		final JFreeChart chart = createChart(dataset);

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(600, 400));
	}

	public GraphPanel(Result result) {
		this.title = result.getName();
		LinkedList<Curve> curves = result.getCurves();
		curves.getFirst().getQueue();
		for(Curve c: curves) {
			stockSerie = createSeries(c.getQueue(), c.getName());
			dataset.addSeries(stockSerie);
		}
		final JFreeChart chart = createChart(dataset);

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(600, 400));
	}

	private XYSeries createSeries(Collection<? extends SimpleData> collection, String name){
		final XYSeries series = new XYSeries(name);
		List<SimpleData> list = new LinkedList<SimpleData>(collection);
		for(int i = list.size()-1; i>0; i--){
			series.add(i, list.get(i).getValue());
		}

		return series;
	}

	private JFreeChart createChart(final XYDataset dataset){

		final JFreeChart chart = ChartFactory.createXYLineChart(
				title, 									// title
				"Date", 							    // x-axis label
				"Price", 							    // y-axis label
				dataset, 								// dataset
				PlotOrientation.VERTICAL, 				// orientation
				true, 							    	// legend
				true, 							    	// tooltips
				false);									// urls


		chart.setBackgroundPaint(Color.WHITE);
		double low = getLowestLow(dataset);
		double high = getHighestHigh(dataset);

		
		chart.getXYPlot().getRangeAxis().setRange(low*0.99, high*1.01);
		

		if((high - low) < 1){
			NumberAxis range = (NumberAxis)chart.getXYPlot().getRangeAxis();
			range.setAutoTickUnitSelection(true);
			range.setTickUnit(new NumberTickUnit(0.1));
		}else if((high - low) < 10){
			NumberAxis range = (NumberAxis)chart.getXYPlot().getRangeAxis();
			range.setAutoTickUnitSelection(true);
			range.setTickUnit(new NumberTickUnit(0.2));
		}else if((high - low) < 50){
			NumberAxis range = (NumberAxis)chart.getXYPlot().getRangeAxis();
			range.setAutoTickUnitSelection(true);
			range.setTickUnit(new NumberTickUnit(2.5));
		}else if((high-low) < 100){
			NumberAxis range = (NumberAxis)chart.getXYPlot().getRangeAxis();
			range.setAutoTickUnitSelection(true);
			range.setTickUnit(new NumberTickUnit(10));
		}


		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();

		plot.setBackgroundPaint(new Color(219,225,255));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		plot.setRangeZeroBaselineVisible(false);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		renderer.setSeriesLinesVisible(0, true);
		renderer.setSeriesShapesVisible(0, false);

		for(int i = 0; i<dataset.getSeriesCount(); i++){
			renderer.setSeriesLinesVisible(i, true);
			renderer.setSeriesShapesVisible(i, false);
		}

		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}

	private double getLowestLow(XYDataset dataset){
		double lowest = dataset.getYValue(0, 0);

		for(int i = 0; i < dataset.getSeriesCount(); i++){
			for(int j = 1; j < dataset.getItemCount(i); j++){
				if(dataset.getYValue(i,j) < lowest){
					lowest = dataset.getYValue(i,j);
				}
			}
		}

		return lowest;
	}

	private double getHighestHigh(XYDataset dataset){
		double highest = dataset.getYValue(0,0);

		for(int i = 0; i < dataset.getSeriesCount(); i++){
			for(int j = 1; j < dataset.getItemCount(i); j++){
				if(dataset.getYValue(i,j) > highest){
					highest = dataset.getYValue(i,j);
				}
			}
		}
		return highest;
	}

	public ChartPanel getGraphPanel(){
		return chartPanel;	
	}

	public void addMethod(Collection<? extends SimpleData> queue, String method){
		LinkedList<? extends SimpleData> list = new LinkedList<SimpleData>(queue);
		if(method.equals("SMA")){

			Collections.reverse(list);
			XYSeries series = createSeries(list, "SMA");

			dataset.addSeries(series);
			createChart(dataset);
		}
		if(method.equals("EMA")){
			XYSeries series = createSeries(list, "EMA");

			dataset.addSeries(series);
			createChart(dataset);
		}
		if(method.equals("MACD")){
			XYSeries series = createSeries(list, "Signal Line");

			dataset.addSeries(series);
			createChart(dataset);
		}
	}

	public void deleteMethod(String method){
		dataset.removeSeries(dataset.getSeries(method));
		createChart(dataset);
	}

}
