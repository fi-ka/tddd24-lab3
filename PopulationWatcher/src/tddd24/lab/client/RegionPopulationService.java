package tddd24.lab.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("regionPopulation")
public interface RegionPopulationService extends RemoteService {

  RegionPopulation[] getPopulations(String[] addedRegions);
  Boolean isValidRegion(String region);
}