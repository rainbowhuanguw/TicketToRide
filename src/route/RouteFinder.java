package route;

import city.City;
import city.CityNames;
import route.graph.Graph;

import java.util.Set;

public class RouteFinder {
    private static Graph<City, Route<City>> graph;
    private static CityNames cityNames = CityNames.getInstance();

    public RouteFinder(Graph<City, Route<City>> inputGraph) {
        graph = inputGraph;
    }


    /**
     * given two city names, search for a route
     * @param cityName1 city name inputted to search for route
     * @param cityName2 city name inputted to search for route
     * @return a route between these two cities if exists, direction: from city1 to city2;
     *         if doesn't exist such route, return null
     */
    public Route<City> searchForRoute(String cityName1, String cityName2) {


         City city1 = cityNames.getCityByName(cityName1);
         City city2 = cityNames.getCityByName(cityName2);
         assert city1 != null;
         assert city2 != null;
         Set<Route<City>> routesFrom = graph.outgoingRoutesFrom(city1);
         for (Route<City> r: routesFrom) {
             if (r.getCity2().getCityName().equalsIgnoreCase(cityName2)) {
                 return r;
             }
         }
         return null;
    }


}
