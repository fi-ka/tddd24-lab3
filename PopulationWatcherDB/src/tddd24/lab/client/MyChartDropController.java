package tddd24.lab.client;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.DataTable;

public class MyChartDropController extends AbstractDropController{

	private MyPieChartHandler pieHandler;
	private PopulationWatcher populationWatcher;
	
	public MyChartDropController(Widget dropTarget,MyPieChartHandler pieHandler, PopulationWatcher populationWatcher) {
		super(dropTarget);
		this.pieHandler = pieHandler;
		this.populationWatcher = populationWatcher;
	}
	
	@Override
	public void onDrop(DragContext context) {
		super.onDrop(context);
		
		String region = ((Label) context.selectedWidgets.get(0)).getText().toLowerCase();
		int row = populationWatcher.getAddedRegions().indexOf(region) +1;
		DataTable pieData = pieHandler.getPieData();
		pieData.addRow();
		pieData.setValue(pieData.getNumberOfRows()-1, 0, region);
		pieData.setValue(pieData.getNumberOfRows()-1, 1, Integer.parseInt(populationWatcher.getRegionFlexTable().getText(row, 1).replace(",","")));
		pieHandler.draw();
	}
}
