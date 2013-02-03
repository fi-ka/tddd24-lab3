package tddd24.lab.client;

public class RegionPopulation {

	  private String region;
	  private double population;
	  private double change;

	  public RegionPopulation() {
	  }

	  public RegionPopulation(String region, double population, double change) {
	    this.region = region;
	    this.population = population;
	    this.change = change;
	  }

	  public String getRegion() {
	    return this.region;
	  }

	  public double getPopulation() {
	    return this.population;
	  }

	  public double getChange() {
	    return this.change;
	  }

	  public double getChangePercent() {
	    return 100.0 * this.change / this.population;
	  }

	  public void setRegion(String region) {
	    this.region = region;
	  }

	  public void setPopulation(double population) {
	    this.population = population;
	  }

	  public void setChange(double change) {
	    this.change = change;
	  }
}
