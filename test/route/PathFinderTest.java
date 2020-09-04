package route;

import city.City;
import org.junit.jupiter.api.Test;
import route.graph.PathFinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

    private Map<String, Set<Route<City>>> map;


    public PathFinderTest() {
        // manually create 2 maps
        map = new HashMap<>();

        map.put( "Vancouver", Set.of( new Route<>(
                new City("Vancouver"),
                new City("Seattle"),
                List.of("grey", "grey"),
                1)));

        map.put("Seattle", Set.of(
                new Route<>(new City("Seattle"),
                        new City("Vancouver"),
                        List.of("grey", "grey"), 1),
                new Route<>(new City("Seattle"),
                        new City("Portland"),
                        List.of("grey", "grey"),
                        1)));

        map.put("Portland", Set.of(
                new Route<>(new City("Portland"),
                        new City("Seattle"),
                        List.of("grey", "grey"),
                        1),
                new Route<>(new City("Portland"),
                        new City("San Francisco"),
                        List.of("purple", "green"),
                        5),
                new Route<>( new City("Portland"),
                        new City("Salt Lake City"),
                        List.of("blue"),
                        6)));
        map.put("San Francisco", Set.of(
                new Route<>( new City("San Francisco"),
                        new City("Los Angeles"),
                        List.of("purple", "yellow"),
                        3),
                new Route<>( new City("San Francisco"),
                        new City("Portland"),
                        List.of("purple", "green"),
                        5)));
        map.put("Salt Lake City", Set.of(
                new Route<>( new City("Salt Lake City"),
                        new City("Portland"),
                        List.of("blue"),
                        6)));
    }

    @Test
    void findPath_pathExists_VanSea_returnsTrue () {
        PathFinder finder = new PathFinder(map, "Vancouver", "San Francisco");
        assertTrue(finder.findPath());
    }

    @Test
    void findPath_pathExists_SalSf_returnsTrue () {
        PathFinder finder = new PathFinder(map, "Salt Lake City", "San Francisco");
        assertTrue(finder.findPath());
    }

    @Test
    void findPath_pathNotExist_LaSf_returnsFalse() {
        // LA doesn't exist in the given map
        PathFinder finder = new PathFinder(map, "Los Angeles", "San Francisco");
        assertFalse(finder.findPath());
    }

    @Test
    void findPath_pathNotExist_DenOk_returnsFalse() {
        //both cities don't exist the given map
        PathFinder finder = new PathFinder(map, "Denver", "Oklahoma City");
        assertFalse(finder.findPath());
    }

}