package program;

import java.util.Date;
import java.util.PriorityQueue;

import util.Util;

import analyzer.ExponentialMovingAverage;
import analyzer.FundamentalAnalysis;
import analyzer.SimpleMovingAverage;

import data.DailyData;
import data.LargeDouble;
import data.QuarterlyData;
import data.SimpleData;
import frontend.MainFrame;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainFrame m = new MainFrame();
		m.setVisible(true);
	}

}


