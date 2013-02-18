package tddd24.lab.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegionPopulationServiceAsync {
	  void isValidRegion(String region, AsyncCallback<Boolean> callback);
	  
	  void getPopulations(String[] symbols, AsyncCallback<RegionPopulation[]> callback);
	  void getAvailableRegions(AsyncCallback<ArrayList<String>> callback);
	  
	  void addRegions(String[] regions, AsyncCallback<Void> callback);
	  void removeRegions(String[] regions, AsyncCallback<Void> callback);

	  void updatePopulationData(String region, int data,
			  AsyncCallback<Void> callback);
	  void updateChangeData(String region, int data,
			  AsyncCallback<Void> callback);
}
