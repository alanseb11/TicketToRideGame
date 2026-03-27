package Backend_TicketSelection;

public class DestinationTicket {
    private final String startCity;
    private final String destinationCity;
    private final int points;

    public DestinationTicket(String startCity, String destinationCity, int points) {
        this.startCity = startCity;
        this.destinationCity = destinationCity;
        this.points = points;
    }

    public String getStartCity() {
        return startCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return startCity + " to " + destinationCity + " (" + points + " points)";
    }
}