package backend;

import java.util.*;

public class DestinationTicket {

    private City cityA;

    private City cityB;

    private int points;

    public DestinationTicket(City cityA, City cityB, int points) {
        this.cityA = cityA;
        this.cityB = cityB;
        this.points = points;
    }


    /**
     * Method that checks if Player connected the two cities listed in this Destination
     * Ticket Card by using Breadth-First Search on the Player's claimed routes
     *
     * @param player - the player with the Destination Ticket
     * @return a boolean saying if the player has or hasn't connected the cities
     */
    public boolean checkPlayerConnect(Player player) {
        return bfs(player.getPlayerRoutes(), cityA, cityB, new ArrayList<>());
    }

    /**
     * Breadth First Search of Player's routes claimed graph
     *
     * @param routes - routes claimed by player
     * @param startCity - city to start search from
     * @param targetCity - city to end search at
     * @param visitedRoutes - stores routes already visited, ensuring no repeats
     * @return boolean that says whether or not a pah exists between the two cities
     */
    public boolean bfs(HashMap<City, ArrayList<Route>> routes, City startCity,
                       City targetCity, List<Route> visitedRoutes) {

        // Initialise queue to store cities that need searching from
        Queue<City> queueOfCities = new LinkedList<>();

        // Add the starting city to the queue
        queueOfCities.add(startCity);

        // Keeps searching until all cities are covered
        while (!queueOfCities.isEmpty()) {
            // Get the first city from the top of the queue
            City city = queueOfCities.poll();

            // Go through all routes connected to this city
            for  (Route route : routes.get(city)) {

                // If route has not yet been visited
                if (!visitedRoutes.contains(route)) {

                    // Mark it as visited by adding to visited routes
                    visitedRoutes.add(route);

                    // Get the next city that this route connects to
                    if (route.getCityA() != city) {

                        // Check if the next city is the target
                        if (route.getCityA().equals(targetCity)) {
                            return true;
                        }
                        // If not, then add it to the queue for further processing
                        queueOfCities.add(route.getCityA());
                    } else {
                        if (route.getCityB().equals(targetCity)) {
                            return true;
                        }
                        queueOfCities.add(route.getCityB());
                    }
                }
            }
        }
        // Return false if after BFS, nothing can be found
        return false;

    }

    /**
     * Getter method for the points of this Destination Ticket
     *
     * @return points of the Destination Ticket
     */
    public int getPoints() {
        return points;
    }
}
