package city;

import java.util.HashMap;
import java.util.Map;

public class CityNames {

    private static Map<String, City> cityMap = new HashMap<>();
    private static CityNames cityNames = new CityNames();

    private CityNames() {}

    public City addToCityNames(String cityName) {
        if (!exists(cityName)) {
            City city = new City(cityName);
            cityMap.put(cityName, city);
            return city;
        } else {
            return cityMap.get(cityName);
        }
    }

    public static CityNames getInstance() { return cityNames; }


    public City getCityByName(String cityName) {
        if (exists(cityName)) {
            return cityMap.get(cityName);
        }
        System.out.println("This city doesn't exist in map. ");
        return null;
    }

    public Map<String, City> getCityMap() { return cityMap; }

    public boolean exists(String cityName) {
        return cityMap.containsKey(cityName);
    }
}
