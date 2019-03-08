package ur.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BuildingCostsPropertyReader {
	private static Properties properties;
	static {
		InputStream inputStream = null;
		try {
			properties = new Properties();
			String propFileName = "building_costs.properties";

			inputStream = BuildingCostsPropertyReader.class.getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<MaterialCost> get_resorce_costs(String resource) {
		return parseValue(properties.getProperty(resource));
	}

	/**
	 * Parse property values of the form:
	 * "Plasma:10,Titanium:5"
	 * @return
	 */
	private static List<MaterialCost> parseValue(String value) {
		String[] costs = value.split(",");
		ArrayList<MaterialCost> parsed = new ArrayList<MaterialCost>(costs.length);
		for (String c : costs) {
			String[] pair_material_cost = c.split(":");
			parsed.add(new MaterialCost(pair_material_cost[0], Integer.parseInt(pair_material_cost[1])));
		}
		return parsed;

	}

	/**
	 * Return the value of a property time_{target}
	 * This indicates the time cost to reach that defense state
	 * @param target
	 * @return the value or zero if there is no defined time cost for this state
	 */
	public static int get_time_cost(String target) {
		String  value = properties.getProperty("time_" + target);
		if(value == null) {
			return 0;
		} else {
			return Integer.parseInt(value);
		}
	}

}
