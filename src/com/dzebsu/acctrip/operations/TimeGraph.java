package com.dzebsu.acctrip.operations;

import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class TimeGraph {

	public View getIntent(Context context) {

		Calendar cal = Calendar.getInstance();
		long time = cal.getTimeInMillis();
		// Our first data
		long[] x = { time + 86400000, time + 166400000, time + 86400000, time + 86400000, time + 286400000,
				time + 186400000, time + 486400000, time + 386400000, time + 86400000, time + 864000000 }; // x
																											// values!
		int[] y = { 30, 34, 45, 57, 77, 89, 100, 111, 123, 145 }; // y values!
		TimeSeries series = new TimeSeries("Line1");
		for (int i = 0; i < x.length; i++) {
			series.add(x[i], y[i]);
		}

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(series);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds
																				// a
																				// collection
																				// of
																				// XYSeriesRenderer
																				// and
																				// customizes
																				// the
																				// graph
		XYSeriesRenderer renderer = new XYSeriesRenderer(); // This will be used
															// to customize line
															// 1 // This will be
															// used to
															// customize
															// line 2
		mRenderer.addSeriesRenderer(renderer);

		// Customization time for line 1!
		renderer.setColor(Color.BLACK);
		renderer.setPointStyle(PointStyle.SQUARE);
		renderer.setFillPoints(true);

		View view = ChartFactory.getTimeChartView(context, dataset, mRenderer, "dd mm");
		return view;

	}
}
