package carddeck;


/**
 * This class deck manager is responsible for filling in all the deck for the first time, and coordinate filling
 * and discarding behaviors between deck classes, such as filling in face-up deck from face-down when the former is empty,
 * filling in face-down deck from discards when the former is empty,
 */
public class DeckManager {
    private static TaskCardDeck taskCardDeck = TaskCardDeck.getObjectInstance();
    private static FaceDownDeck faceDownDeck = FaceDownDeck.getObjectInstance();
    private static FaceUpDeck faceUpDeck = FaceUpDeck.getObjectInstance();
    private static DiscardsDeck discardsDeck = DiscardsDeck.getObjectInstance();
    private static DeckManager manager = new DeckManager();

    // private constructor
    private DeckManager() {
        taskCardDeck.createCardDeck();
        faceDownDeck.createCardDeck();
        faceUpDeck.createCardDeck();
        discardsDeck.createCardDeck();
    }

    public static DeckManager getObjectInstance() {
        return manager;
    }

    /**
     * be responsible for inter-deck activities, such as filling decks
     */
    public void manageDecks() {
        // fill face-down deck
        if (faceDownDeck.isEmpty() && !discardsDeck.isEmpty()) {
            faceDownDeck.fillDeck();
        }

        // fill face-up deck
        if (faceUpDeck.isEmpty() && !faceDownDeck.isEmpty()) {
            faceUpDeck.fillDeck();
        }
        while (faceUpDeck.isFull() && faceUpDeck.has3OrMoreLocoCard()) {
            faceUpDeck.discardAll();
            if (!faceDownDeck.isEmpty()) {
                faceUpDeck.fillDeck();
            }
        }

        System.out.print("up"  + faceUpDeck.getSize());
        System.out.println(" down"  + faceDownDeck.getSize());
    }



}
