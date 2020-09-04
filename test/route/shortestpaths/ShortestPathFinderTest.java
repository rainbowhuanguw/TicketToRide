package route.shortestpaths;

import card.TaskCard;
import city.City;
import org.junit.jupiter.api.Test;
import route.graph.Graph;
import route.Route;
import route.RouteFileReader;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShortestPathFinderTest {


    private RouteFileReader routeFileReader = RouteFileReader.getRouteFileReaderInstance();


    private Set<Route<City>> allRoutes;
    private Graph<City, Route<City>> graph;

    public ShortestPathFinderTest() {
        routeFileReader.createAllRoutes();
        allRoutes = RouteFileReader.getRoutes();
        graph = new Graph<>(allRoutes);
    }
    @Test
    void findShortestPath_DirectPathShort_returnsSize1Routes() {
        TaskCard taskCard= new TaskCard(new City("Seattle"), new City("Portland"), 2);
        ShortestPathFinder<Graph<City, Route<City>>, City, Route<City> > finder = new ShortestPathFinder<>(graph, taskCard);
        ShortestPath<City, Route<City>> path = finder.findShortestPath();
        List<Route<City>> routes = path.edges();
        assertEquals(routes.size(), 1);
    }

    @Test
    void findShortestPath_DirectPathLong_returnsSize1() {
        TaskCard taskCard= new TaskCard(new City("San Francisco"), new City("Salt Lake City"), 4);
        ShortestPathFinder<Graph<City, Route<City>>, City, Route<City> > finder = new ShortestPathFinder<>(graph, taskCard);
        ShortestPath<City, Route<City>> path = finder.findShortestPath();
        List<Route<City>> routes = path.edges();
        assertEquals(routes.size(), 1);
    }

    @Test
    void findShortestPath_IndirectPathShort_returnsSize2() {
        TaskCard taskCard= new TaskCard(new City("San Francisco"), new City("Denver"), 4);
        ShortestPathFinder<Graph<City, Route<City>>, City, Route<City> > finder = new ShortestPathFinder<>(graph, taskCard);
        ShortestPath<City, Route<City>> path = finder.findShortestPath();
        List<Route<City>> routes = path.edges();
        assertEquals(routes.size(), 2);
    }

    @Test
    void findShortestPath_IndirectPathLong_returnsSize8() {
        TaskCard taskCard= new TaskCard(new City("Vancouver"), new City("Miami"), 20);
        ShortestPathFinder<Graph<City, Route<City>>, City, Route<City> > finder = new ShortestPathFinder<>(graph, taskCard);
        ShortestPath<City, Route<City>> path = finder.findShortestPath();
        List<Route<City>> routes = path.edges();
        assertEquals(routes.size(), 8);
    }

}