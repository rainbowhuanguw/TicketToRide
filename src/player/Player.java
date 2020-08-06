package player;

import card.TaskCard;
import card.TrainCard;
import carddeck.FaceDownDeck;
import carddeck.FaceUpDeck;
import carddeck.TaskCardDeck;
import city.City;
import game.Game;
import route.PathFinder;
import route.Route;

import java.util.*;

/**
 * Author: Rainbow Huang
 * Date: July 15, 2020
 */
//TODO: last round and winner note? how to calculate the maximum number drawing cards of 1 or 2

public class Player {

    // class variables
    private static FaceUpDeck faceUpDeck = FaceUpDeck.getObjectInstance();
    private static FaceDownDeck faceDownDeck = FaceDownDeck.getObjectInstance();
    private static TaskCardDeck taskCardDeck = TaskCardDeck.getObjectInstance();

    // constants
    private static final int TRAIN_NUM = 45;
    private static final Map<Integer, Integer> CARS_TO_POINTS = Map.of(
            1, 1, 2, 2, 3, 4, 4, 7, 5, 10, 6, 15);
    private static final Set<String> COLORS = Set.of("purple", "blue", "red", "yellow", "rainbow",
            "green", "black", "orange", "white");
    private static final int STARTING_HAND_NUM = 4;

    // player attributes
    private String playerName;
    private String playerId;
    private int points;
    private int numOfTrainsLeft;
    private int numOfTrainCards;
    private Map<String, List<TrainCard>> ownedTrainCards; // map colors to number of cards in this color
    protected Map<TaskCard, Boolean> ownedTaskCards; // map a task card to its completion status
    private Map<String, Set<Route<City>>> ownedRoutes;
    private Scanner scanner;
    private boolean ableToDrawTrainCard;
    private boolean ableToDrawRainbowCard;

    /**
     * constructor
     * @param playerName    input userName
     */
    public Player(String playerName) {
        this.playerName = playerName;
        playerId = null;
        points = 0;
        numOfTrainsLeft = TRAIN_NUM;
        numOfTrainCards = 0;
        ownedTrainCards = new HashMap<>();
        for (String color : COLORS) {
            ownedTrainCards.put(color, new LinkedList<>());
        }
        ownedTaskCards = new HashMap<>();
        ownedRoutes = new HashMap<>();
        scanner = new Scanner(System.in);
        ableToDrawTrainCard = true;
        ableToDrawRainbowCard = true;
    }

    /*-------------------------------------- getter methods -------------------------------------------------*/
    /**
     * getter methods to get player info:
     * (1) player name,
     * (2) player id,
     * (3) player points,
     * (4) number of trains left for a player
     * (5) number of ticket cards left for a player
     * (6) number of task cards owned by a player (including completed and incomplete)
     */
    public String getPlayerName() { return playerName; }

    public String getPlayerId() {
        return playerId;
    }

    public int getPoints() {
        return points;
    }

    public int getNumOfTrainLeft() {
        return numOfTrainsLeft;
    }

    public int getNumOfTrainCard() {
        return numOfTrainCards;
    }

    public int getNumOfTaskCard() { return ownedTaskCards.size(); }

    public Map<String, Set<Route<City>>> getOwnedRoutes() { return ownedRoutes; }

    public boolean isAbleToDrawTrainCard() { return ableToDrawTrainCard; }

    public boolean isAbleToDrawRainbowCard() { return ableToDrawRainbowCard; }
    /**
     * always returns true as the Player class represents the default behaviors of a human player
     */
    public boolean isHumanPlayer() { return true; }

    /*--------------------------------------- setter methods ------------------------------------------------*/
    /**
     * set player id
     */
    public void setPlayerId(String playerId) {
        if (this.playerId == null) {
            this.playerId = playerId;
        }
    }

    /**
     * set card drawing status, used in game module to control if the player can draw more cards and the type of cards
     * (rainbow or non-rainbow cards) he/she can draw in a turn
     */
    public void setAbleToDrawTrainCard() { ableToDrawTrainCard = true;}

    public void setUnableToDrawTrainCard() { ableToDrawTrainCard = false; }

    public void setAbleToDrawRainbowCard() { ableToDrawRainbowCard = true; }

    public void setUnableToDrawRainbowCard() { ableToDrawRainbowCard = false; }

