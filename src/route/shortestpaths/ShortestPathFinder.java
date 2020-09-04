package route.shortestpaths;

import card.TaskCard;
import city.City;
import city.CityNames;
import route.graph.Graph;
import route.Route;

import java.util.*;

/**
 * This class finds the shortest route between two cities, usually used to aid computer decision, especially in
 * finding the shortest/quickest way to finish a task.
 * Algorithm implemented: Dijkstra's.
 */
public class ShortestPathFinder<G extends Graph<V, E>, V extends City, E extends Route<V>> {

    private Graph<V, E> graph;
    private V city1;
    private V city2;
    private CityNames cityNames = CityNames.getInstance();

    // two constructors
    public ShortestPathFinder(Graph<V, E> graph, TaskCard taskCard) {
        this(graph, taskCard.getCity1(), taskCard.getCity2());
    }

    public ShortestPathFinder(Graph<V, E> graph, City city1, City city2) {
        this.graph = graph;
        this.city1 = (V) city1;
        this.city2 = (V) city2;
    }

    public ShortestPath<V, E> findShortestPath() {
        // the graph is empty

        // start = end case
        if (Objects.equals(city1, city2)) {
            return new ShortestPath.SingleVertex<>(city1);
        }

        Set<V> visited = new HashSet<>();
        HeapMinPQ<V> priorityQueue = new HeapMinPQ<>();
        Map<V, Double> distancesToV = new HashMap<>();
        Map<V, E> predecessorToV = new HashMap<>();

        // get the exact city object instance from the map
        V transformed_city1 = (V) cityNames.getCityByName(city1.getCityName());
        V transformed_city2 = (V) cityNames.getCityByName(city2.getCityName());

        // mark source distance and priority as 0,
        distancesToV.put(transformed_city1, 0.0);
        priorityQueue.add(transformed_city1, 0.0);

        // there are still unvisited vertices
        while (!visited.contains(transformed_city2) && !priorityQueue.isEmpty()) {
            //
            // get the node with the smallest priority, and get its outgoing edges
            V from = priorityQueue.removeMin();
            V transformed_from = (V) cityNames.getCityByName(from.getCityName());
            if (!from.equals(transformed_city2)) {
                Set<E> outgoingRoutes = graph.outgoingRoutesFrom(transformed_from); //TODO: error here
                for (E edge : outgoingRoutes) {
                    updateMapsAndPQ(edge, visited, priorityQueue, predecessorToV, distancesToV);
                }
            }
            visited.add(transformed_from); // mark node as processed
        }

        List<E> shortestPath = addToPath(transformed_city1, transformed_city2, predecessorToV);

        if (shortestPath.size() > 0) {
            return new ShortestPath.Success<>(shortestPath);
        } else {
            return new ShortestPath.Failure<>();
        }
    }

    /**
     * updates distancesToV, priorityQueue, and predecessorToV
     * when appearing a new edge OR the new distance is smaller than the old one
     */
    private void updateMapsAndPQ(E route, Set<V> visited, HeapMinPQ<V> priorityQueue,
                                 Map<V, E> predecessorToV, Map<V, Double> distancesToV) {
        V from = route.getCity1();
        V to = route.getCity2();
        double weight = route.getNumOfCars();

        // update when introduces new vertices
        double newDistance = distancesToV.get(from) + weight;

        if (!visited.contains(to)) {                            // to node can't be explored
            if (!distancesToV.containsKey(to)) {                // haven't seen this before
                priorityQueue.add(to, newDistance);            // update priorityQueue
                distancesToV.put(to, newDistance);             // update distancesToV
                predecessorToV.put(to, route);                  // update predecessorToV
            } else {                                           //  seen this vertex before
                if (distancesToV.get(to) > newDistance) {       // new smaller distance
                    priorityQueue.changePriority(to, newDistance); // update priorityQueue
                    distancesToV.put(to, newDistance);             // update distancesToV
                    predecessorToV.put(to, route);                  // update predecessorToV
                }
            }
        }
    }

    /**
     * convert paths to the original sequence, from start to end
     */
    private List<E> addToPath(V start, V end, Map<V, E> predecessorToV) {
        Stack<E> stack = new Stack<>();        // reverse order
        if (!predecessorToV.containsKey(end)) {    // path can't get to end, failure
            return new ArrayList<>();
        } else { // path gets to end point, there is a path
            List<E> shortestPaths = new ArrayList<>();
            V curr = end;
            while (predecessorToV.containsKey(curr)) {
                stack.push(predecessorToV.get(curr));
                curr = predecessorToV.get(curr).getCity1();
            }
            while (!stack.isEmpty()) {
                shortestPaths.add(stack.pop());
            }
            return shortestPaths;
        }
    }


}
