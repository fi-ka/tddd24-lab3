package tddd24.lab.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import tddd24.lab.client.RegionPopulation;

public class DatabaseHandler {
	public void initiateDatabase() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager
					.getConnection("jdbc:sqlite:/F:/Program/SQLite/test");

			Statement stat = conn.createStatement();
			stat.executeUpdate("drop table if exists regionPopulation;");

			stat.executeUpdate("create table regionPopulation (region varchar(20), population int, change int, primary key (region));");
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insert(String region, int population, int change) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager
					.getConnection("jdbc:sqlite:/F:/Program/SQLite/test");

			PreparedStatement prep = conn
					.prepareStatement("insert into regionPopulation values (?, ?, ?);");
			prep.setString(1, region);
			prep.setInt(2, population);
			prep.setInt(3, change);
			prep.execute();

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void remove(String region)
	{
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager
					.getConnection("jdbc:sqlite:/F:/Program/SQLite/test");

			PreparedStatement prep = conn
					.prepareStatement("delete from regionPopulation where region = \"" + region + "\";");
			prep.execute();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public RegionPopulation getRegionPopulation(String region) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager
					.getConnection("jdbc:sqlite:/F:/Program/SQLite/test");

			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select population, change from regionPopulation r where r.region = \"" + region + "\";");
			if (rs.next() == false)
				return null;
			int population = rs.getInt("population");
			int change = rs.getInt("change");
			rs.close();
			conn.close();
			return new RegionPopulation(region, population, change);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> getAllRegions(){
		ArrayList<String> regions = new ArrayList<String>();
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager
					.getConnection("jdbc:sqlite:/F:/Program/SQLite/test");

			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select region from regionPopulation;");
			while(rs.next())
			{
				regions.add(rs.getString("region"));
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return regions;
	}
}