     /* --------------------------------------- claim a route ------------------------------------------------- */
    /**
     * player can choose to claim a route, use train cars and get points, return a list of possible card combinations
     * @param route the route being chosen
     * @param routeColor if exists several colors for a route, what color/which route of parallel route is chosen
     * @return a list of possible card combinations
     */
    public List<Map<String, Integer>> getCardCombinationOnATargetRoute(Route<City> route, String routeColor) {

        if (hasEnoughCards(route, routeColor) && route.isColorAvailable(routeColor)) {
            return enumerateCombinations(route, routeColor);
        }

        return null;
    }

    /**
     * check if have enough cards, if a grey route, check if the player has equivalent number of or more cards
     * in the same color in any color or have enough locomotive cards in combination with colored cards;
     * if a route in other non-grey colors, check if the player has equivalent number or more cards in
     * that specific color or have enough locomotive cards in combination with colored cards
     * @param route    the target route the player wants to claim
     * @param routeColor    the route color the player chooses
     * @return  true if the player has enough cards to claim the target route, false otherwise
     */
    private boolean hasEnoughCards(Route<City> route, String routeColor) {
        // not enough cards in total
        if (numOfTrainCards < route.getNumOfCars()) {
            return false;
        } else {
            int rainbowNum = ownedTrainCards.get("rainbow").size();
            int numOfCarsNeeded = route.getNumOfCars();
            // grey routes, check every color
            if (routeColor.equals("grey")) {
                for (String colorKey : ownedTrainCards.keySet()) {
                    int currColorNum = ownedTrainCards.get(colorKey).size();
                    if (currColorNum >= route.getNumOfCars() ||
                            currColorNum + rainbowNum >= numOfCarsNeeded) {
                        return true;
                    }
                }
                return false;
            } else { // non-grey routes
                int currColorNum = ownedTrainCards.get(routeColor).size();
                return (currColorNum >= route.getNumOfCars() ||
                        currColorNum + rainbowNum >= numOfCarsNeeded);
            }
        }
    }

    /**
     * check the possible combinations of cards that a player can play given the color and number of cards needed
     * e.g. a 5-car yellow route can be claimed using 5 yellow tickets or n yellow tickets and (5-n) locomotive tickets,
     * grey color routes can be claimed using equivalent number of same color tickets
     * @param route    the target route the player wants to claim
     * @param routeColor    the route color the player chooses
     * @return  a list of maps, the maps are usually contain one or two pairs of "color - number of cards"
     */
    private List<Map<String, Integer>> enumerateCombinations(Route<City> route, String routeColor) {
        List<Map<String, Integer>> outerList = new ArrayList<>();
        // non grey color route
        if (!routeColor.equals("grey")) {
            createCombinationMapOfAColor(route, routeColor, outerList);
        } else { // grey color route, loop through every color
            for (String color : COLORS) {
                if (!color.equals("rainbow")) {
                    createCombinationMapOfAColor(route, color, outerList);
                }
            }
        }
        return outerList;
    }

    /**
     * helper function to enumerateCombinations(), used to create combination map of rainbow cards and color cards
     * player can play to claim route, double loop for grey color route
     * @param route the target route the player want to claim
     * @param ticketColor colors of the tickets player used to claim the target route
     * @param outerList   a list of other train card combinations defined in enumerateCombination ()
     */
    //TODO: check algorithm
    private void createCombinationMapOfAColor(Route<City> route, String ticketColor, List<Map<String, Integer>> outerList) {
        Map<String, Integer> innerMap;
        int rainbowNum = ownedTrainCards.get("rainbow").size();
        int currColorNum = ownedTrainCards.get(ticketColor).size();
        int numOfCarsNeeded = route.getNumOfCars();

        // more rainbow cards than needed
        if (rainbowNum >= numOfCarsNeeded) {
            innerMap = new HashMap<>();
            innerMap.put("rainbow", numOfCarsNeeded);
            if (!outerList.contains(innerMap)) {
                outerList.add(innerMap);
            }
        }
        // more color cards than needed
        if (currColorNum >= numOfCarsNeeded) {
            innerMap = new HashMap<>();
            innerMap.put(ticketColor, numOfCarsNeeded);
            if (!outerList.contains(innerMap)) {
                outerList.add(innerMap);
            }
        }
        // middle cases, mixed color and rainbow cards
        if ((rainbowNum + currColorNum >= numOfCarsNeeded) && (currColorNum < numOfCarsNeeded) &&
                (rainbowNum < numOfCarsNeeded) && (currColorNum > 1)) {
            for (int i = currColorNum; i > 0; i--) {
                innerMap = new HashMap<>();
                innerMap.put("rainbow", Math.max(0, numOfCarsNeeded - i));
                innerMap.put(ticketColor, i);
                if (!outerList.contains(innerMap)) {
                    outerList.add(innerMap);
                }
            }
        }
    }
        // if not enough colored cards, (rainbowNum + currColorNum - numOfCarsNeeded) kinds of combinations

