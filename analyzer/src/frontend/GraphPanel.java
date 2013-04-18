package frontend;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.TimeZone;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.time.Week;
import org.jfree.data.xy.XYDataItem;
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
		System.out.println(result.getName());
		this.title = result.getName()+" - Signal: "+result.getSignal().getString();
		LinkedList<Curve> curves = result.getCurves();
		curves.getFirst().getQueue();
		
		final JFreeChart chart = this.createChart(curves, this.title);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(600, 400));
		
		/*
		System.out.println(result.getName());
		this.title = result.getName();
		LinkedList<Curve> curves = result.getCurves();
		curves.getFirst().getQueue();
		for(Curve c: curves) {
			stockSerie = createSeries(c.getQueue(), c.getName());
			dataset.addSeries(stockSerie);
		}
		final JFreeChart chart = createChart(dataset);
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(600, 400));*/
	}

	private XYSeries createSeries(Collection<? extends SimpleData> collection, String name){
		final XYSeries series = new XYSeries(name);
		List<SimpleData> list = new LinkedList<SimpleData>(collection);
		for(int i = list.size()-1; i>0; i--){
			
			//list.get(i).getDate().get(Calendar.DATE)
			series.add(i, list.get(i).getValue());
		}

		return series;
	}

	private JFreeChart createChart(List<Curve> curves, String title) {
		double low = Double.MAX_VALUE;
		double high = Double.MIN_VALUE;
    	CategoryDataset[] datasets = new CategoryDataset[curves.size()];
    	for(int i = 0; i<curves.size(); i++) {
    		DefaultKeyedValues data = new DefaultKeyedValues();
    		for(SimpleData s:curves.get(i).getQueue()) {
    			Calendar date = s.getDate();
    			data.addValue(date.get(Calendar.DATE)+"/"+date.get(Calendar.MONTH) ,
    								   s.getValue());
    			low = Math.min(s.getValue(), low);
    			high = Math.max(s.getValue(), high);
    		}
    		datasets[i] = DatasetUtilities.createCategoryDataset(curves.get(i).getName(), data);
    	}
    	JFreeChart chart = ChartFactory.createBarChart(title,
    			"Date",
    			"Value",
    			datasets[0],
    			PlotOrientation.VERTICAL,
    			true,true,false);
    	chart.setBackgroundPaint(Color.white);
    	//Plot and rednerer
    	LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setBaseLinesVisible(true); //TUrn of the lines
    	CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRenderer(0, renderer);
        
        NumberAxis numberAxis = (NumberAxis)plot.getRangeAxis();        
        numberAxis.setRange(new Range(low*0.99,high*1.01)); 
        
        for(int i = 1;i<datasets.length;i++) {
        	plot.setDataset(i, datasets[i]);
            LineAndShapeRenderer tempRenderer = new LineAndShapeRenderer();
            renderer.setBaseLinesVisible(true);
            plot.setRenderer(i, tempRenderer);
        }
        //Configure date axis
        CategoryAxis dateAxis = (CategoryAxis)plot.getDomainAxis(); 
        Font f = dateAxis.getLabelFont();
        f=dateAxis.getLabelFont().deriveFont(0, 8);
        dateAxis.setTickLabelFont(f);
        
		//dateAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE); 
		//((ValueAxis) dateAxis).setVerticalTickLabels(true); 
		dateAxis.setUpperMargin(0.00); 
		dateAxis.setLowerMargin(0.00); 
        
        return chart;
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
		
		//DateAxis d = new Date;
		for(int i = 0; i<dataset.getSeriesCount(); i++){
			renderer.setSeriesLinesVisible(i, true);
			renderer.setSeriesShapesVisible(i, false);
		}
		
		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		// Configure the X axis 
		DateAxis dateAxis = (DateAxis)plot.getDomainAxis(); 
		dateAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE); 
		dateAxis.setVerticalTickLabels(true); 
		dateAxis.setUpperMargin(0.00); 
		dateAxis.setLowerMargin(0.00); 
		
		DateTickUnit unit = new DateTickUnit(DateTickUnit.DAY, dataset.getItemCount(0), new java.text.SimpleDateFormat( "w yyyy" )); 
		dateAxis.setTickUnit(unit, false, true);
		///////////////
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
