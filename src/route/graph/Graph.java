package route.graph;

import city.City;
import route.Route;

import java.util.*;

/**
 * This class is an adjacency list undirected graph of city and route
 * @param <V>   a City object - vertex
 * @param <E>  a Route object - weighted edge
 */
public class Graph<V extends City, E extends Route<V>> {

    private List<E> allRoutes;
    protected Map<V, Set<E>> adjacencyList; // map a city object to the routes starting from there

    /**
     * construct a new graph with the given edges, edges are generated using RouteFileReader
     * ignores duplicate edges (e.g. include the edge of only one direction, not both direction)
     * @param routes    the routes connecting two cities, edges in the graph
     * @throws  NullPointerException if routes is null or contains null entries
     */
    public Graph(Collection<E> routes) {
        this.allRoutes = new ArrayList<>();
        this.adjacencyList = new HashMap<>();

            // add route to its city1 key
        for (E r : routes) {
            if (!adjacencyList.containsKey(r.getCity1())) {
                adjacencyList.put(r.getCity1(), new HashSet<>());
            }
            adjacencyList.get(r.getCity1()).add(r);
            // add to all routes
            allRoutes.add(r);

            // add a reversed route to its city2 key
            if (!adjacencyList.containsKey(r.getCity2())) {
                adjacencyList.put(r.getCity2(), new HashSet<>());
            }
            adjacencyList.get(r.getCity2()).add((E) r.reversed());
        }
    }

    /**
     * get all outgoing routes from a specific city
     * @param city  a given city where the routes start from
     * @return  a set of routes departing from this city(uses this city as city1)
     */
    public Set<E> outgoingRoutesFrom(V city) {
        return Collections.unmodifiableSet(adjacencyList.getOrDefault(city, Set.of()));
    }

    /**
     * get all cities in the graph
     * @return  a set of all city objects in this graph
     */
    public Set<V> getAllCities() {
        return Collections.unmodifiableSet(this.adjacencyList.keySet());
    }

    /**
     * get all routes, two routes of both direction for each pair
     * @return  a set of all routes in this graph
     */
    public List<E> getAllRoutes() { return Collections.unmodifiableList(this.allRoutes); }

}

