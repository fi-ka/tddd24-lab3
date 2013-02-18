package tddd24.lab.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
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
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopulationWatcher implements EntryPoint {
	private final static int REFRESH_INTERVAL = 5000;

	private HorizontalPanel mainPanel = new HorizontalPanel();
	private VerticalPanel leftPanel = new VerticalPanel();
	private VerticalPanel rightPanel = new VerticalPanel();
	private FlexTable regionFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newRegionTextBox = new TextBox();
	private Button addRegionButton = new Button("Add");
	private Button delistRegionButton = new Button("Delist");
	private Label lastUpdatedLabel = new Label();
	private Label errorMsgLabel = new Label();

	private DialogBox uploadCellDataDialog = new DialogBox();
	private TextBox dialogTextBox = new TextBox();

	private TextArea addToServerArea = new TextArea();
	private HorizontalPanel serverButtonsPanel = new HorizontalPanel();
	private Button addToServerButton = new Button("Add to server");
	private Button removeFromServerButton = new Button("Remove from server");
	private FlexTable availableRegionsTable = new FlexTable();

	private ArrayList<String> addedRegions = new ArrayList<String>();

	private RegionPopulationServiceAsync regionPopulationSvc = GWT
			.create(RegionPopulationService.class);

	private MyFlexTableDragController dragController;
	private MyChartDropController dropController;

	private int cellIndex;
	private String currentUploadRegion;

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

		// Assemble upload cell data DialogBox
		

		dialogTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					try {
						uploadCellData(Integer.parseInt(dialogTextBox.getText()));
						uploadCellDataDialog.hide();
					} catch (Exception e) {
						dialogTextBox.setText("Enter a number!");
					}
				}
				if (event.getCharCode() == KeyCodes.KEY_ESCAPE) {
					uploadCellDataDialog.hide();
				}
			}
		});
		uploadCellDataDialog.setWidget(dialogTextBox);
		
		// Add clicklistener to table to be able to change individual cells
		regionFlexTable.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int row = regionFlexTable.getCellForEvent(event).getRowIndex();
				cellIndex = regionFlexTable.getCellForEvent(event)
						.getCellIndex();
				currentUploadRegion = ((Label) ((HorizontalPanel) regionFlexTable
						.getWidget(row, 0)).getWidget(0)).getText().toLowerCase();
				uploadCellDataDialog.showRelativeTo(regionFlexTable.getWidget(
						row, cellIndex));
				uploadCellDataDialog.show();
			}
		});

		// create table for available regions and set style
		availableRegionsTable.setText(0, 0, "Currently Available regions");
		availableRegionsTable.getCellFormatter().addStyleName(0, 0,
				"availableRegionsTableHeader");
		availableRegionsTable.addStyleName("availableRegionsTable");
		updateAvailableRegionTable();

		// Create piechart and add to rightpanel
		MyPieChartHandler pieHandler = new MyPieChartHandler(rightPanel);

		// Assemble Add Region panel.
		addPanel.add(newRegionTextBox);
		addPanel.add(addRegionButton);
		addPanel.add(delistRegionButton);
		addPanel.addStyleName("addPanel");

		// Assamble server buttons panel

		serverButtonsPanel.add(addToServerButton);
		serverButtonsPanel.add(removeFromServerButton);

		// Assemble left panel.
		errorMsgLabel.setStyleName("errorMessage");
		errorMsgLabel.setVisible(false);
		lastUpdatedLabel.setText("Last update :");
		addToServerArea.setWidth("220px");
		addToServerArea.setHeight("90px");
		addToServerArea.setText("exempel l\u00e4n;123456;1234");

		leftPanel.add(errorMsgLabel);
		leftPanel.add(regionFlexTable);
		leftPanel.add(lastUpdatedLabel);
		leftPanel.add(addPanel);
		leftPanel.add(addToServerArea);
		leftPanel.add(serverButtonsPanel);

		leftPanel.add(availableRegionsTable);

		// Assemble Main panel
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);

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
				addRegion(newRegionTextBox.getText());
			}
		});

		// Listen for keyboard events in the input box.
		newRegionTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					addRegion(newRegionTextBox.getText());
				}
			}
		});

		addToServerButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addToServer(addToServerArea.getText());
			}
		});

		removeFromServerButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeFromServer(addToServerArea.getText());
			}
		});

		// GWT-DND
		RootPanel.get("regionList").getElement().getStyle()
				.setProperty("position", "relative");
		dragController = new MyFlexTableDragController(
				RootPanel.get("regionList"), false);
		dragController.setBehaviorDragProxy(true);
		dropController = new MyChartDropController(rightPanel, pieHandler, this);
		dragController.registerDropController(dropController);

	}

	/**
	 * Add Region to FlexTable. Executed when the user clicks the
	 * addRegionButton or presses enter in the newRegionTextBox.
	 */
	private void addRegion(String region) {
		final String newRegion = region.toLowerCase().trim();
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
		Label regionLabel = new Label(region.substring(0, 1).toUpperCase()
				+ region.substring(1));
		HorizontalPanel regionPanel = new HorizontalPanel();
		regionPanel.add(regionLabel);
		regionFlexTable.setWidget(row, 0, regionPanel);
		dragController.makeDraggable(regionLabel);

		Label populationLabel = new Label();
		regionFlexTable.setWidget(row, 1, populationLabel);

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

	private void addToServer(String textRegions) {
		String[] regions = textRegions.split("\n");

		if (regionPopulationSvc == null)
			regionPopulationSvc = GWT.create(RegionPopulationService.class);

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				updateAvailableRegionTable();
			}

			@Override
			public void onSuccess(Void result) {
				updateAvailableRegionTable();
			}
		};
		regionPopulationSvc.addRegions(regions, callback);
	}

	private void removeFromServer(String textRegions) {
		String[] regions = textRegions.split("\n");

		if (regionPopulationSvc == null)
			regionPopulationSvc = GWT.create(RegionPopulationService.class);

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Void result) {
				updateAvailableRegionTable();
			}
		};
		regionPopulationSvc.removeRegions(regions, callback);
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
							+ "' is no longer available";
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
		Label populationWidget = (Label) regionFlexTable.getWidget(row, 1);
		populationWidget.setText(populationText);
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

	private void updateAvailableRegionTable() {
		if (regionPopulationSvc == null) {
			regionPopulationSvc = GWT.create(RegionPopulationService.class);
		}
		AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(ArrayList<String> result) {
				availableRegionsTable.clear(true);
				int i = 1;
				for (String region : result) {
					availableRegionsTable.setText(i, 0, region.substring(0, 1)
							.toUpperCase() + region.substring(1));
					i++;
				}
			}
		};
		regionPopulationSvc.getAvailableRegions(callback);
	}

	public FlexTable getRegionFlexTable() {
		return regionFlexTable;
	}

	public ArrayList<String> getAddedRegions() {
		return addedRegions;
	}

	private void uploadCellData(int newCellData) {
		switch (cellIndex) {
		case 1:
			uploadPopulationData(currentUploadRegion, newCellData);
			break;
		case 2:
			uploadChangeData(currentUploadRegion, newCellData);
			break;
		default:
		}
	}

	private void uploadChangeData(String regionKey, int newCellData) {
		if (regionPopulationSvc == null) {
			regionPopulationSvc = GWT.create(RegionPopulationService.class);
		}
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Void result) {
			}
		};
		regionPopulationSvc.updateChangeData(regionKey, newCellData, callback);
	}

	private void uploadPopulationData(String regionKey, int newCellData) {
		if (regionPopulationSvc == null) {
			regionPopulationSvc = GWT.create(RegionPopulationService.class);
		}
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			public void onSuccess(Void result) {
			}
		};
		regionPopulationSvc.updatePopulationData(regionKey, newCellData, callback);
	}

}