package frontend;

import backend.City;
import backend.Colour;
import backend.Player;
import backend.Route;

import javax.swing.JPanel;
import java.awt.*;

public class MapPanel extends JPanel {

    private RouteVisual[] routeVisuals;

    public MapPanel() {
        setBackground(new Color(230, 220, 190));
        createRouteVisuals();
    }

    private void createRouteVisuals() {
        Player p1 = new Player("Player 1", "blue");
        Player p2 = new Player("Player 2", "green");

        Route route1 = new Route(Colour.BLACK, City.HYDE_PARK, City.BAKER_STREET, 4, null);

        Rectangle[] route1Segments = {
                new Rectangle(120, 520, 32, 14),
                new Rectangle(150, 490, 32, 14),
                new Rectangle(180, 460, 32, 14),
                new Rectangle(210, 430, 32, 14)
        };

        Route route2 = new Route(Colour.GREEN, City.BAKER_STREET, City.BRITISH_MUSEUM, 3, null);

        Rectangle[] route2Segments = {
                new Rectangle(210, 300, 32, 14),
                new Rectangle(250, 310, 32, 14),
                new Rectangle(290, 320, 32, 14)
        };

        Route route3 = new Route(Colour.ORANGE, City.WATERLOO, City.ELEPHANT_CASTLE, 2, null);

        Rectangle[] route3Segments = {
                new Rectangle(430, 500, 32, 14),
                new Rectangle(470, 530, 32, 14)
        };

        route1.setOwner(p1);
        route2.setOwner(p2);

        routeVisuals = new RouteVisual[] {
                new RouteVisual(route1, route1Segments),
                new RouteVisual(route2, route2Segments),
                new RouteVisual(route3, route3Segments)
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawRoutes(g2);
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

    private Color getDrawColor(Route route) {
        if (route.getOwner() != null) {
            return convertStringToColor(route.getOwner().getColour());
        }

        return convertColourToAwtColor(route.getColour());
    }

    private String getPlayerLabel(String playerName) {
        if (playerName.equalsIgnoreCase("Player 1")) return "P1";
        if (playerName.equalsIgnoreCase("Player 2")) return "P2";
        return "P";
    }

    private Color convertColourToAwtColor(Colour colour) {
        switch (colour) {
            case GREEN: return Color.GREEN;
            case YELLOW: return Color.YELLOW;
            case ORANGE: return Color.ORANGE;
            case PINK: return Color.PINK;
            case BLACK: return Color.BLACK;
            case MULTI: return Color.LIGHT_GRAY;
            default: return Color.GRAY;
        }
    }

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