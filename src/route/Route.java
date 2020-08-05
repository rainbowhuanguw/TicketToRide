package route;

import city.City;

import java.util.*;

/**
 * This class can either a directed weighted route between two different cities or two parallel routes between two cities.
 * Whether the class represent one or more routes are noted by the field of "colors" which lists all the colors of this
 * route between two cities, the number of parallel routes can be up to two. Colors may be repeated.
 */
public class Route<City> {

    private City city1;
    private City city2;
    private int numOfCars;
    private String routeId;
    private List<String> colors;
    private Map<Integer, String> indexToOwnerIds;

    /**
     * construct a new route given two cities, one or more colors, and length of the route represented by
     * number of cars
     */
    public Route(City city1, City city2, List<String> colors, int numOfCars) {
        this.city1 = (city1);
        this.city2 = (city2);

        this.numOfCars = numOfCars;
        this.routeId = null;
        this.colors = colors;
        this.indexToOwnerIds = new HashMap<>();
        for (int i = 0; i < colors.size(); i++) {
            indexToOwnerIds.put(i, null);
        }
    }

    /*--------------------------------- getter methods --------------------------------------------------------*/

    public City getCity1() { return city1; }

    public City getCity2() { return city2; }

    public List<String> getColors() { return colors; }

    public int getNumOfCars() { return numOfCars; }

    public String getRouteId() { return routeId; }

    /**
     * get owner id of a given index of the colors list
     * @param index a given index
     * @return  owner id
     */
    public String getOwnerId(int index) {
        if (index >= colors.size()) {
            throw new IllegalArgumentException("Invalid index.");
        }
        return indexToOwnerIds.get(index);
    }

    /*--------------------------------- setter methods --------------------------------------------------------*/

    /**
     * set owner id of a route when a player claims a route
     * @throws      IllegalStateException   when the route is occupied
     * @throws      IllegalArgumentException    when the input owner id is invalid, null or non existing player
     */
    public void setOwner(String ownerId, String color) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Invalid owner id.");
        }
        if (!hasColorInIndex(color, 0) && !hasColorInIndex(color, 1)) {
            throw new IllegalArgumentException("The route doesn't have this color.");
        }
        if (hasOccupiedParallelRoute(ownerId)) {
            throw new IllegalArgumentException("This player has occupied a parallel route, cannot set owner.");
        }
        if (hasColorInIndex(color, 0) && isAvailableInIndex(0)) {
            indexToOwnerIds.put(0, ownerId);
        } else if (colors.size() > 1 && hasColorInIndex(color, 1) && isAvailableInIndex(1)) {
            indexToOwnerIds.put(1, ownerId);
        }
    }

    /**
     * set route id with a given route id
     * @param routeId  a given route id generated from city names and length
     */
    protected void setRouteId(String routeId) {
        if (routeId == null) {
            throw new IllegalArgumentException("Route ID cannot be set to null.");
        }
        if (getRouteId() != null) {
            throw new IllegalStateException("Route Id already exists, cannot reset id.");
        }
        this.routeId = routeId;
    }


    /**
     * generate a new route of same colors and same length but in reversed direction
     */
    public Route<City> reversed() {
        return new Route<>(this.city2, this.city1, this.colors, this.numOfCars);
    }


    /**
     * check if the target color is located in the target index
     * @param color color
     * @return true if so, false otherwise
     * @throws IllegalArgumentException     when index is larger than or equal to 2
     */
    private boolean hasColorInIndex(String color, int index) {
        if (index >= 2) {
            throw new IllegalArgumentException("Index should be 0 or 1.");
        }
        return colors.get(index).equals(color);
    }


    /**
     * the indexToOwnerIds hash map uses the index of colors list as key, it memorizes the ownerId as values.
     * By default if the routes are not claimed by any player, its value in the hash map will be set to null.
     * @return true if that given index correspond to a null value, false otherwise
     * @throws IllegalArgumentException     when the input index is not valid, >= 2
     */
    private boolean isAvailableInIndex(int index) {
        if (index >= 2) {
            throw new IllegalArgumentException("Index should be 0 or 1.");
        }
        return indexToOwnerIds.get(index) == null;
    }


    /**
     * check if an owner has already occupied a parallel route in same or different color
     * the rules determines that a player cannot occupy both parallel routes
     * @param ownerId  the owner id needs to be checked against
     * @return true if the owner has occupied another parallel route, false otherwise
     */
    private boolean hasOccupiedParallelRoute(String ownerId) {
        for (int key : indexToOwnerIds.keySet()) {
            if (indexToOwnerIds.get(key) != null && indexToOwnerIds.get(key).equals(ownerId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if this route has parallel routes, indicated by color
     * @return true if the route have two colors(same or different), false otherwise
     */
    private boolean hasParallelRoute() {
        return colors.size() == 2;
    }

    /**
     * check if a given index, indicating a specific color, is available
     * @param index 0 or 1 if there are 2 colors, 0 if only 1 color
     * @return  true if the route hasn't been occupied by any player, false otherwise
     */
    private boolean isIndexAvailable(int index) {
        return indexToOwnerIds.get(index) == null;
    }

    /**
     * return how many/are there parallel routes in this route color
     * @param   routeColor target color to be checked
     * @return  0 if no such color, 1 if the only or 1 in 2 colors is this color, 2 if both colors are this color
     */
    private int colorCount(String routeColor) {
        int count = 0;
        for (String color: colors) {
            if (color.equals(routeColor)) {
                count++;
            }
        }
        return count;
    }

    /**
     * check if a color is available, using the helper function isIndexAvailable()
     * @param routeColor    a color of this route, will be input by the player in the game module
     * @return  true if the given color hasn't been occupied if only 1 color,
     * or 1 of 2 same target colors hasn't been occupied, false otherwise
     */
    public boolean isColorAvailable(String routeColor) {
        if (colorCount(routeColor) == 0) {
            throw new IllegalArgumentException("Doesn't have this color.");
            // only one route and one in this color, that route is occupied
        }
        // only one route in that color and color is available
        if (!hasParallelRoute()) {
            return hasColorInIndex(routeColor, 0) && isIndexAvailable(0);
        } else {
            // two routes
            if (colorCount(routeColor) == 1) {
                // only one in this color
                if (hasColorInIndex(routeColor, 0) ) {
                    return isIndexAvailable(0);
                } else {
                    return isIndexAvailable(1);
                }
            } else {
                // two in this color
                return isIndexAvailable(0) || isIndexAvailable(1);
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route<?> route = (Route<?>) o;
        return numOfCars == route.numOfCars &&
                city1.equals(route.city1) &&
                city2.equals(route.city2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city1, city2, numOfCars);
    }


    /**
     * print out/ display all colors
     */
    public void displayColors() {
        if (colors.size() == 1 || colors.get(0).equals(colors.get(1))) {
            System.out.println("There is only one color available in this route: " + colors.get(0));
        } else {
            System.out.println("There are two colors available in this route: " + colors.get(0) + " and "
                    + colors.get(1));
        }
    }
}
