package frontend;

import backend.Route;
import java.awt.Rectangle;

public class RouteVisual {

    private Route route;
    private Rectangle[] segments;

    public RouteVisual(Route route, Rectangle[] segments) {
        this.route = route;
        this.segments = segments;
    }

    public Route getRoute() {
        return route;
    }

    public Rectangle[] getSegments() {
        return segments;
    }
}