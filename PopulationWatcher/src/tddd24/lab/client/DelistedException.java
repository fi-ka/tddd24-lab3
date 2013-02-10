package tddd24.lab.client;
import java.io.Serializable;


public class DelistedException extends Exception implements Serializable{
	  private String region;

	  public DelistedException() {
	  }

	  public DelistedException(String region) {
	    this.region = region;
	  }

	  public String getRegion() {
	    return this.region;
	  }
}
