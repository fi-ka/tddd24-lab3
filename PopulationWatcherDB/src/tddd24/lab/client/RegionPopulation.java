package tddd24.lab.client;

import java.io.Serializable;

public class RegionPopulation implements Serializable{

	  private String region;
	  private int population;
	  private int change;

	  public RegionPopulation() {
	  }

	  public RegionPopulation(String region, int population, int change) {
	    this.region = region;
	    this.population = population;
	    this.change = change;
	  }

	  public String getRegion() {
	    return this.region;
	  }

	  public int getPopulation() {
	    return this.population;
	  }

	  public int getChange() {
	    return this.change;
	  }

	  public double getChangePercent() {
	    return 100.0 * this.change / this.population;
	  }

	  public void setRegion(String region) {
	    this.region = region;
	  }

	  public void setPopulation(int population) {
	    this.population = population;
	  }

	  public void setChange(int change) {
	    this.change = change;
	  }
}
