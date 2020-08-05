package carddeck;

import card.TrainCard;

import java.util.ArrayList;
import java.util.List;

public class FaceUpDeck implements ICardDeck, IDiscardable, IDrawable, IFillable {

    // class variable
    private static List<TrainCard> faceUpDeckList = new ArrayList<>();
    private static FaceUpDeck faceUpDeckObject = new FaceUpDeck();
    private static FaceDownDeck faceDownDeckObject = FaceDownDeck.getObjectInstance();
    private static DiscardsDeck discardsDeckObject = DiscardsDeck.getObjectInstance();


    // constants
    private final int CARD_NUM = 5;

    // instance variable
    private int locoCardNum;
    private boolean existed;
    private int size;

    /**
     * private constructor, fill deck with 5 cards in instantiation
     */
    private FaceUpDeck() {
        locoCardNum = 0;
        existed = false;
        size = 0;
    }


    /**
     * uses reset method instead of remove method when drawing and putting cards, cannot use the auto
     * list size, counts non-null objects after instantiation
     */
    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return getSize() == 0;
    }

    public boolean isFull() {
        return getSize() == CARD_NUM;
    }


    /** get color by index
     * @return return a color string of the card at the given index
     */
    public static String getColorByIndex(int index) {
        assert faceUpDeckList.get(index) != null;
        return faceUpDeckList.get(index).getColor();
    }

    /**
     * helper functions to increase and decrease self-calculated size by 1 at once
     */
    private void increaseSize() { size++; }

    private void decreaseSize() {
        size--;
    }


    /* --------------------------------- singleton behaviors -------------------------------------- */

    /**
     * return the only instance of faceUpDeck
     */
    public static FaceUpDeck getObjectInstance() {
        return faceUpDeckObject;
    }


    @Override
    public void createCardDeck() {
        if (!existed) {
            faceUpDeckObject.fillDeck();
            existed = true;
        }
    }

    /**
     * get and display the cards in the deck
     *
     * @return FaceUpDeckList
     */
    public static List<TrainCard> getListInstance() {
        return faceUpDeckList;
    }

    /* --------------------------------- discard behaviors -------------------------------------- */

    /**
     * discard a card, seldom used on its own, used in discardAll()
     *
     * @param index: 0 - 4
     * @throws IllegalArgumentException  when index is not within 0-4 range
     * @throws IndexOutOfBoundsException when the current length is smaller than the input index
     */
    @Override
    public void discard(int index) {
        // if (index >= Math.min(CARD_NUM, getSize()) || index < 0) {
        //   throw new IllegalArgumentException("Invalid index, index needs to be in 0 - 4 range.");
        // }

        TrainCard discardedCard = faceUpDeckList.set(index, null); // if want to use remove, use iterator
        setLocoCardNum(false, discardedCard);
        discardsDeckObject.addToDeck(discardedCard); // add to discards deck
        decreaseSize();
    }


    /**
     * discard all cards if there are three or more locomotive cards,
     * cannot discard cards if there are less than 5 cards left (likely when there are
     * no cards left in the face-down deck)
     */
    public void discardAll() {
        // if not full, cannot discard
        if (has3OrMoreLocoCard() && isFull()) {
            System.out.println("More than 3 locomotive cards on the deck. Cards are being discarded. ");
            for (int i = 0; i < CARD_NUM; i++) {
                discard(i);
            }
            locoCardNum = 0;
        }
    }


    /*--------------------------------- draw card behaviors --------------------------------------*/

    /**
     * draw a card from a given position, then fill in with the first card drawn from the face-down deck
     *
     * @param index: 0 - 4
     * @throws IllegalStateException    when the face-up deck is empty
     * @throws IllegalArgumentException when input index is not within 0 - 4
     */
    @Override
    public TrainCard drawACard(int index) {
        if (isEmpty()) {
            throw new IllegalStateException("The deck is empty, cannot draw a card.");
        }
        if (index >= CARD_NUM || index < 0) {
            throw new IllegalArgumentException("Invalid index, index needs to be in 0 - 4 range.");
        }

        TrainCard drawnCard;

        // if there are at least 1 card in face-down deck, draw and fill in
        if (!faceDownDeckObject.isEmpty()) {
            drawnCard = faceUpDeckList.get(index);
            faceUpDeckList.set(index, null);
            addACard(index);
        } else { // no cards left in face-down deck
            drawnCard = faceUpDeckList.set(index, null);
        }
        setLocoCardNum(false, drawnCard);
        decreaseSize();

        /* moved to deck manager
        // optional discard and fill
        while (has3OrMoreLocoCard()) {
            discardAll();
            fillDeck();
        }
         */

        return drawnCard;
    }

    /* ------------------------- fill deck behavior ---------------------------------------- */

    /**
     * fill up deck by drawing cards from face-down deck, this operation is only possible when there are
     * no card in face-up deck and there're still cards in face-down deck
     * possible scenarios: (1) when the face-up deck is first created
     * (2) when all the cards are discarded
     *
     */
    @Override
    public void fillDeck() {
        if (faceDownDeckObject.isEmpty()) {
            System.out.println("Face-down deck is empty.");
        } else {
            // face down not empty and this empty
            if (this.isEmpty()) {
                // empty deck, fill up all five spots
                for (int i = 0; i < CARD_NUM; i++) {
                    addACard(i);
                }
                // recursion discard and fill
                if (isFull() && has3OrMoreLocoCard()) {
                    discardAll();
                    fillDeck();
                }
            }
        }
    }

    /**
     * add a card to the given position to face-up deck
     *
     * @param index: given position where the card is missing
     * @throws IllegalStateException    when face-down deck is empty
     * @throws IllegalArgumentException when the given index is occupied by an existing card
     */
    private void addACard(int index) {
        if (isFull() && faceUpDeckList.get(index) != null) {
            throw new IllegalArgumentException("A card exists in this given index.");
        }
        if (faceDownDeckObject.isEmpty()) {
            throw new IllegalStateException("Cannot draw cards from empty face-down deck.");
        }

        TrainCard currCard = faceDownDeckObject.drawACard(DEFAULT_DRAWING_POS);
        setLocoCardNum(true, currCard);

        // different adding behavior
        if (!existed) {
            faceUpDeckList.add(currCard);
        } else {
            faceUpDeckList.set(index, currCard);
        }
        increaseSize();

    }

    /**
     * check if there are 3 or more locomotive cards, used in discard() to decide whether the cards
     * should be discarded
     *
     * @return true if so, false otherwise
     */
    public boolean has3OrMoreLocoCard() {
        return locoCardNum >= 3;
    }


    /**
     * change locomotive number accordingly to the conditions whether the card is being added to the deck
     * or being removed, and whether the current card is a locomotive card or not
     *
     * @param isBeingAdded: true if it's being added, false if being removed;
     * @param card:         current card, isLocomotive(card)
     */
    private void setLocoCardNum(boolean isBeingAdded, TrainCard card) {
        assert card != null;
        boolean isLocomotive = (card.getColor().equals("rainbow"));
        if (isBeingAdded && isLocomotive) {
            locoCardNum++;
        } else if (!isBeingAdded && isLocomotive) {
            locoCardNum--;
        }
    }


    /**
     * display the deck by printing out each card
     */
    public void display() {
        System.out.println("The following " + faceUpDeckList.size() + " cards are available. ");
        if (!faceUpDeckList.isEmpty()) {
            for (int i = 0; i < faceUpDeckList.size(); i++) {
                System.out.println("    Choice " + i + " " + faceUpDeckList.get(i).getColor());
            }
        }
    }
}