    /**
     * a player chooses from the combination list of which combination he/she wants to play,
     * remove the cards of their choices from owned train card list, set route's owner id to the current player,
     * add points accordingly.
     */
    public void chooseACombination(Route<City> route, String routeColor, List<Map<String, Integer>> combinationList,
                                   int index) {
        // choose combination by index
        Map<String, Integer> chosenMap = combinationList.get(index);
        // use cards
        removeTrainCards(chosenMap);
        // claim the route
        route.setOwner(playerId, routeColor);
        addToOwnedRoutes(route);
        // add points
        addPoints(route.getNumOfCars());
        // try if this finishes a task
        finishATask();
    }

    /**
     * try every pair of cities on every taskCard, find a path if between two cities, if a path is found,
     * then the task card can be set to complete
     */
    private void finishATask() {
        // loop through task cards
        for (TaskCard taskCard : ownedTaskCards.keySet()) {
            if (!taskCard.isCompleted()) {
                PathFinder finder = new PathFinder(ownedRoutes, taskCard.getCity1().getCityName(),
                        taskCard.getCity2().getCityName());
                boolean res = finder.findPath();
                if (res) {
                    System.out.println("Congrats, you have finished a task");
                    taskCard.setCompleted();
                    addPoints(taskCard.getPoints());
                    break;
                }
            }
        }
    }

    /**
     * remove a list of train cards from the hash map after the player uses these cards to claim a route
     * @param colorNumMap: a map of cards, uses color string as key, and number of cards in that color as value
     *                   the cards being removed will be either: (1) all in same color,
     *                                                          (2) some same color + some rainbow color
     * @throws IllegalArgumentException     when player doesn't have all the cards being removed
     * @throws IllegalStateException        when the player doesn't have any train card
     */
    private void removeTrainCards(Map<String, Integer> colorNumMap) {
        if (ownedTrainCards.isEmpty()) {
            throw new IllegalStateException("This player doesn't have any train cards, cannot remove.");
        }
        // doesn't contain any cards in this color or doesn't contain enough cards
        for (String color : colorNumMap.keySet()) {
            if (!ownedTrainCards.containsKey(color) ||
                    ownedTrainCards.get(color).size() < colorNumMap.get(color)) {
                throw new IllegalArgumentException("This player doesn't have any card or enough required cards.");
            }
        }

        int counter = 0;

        // loop and remove from hash map
        for (String color : colorNumMap.keySet()) {
            int num = colorNumMap.get(color);
            if (num > 0) {
                List<TrainCard> currColorCards = ownedTrainCards.get(color);
                // uses iterator to loop through
                Iterator<TrainCard> iterator = currColorCards.iterator();
                // remove from the first position, order doesn't matter here
                while (iterator.hasNext() && counter < num) {
                    TrainCard card = iterator.next();
                    card.clearOwnerId();
                    iterator.remove();
                    counter++;
                }
            }
            numOfTrainCards -= num;
        }
    }

    /**
     * add points after a player claims a route, points are given based on the length of the routes
     * @param numOfCars     number of cars played to claim a route
     * @throws  IllegalArgumentException    when cars are negative or 0
     * @throws  IllegalArgumentException    when points are given in numbers other than 1, 2, 4, 7, 10, 15
     */
    private void addPoints(int numOfCars) {
        if (numOfCars <= 0) {
            throw new IllegalArgumentException("Number of cars have to be positive integers.");
        } else if (!CARS_TO_POINTS.containsKey(numOfCars)) {
            throw new IllegalArgumentException("Number of cars should be in the range of 1 - 6. " +
                    "Other numbers are not allowed.");
        }
        int points = CARS_TO_POINTS.get(numOfCars);
        this.points += points;
    }

