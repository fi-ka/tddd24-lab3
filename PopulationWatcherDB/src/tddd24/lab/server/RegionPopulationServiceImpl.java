package tddd24.lab.server;

import java.util.ArrayList;
import java.util.Random;

import tddd24.lab.client.DelistedException;
import tddd24.lab.client.RegionPopulation;
import tddd24.lab.client.RegionPopulationService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RegionPopulationServiceImpl extends RemoteServiceServlet implements
		RegionPopulationService {
	
	private DatabaseHandler dbHandler;

	public RegionPopulationServiceImpl() {
		
		
		dbHandler = new DatabaseHandler();
		dbHandler.initiateDatabase();
		initiateDataSets();
	}

	public RegionPopulation[] getPopulations(String[] addedRegions)  throws DelistedException{
		RegionPopulation[] regionPopulations = new RegionPopulation[addedRegions.length];
		Random rnd = new Random();
		for (int i = 0; i < addedRegions.length; i++) {
			String region = addedRegions[i];
			RegionPopulation regionPopulation = dbHandler.getRegionPopulation(region);
			if(regionPopulation == null)
			{
				throw new DelistedException(addedRegions[i]);
			}
			
			
			int randomChange = rnd.nextInt(11) - 5;
			
			regionPopulation.setPopulation(regionPopulation.getPopulation()  + randomChange);
			regionPopulation.setChange(regionPopulation.getChange() + randomChange);
			
			regionPopulations[i] = regionPopulation;
		}

		return regionPopulations;
	}
	
	public Boolean isValidRegion(String region){
		return dbHandler.getRegionPopulation(region) != null;
	}
	
	private void initiateDataSets() {
		dbHandler.insert("stockholms l\u00e4n", 2091473, 37130);
		dbHandler.insert("uppsala l\u00e4n", 338630, 2748);
		dbHandler.insert("s\u00f6dermanlands l\u00e4n", 272563, 1825);
		dbHandler.insert("\u00f6sterg\u00f6tlands l\u00e4n", 431075, 1433);
		dbHandler.insert("j\u00f6nk\u00f6pings l\u00e4n", 337896, 1030);
		dbHandler.insert("kronobergs l\u00e4n", 184654, 714);
		dbHandler.insert("kalmar l\u00e4n", 233090, -446);
		dbHandler.insert("gotlands l\u00e4n", 57308, 39);
		dbHandler.insert("blekinge l\u00e4n", 152979, -248);
		dbHandler.insert("sk\u00e5ne l\u00e4n", 1252933, 9604);
		dbHandler.insert("hallands l\u00e4n", 301724, 2240);
		dbHandler.insert("v\u00e4stra g\u00f6talands l\u00e4n", 1590604, 10307);
		dbHandler.insert("v\u00e4rmlands l\u00e4n", 272736, 529);
		dbHandler.insert("\u00f6rebro l\u00e4n", 281572, 1342);
		dbHandler.insert("v\u00e4stmanlands l\u00e4n", 254257, 1501);
		dbHandler.insert("dalarnas l\u00e4n", 276565, -482);
		dbHandler.insert("g\u00e4vleborgs l\u00e4n", 276130, -378);
		dbHandler.insert("v\u00e4sternorrlands l\u00e4n", 242155, -470);
		dbHandler.insert("j\u00e4mtlands l\u00e4n", 126299, -392);
		dbHandler.insert("v\u00e4sterbottens l\u00e4n", 259667, 381);
		dbHandler.insert("norrbottens l\u00e4n", 248545, -64);
	}

	@Override
	public void addRegions(String[] regionPopulations) throws Exception {
		for(int i = 0; i < regionPopulations.length; i++)
		{
			String[] regionPopulation = regionPopulations[i].split(";");
			int changeAmount = Integer.parseInt(regionPopulation[2].trim());
			int population = Integer.parseInt(regionPopulation[1].trim());
			String region = regionPopulation[0].trim().toLowerCase();
			dbHandler.insert(region, population, changeAmount);
		}
	}
	
	@Override
	public void removeRegions(String[] regionsRemove) throws Exception {
		for(int i = 0; i < regionsRemove.length; i++)
		{
			String region = regionsRemove[i].trim().toLowerCase();
			dbHandler.remove(region);
		}
	}

	@Override
	public ArrayList<String> getAvailableRegions() {
		return dbHandler.getAllRegions();
	}
}
