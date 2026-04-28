package frontend;

import backend.Player;
import backend.Route;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class MapPanel extends JPanel {

    private RouteVisual[] routeVisuals;

    public MapPanel() {
        setBackground(new Color(230, 220, 190));
        createRouteVisuals();
    }

    private void createRouteVisuals() {
        // test players
        Player p1 = new Player("Player 1", "blue");
        Player p2 = new Player("Player 2", "red");

        // Route 1: Hyde Park -> Baker Street
        Route route1 = new Route("black", "Hyde Park", "Baker Street", 4);

        Rectangle[] route1Segments = new Rectangle[] {
                new Rectangle(120, 520, 32, 14),
                new Rectangle(150, 490, 32, 14),
                new Rectangle(180, 460, 32, 14),
                new Rectangle(210, 430, 32, 14)
        };

        // Route 2: Baker Street -> British Museum
        Route route2 = new Route("red", "Baker Street", "British Museum", 3);

        Rectangle[] route2Segments = new Rectangle[] {
                new Rectangle(210, 300, 32, 14),
                new Rectangle(250, 310, 32, 14),
                new Rectangle(290, 320, 32, 14)
        };

        // Route 3: Waterloo -> Elephant & Castle
        Route route3 = new Route("orange", "Waterloo", "Elephant & Castle", 2);

        Rectangle[] route3Segments = new Rectangle[] {
                new Rectangle(430, 500, 32, 14),
                new Rectangle(470, 530, 32, 14)
        };

        // test ownership
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
            Rectangle[] segments = routeVisual.getSegments();

            Color drawColor = getDrawColor(route);

            for (Rectangle segment : segments) {
                g2.setColor(drawColor);
                g2.fillRoundRect(segment.x, segment.y, segment.width, segment.height, 8, 8);

                g2.setColor(Color.WHITE);
                g2.drawRoundRect(segment.x, segment.y, segment.width, segment.height, 8, 8);

                // Draw owner text if claimed
                if (route.getOwner() != null) {
                    g2.setFont(new Font("Arial", Font.BOLD, 10));
                    g2.setColor(Color.WHITE);
                    String label = getPlayerLabel(route.getOwner().getName());
                    g2.drawString(label, segment.x + 8, segment.y + 10);
                }
            }
        }
    }

    private String getPlayerLabel(String playerName) {
        if (playerName.equalsIgnoreCase("Player 1")) {
            return "P1";
        } else if (playerName.equalsIgnoreCase("Player 2")) {
            return "P2";
        }
        return "P";
    }

    private Color getDrawColor(Route route) {
        if (route.getOwner() != null) {
            return convertStringToColor(route.getOwner().getColour());
        }
        return convertStringToColor(route.getColour());
    }

    private Color convertStringToColor(String colour) {
        switch (colour.toLowerCase()) {
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "green":
                return Color.GREEN;
            case "yellow":
                return Color.YELLOW;
            case "black":
                return Color.BLACK;
            case "orange":
                return Color.ORANGE;
            case "pink":
                return Color.PINK;
            case "white":
                return Color.LIGHT_GRAY;
            default:
                return Color.GRAY;
        }
    }
}