    /**
     * add a route and its reverse to the owned routes map which keys are starting cities,
     * values are a set of routes which their city1 fields are the keys
     */
    private void addToOwnedRoutes(Route<City> route) {
        String fromCityName = route.getCity1().getCityName();
        String toCityName = route.getCity2().getCityName();
        if (!ownedRoutes.containsKey(fromCityName)) {
            ownedRoutes.put(fromCityName, new HashSet<>());
        }
        ownedRoutes.get(fromCityName).add(route);
        if (!ownedRoutes.containsKey(toCityName)) {
            ownedRoutes.put(toCityName, new HashSet<>());
        }
        ownedRoutes.get(fromCityName).add(route);
    }

    /**
     * deduct the task cards' points if task cards were not finish
     */
    public void calculateUnfinishedTasks() {
        for (TaskCard taskCard : ownedTaskCards.keySet()) {
            boolean status = ownedTaskCards.get(taskCard);
            if (!status) {
                deductPoints(taskCard.getPoints());
            }
        }
    }
    /**
     * deduct points when the game is finished if some task cards were not finished
     */
    private void deductPoints(int points) {
        this.points = Math.max(0, this.points - points);
    }

    /* --------------------------------------------  draw a train card ------------------------------------------*/

    /**
     * gets a hand of 4 train cards at the beginning of the game
     */
    public void getStartingHand() {
        for (int i = 0; i < STARTING_HAND_NUM; i++) {
            TrainCard card = faceDownDeck.drawACard(0);
            addATrainCard(card);
        }
    }

    /**
     * the player chooses to draw a card from the face-up deck,
     * note: change return type to TrainCard for testing
     * @param index specific index
     * @return the chosen train card to check its color, which is relevant to whether the player can draw a second card
     */
    public TrainCard drawAFaceUpCard(int index) {
        TrainCard card = faceUpDeck.drawACard(index);
        addATrainCard(card);
        return card;
    }

    /**
     * the player chooses to draw a card from the face-down deck
     */
    public TrainCard drawAFaceDownCard() {
        TrainCard card = faceDownDeck.drawACard(0);
        addATrainCard(card);
        return card;
    }


    /**
     * add a train card to the trainCards hash map after the player draws a train card
     * @param card:  a train card the player draws from the face-up or face-down deck
     * @throws IllegalArgumentException     this train card already exists in the map
     */
    public void addATrainCard(TrainCard card) {
        String color = card.getColor();
        if (!ownedTrainCards.containsKey(color)) {
            ownedTrainCards.put(color, new LinkedList<>());
        }
        if (ownedTrainCards.get(color).contains(card)) {
            throw new IllegalArgumentException("Duplicated card, cannot add this card.");
        }
        ownedTrainCards.get(color).add(card);
        card.setOwnerId(playerId);
        numOfTrainCards++;
    }

    /* ---------------------------------------------- draw task cards --------------------------------------------*/

    /**
     * choose the draw task card option in a turn, invoke dealCards() from taskCardDeck class
     * and get the dealtCards list
     * @return  a list of task cards
     */
    public List<TaskCard> drawTaskCards() {
        taskCardDeck.dealCards();
        return taskCardDeck.getDealt3Cards();
    }

    /**
     * human player's behavior to generate a list of card indices to be put in to the
     */
    public Set<Integer> generateCardIndices() {
        int min_firstTurn = 2;
        int min_otherTurns = 1;
        int max = 3;

        int numOfIndices;
        if (getNumOfTaskCard() == 0) {
            System.out.print("Select how many task cards (" + min_firstTurn  + "-" + max + ") you want to draw: ");
            numOfIndices =
                    Game.checkIntInput(scanner, scanner.next(), min_firstTurn, max + 1);
        } else {
            System.out.print("Select how many task cards (" + min_otherTurns  + "-" + max + ") you want to draw: ");
            numOfIndices = Game.checkIntInput(scanner, scanner.next(), min_otherTurns, max + 1);
        }

        // if the user wants to get all three
        if (numOfIndices == max) {
            System.out.println("All three cards have been added to your bag. ");
            return Set.of(0, 1, 2);
        }

        Set<Integer> cardIndices = new HashSet<>();
        for (int i = 0; i < numOfIndices; i++) {
            if (i == 0) {
                System.out.print("Select the 1st of " + numOfIndices + " target choices: ");
            } else {
                System.out.print("Select the 2nd of " + numOfIndices + " target choices: ");
            }

            int temp = scanner.nextInt();
            while (temp < 0 || temp >= max) {
                System.out.println("Input a number between 0 and " + max + ".");
                temp = scanner.nextInt();
            }
            while (cardIndices.contains(temp)) {
                System.out.println("This card is chosen, choose a different one.");
                temp = scanner.nextInt();
            }
            cardIndices.add(temp);
        }
        return cardIndices;
    }

