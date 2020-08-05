package carddeck;

/**
 * This class defines the fill deck behavior of face-down and face-up deck. These two classes both filling in with
 * cards drawn from another deck to fill in all empty spots when they are empty.
 * Scenario 1: filling in 5 spots in face-up deck after its first instantiation from face-down deck,
 *             and after both face-up and -down decks are empty
 * Scenario 2: filling in all spots in face-down deck from discards deck when both face-up and -down decks are empty
 */
public interface IFillable {

    /**
     * fill a card deck with enough cards from another target card deck
     */
    void fillDeck();
}
