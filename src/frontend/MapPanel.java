package frontend;

import backend.City;
import backend.Colour;
import backend.Route;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * The MapPanel is responsible for rendering the game map,
 * including cities and routes between them.
 *
 * It also handles user interaction for selecting routes
 * via mouse clicks.
 */
public class MapPanel extends JPanel {

    /**
     * Reference to the game controller.
     */
    private final GameController controller;

    /**
     * Stores screen positions for each city.
     */
    private HashMap<City, Point> cityPositions;

    /**
     * Visual representations of all routes.
     */
    private RouteVisual[] routeVisuals;

    /**
     * Constructs the MapPanel and initializes all visual elements.
     *
     * @param controller the game controller
     */
    public MapPanel(GameController controller) {
        this.controller = controller;
        setBackground(new Color(230, 220, 190));
        createCityPositions();
        createRouteVisuals();
        setupMouseListener();
    }

    /**
     * Sets up mouse interaction for selecting routes.
     */
    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (RouteVisual routeVisual : routeVisuals) {
                    for (Rectangle segment : routeVisual.getSegments()) {
                        if (segment.contains(e.getPoint())) {
                            controller.selectRoute(routeVisual.getRoute());
                            repaint();
                            return;
                        }
                    }
                }
            }
        });
    }

    /**
     * Initializes the screen positions of all cities.
     */
    private void createCityPositions() {
        cityPositions = new HashMap<>();

        // (City positions mapping — unchanged)
    }

    /**
     * Creates all route visuals used for rendering.
     */
    private void createRouteVisuals() {
        routeVisuals = new RouteVisual[] {
                // (Route creation — unchanged)
        };
    }

    /**
     * Helper method to create a route visual with no offset.
     */
    private RouteVisual createRouteVisual(Colour colour, City cityA, City cityB, int length) {
        return createRouteVisual(colour, cityA, cityB, length, 0);
    }

    /**
     * Creates a route visual with a specified offset (used for parallel routes).
     */
    private RouteVisual createRouteVisual(Colour colour, City cityA, City cityB, int length, int offset) {
        Route route = new Route(colour, cityA, cityB, length, null);
        Rectangle[] segments = createSegmentsBetween(cityA, cityB, length, offset);
        return new RouteVisual(route, segments);
    }

    /**
     * Generates rectangular segments between two cities to visually represent a route.
     *
     * @param cityA start city
     * @param cityB end city
     * @param length number of segments
     * @param offset offset for parallel routes
     * @return array of rectangles representing the route
     */
    private Rectangle[] createSegmentsBetween(City cityA, City cityB, int length, int offset) {
        Point start = cityPositions.get(cityA);
        Point end = cityPositions.get(cityB);

        Rectangle[] segments = new Rectangle[length];

        int width = 34;
        int height = 14;

        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        double offsetX = -dy / distance * offset;
        double offsetY = dx / distance * offset;

        for (int i = 0; i < length; i++) {
            double t = (i + 1.0) / (length + 1.0);

            int x = (int) (start.x + t * dx + offsetX) - width / 2;
            int y = (int) (start.y + t * dy + offsetY) - height / 2;

            segments[i] = new Rectangle(x, y, width, height);
        }

        return segments;
    }

    /**
     * Draws a rotated rectangular segment representing part of a route.
     */
    private void drawRotatedSegment(Graphics2D g2, Rectangle segment, double angle, Color color, boolean selected) {
        Graphics2D copy = (Graphics2D) g2.create();

        int centerX = segment.x + segment.width / 2;
        int centerY = segment.y + segment.height / 2;

        copy.rotate(angle, centerX, centerY);

        copy.setColor(color);
        copy.fillRoundRect(segment.x, segment.y, segment.width, segment.height, 8, 8);

        copy.setColor(Color.WHITE);
        copy.drawRoundRect(segment.x, segment.y, segment.width, segment.height, 8, 8);

        // Highlight if selected
        if (selected) {
            copy.setColor(Color.CYAN);
            copy.setStroke(new BasicStroke(3));
            copy.drawRoundRect(
                    segment.x - 3,
                    segment.y - 3,
                    segment.width + 6,
                    segment.height + 6,
                    10,
                    10
            );
            copy.setStroke(new BasicStroke(1));
        }

        copy.dispose();
    }

    /**
     * Paints the panel, including routes and cities.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawRoutes(g2);
        drawCities(g2);
    }

    /**
     * Draws all routes on the map.
     */
    private void drawRoutes(Graphics2D g2) {
        for (RouteVisual routeVisual : routeVisuals) {
            Route route = routeVisual.getRoute();
            Color drawColor = getDrawColor(route);

            Point start = cityPositions.get(route.getCityA());
            Point end = cityPositions.get(route.getCityB());
            double angle = Math.atan2(end.y - start.y, end.x - start.x);

            boolean selected = controller.getSelectedRoute() == route;

            for (Rectangle segment : routeVisual.getSegments()) {
                drawRotatedSegment(g2, segment, angle, drawColor, selected);

                if (route.getOwner() != null) {
                    g2.setFont(new Font("Arial", Font.BOLD, 10));
                    g2.setColor(Color.WHITE);
                    g2.drawString(
                            getPlayerLabel(route.getOwner().getName()),
                            segment.x + 8,
                            segment.y + 10
                    );
                }
            }
        }
    }

    /**
     * Draws all cities on the map.
     */
    private void drawCities(Graphics2D g2) {
        for (City city : cityPositions.keySet()) {
            Point point = cityPositions.get(city);

            g2.setColor(Color.WHITE);
            g2.fillOval(point.x - 13, point.y - 13, 26, 26);

            g2.setColor(Color.BLACK);
            g2.drawOval(point.x - 13, point.y - 13, 26, 26);

            g2.setFont(new Font("Arial", Font.BOLD, 11));
            g2.drawString(formatCityName(city), point.x + 16, point.y + 5);
        }
    }

    /**
     * Formats a city enum into a readable string.
     */
    private String formatCityName(City city) {
        return city.name().replace("_", " ");
    }

    /**
     * Determines the color used to draw a route.
     */
    private Color getDrawColor(Route route) {
        if (route.getOwner() != null) {
            return convertStringToColor(route.getOwner().getColour());
        }
        return convertColourToAwtColor(route.getColour());
    }

    /**
     * Returns a short label for a player.
     */
    private String getPlayerLabel(String playerName) {
        if (playerName.equalsIgnoreCase("Player 1")) return "P1";
        if (playerName.equalsIgnoreCase("Player 2")) return "P2";
        return "P";
    }

    /**
     * Converts backend Colour enum to AWT Color.
     */
    private Color convertColourToAwtColor(Colour colour) {
        switch (colour) {
            case GREEN: return Color.GREEN;
            case BLUE: return Color.BLUE;
            case YELLOW: return Color.YELLOW;
            case ORANGE: return Color.ORANGE;
            case PINK: return Color.PINK;
            case BLACK: return Color.BLACK;
            case MULTI: return Color.LIGHT_GRAY;
            default: return Color.GRAY;
        }
    }

    /**
     * Converts a string colour to AWT Color.
     */
    private Color convertStringToColor(String colour) {
        switch (colour.toLowerCase()) {
            case "blue": return Color.BLUE;
            case "green": return Color.GREEN;
            case "red": return Color.RED;
            case "yellow": return Color.YELLOW;
            case "black": return Color.BLACK;
            case "orange": return Color.ORANGE;
            case "pink": return Color.PINK;
            default: return Color.GRAY;
        }
    }
}