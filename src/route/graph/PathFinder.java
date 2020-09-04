package route.graph;

import city.City;
import route.Route;

import java.util.*;

public class PathFinder {

    private Map<String, Set<Route<City>>> map;
    private String cityName1;
    private String cityName2;
    private Set<String> visited;
    private boolean searched;
    private boolean found;

    public PathFinder (Map<String, Set<Route<City>>> map, String cityName1, String cityName2) {
        this.map = map;
        this.cityName1 = cityName1;
        this.cityName2 = cityName2;
        visited = new HashSet<>();
        searched = false;
        found = false;
    }

    /**
     * check if there is a connected path between two cities
     * @return true if there is a connected path, false otherwise
     */
    public boolean findPath() {
        // if the map is empty or doesn't contain one of the city names
        if (map.isEmpty() || !map.containsKey(cityName1) || !map.containsKey(cityName2)) {
            return false;
        }
        if (!searched) {
            // take either of the city names, here take city name 1
            searched = true;
            found = findPathHelper(cityName1);
        }
        return found;

    }

    private boolean findPathHelper(String cityName) {
        visited.add(cityName);
        if (cityName.equals(cityName2)) {
            return true;
        } else {
            Set<Route<City>> routesOfCity = map.get(cityName);
            for (Route<City> route : routesOfCity) {
                String routeToCityName = route.getCity2().getCityName();
                if (!visited.contains(routeToCityName)) {
                    return findPathHelper(routeToCityName);
                }
            }
        }
        return false;
    }
}
