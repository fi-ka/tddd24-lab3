package tddd24.lab.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegionPopulationServiceAsync {
	  void isValidRegion(String region, AsyncCallback<Boolean> callback);
	  
	  void getPopulations(String[] symbols, AsyncCallback<RegionPopulation[]> callback);
	  void getDelistedRegions(AsyncCallback<ArrayList<String>> callback);
	  void getAvailableRegions(AsyncCallback<ArrayList<String>> callback);
	  
	  void addRegions(String[] regions, AsyncCallback<Void> callback);
	  void removeRegions(String[] regions, AsyncCallback<Void> callback);
	  void delistRegion(String region, AsyncCallback<Void> callback);
}
