package tddd24.lab.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("regionPopulation")
public interface RegionPopulationService extends RemoteService {
  Boolean isValidRegion(String region);
  
  RegionPopulation[] getPopulations(String[] addedRegions) throws DelistedException;
  ArrayList<String> getAvailableRegions();
  
  void addRegions(String[] regions) throws Exception;
  void removeRegions(String[] regions) throws Exception;
  
  void updatePopulationData(String region, int data); 
  void updateChangeData(String region, int data);
}