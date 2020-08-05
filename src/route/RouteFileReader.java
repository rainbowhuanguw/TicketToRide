package route;

import city.City;
import city.CityNames;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RouteFileReader {

    // class variables
    private static Set<Route<City>> routes = new HashSet<>();
    private static CityNames cityNames = CityNames.getInstance();
    private static RouteFileReader reader;
    static {
        try {
            reader = new RouteFileReader("src/file/routes_colorsInList.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // instance variables
    private File routesFile;
    private Scanner fileScanner;


    /**
     * private constructor
     * @param   fileName   the given file name
     * @throws FileNotFoundException if the input file name doesn't exist
     */
    private RouteFileReader(String fileName) throws FileNotFoundException {
        routesFile = new File(fileName);
        fileScanner = new Scanner(routesFile);
    }


    /**
     * get the only instance of the reader
     * @return  the RouteFileReader object
     */
    public static RouteFileReader getRouteFileReaderInstance() {
        return reader;
    }


    /**
     * get a set of all the routes from the file, note these are directly routes,
     * the allRoutes variable in the graph class are double this size, both directional for every route
     * @return  a set of all routes
     */
    public static Set<Route<City>> getRoutes() {
        return routes;
    }


    /**
     * read file line by line and convert a line of string to a route
     */
    public void createAllRoutes() {
        if (routes.isEmpty()) {
            int i = 0;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                Route<City> route = convertStringToRoute(line);
                routes.add(route);
                setRouteId(route, i);
                i++;
            }
            fileScanner.close();
        }
    }

    /**
     * convert a string of line to a route
     * a line in the format: String City1 | String City2 | List<String> color,color | points
     * @param  routeString   a line in the file
     * @return   route constructed based on the input string
     */
    private Route<City> convertStringToRoute(String routeString) {
        String[] parts = routeString.split("\\|");
        String cityName1 = parts[0];
        String cityName2 = parts[1];

        // create or get city 1
        City city1 = cityNames.addToCityNames(cityName1);
        City city2 = cityNames.addToCityNames(cityName2);

        List<String> colors = Arrays.asList(parts[2].split(","));
        int numOfCars = Integer.parseInt(parts[3]);
        return new Route<>(city1, city2, colors, numOfCars);
    }

    /**
     * set a route's id using the route's two cities first three letters, points, and a consecutive number
     * format: "R" + first 3 letters of city1 + first 3 letters of city2 + points + "-" + number
     * example: RSeaVan3-1
     * @param route     the route being set id to
     * @param num       the number of integer, represents the i-th
     */
    private void setRouteId(Route<City> route, int num) {
        String city1Sub = route.getCity1().getCityName().substring(0,3);
        String city2Sub = route.getCity2().getCityName().substring(0,3);
        String pointsString = route.getNumOfCars() + "";
        String id = ("R" + city1Sub + city2Sub + pointsString + "_" + num);
        route.setRouteId(id);
    }

}
