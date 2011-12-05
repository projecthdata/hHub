/*
 * Copyright 2011 The MITRE Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.projecthdata.weight.ui;

import java.sql.SQLException;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.projecthdata.weight.OrmProvider;
import org.projecthdata.weight.R;
import org.projecthdata.weight.database.WeightDatabaseHelper;
import org.projecthdata.weight.model.WeightReading;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class ChartFragment extends Fragment {
	WeightDatabaseHelper databaseHelper = null;
	private GraphicalView chartView = null;
	private XYMultipleSeriesDataset dataSet = null;
	private TimeSeries series = null;
	private ViewGroup chartContainer = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.chart, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		databaseHelper = ((OrmProvider) getActivity()).getDatabaseHelper();
		series = new TimeSeries("Weight Readings");
		this.dataSet = new XYMultipleSeriesDataset();
		this.dataSet.addSeries(this.series);

		addNewChartViewToContainer();
	

	}

	private void addNewChartViewToContainer() {
		chartView = ChartFactory.getTimeChartView(getActivity(), dataSet,
				getRenderer(), "MM/dd/yyyy");

		chartContainer = (ViewGroup) getActivity().findViewById(
				R.id.weight_chart_container);

		chartContainer.addView(chartView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}



	public void repaintChart() {
		chartView.repaint();

	}

	public void refreshChart() {
		series.clear();
		try {
			for (WeightReading reading : databaseHelper.getWeightDao()) {
				series.add(reading.getDateTime(), reading.getResultValue());
			}
			chartContainer.removeView(chartView);

			addNewChartViewToContainer();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		refreshChart();

	}

	private XYMultipleSeriesRenderer getRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(16);
		renderer.setLabelsTextSize(14);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 10, 10, 0, 10 });

		renderer.setAxesColor(Color.DKGRAY);
		renderer.setLabelsColor(Color.LTGRAY);
		renderer.setBackgroundColor(Color.BLACK);
		renderer.setApplyBackgroundColor(true);
		renderer.setShowLegend(false);
		renderer.setAntialiasing(true);
		renderer.setShowGrid(true);
		renderer.setYAxisMin(0.0);

		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.BLUE);
		r.setPointStyle(PointStyle.SQUARE);
		r.setFillPoints(true);
		renderer.addSeriesRenderer(r);

		return renderer;
	}

}
