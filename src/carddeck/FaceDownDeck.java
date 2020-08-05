package carddeck;

import card.ICard;
import card.TrainCard;

import java.util.List;

public class FaceDownDeck implements ICardDeck, IDrawable, IFillable {

    // class variables
    private static List<TrainCard> faceDownDeckList = FaceDownDeckCreator.getCardDeckListInstance();
    private static FaceDownDeck faceDownDeckObject = new FaceDownDeck();
    private static DiscardsDeck discardsDeckObject = DiscardsDeck.getObjectInstance();
    private static FaceDownDeckCreator creator = FaceDownDeckCreator.getDeckCreatorObjectInstance();
    private static FaceUpDeck faceUpDeck = FaceUpDeck.getObjectInstance();

    /**
     * private constructor
     */
    private FaceDownDeck() {
    }

    /**
     * invoke creator FaceDownDeckCreator
     */
    public void createCardDeck() {
        creator.createCardDeck();
    }

    /**
     * get the only instance of face-down deck object
     * @return FaceDownDeck faceDownDeckObject
     */
    public static FaceDownDeck getObjectInstance() {
        return faceDownDeckObject;
    }


    /**
     * implements ICardDeck interface, get size of card deck
     * @return the number train cards in the face-down card deck
     */
    @Override
    public int getSize() {
        return faceDownDeckList.size();
    }


    /**
     * implements ICardDeck interface, check if card deck is empty based on size
     * @return true if there is no card in the face-down deck, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }


    /**
     * implements IDrawable interface,
     * who: (1) a player can choose to draw in a turn,
     *      (2) face-up deck will automatically draw to fill in as replacement
     * @param index: the card index, always need to be 0, only allowed to draw the first card from the top
     * @return the first card from the face-down deck, mimicking the top card from the stack
     * @throws  IllegalArgumentException     when attempting to draw a card from position other than 0
     * @throws  IllegalStateException        when the card deck is empty, cannot draw a card
     */
    @Override
    public TrainCard drawACard(int index) {
        if (index != DEFAULT_DRAWING_POS) {
            throw new IllegalArgumentException("Invalid index, index can only be " + DEFAULT_DRAWING_POS + ".");
        }
        if (faceDownDeckList.isEmpty()) {
            fillDeck();
            if (faceDownDeckList.isEmpty()) {
                System.out.println("The card deck is empty, cannot draw a card.");
            }
        }
        return faceDownDeckList.remove(index);
    }


    /**
     * fill deck using all the cards in discards deck,
     * make public when invoking fillDeck related tests in FaceDownDeckTest
     */
    @Override
    public void fillDeck() {
        if (discardsDeckObject.isEmpty()) {
            System.out.println("Discards card deck is empty, cannot fill face-down deck. ");
        }
        if (isEmpty() && faceUpDeck.isEmpty() && !discardsDeckObject.isEmpty()) {
            discardsDeckObject.shuffle();
            faceDownDeckList.addAll(DiscardsDeck.getListInstance());
            discardsDeckObject.clearDeck();
        }
    }

}
