package backend;

import frontend.MapPanel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GameSaveManager {

    public static void saveGame(Player[] players, int currentPlayerIndex, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("CURRENT_PLAYER=" + currentPlayerIndex);

            for (int i = 0; i < players.length; i++) {
                Player player = players[i];

                writer.println("PLAYER=" + i);
                writer.println("POINTS=" + player.getPoints());
                writer.println("BUSES=" + player.getBuses());

                writer.println("CARDS_START");
                for (Colour colour : player.getTransportCards().keySet()) {
                    writer.println(colour + "=" + player.getTransportCards().get(colour));
                }
                writer.println("CARDS_END");

                writer.println("ROUTES_START");
                for (City city : player.getPlayerRoutes().keySet()) {
                    for (Route route : player.getPlayerRoutes().get(city)) {
                        if (route.getCityA() == city) {
                            writer.println(route.getCityA() + "," + route.getCityB());
                        }
                    }
                }
                writer.println("ROUTES_END");

                writer.println("TICKETS_START");
                for (DestinationTicket ticket : player.getDestTicketCards()) {
                    writer.println(ticket.getCityA() + "," + ticket.getCityB() + "," + ticket.getPoints());
                }
                writer.println("TICKETS_END");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not save game.", e);
        }
    }

    public static int loadGame(Player[] players, MapPanel mapPanel, String fileName) {
        int currentPlayerIndex = 0;
        int currentPlayerBeingLoaded = -1;
        boolean readingCards = false;
        boolean readingRoutes = false;
        boolean readingTickets = false;

        for (Player player : players) {
            player.clearClaimedRoutes();
            player.getDestTicketCards().clear();
        }

        mapPanel.clearRouteOwners();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("CURRENT_PLAYER=")) {
                    currentPlayerIndex = Integer.parseInt(line.substring("CURRENT_PLAYER=".length()));

                } else if (line.startsWith("PLAYER=")) {
                    currentPlayerBeingLoaded = Integer.parseInt(line.substring("PLAYER=".length()));

                } else if (line.startsWith("POINTS=")) {
                    players[currentPlayerBeingLoaded].setPoints(
                            Integer.parseInt(line.substring("POINTS=".length()))
                    );

                } else if (line.startsWith("BUSES=")) {
                    players[currentPlayerBeingLoaded].setBuses(
                            Integer.parseInt(line.substring("BUSES=".length()))
                    );

                } else if (line.equals("CARDS_START")) {
                    readingCards = true;

                } else if (line.equals("CARDS_END")) {
                    readingCards = false;

                } else if (line.equals("ROUTES_START")) {
                    readingRoutes = true;

                } else if (line.equals("ROUTES_END")) {
                    readingRoutes = false;

                } else if (line.equals("TICKETS_START")) {
                    readingTickets = true;

                } else if (line.equals("TICKETS_END")) {
                    readingTickets = false;

                } else if (readingCards && currentPlayerBeingLoaded >= 0) {
                    String[] parts = line.split("=");
                    Colour colour = Colour.valueOf(parts[0]);
                    int amount = Integer.parseInt(parts[1]);
                    players[currentPlayerBeingLoaded].setTransportCardCount(colour, amount);

                } else if (readingRoutes && currentPlayerBeingLoaded >= 0) {
                    String[] parts = line.split(",");
                    City cityA = City.valueOf(parts[0]);
                    City cityB = City.valueOf(parts[1]);

                    Route route = mapPanel.findRoute(cityA, cityB);

                    if (route != null) {
                        Player owner = players[currentPlayerBeingLoaded];
                        route.setOwner(owner);
                        owner.addClaimedRouteWithoutCost(route);
                    }

                } else if (readingTickets && currentPlayerBeingLoaded >= 0) {
                    String[] parts = line.split(",");
                    City cityA = City.valueOf(parts[0]);
                    City cityB = City.valueOf(parts[1]);
                    int points = Integer.parseInt(parts[2]);

                    DestinationTicket ticket = new DestinationTicket(cityA, cityB, points);
                    players[currentPlayerBeingLoaded].addDestinationTicket(ticket);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load game.", e);
        }

        return currentPlayerIndex;
    }
}