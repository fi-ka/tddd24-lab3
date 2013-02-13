package tddd24.lab.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("regionPopulation")
public interface RegionPopulationService extends RemoteService {

  RegionPopulation[] getPopulations(String[] addedRegions) throws DelistedException;
  Boolean isValidRegion(String region);
  void delistRegion(String region);
  ArrayList<String> getDelistedRegions();
  
}