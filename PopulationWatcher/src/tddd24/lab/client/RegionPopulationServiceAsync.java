package tddd24.lab.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegionPopulationServiceAsync {
	  void getPopulations(String[] symbols, AsyncCallback<RegionPopulation[]> callback);
	  void isValidRegion(String region, AsyncCallback<Boolean> callback);
	  void delistRegion(String region, AsyncCallback<Void> callback);
	  void getDelistedRegions(AsyncCallback<ArrayList<String>> callback);
}
