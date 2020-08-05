package carddeck;

import card.ICard;

/**
 * The IDrawable interface represents the drawing a card behavior from applicable card decks
 * Applicable card decks: FaceUpDeck, FaceDownDeck, TaskCardDeck
 */
public interface IDrawable {
    int DEFAULT_DRAWING_POS = 0;

    ICard drawACard(int i);
}
