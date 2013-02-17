package tddd24.lab.client;


import java.util.List;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MyFlexTableDragController extends PickupDragController{

	public MyFlexTableDragController(AbsolutePanel boundaryPanel,
			boolean allowDroppingOnBoundaryPanel) {
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
	}
	
	@Override
	protected Widget newDragProxy(DragContext context) {
		super.newDragProxy(context);
		List<Widget> list = context.selectedWidgets;
		Label label = (Label)list.get(0);
		//HTML proxy = new HTML();
		Label proxyLabel = new Label();
		proxyLabel.setText(label.getText());
		return proxyLabel;
	}
}
