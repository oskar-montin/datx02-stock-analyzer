package frontend;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
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

import data.DailyData;
import data.SimpleData;


public class GraphPanel extends JPanel {

	private String title;
	private ChartPanel chartPanel;
	private XYSeries stockSerie;
	final XYSeriesCollection dataset = new XYSeriesCollection();

	public GraphPanel(PriorityQueue<DailyData> dailyDataQueue){
		ArrayList<DailyData> ddList = new ArrayList<DailyData>(dailyDataQueue);
		title = ddList.get(0).getStock().getName();

		stockSerie = createSeries(ddList, title);
		dataset.addSeries(stockSerie);
		final JFreeChart chart = createChart(dataset);

		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(600, 400));
	}

	private XYSeries createSeries(List<? extends SimpleData> list, String name){
		final XYSeries series = new XYSeries(name);

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
		renderer.setSeriesLinesVisible(1, false);
		renderer.setSeriesShapesVisible(0, false);

		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;

	}

	private double getLowestLow(XYDataset dataset){
		double lowest = dataset.getYValue(0, 0);
		for(int i=1;i<dataset.getItemCount(0);i++){
			if(dataset.getYValue(0,i) < lowest){
				lowest = dataset.getYValue(0,i);
			}
		}

		return lowest;
	}

	private double getHighestHigh(XYDataset dataset){
		double highest;
		highest = dataset.getYValue(0,0);
		for(int i=1;i<dataset.getItemCount(0);i++){
			if(dataset.getYValue(0,i) > highest){
				highest = dataset.getYValue(0,i);
			}
		}
		return highest;
	}

	public ChartPanel getGraphPanel(){
		return chartPanel;	
	}

	public void addMethod(LinkedList<? extends SimpleData> list, String method){
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
	}

	public void deleteMethod(String method){
		dataset.removeSeries(dataset.getSeries(method));
		createChart(dataset);
	}

}
