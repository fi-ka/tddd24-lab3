package tddd24.lab.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dev.js.rhino.ObjToIntMap.Iterator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PopulationWatcher implements EntryPoint {

	private static final int REFRESH_INTERVAL = 5000; // ms

	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable regionFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newRegionTextBox = new TextBox();
	private Button addRegionButton = new Button("Add");
	private Button delistRegionButton = new Button("Delist");
	private Label lastUpdatedLabel = new Label();
	private Label errorMsgLabel = new Label();
	private FlexTable delistedRegionsTable = new FlexTable();

	private ArrayList<String> addedRegions = new ArrayList<String>();

	private RegionPopulationServiceAsync regionPopulationSvc = GWT
			.create(RegionPopulationService.class);

	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {

		// Create table for region data.
		regionFlexTable.setText(0, 0, "Region");
		regionFlexTable.setText(0, 1, "Population");
		regionFlexTable.setText(0, 2, "Change");
		regionFlexTable.setText(0, 3, "Remove");

		// Add styles to elements in the region list table.
		regionFlexTable.setCellPadding(6);
		regionFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
		regionFlexTable.addStyleName("watchList");
		regionFlexTable.getCellFormatter().addStyleName(0, 1,
				"watchListNumericColumn");
		regionFlexTable.getCellFormatter().addStyleName(0, 2,
				"watchListNumericColumn");
		regionFlexTable.getCellFormatter().addStyleName(0, 3,
				"watchListRemoveColumn");

		//create table for delisted regions and set style
		delistedRegionsTable.setText(0, 0, "Currently Unavailable regions");
		delistedRegionsTable.getCellFormatter().addStyleName(0,0, "delistTableHeader");
		delistedRegionsTable.addStyleName("delistTable");
		updateDelistedRegionTable();
		
		
		// Assemble Add Region panel.
		addPanel.add(newRegionTextBox);
		addPanel.add(addRegionButton);
		addPanel.add(delistRegionButton);
		addPanel.addStyleName("addPanel");
		
		// Assemble Main panel.
		errorMsgLabel.setStyleName("errorMessage");
		errorMsgLabel.setVisible(false);

		lastUpdatedLabel.setText("Last update :");
		mainPanel.add(errorMsgLabel);
		mainPanel.add(regionFlexTable);
		mainPanel.add(lastUpdatedLabel);
		mainPanel.add(addPanel);
		mainPanel.add(delistedRegionsTable);
		
		

		// Associate the Main panel with the HTML host page.
		RootPanel.get("regionList").add(mainPanel);

		// Move cursor focus to the input box.
		newRegionTextBox.setFocus(true);

		// Setup timer to refresh list automatically.
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				refreshWatchList();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

		// listen for mouse events on the Add button.
		addRegionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				addRegion();
			}
		});

		// listen for mouse events on the Delist button.
		delistRegionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				delistRegion();
			}
		});

		// Listen for keyboard events in the input box.
		newRegionTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					addRegion();
				}
			}
		});
	}

	/**
	 * Add Region to FlexTable. Executed when the user clicks the
	 * addRegionButton or presses enter in the newRegionTextBox.
	 */
	private void addRegion() {
		final String newRegion = newRegionTextBox.getText().toLowerCase()
				.trim();
		newRegionTextBox.setFocus(true);

		// Initialize the service proxy for a region check.
		if (regionPopulationSvc == null) {
			regionPopulationSvc = GWT.create(RegionPopulationService.class);
		}
		// Set up the callback object.
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Boolean result) {
				if (result) {
					addRegionToTable(newRegion);
				} else {
					Window.alert("'" + newRegion + "' is not a valid symbol.");
					newRegionTextBox.selectAll();
					return;
				}
			}
		};
		// Make the call to the region population service.
		regionPopulationSvc.isValidRegion(newRegion, callback);
	}

	private void addRegionToTable(final String region) {
		// Don't add the region if it's already in the table.
		if (addedRegions.contains(region))
			return;

		// Add the region to the table.
		int row = regionFlexTable.getRowCount();
		addedRegions.add(region);
		regionFlexTable.setText(row, 0, region);
		regionFlexTable.setWidget(row, 2, new Label());
		regionFlexTable.getCellFormatter().addStyleName(row, 1,
				"watchListNumericColumn");
		regionFlexTable.getCellFormatter().addStyleName(row, 2,
				"watchListNumericColumn");
		regionFlexTable.getCellFormatter().addStyleName(row, 3,
				"watchListRemoveColumn");

		// Add a button to remove this region from the table.
		Button removeRegionButton = new Button("x");
		removeRegionButton.addStyleDependentName("remove");
		removeRegionButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int removedIndex = addedRegions.indexOf(region);
				addedRegions.remove(removedIndex);
				regionFlexTable.removeRow(removedIndex + 1);
			}
		});
		regionFlexTable.setWidget(row, 3, removeRegionButton);

		// Get the region population.
		refreshWatchList();
	}

	private void delistRegion() {
		// Initialize the service proxy for a region check.
		if (regionPopulationSvc == null) {
			regionPopulationSvc = GWT.create(RegionPopulationService.class);
		}
		// Set up the callback object.
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Void result) {
				updateDelistedRegionTable();
			}
		};
		
		regionPopulationSvc.delistRegion(newRegionTextBox.getText(), callback);
	}

	/**
	 * Generate random change to populations
	 */
	private void refreshWatchList() {
		// Initialize the service proxy.
		if (regionPopulationSvc == null) {
			regionPopulationSvc = GWT.create(RegionPopulationService.class);
		}
		// Set up the callback object.
		AsyncCallback<RegionPopulation[]> callback = new AsyncCallback<RegionPopulation[]>() {
			public void onFailure(Throwable caught) {
				// If the region is in the list of delisted regions, display an
				// error message.
				String details = caught.getMessage();
				if (caught instanceof DelistedException) {
					details = "Region '"
							+ ((DelistedException) caught).getRegion()
							+ "' is currently not available";
				}

				errorMsgLabel.setText("Error: " + details);
				errorMsgLabel.setVisible(true);
			}

			public void onSuccess(RegionPopulation[] result) {
				updateTable(result);
			}
		};
		// Make the call to the stock price service.
		regionPopulationSvc.getPopulations(addedRegions.toArray(new String[0]),
				callback);
	}

	/**
	 * Update the Population and Change fields for all the rows in the region
	 * table.
	 * 
	 * @param populations
	 *            Region data for all rows.
	 */
	private void updateTable(RegionPopulation[] populations) {
		for (int i = 0; i < populations.length; i++) {
			updateTable(populations[i]);
		}

		// Set last-update timestamp
		lastUpdatedLabel.setText("Last update : "
				+ DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM)
						.format(new Date()));

		// Clear any errors.
		errorMsgLabel.setVisible(false);
	}

	/**
	 * Update a single row in the region table.
	 * 
	 * @param population
	 *            Region data for a single row.
	 */
	private void updateTable(RegionPopulation population) {
		// Make sure the region is still in the region table.
		if (!addedRegions.contains(population.getRegion())) {
			return;
		}

		int row = addedRegions.indexOf(population.getRegion()) + 1;

		// Format the data in the population and Change fields.
		String populationText = NumberFormat.getFormat("#,##0").format(
				population.getPopulation());
		String changeText = NumberFormat.getFormat("+#,##0;-#,##0").format(
				population.getChange());
		String changePercentText = NumberFormat
				.getFormat("+#,##0.00;-#,##0.00").format(
						population.getChangePercent());

		// Populate the population and Change fields with new data.
		regionFlexTable.setText(row, 1, populationText);
		Label changeWidget = (Label) regionFlexTable.getWidget(row, 2);
		changeWidget.setText(changeText + " (" + changePercentText + "%)");

		// Change the color of text in the Change field based on its value.
		String changeStyleName = "noChange";
		if (population.getChangePercent() < -0.1f) {
			changeStyleName = "negativeChange";
		} else if (population.getChangePercent() > 0.1f) {
			changeStyleName = "positiveChange";
		}

		changeWidget.setStyleName(changeStyleName);
	}
	
	private void updateDelistedRegionTable(){
		if(regionPopulationSvc == null){
			regionPopulationSvc = GWT.create(RegionPopulationService.class);
		}
		AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(ArrayList<String> result) {
				int i = 1;
				for(String region : result)
				{
					delistedRegionsTable.setText(i, 0, region);
					i++;
				}
			}
		};
		regionPopulationSvc.getDelistedRegions(callback);
	}
}
