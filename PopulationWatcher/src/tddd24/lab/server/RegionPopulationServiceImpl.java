package tddd24.lab.server;

import java.util.ArrayList;
import java.util.Random;

import tddd24.lab.client.RegionPopulation;
import tddd24.lab.client.RegionPopulationService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RegionPopulationServiceImpl extends RemoteServiceServlet implements
		RegionPopulationService {

	private ArrayList<String> regions = new ArrayList<String>();
	private ArrayList<Integer> populations = new ArrayList<Integer>();

	public RegionPopulationServiceImpl() {
		initiateDataSets();
	}

	public RegionPopulation[] getPopulations(String[] addedRegions) {
		final float MAX_POPULATION_CHANGE = 0.01f; // +/- .5%
		Random rnd = new Random();

		RegionPopulation[] regionPopulations = new RegionPopulation[addedRegions.length];
		for (int i = 0; i < addedRegions.length; i++) {
			int population = populations.get(regions.indexOf(addedRegions[i]));
			int maxChangeAmount = Math
					.round(population * MAX_POPULATION_CHANGE);
			int change = rnd.nextInt((int) (maxChangeAmount))
					- (maxChangeAmount / 2);

			regionPopulations[i] = new RegionPopulation(addedRegions[i],
					population + change, change);
		}

		return regionPopulations;
	}
	
	public Boolean isValidRegion(String region) {
		return regions.contains(region);
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

	}


}
