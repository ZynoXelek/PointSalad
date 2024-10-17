package code.cards;

import java.util.ArrayList;

/**
 * Interface for a card.
 */
public interface ICard {

	/**
	 * Flips the card.
	 */
	public void flip();

	@Override
	public String toString();

	/**
	 * Gets the string representation of a hand of cards.
	 * 
	 * @param hand The hand of cards
	 * 
	 * @return The string representation of the hand of cards
	 */
	public String handToString(ArrayList<ICard> hand);

	/**
	 * Creates and returns a copy of the card.
	 * 
	 * @return A copy of the card
	 */
	public ICard copy();
}
