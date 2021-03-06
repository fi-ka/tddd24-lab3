package tddd24.lab.server;

import java.util.ArrayList;
import java.util.Random;

import tddd24.lab.client.DelistedException;
import tddd24.lab.client.RegionPopulation;
import tddd24.lab.client.RegionPopulationService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RegionPopulationServiceImpl extends RemoteServiceServlet implements
		RegionPopulationService {

	private ArrayList<String> regions = new ArrayList<String>();
	private ArrayList<String> delistedRegions = new ArrayList<String>();
	private ArrayList<Integer> populations = new ArrayList<Integer>();
	private ArrayList<Integer> change = new ArrayList<Integer>();
	
	

	public RegionPopulationServiceImpl() {
		initiateDataSets();
	}

	public RegionPopulation[] getPopulations(String[] addedRegions)  throws DelistedException{
		RegionPopulation[] regionPopulations = new RegionPopulation[addedRegions.length];
		for (int i = 0; i < addedRegions.length; i++) {
			Random rnd = new Random();
			if(!regions.contains(addedRegions[i]))
			{
				throw new DelistedException(addedRegions[i]);
			}
			
			int index = regions.indexOf(addedRegions[i]);
			int population = populations.get(index);
			int changeAmount = change.get(index);
			
			int randomChange = rnd.nextInt(11) - 5;
			
			regionPopulations[i] = new RegionPopulation(addedRegions[i],
					population + randomChange, changeAmount + randomChange);
		}

		return regionPopulations;
	}
	
	public Boolean isValidRegion(String region){
	
		return regions.contains(region);
	}
	

	public void delistRegion(String region) {
		if(!delistedRegions.contains(region))
			delistedRegions.add(region);
	}

	private void initiateDataSets() {
		regions.add("stockholms l\u00e4n");
		regions.add("uppsala l\u00e4n");
		regions.add("s\u00f6dermanlands l\u00e4n");
		regions.add("\u00f6sterg\u00f6tlands l\u00e4n");
		regions.add("j\u00f6nk\u00f6pings l\u00e4n");
		regions.add("kronobergs l\u00e4n");
		regions.add("kalmar l\u00e4n");
		regions.add("gotlands l\u00e4n");
		regions.add("blekinge l\u00e4n");
		regions.add("sk\u00e5ne l\u00e4n");
		regions.add("hallands l\u00e4n");
		regions.add("v\u00e4stra g\u00f6talands l\u00e4n");
		regions.add("v\u00e4rmlands l\u00e4n");
		regions.add("\u00f6rebro l\u00e4n");
		regions.add("v\u00e4stmanlands l\u00e4n");
		regions.add("dalarnas l\u00e4n");
		regions.add("g\u00e4vleborgs l\u00e4n");
		regions.add("v\u00e4sternorrlands l\u00e4n");
		regions.add("j\u00e4mtlands l\u00e4n");
		regions.add("v\u00e4sterbottens l\u00e4n");
		regions.add("norrbottens l\u00e4n");
		
		populations.add(2091473);
		populations.add(338630);
		populations.add(272563);
		populations.add(431075);
		populations.add(337896);
		populations.add(184654);
		populations.add(233090);
		populations.add(57308);
		populations.add(152979);
		populations.add(1252933);
		populations.add(301724);
		populations.add(1590604);
		populations.add(272736);
		populations.add(281572);
		populations.add(254257);
		populations.add(276565);
		populations.add(276130);
		populations.add(242155);
		populations.add(126299);
		populations.add(259667);
		populations.add(248545);
		
		change.add(37130);
		change.add(2748);
		change.add(1825);
		change.add(1433);
		change.add(1030);
		change.add(714);
		change.add(-446);
		change.add(39);
		change.add(-248);
		change.add(9604);
		change.add(2240);
		change.add(10307);
		change.add(529);
		change.add(1342);
		change.add(1501);
		change.add(-482);
		change.add(-378);
		change.add(-470);
		change.add(-392);
		change.add(381);
		change.add(-64);
	}

	@Override
	public ArrayList<String> getDelistedRegions() {
		return delistedRegions;
	}

	@Override
	public void addRegions(String[] regionPopulations) throws Exception {
		for(int i = 0; i < regionPopulations.length; i++)
		{
			String[] regionPopulation = regionPopulations[i].split(";");
			int changeAmount = Integer.parseInt(regionPopulation[2].trim());
			int population = Integer.parseInt(regionPopulation[1].trim());
			String currentRegion = regionPopulation[0].trim().toLowerCase();
			if(!regions.contains(currentRegion)){
				regions.add(currentRegion);
				populations.add(population);
				change.add(changeAmount);
			}
		}
	}
	
	@Override
	public void removeRegions(String[] regionsRemove) throws Exception {
		for(int i = 0; i < regionsRemove.length; i++)
		{
			String currentRegion = regionsRemove[i].trim().toLowerCase();
			if(regions.contains(currentRegion)){
				int index = regions.indexOf(currentRegion);
				regions.remove(index);
				populations.remove(index);
				change.remove(index);
			}
		}
	}

	@Override
	public ArrayList<String> getAvailableRegions() {
		return regions;
	}
}
