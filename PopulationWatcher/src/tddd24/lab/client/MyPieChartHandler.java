package tddd24.lab.client;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart.PieOptions;

public class MyPieChartHandler {
	private PieChart pie; // mmm pie
	private DataTable pieData;
	
	public MyPieChartHandler(final Panel panel){
		Runnable onLoadCallback = new Runnable() {
			public void run() {
				// Create a pie chart visualization.
				pie = new PieChart(createTable(), createOptions());
				panel.add(pie);
			}
		};

		VisualizationUtils.loadVisualizationApi(onLoadCallback,
				PieChart.PACKAGE);
	}
	
	// PieChart
	private PieOptions createOptions() {
		PieOptions options = PieOptions.create();
		options.setWidth(400);
		options.setHeight(240);
		options.set3D(true);
		options.setTitle("Region population comparison");
		return options;
	}

	private AbstractDataTable createTable() {
		pieData = DataTable.create();
		pieData.addColumn(ColumnType.STRING, "Region");
		pieData.addColumn(ColumnType.NUMBER, "Population");
		return pieData;
	}

	public PieChart getPie() {
		return pie;
	}

	public DataTable getPieData() {
		return pieData;
	}

 }
