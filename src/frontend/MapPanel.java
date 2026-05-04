package frontend;

import backend.City;
import backend.Colour;
import backend.Route;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class MapPanel extends JPanel {

    private final GameController controller;
    private HashMap<City, Point> cityPositions;
    private RouteVisual[] routeVisuals;

    public MapPanel(GameController controller) {
        this.controller = controller;
        setBackground(new Color(230, 220, 190));
        createCityPositions();
        createRouteVisuals();
        setupMouseListener();
    }

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

    private void createCityPositions() {
        cityPositions = new HashMap<>();

        cityPositions.put(City.REGENTS_PARK, new Point(120, 100));
        cityPositions.put(City.BAKER_STREET, new Point(150, 180));
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
        routeVisuals = new RouteVisual[] {
                // Top / left
                createRouteVisual(Colour.GREEN, City.REGENTS_PARK, City.KINGS_CROSS, 4),
                createRouteVisual(Colour.BLUE, City.REGENTS_PARK, City.BAKER_STREET, 2),
                createRouteVisual(Colour.YELLOW, City.REGENTS_PARK, City.BRITISH_MUSEUM, 3),
                createRouteVisual(Colour.ORANGE, City.BAKER_STREET, City.BRITISH_MUSEUM, 4),
                createRouteVisual(Colour.BLACK, City.HYDE_PARK, City.BAKER_STREET, 4),
                createRouteVisual(Colour.MULTI, City.BAKER_STREET, City.PICCADILLY_CIRCUS, 4),
                createRouteVisual(Colour.MULTI, City.PICCADILLY_CIRCUS, City.BRITISH_MUSEUM, 2),

                // Middle
                createRouteVisual(Colour.BLACK, City.KINGS_CROSS, City.BRITISH_MUSEUM, 2),
                createRouteVisual(Colour.PINK, City.KINGS_CROSS, City.THE_CHARTERHOUSE, 3),
                createRouteVisual(Colour.MULTI, City.BRITISH_MUSEUM, City.COVENT_GARDEN, 1, -10),
                createRouteVisual(Colour.MULTI, City.BRITISH_MUSEUM, City.COVENT_GARDEN, 1, 10),
                createRouteVisual(Colour.BLUE, City.BRITISH_MUSEUM, City.THE_CHARTERHOUSE, 4),
                createRouteVisual(Colour.GREEN, City.THE_CHARTERHOUSE, City.BRICK_LANE, 3),
                createRouteVisual(Colour.MULTI, City.COVENT_GARDEN, City.ST_PAULS, 3,-10),
                createRouteVisual(Colour.MULTI, City.COVENT_GARDEN, City.ST_PAULS, 3,10),
                createRouteVisual(Colour.PINK, City.COVENT_GARDEN, City.TRAFALGAR_SQUARE, 1,-10),
                createRouteVisual(Colour.BLACK, City.COVENT_GARDEN, City.TRAFALGAR_SQUARE, 1,10),
                createRouteVisual(Colour.GREEN, City.COVENT_GARDEN, City.PICCADILLY_CIRCUS, 1,-10),
                createRouteVisual(Colour.YELLOW, City.COVENT_GARDEN, City.PICCADILLY_CIRCUS, 1,10),
                createRouteVisual(Colour.MULTI, City.PICCADILLY_CIRCUS, City.HYDE_PARK, 2,-10),
                createRouteVisual(Colour.MULTI, City.PICCADILLY_CIRCUS, City.HYDE_PARK, 2,10),

                // Bottom left
                createRouteVisual(Colour.ORANGE, City.HYDE_PARK, City.BUCKINGHAM_PALACE, 1,-10),
                createRouteVisual(Colour.YELLOW, City.HYDE_PARK, City.BUCKINGHAM_PALACE, 1,10),
                createRouteVisual(Colour.PINK, City.PICCADILLY_CIRCUS, City.BUCKINGHAM_PALACE, 2),
                createRouteVisual(Colour.BLUE, City.PICCADILLY_CIRCUS, City.TRAFALGAR_SQUARE, 1,10),
                createRouteVisual(Colour.ORANGE, City.PICCADILLY_CIRCUS, City.TRAFALGAR_SQUARE, 1,-10),
                createRouteVisual(Colour.GREEN, City.BUCKINGHAM_PALACE, City.BIG_BEN, 2),
                createRouteVisual(Colour.BLUE, City.WATERLOO, City.BIG_BEN, 1),
                createRouteVisual(Colour.MULTI, City.WATERLOO, City.TRAFALGAR_SQUARE, 2),
                createRouteVisual(Colour.MULTI, City.BUCKINGHAM_PALACE, City.TRAFALGAR_SQUARE, 2),
                createRouteVisual(Colour.MULTI, City.TRAFALGAR_SQUARE, City.BIG_BEN, 1),

                // Bottom / right
                createRouteVisual(Colour.YELLOW, City.BIG_BEN, City.ELEPHANT_CASTLE, 3),
                createRouteVisual(Colour.ORANGE, City.WATERLOO, City.ELEPHANT_CASTLE, 2),
                createRouteVisual(Colour.PINK, City.WATERLOO, City.GLOBE_THEATRE, 2),
                createRouteVisual(Colour.GREEN, City.GLOBE_THEATRE, City.ELEPHANT_CASTLE, 2),
                createRouteVisual(Colour.MULTI, City.GLOBE_THEATRE, City.TOWER_OF_LONDON, 3),
                createRouteVisual(Colour.PINK, City.ST_PAULS, City.TOWER_OF_LONDON, 3,10),
                createRouteVisual(Colour.YELLOW, City.ST_PAULS, City.TOWER_OF_LONDON, 3,-10),
                createRouteVisual(Colour.BLUE, City.BRICK_LANE, City.TOWER_OF_LONDON, 3),
                createRouteVisual(Colour.BLACK, City.ELEPHANT_CASTLE, City.TOWER_OF_LONDON, 4),
                createRouteVisual(Colour.ORANGE, City.BRICK_LANE, City.ST_PAULS, 3),
                createRouteVisual(Colour.MULTI, City.ST_PAULS, City.GLOBE_THEATRE, 1,10),
                createRouteVisual(Colour.MULTI, City.ST_PAULS, City.GLOBE_THEATRE, 1,-10),
                createRouteVisual(Colour.BLACK, City.ST_PAULS, City.THE_CHARTERHOUSE, 1)
        };
    }

    private RouteVisual createRouteVisual(Colour colour, City cityA, City cityB, int length) {
        return createRouteVisual(colour, cityA, cityB, length, 0);
    }

    private RouteVisual createRouteVisual(Colour colour, City cityA, City cityB, int length, int offset) {
        Route route = new Route(colour, cityA, cityB, length, null);
        Rectangle[] segments = createSegmentsBetween(cityA, cityB, length, offset);
        return new RouteVisual(route, segments);
    }

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

    private void drawRotatedSegment(Graphics2D g2, Rectangle segment, double angle, Color color, boolean selected) {
        Graphics2D copy = (Graphics2D) g2.create();

        int centerX = segment.x + segment.width / 2;
        int centerY = segment.y + segment.height / 2;

        copy.rotate(angle, centerX, centerY);

        copy.setColor(color);
        copy.fillRoundRect(segment.x, segment.y, segment.width, segment.height, 8, 8);

        copy.setColor(Color.WHITE);
        copy.drawRoundRect(segment.x, segment.y, segment.width, segment.height, 8, 8);

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
            case BLUE:
                return Color.BLUE;
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