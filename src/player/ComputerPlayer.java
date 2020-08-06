package player;

import card.TrainCard;
import carddeck.FaceDownDeck;
import carddeck.FaceUpDeck;

import java.util.*;

public class ComputerPlayer extends Player {

    private final int MAX_DRAW = 2;
    private final int MAX_FACE_UP_NUM = 5;


    private int intelligenceLevel;
    private Random rand;
    private long seed = 24; // randomly choose a seed
    private int counter;

    /**
     * constructor
     * @param playerName input userName
     */
    public ComputerPlayer(String playerName, int intelligenceLevel) {
        super(playerName);
        this.intelligenceLevel = intelligenceLevel;
        rand = new Random();
        rand.setSeed(seed);
        counter = 0;
    }

    /**
     * always return false, a computer player can never become human
     */
    @Override
    public boolean isHumanPlayer() { return false; }

    /**
     * use random number to automatically generate card indices in drawing task cards
     * @return a set of card indices to be used in drawing face-up cards
     */
    @Override
    public Set<Integer> generateCardIndices() {
        int min_firstTurn = 2;
        int min_otherTurns = 1;
        int max = 3;

        int numOfIndices;
        if (super.ownedTaskCards.isEmpty()) {
            // 1 to 3 cards
            numOfIndices = rand.nextInt(max - min_firstTurn) + min_firstTurn;
        } else {
            // 2 to 3 cards
            numOfIndices = rand.nextInt(max - min_otherTurns) + min_otherTurns;
        }

        Set<Integer> cardIndices = new HashSet<>();
        for (int i = 0; i < numOfIndices; i++) {
            int index = rand.nextInt(numOfIndices);
            while (cardIndices.contains(index)) {
                index = rand.nextInt(numOfIndices);
            }
            cardIndices.add(index);
        }
        return cardIndices;
    }


    /**
     * decision making
     */
    public void play() { computerDrawTrainCard(); }

    /**
     * draws 1 - 3 cards each time randomly
     */
    private void computerDrawTaskCard() {

    }


    private TrainCard computerDrawFaceUp() {
        TrainCard currCard = null;

        // choose to draw face-up cards
        if (!FaceUpDeck.getObjectInstance().isEmpty()) {
            // generate random index
            int faceUpIndex = rand.nextInt(MAX_FACE_UP_NUM);

            // regenerate index if the card it's pointing to is null
            while (FaceUpDeck.getListInstance().get(faceUpIndex) == null) {
                faceUpIndex = rand.nextInt(MAX_FACE_UP_NUM);
            }

            // check if the color is rainbow color
            // if so, get only one card
            assert FaceUpDeck.getColorByIndex(faceUpIndex) != null;
            while (FaceUpDeck.getColorByIndex(faceUpIndex)
                    .equalsIgnoreCase("rainbow")) {
                if (isAbleToDrawRainbowCard()) {
                    currCard = super.drawAFaceUpCard(faceUpIndex);
                    setUnableToDrawRainbowCard();
                    setUnableToDrawTrainCard();
                    counter++;
                    break;
                } else {
                    // generate another index
                    faceUpIndex = rand.nextInt(MAX_FACE_UP_NUM);
                }
            }
            // if not, allow to draw a second card
            if (!FaceUpDeck.getColorByIndex(faceUpIndex)
                    .equalsIgnoreCase("rainbow") && isAbleToDrawTrainCard()) {
                currCard = super.drawAFaceUpCard(faceUpIndex);
                setUnableToDrawRainbowCard();
                counter++;
            }
        }
        return currCard;
    }

    /**
     * generate a random number of 0 or 1, as 0 represents drawing from face-up deck and 1 from face-down,
     * controlling drawing number max of 2 each turn, make public for testing
     *
     */
    public void computerDrawTrainCard() {
        setAbleToDrawTrainCard();
        setAbleToDrawRainbowCard();
        TrainCard currCard;
        // counter to memorize count and stop drawing
        counter = 0;
        if (FaceUpDeck.getObjectInstance().getSize() + FaceDownDeck.getObjectInstance().getSize() >= 2) {
            while (isAbleToDrawTrainCard() && counter < MAX_DRAW) {
                // randomly generate decision to draw face-up or down cards
                int upOrDownOption = rand.nextInt(2);

                // choose to draw face-up cards
                if (upOrDownOption == 0) {
                    currCard = computerDrawFaceUp();
                } else {
                    if (!FaceDownDeck.getObjectInstance().isEmpty()) {
                        currCard = drawAFaceDownCard();
                        counter++;
                    } else {
                        currCard = computerDrawFaceUp();
                    }
                }
                // else do nothing

                // TODO: testing, delete when finish
                if (currCard != null) {
                    System.out.print(upOrDownOption + ":   ");
                    System.out.println(currCard.getColor());
                }
            }

            setUnableToDrawTrainCard();

            // print out differently based on number of cards drawn
            System.out.print(super.getPlayerName() + " has drawn " + counter + " train card");
            if (counter == 1) {
                System.out.println(".");
            } else {
                System.out.println("s.");
            }
        } else {
            System.out.println("No more cards in the deck, cannot draw.");
        }
    }

    /**
     * play randomly
     */
    private void level1Play() {
    }

    private void level2Play() {}

    private void level3Play() {}


}
