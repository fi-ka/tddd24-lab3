package tddd24.lab.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("regionPopulation")
public interface RegionPopulationService extends RemoteService {
  Boolean isValidRegion(String region);
  
  ArrayList<String> getDelistedRegions();
  RegionPopulation[] getPopulations(String[] addedRegions) throws DelistedException;
  ArrayList<String> getAvailableRegions();
  
  void addRegions(String[] regions) throws Exception;
  void delistRegion(String region);
}