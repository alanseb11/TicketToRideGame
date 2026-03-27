import java.util.*;

public class ticket_to_ride_prototype {

    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();

    static String[] cities = {"A", "B", "C", "D", "E"};

    static Route[] routes = {
            new Route("A", "B", "red", 2),
            new Route("B", "C", "blue", 2),
            new Route("C", "D", "green", 2),
            new Route("D", "E", "yellow", 2),
            new Route("A", "C", "red", 3),
            new Route("B", "D", "blue", 3),
            new Route("C", "E", "green", 3)
    };

    static String[] deckColors = {"red", "blue", "green", "yellow"};
    static ArrayList<String> deck = new ArrayList<>();

    public static void main(String[] args) {
        setupDeck();

        Player human = new Player("You");
        Player ai = new Player("AI");

        dealStartingCards(human);
        dealStartingCards(ai);

        System.out.println("=== Basic Ticket to Ride Prototype ===");
        System.out.println("2 players: You vs AI");
        System.out.println("Goal: claim routes and get the highest score.\n");

        boolean gameOver = false;
        int turn = 1;

        while (!gameOver) {
            System.out.println("------------------------");
            System.out.println("TURN " + turn);
            System.out.println("------------------------");

            playerTurn(human);
            if (allRoutesTaken() || human.trains <= 0) {
                gameOver = true;
                break;
            }

            aiTurn(ai);
            if (allRoutesTaken() || ai.trains <= 0) {
                gameOver = true;
            }

            turn++;
        }

        System.out.println("\n=== GAME OVER ===");
        printFinal(human, ai);
    }

    static class Player {
        String name;
        int score = 0;
        int trains = 10;
        ArrayList<String> hand = new ArrayList<>();

        Player(String name) {
            this.name = name;
        }
    }

    static class Route {
        String city1;
        String city2;
        String color;
        int length;
        String owner = "";

        Route(String city1, String city2, String color, int length) {
            this.city1 = city1;
            this.city2 = city2;
            this.color = color;
            this.length = length;
        }
    }

    static void setupDeck() {
        for (int i = 0; i < 12; i++) {
            deck.addAll(Arrays.asList(deckColors));
        }
        Collections.shuffle(deck);
    }

    static void dealStartingCards(Player p) {
        for (int i = 0; i < 4; i++) {
            drawCard(p);
        }
    }

    static void drawCard(Player p) {
        if (!deck.isEmpty()) {
            p.hand.add(deck.remove(0));
        }
    }

    static void playerTurn(Player p) {
        System.out.println(p.name + " | Score: " + p.score + " | Trains: " + p.trains);
        System.out.println("Your hand: " + p.hand);
        printRoutes();

        System.out.println("Choose action:");
        System.out.println("1. Draw 2 cards");
        System.out.println("2. Claim a route");
        System.out.print("> ");
        String choice = sc.nextLine();

        if (choice.equals("1")) {
            drawCard(p);
            drawCard(p);
            System.out.println("You drew 2 cards.");
        } else if (choice.equals("2")) {
            claimRouteMenu(p);
        } else {
            System.out.println("Invalid choice. Turn skipped.");
        }
    }

    static void claimRouteMenu(Player p) {
        System.out.print("Enter route number: ");
        try {
            int routeIndex = Integer.parseInt(sc.nextLine()) - 1;
            if (routeIndex < 0 || routeIndex >= routes.length) {
                System.out.println("Invalid route.");
                return;
            }

            Route r = routes[routeIndex];

            if (!r.owner.isEmpty()) {
                System.out.println("Route already taken.");
                return;
            }

            if (p.trains < r.length) {
                System.out.println("Not enough trains left.");
                return;
            }

            int count = countColor(p, r.color);
            if (count < r.length) {
                System.out.println("Not enough " + r.color + " cards.");
                return;
            }

            removeColorCards(p, r.color, r.length);
            r.owner = p.name;
            p.trains -= r.length;
            p.score += r.length;

            System.out.println("You claimed " + r.city1 + "-" + r.city2 + " for " + r.length + " points.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    static void aiTurn(Player ai) {
        System.out.println("\nAI TURN");

        ArrayList<Route> possible = new ArrayList<>();
        for (Route r : routes) {
            if (r.owner.isEmpty() && ai.trains >= r.length && countColor(ai, r.color) >= r.length) {
                possible.add(r);
            }
        }

        if (!possible.isEmpty() && random.nextBoolean()) {
            Route r = possible.get(random.nextInt(possible.size()));
            removeColorCards(ai, r.color, r.length);
            r.owner = ai.name;
            ai.trains -= r.length;
            ai.score += r.length;
            System.out.println("AI claimed " + r.city1 + "-" + r.city2 + ".");
        } else {
            drawCard(ai);
            drawCard(ai);
            System.out.println("AI drew 2 cards.");
        }
    }

    static int countColor(Player p, String color) {
        int count = 0;
        for (String c : p.hand) {
            if (c.equals(color)) {
                count++;
            }
        }
        return count;
    }

    static void removeColorCards(Player p, String color, int amount) {
        for (int i = 0; i < p.hand.size() && amount > 0; i++) {
            if (p.hand.get(i).equals(color)) {
                p.hand.remove(i);
                i--;
                amount--;
            }
        }
    }

    static void printRoutes() {
        System.out.println("Available routes:");
        for (int i = 0; i < routes.length; i++) {
            Route r = routes[i];
            String status = r.owner.isEmpty() ? "free" : "taken by " + r.owner;
            System.out.println((i + 1) + ". " + r.city1 + "-" + r.city2 +
                    " | color: " + r.color +
                    " | length: " + r.length +
                    " | " + status);
        }
        System.out.println();
    }

    static boolean allRoutesTaken() {
        for (Route r : routes) {
            if (r.owner.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    static void printFinal(Player p1, Player p2) {
        System.out.println(p1.name + " score: " + p1.score);
        System.out.println(p2.name + " score: " + p2.score);

        if (p1.score > p2.score) {
            System.out.println(p1.name + " wins!");
        } else if (p2.score > p1.score) {
            System.out.println(p2.name + " wins!");
        } else {
            System.out.println("It's a draw!");
        }
    }
}
