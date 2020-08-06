package game;

import card.TaskCard;
import card.TrainCard;
import carddeck.*;
import city.City;
import city.CityNames;
import color.RouteColors;
import color.TrainCardColors;
import player.ComputerPlayer;
import player.Player;
import player.PlayersCreator;
import route.Graph;
import route.Route;
import route.RouteFileReader;
import route.RouteFinder;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Game {

    private final int DEFAULT_DIFF_LEVEL = 1;

    // attributes
    private boolean lastTurn;
    private int playerNum;

    // deck objects
    private TaskCardDeck taskCardDeck;
    private FaceDownDeck faceDownDeck;
    private FaceUpDeck faceUpDeck;
    private DiscardsDeck discardsDeck;
    private DeckManager manager;
    private List<Player> players;

    // graph and routes
    private RouteFileReader routeFileReader;
    private Set<Route<City>> allRoutes;
    private Graph<City, Route<City>> graph;
    private CityNames cityNames;
    private RouteFinder finder;

    public Game(int playerNum, String playerName) {
        // create players
        this.playerNum = playerNum;
        lastTurn = false;
        PlayersCreator.createPlayers(playerNum, playerName, DEFAULT_DIFF_LEVEL);
        players = PlayersCreator.getPlayers();

        // create decks
        taskCardDeck = TaskCardDeck.getObjectInstance();
        faceDownDeck = FaceDownDeck.getObjectInstance();
        faceUpDeck = FaceUpDeck.getObjectInstance();
        discardsDeck = DiscardsDeck.getObjectInstance();
        manager = DeckManager.getObjectInstance();

        // graph and routes
        routeFileReader = RouteFileReader.getRouteFileReaderInstance();
        routeFileReader.createAllRoutes();
        allRoutes = RouteFileReader.getRoutes();
        graph = new Graph<>(allRoutes);
        cityNames = CityNames.getInstance();
        finder = new RouteFinder(graph);
    }

    public boolean isLastTurn() {
        return lastTurn;
    }

    /**
     * set the lastTurn variable as true when one of the players has 2 or fewer train cards
     */
    private void setLastTurn() {
        for (Player p : players) {
            if (p.getNumOfTrainLeft() <= 2) {
                lastTurn = true;
                break;
            }
        }
    }

    /**
     * return players playing this round
     */
    public List<Player> getPlayers() { return players; }

    /**
     * game set up
     * deal 4 trainCards to each player,
     * deal 3 task cards to each player and player should choose at least 2 cards to keep
     */
    public void setUp() throws InterruptedException {
        System.out.println("======================= game set up =========================");

        System.out.println("Now let's set up for the game, let's get a starting hand of task cards and train cards. ");
        for (Player p : players) {
            // get train cards
            if (p.isHumanPlayer()) {
                System.out.println("You can choose 2 or more cards from the following 3 task cards. ");
            } else {
                System.out.println(p.getPlayerName() + " is choosing task cards. ");
            }
            p.getStartingHand();
            System.out.println(p.getPlayerName() + " has got the starting hand of train cards.");
            // get task cards
            List<TaskCard> taskCards = p.drawTaskCards();
            if (p.isHumanPlayer()) {
                p.displayTaskCards(taskCards);
            }
            Set<Integer> cardIndices = p.generateCardIndices();
            p.chooseTaskCards(taskCards, cardIndices);
            System.out.println("--------------------------------------------------");
            TimeUnit.SECONDS.sleep(2);
        }
        System.out.println("There are " + faceUpDeck.getSize() + " cards in the face-up deck. ");
        System.out.println("There are " + faceDownDeck.getSize() + " cards in the face-deck deck. ");
        System.out.println("There are " + discardsDeck.getSize() + " cards in the discards deck");
        System.out.println("Set up finished. ");
    }

    /**
     * check if a input city name is valid
     * @param scanner scanner to accept input
     * @param inputContent  the content received from scanner
     * @return  a valid city name
     */
    public String inputCityNamePrompt(Scanner scanner, String inputContent) {
        String cityName = scanner.nextLine().strip();
        while (!cityNames.exists(cityName)) {
            System.out.print("This city doesn't exist, try again, " +
                    "type in the name of " + inputContent + " of your route: ");
            cityName = scanner.nextLine();
        }

        return cityName;
    }

    /**
     * check if a input activity option is valid, prompt the users to input correct input
     * options: a - draw train cards
     *          b - draw task cards
     *          c - claim a route
     * @param scanner scanner to accept input
     * @param player current player playing the turn
     * @return a String representation of an acceptable/doable command
     */
    public String inputActivityOptionPrompt(Scanner scanner, Player player) {
        Set<String> options = new HashSet<>(Set.of("a", "b", "c"));

        // prompt input, which behavior the player wants to perform
        System.out.print("\nChoose what activity you want to do: \n" +
                "   (a)draw train cards,\n" +
                "   (b)draw task cards, \n" +
                "   (c)claim a route.\n" + "Type in a, b, or c: ");

        // filter out invalid input
        String command = scanner.next().strip().toLowerCase();
        while (!options.contains(command)) {
            System.out.print("Invalid input. Try again. Type in a, b, or c: ");
            command = scanner.next().strip().toLowerCase();
        }

        // check if drawing train cards is ok
        if (faceUpDeck.getSize() + faceDownDeck.getSize() <= 2 && discardsDeck.isEmpty()) {
            options.remove("a");
        }

        // check if drawing task cards is ok
        if (taskCardDeck.isEmpty()) {
            options.remove("b");
        }

        // check if the player has at least one card to claim routes
        if (player.getNumOfTrainCard() == 0) {
            options.remove("c");
        }

        while (!options.contains(command)) {
            if (command.equals("a")) {
                System.out.print("Train cards have been exhausted, please try other options.");
            } else if (command.equals("b")) {
                System.out.println("Task cards have been exhausted, please try other options.");
            } else {
                System.out.println("Not enough train cards, please try other options.");
            }
            command = scanner.next().strip().toLowerCase();
        }

        return command;
    }


    /**
     * check if the user input is a valid integer input given lower and upper bounds
     * @param lowerBound inclusive
     * @param upperBound exclusive
     * @return  an acceptable int within bound
     */
    public static int checkIntInput(Scanner scanner,  String input, int lowerBound, int upperBound) {
        input = input.strip();
        if (input.length() == 1) {
            char c = input.charAt(0);
            if ((c >= ((char)(lowerBound + '0')) && (c < (char)(upperBound + '0')))) {
                return Integer.parseInt(input);
            } else {
                System.out.print("Invalid input, type in a number between " + lowerBound + "(inclusive) and "
                        + upperBound + "(exclusive): ");
                return checkIntInput(scanner, scanner.next(), lowerBound, upperBound);
            }
        } else {
            System.out.print("Invalid input, input should be no longer than 1 digit. Try again: ");
            return checkIntInput(scanner, scanner.next(), lowerBound, upperBound);
        }
    }


    private void humanDrawFaceUp(Scanner scanner, Player p) {
        System.out.print("Input a number from 0(inclusive) to " + faceUpDeck.getSize()
                + "(exclusive): " );
        // not enough face-up cards to draw
        if (faceUpDeck.getSize() < 2) {
            System.out.println("Not enough cards in the face-up deck, try other options.");
        }

        int faceUpIndex = checkIntInput(scanner, scanner.next(), 0, faceUpDeck.getSize());
        TrainCard tempCard = FaceUpDeck.getListInstance().get(faceUpIndex);

        // when the face-down is empty, there will be some nulls in the face-up
        while (tempCard == null && faceDownDeck.isEmpty()) {
            System.out.print("This choice is not available now " + "Try again, input another number " +
                    "between 0 - 5" + " except " + faceUpIndex + ": ");
            faceUpIndex = checkIntInput(scanner, scanner.next(), 0, faceUpDeck.getSize());
            tempCard = FaceUpDeck.getListInstance().get(faceUpIndex);
        }

        // prompt to draw other card if the second one is rainbow
        while (tempCard.getColor().equalsIgnoreCase("rainbow") &&
                !p.isAbleToDrawRainbowCard()) {
            System.out.print("You have drawn another card in this round, " +
                    "cannot draw a rainbow card. Try again, input a number between 0 - " +
                    faceUpDeck.getSize() + " except " + faceUpIndex + ": ");
            faceUpIndex = checkIntInput(scanner, scanner.next(), 0, faceUpDeck.getSize());
            tempCard = FaceUpDeck.getListInstance().get(faceUpIndex);
        }
        TrainCard currCard = p.drawAFaceUpCard(faceUpIndex);
        // can't draw the second card if the first card is a face-up rainbow card
        if (currCard.getColor().equalsIgnoreCase("rainbow")) {
            p.setUnableToDrawTrainCard();
        }
    }


    /**
     * prompt human player to draw train card
     * @param scanner   the scanner to accept user input
     * @param p     current player playing the turn
     */
    private void humanDrawTrainCard(Scanner scanner, Player p) {
        System.out.println("You have chosen to draw train cards.");

        // display the cards owned by the player
        p.displayOwnedTrainCards();

        p.setAbleToDrawTrainCard(); // set the player to be able to draw cards
        p.setAbleToDrawRainbowCard();

        int counter = 0; // set counter to memorize the number of cards being drawn each turn

        // each player can draw two cards by default in each turn
        while (p.isAbleToDrawTrainCard() && counter < 2) {
            if (counter == 0) {
                System.out.println("You are drawing the 1st card of this round. ");
            } else {
                System.out.println("You are drawing the 2nd card of this round.");
            }
            // display face-up deck cards
            faceUpDeck.display();
            System.out.print("Do you want to draw from the face-up or face-down deck? \n" +
                    "   (u) face-up deck, \n" + "   (d) face-down deck. \n" +
                    "Type in u(up) or d(down): ");

            String drawTrainCardCommand = scanner.next();
            while (!drawTrainCardCommand.equalsIgnoreCase("u") &&
                    !drawTrainCardCommand.equalsIgnoreCase("d")) {
                System.out.print("Try again. Invalid input, type in u(up) or d(down): ");
                drawTrainCardCommand = scanner.next();
            }

            // draw from face-up
            if (drawTrainCardCommand.equalsIgnoreCase("u")) {
                humanDrawFaceUp(scanner, p);

                // draw from face-down, no rainbow card restriction, blind draw
            } else {
                // if face-down is empty
                if (faceDownDeck.isEmpty()) {
                    System.out.println("The face-down cards have been exhausted, draw face-up cards instead.");
                    humanDrawFaceUp(scanner, p);
                } else {
                    // draw from face-down
                    p.drawAFaceDownCard();
                }
            }
            // can't draw a rainbow card as the second card
            p.setUnableToDrawRainbowCard();

            // manage deck within the drawing train card function, after drawing each card
            manager.manageDecks();
            counter ++;
        }
    }

    /**
     * prompt human player to draw task cards
     * @param scanner scanner to accept input
     * @param p current player playing the turn
     */
    private void humanDrawTaskCard(Scanner scanner, Player p) {
        // draw task cards
        System.out.println("You have chosen to draw task cards. ");
        System.out.println("Choose 1 or more cards from the following 3 cards. ");
        List<TaskCard> taskCards = p.drawTaskCards();
        p.displayTaskCards(taskCards);
        Set<Integer> cardIndices = p.generateCardIndices();
        p.chooseTaskCards(taskCards, cardIndices);
    }

    /**
     * prompt human player to claim route
     * @param scanner scanner to accept input
     * @param p current player playing the turn
     */
    private void humanClaimRoute(Scanner scanner, Player p) {
        // input city 1
        System.out.println("You have chosen to claim a route.");
        System.out.println("Type in the two city names of this target route, orders don't matter. ");
        System.out.print("City 1: ");
        scanner.nextLine();
        String cityName1 = inputCityNamePrompt(scanner, "city 1");

        // input city 2
        System.out.print("City 2: ");
        String cityName2 = inputCityNamePrompt(scanner, "city 2");

        // check if route exists
        Route<City> route = finder.searchForRoute(cityName1, cityName2);
        System.out.print("Searching for route...\n");

        while (route == null) {
            System.out.println("There are no such route existing between " + cityName1 + " and " + cityName2);
            System.out.print("Try again. Type in city 1 name: ");
            cityName1 = inputCityNamePrompt(scanner, "city 1");
            System.out.print("Type in city 2 name: ");
            cityName2 = inputCityNamePrompt(scanner, "city 2");
            route = finder.searchForRoute(cityName1, cityName2);
        }
        System.out.println("Route found.");

        // input color
        route.displayColors();
        System.out.print("Type in a color of your choice: ");
        String routeColor = scanner.next().strip();
        while (!RouteColors.contains(routeColor) || !route.getColors().contains(routeColor)) {
            System.out.print("Invalid color, try again: ");
            routeColor = scanner.next();
        }

        // get combinations of train cards
        List<Map<String, Integer>> combinations = p.getCardCombinationOnATargetRoute(route, routeColor);
        p.displayCardCombinations(combinations);
        // no combination, choose another route
        if (combinations == null) {
            System.out.println("Don't have enough cards to claim this route, try another route.");
            humanClaimRoute(scanner, p);
        } else {
            System.out.print("Type in choice number between 0 and " + combinations.size() + ": ");
            int index = checkIntInput(scanner, scanner.next(), 0, combinations.size());
            p.chooseACombination(route, routeColor, combinations, index);
            setLastTurn(); // set last turn only after a player claims routes/ uses cars
        }
    }

    /**
     * play a turn,
     * integrate all the draw face-up, face-down, task cards, claim routes behaviors of a player
     * @param scanner  scanner to accept input
     */
    public void playATurn(Scanner scanner) throws InterruptedException {
        for (Player p : players) {
            p.setAbleToDrawTrainCard();
            p.setAbleToDrawRainbowCard();
            // human player behavior
            if (p.isHumanPlayer()) {
                // prompt input, which behavior the player wants to perform
                String command = inputActivityOptionPrompt(scanner, p);

                if (command.equalsIgnoreCase("a")) {
                    // draw from face-down or down
                    humanDrawTrainCard(scanner, p);
                } else if (command.equalsIgnoreCase("b")) {
                    // draw task cards
                    humanDrawTaskCard(scanner, p);
                } else {
                    // claim a route
                    humanClaimRoute(scanner, p);
                }
                System.out.println("\nYour turn is over. Please wait. ");
                TimeUnit.SECONDS.sleep(2);
            } else {
                // computer player
                System.out.println("Now it is " + p.getPlayerName() + "'s turn.");
                TimeUnit.SECONDS.sleep(2);
                // TODO: not finished, computer player's logic
                if (!faceUpDeck.isEmpty() || !faceDownDeck.isEmpty()) {
                    ((ComputerPlayer)p).play();
                }
            }
            System.out.println("--------------------------------------------------");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("        Welcome to Ticket to Ride, are you ready?");
        System.out.println("********************************************************");

        // get number of players
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select number (2 to 5) of players: ");

        int playerNum = Game.checkIntInput(scanner, scanner.next(), 2, 6);
        // get player name
        System.out.print("Type in your user name: ");
        String playerName = scanner.next();
        System.out.print("Hello " + playerName + ", welcome to the game.\n\n");
        Game game = new Game(playerNum, playerName);
        game.setUp();

        System.out.println("Now the game starts. ");
        int turnCounter = 1;
        while (!game.isLastTurn()) {
            System.out.print("\n========================== Turn " + turnCounter +" ===========================\n");
            game.playATurn(scanner);
            turnCounter++;
        }

        System.out.println("Caution, this is the last turn of this round. ");

        int maxPoint = 0;
        String winnerName = "";

        // TODO: play last turn, implements longest path finder
        if (game.isLastTurn()) {
            for (Player p : game.players) {
                p.calculateUnfinishedTasks(); // deal with unfinished tasks, deduct points
                if (p.getPoints() > maxPoint) {
                    maxPoint = p.getPoints();
                }
            }
        }
        System.out.println("Player " + winnerName + " wins. ");
    }

}
