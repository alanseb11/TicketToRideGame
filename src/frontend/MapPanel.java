package frontend;

import backend.City;
import backend.Colour;
import backend.Player;
import backend.Route;

import javax.swing.JPanel;
import java.awt.*;
import java.util.HashMap;

public class MapPanel extends JPanel {

    private HashMap<City, Point> cityPositions;
    private RouteVisual[] routeVisuals;

    public MapPanel() {
        setBackground(new Color(230, 220, 190));
        createCityPositions();
        createRouteVisuals();
    }

    private void createCityPositions() {
        cityPositions = new HashMap<>();

        cityPositions.put(City.REGENTS_PARK, new Point(120, 100));
        cityPositions.put(City.BAKER_STREET, new Point(180, 180));
        cityPositions.put(City.BRITISH_MUSEUM, new Point(300, 240));
        cityPositions.put(City.HYDE_PARK, new Point(130, 480));
        cityPositions.put(City.BUCKINGHAM_PALACE, new Point(280, 520));
        cityPositions.put(City.PICCADILLY_CIRCUS, new Point(220, 360));
        cityPositions.put(City.KINGS_CROSS, new Point(430, 90));
        cityPositions.put(City.THE_CHARTERHOUSE, new Point(540, 210));
        cityPositions.put(City.ST_PAULS, new Point(610, 300));
        cityPositions.put(City.COVENT_GARDEN, new Point(430, 360));
        cityPositions.put(City.TRAFALGAR_SQUARE, new Point(390, 430));
        cityPositions.put(City.BIG_BEN, new Point(430, 550));
        cityPositions.put(City.WATERLOO, new Point(580, 470));
        cityPositions.put(City.GLOBE_THEATRE, new Point(690, 400));
        cityPositions.put(City.TOWER_OF_LONDON, new Point(820, 310));
        cityPositions.put(City.BRICK_LANE, new Point(840, 160));
        cityPositions.put(City.ELEPHANT_CASTLE, new Point(690, 560));
    }

    private void createRouteVisuals() {
        RouteVisual route1 = createRouteVisual(Colour.BLACK, City.HYDE_PARK, City.BAKER_STREET, 4);
        RouteVisual route2 = createRouteVisual(Colour.ORANGE, City.BAKER_STREET, City.BRITISH_MUSEUM, 3);
        RouteVisual route3 = createRouteVisual(Colour.GREEN, City.REGENTS_PARK, City.KINGS_CROSS, 4);
        RouteVisual route4 = createRouteVisual(Colour.YELLOW, City.REGENTS_PARK, City.BRITISH_MUSEUM, 4);
        RouteVisual route5 = createRouteVisual(Colour.BLACK, City.KINGS_CROSS, City.COVENT_GARDEN, 4);
        RouteVisual route6 = createRouteVisual(Colour.GREEN, City.THE_CHARTERHOUSE, City.BRICK_LANE, 4);
        RouteVisual route7 = createRouteVisual(Colour.ORANGE, City.ST_PAULS, City.TOWER_OF_LONDON, 4);
        RouteVisual route8 = createRouteVisual(Colour.PINK, City.GLOBE_THEATRE, City.TOWER_OF_LONDON, 4);
        RouteVisual route9 = createRouteVisual(Colour.GREEN, City.BUCKINGHAM_PALACE, City.BIG_BEN, 3);
        RouteVisual route10 = createRouteVisual(Colour.YELLOW, City.BIG_BEN, City.ELEPHANT_CASTLE, 4);
        RouteVisual route11 = createRouteVisual(Colour.ORANGE, City.WATERLOO, City.ELEPHANT_CASTLE, 3);
        RouteVisual route12 = createRouteVisual(Colour.GREEN, City.WATERLOO, City.GLOBE_THEATRE, 3);
        RouteVisual route13 = createRouteVisual(Colour.BLACK, City.GLOBE_THEATRE, City.ELEPHANT_CASTLE, 4);

        Player p1 = new Player("Player 1", "blue");
        Player p2 = new Player("Player 2", "red");

        route1.getRoute().setOwner(p1);
        route2.getRoute().setOwner(p2);

        routeVisuals = new RouteVisual[] {
                route1, route2, route3, route4, route5, route6, route7,
                route8, route9, route10, route11, route12, route13
        };
    }

    private RouteVisual createRouteVisual(Colour colour, City cityA, City cityB, int length) {
        Route route = new Route(colour, cityA, cityB, length, null);
        Rectangle[] segments = createSegmentsBetween(cityA, cityB, length);
        return new RouteVisual(route, segments);
    }

    private Rectangle[] createSegmentsBetween(City cityA, City cityB, int length) {
        Point start = cityPositions.get(cityA);
        Point end = cityPositions.get(cityB);

        Rectangle[] segments = new Rectangle[length];

        int width = 34;
        int height = 14;

        for (int i = 0; i < length; i++) {
            double t = (i + 1.0) / (length + 1.0);

            int x = (int) (start.x + t * (end.x - start.x)) - width / 2;
            int y = (int) (start.y + t * (end.y - start.y)) - height / 2;

            segments[i] = new Rectangle(x, y, width, height);
        }

        return segments;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawRoutes(g2);
        drawCities(g2);
    }

    private void drawRoutes(Graphics2D g2) {
        for (RouteVisual routeVisual : routeVisuals) {
            Route route = routeVisual.getRoute();
            Color drawColor = getDrawColor(route);

            for (Rectangle segment : routeVisual.getSegments()) {
                g2.setColor(drawColor);
                g2.fillRoundRect(segment.x, segment.y, segment.width, segment.height, 8, 8);

                g2.setColor(Color.WHITE);
                g2.drawRoundRect(segment.x, segment.y, segment.width, segment.height, 8, 8);

                if (route.getOwner() != null) {
                    g2.setFont(new Font("Arial", Font.BOLD, 10));
                    g2.setColor(Color.WHITE);
                    g2.drawString(getPlayerLabel(route.getOwner().getName()), segment.x + 8, segment.y + 10);
                }
            }
        }
    }

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

    private String formatCityName(City city) {
        return city.name().replace("_", " ");
    }

    private Color getDrawColor(Route route) {
        if (route.getOwner() != null) {
            return convertStringToColor(route.getOwner().getColour());
        }

        return convertColourToAwtColor(route.getColour());
    }

    private String getPlayerLabel(String playerName) {
        if (playerName.equalsIgnoreCase("Player 1")) {
            return "P1";
        } else if (playerName.equalsIgnoreCase("Player 2")) {
            return "P2";
        }
        return "P";
    }

    private Color convertColourToAwtColor(Colour colour) {
        switch (colour) {
            case GREEN:
                return Color.GREEN;
            case YELLOW:
                return Color.YELLOW;
            case ORANGE:
                return Color.ORANGE;
            case PINK:
                return Color.PINK;
            case BLACK:
                return Color.BLACK;
            case MULTI:
                return Color.LIGHT_GRAY;
            default:
                return Color.GRAY;
        }
    }

    private Color convertStringToColor(String colour) {
        switch (colour.toLowerCase()) {
            case "blue":
                return Color.BLUE;
            case "green":
                return Color.GREEN;
            case "red":
                return Color.RED;
            case "yellow":
                return Color.YELLOW;
            case "black":
                return Color.BLACK;
            case "orange":
                return Color.ORANGE;
            case "pink":
                return Color.PINK;
            default:
                return Color.GRAY;
        }
    }
}