package frontend;

import backend.Route;
import java.awt.Rectangle;

/**
 * The RouteVisual class represents the visual components of a route
 * on the map.
 *
 * It stores the underlying Route object along with its corresponding
 * rectangular segments used for rendering and interaction.
 */
public class RouteVisual {

    /**
     * The backend route associated with this visual.
     */
    private Route route;

    /**
     * Rectangular segments used to visually represent the route.
     */
    private Rectangle[] segments;

    /**
     * Constructs a RouteVisual with a route and its visual segments.
     *
     * @param route the backend route
     * @param segments the rectangles representing the route visually
     */
    public RouteVisual(Route route, Rectangle[] segments) {
        this.route = route;
        this.segments = segments;
    }

    /**
     * Returns the route associated with this visual.
     *
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * Returns the visual segments of the route.
     *
     * @return array of rectangles representing the route
     */
    public Rectangle[] getSegments() {
        return segments;
    }
}