    /**
     * choose a list of cards
     * @param cards a list of cards generated from invoking drawTaskCards()
     * @param cardIndices  a list of indices that indicate the card locations being chosen
     */
    public void chooseTaskCards(List<TaskCard> cards, Set<Integer> cardIndices) {
        // first draw after the game begins, get at least 2
        if (ownedTaskCards.isEmpty() && cardIndices.size() < 2) {
            throw new IllegalArgumentException("The player needs to choose at least 2 task cards in this turn.");
        } else if (!ownedTaskCards.isEmpty() && cardIndices.size() < 1) {
            throw new IllegalArgumentException("The player needs to choose at least 1 task card(s) in this turn.");
        }
        // add chosen cards
        for (int index : cardIndices) {
            addATaskCard(cards, index);

        }
        // discard unselected cards
        List<Integer> unSelectedIndices = getUnSelectedIndices(cardIndices);
        for (int index: unSelectedIndices) {
            taskCardDeck.discard(index);
        }
    }


    /**
     * generate a list of indices of unselected cards， turn modifier to be public for testing
     * @param cardIndices  a list of indices of selected cards,
     * @return  a list of indices of unselected cards
     */
    private List<Integer> getUnSelectedIndices(Set<Integer> cardIndices) {
        List<Integer> indices = new ArrayList<>(List.of(0, 1, 2));
        indices.removeAll(cardIndices);
        return indices;
    }

    /**
     * add a task card to the taskCards hash set after the player chooses a task card,
     * set the cards' complete status to false
     * @param card: a task card the player chooses from 3 cards dealt to him/her,
     *            (1) either at the start of the game, or (2) in a turn when player chooses to draw task cards
     * @throws  IllegalArgumentException    this task card already exists in the set
     */
    public void addATaskCard(TaskCard card) {
        if (ownedTaskCards.containsKey(card)) {
            throw new IllegalArgumentException("Duplicated card, cannot add this card.");
        }
        ownedTaskCards.put(card, false);
        card.setOwnerId(playerId);
    }

    private void addATaskCard(List<TaskCard> cards, int index) {
        TaskCard card = cards.get(index);
        cards.set(index, null);
        addATaskCard(card);
    }



    /*----------------------------------display functions for game module--------------------------------------------*/
    public void displayTaskCards(List<TaskCard> taskCards) {
        if (taskCards == null || taskCards.isEmpty()) {
            System.out.println("No available task cards.");
            return;
        }
        for (int i = 0; i< taskCards.size(); i++) {
            System.out.print("Choice " + i + " -> ");
            TaskCard card = taskCards.get(i);
            System.out.println("Task: " + card.getCity1().getCityName()
                    + " - " + card.getCity2().getCityName() + ", points: " + card.getPoints());
        }
    }

    /**
     * print out / display the train card combinations
     */
    public void displayCardCombinations(List<Map<String, Integer>> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No existing combination.");
            return;
        }
        System.out.println("The following " + list.size() + " combinations are available. ");
        for (int i = 0; i < list.size(); i++) {
            System.out.print("combination " + i + " : ");
            for (String color : list.get(i).keySet()) {
                int num = list.get(i).get(color);
                if (num != 0) {
                    System.out.print(num + " " + color );
                    if(num == 1) {
                        System.out.println(" card");
                    } else {
                        System.out.println(" cards");
                    }
                }
            }
        }
    }

    /**
     *
     */
    public void displayOwnedTrainCards() {
        if (ownedTrainCards.isEmpty()) {
            System.out.println("You do not own any train card. ");
        } else {
            System.out.println("You own: ");
            for (String color: ownedTrainCards.keySet()) {
                int num = ownedTrainCards.get(color).size();
                if (num > 0) {
                    System.out.print("    " + num + " " + color);
                    if (num == 1) {
                        System.out.println(" card.");
                    } else {
                        System.out.println(" cards.");
                    }
                }
            }
        }
    }


}
