package java.cards;

import java.util.ArrayList;

/**
 * Interface for a card factory
 */
public interface ICardFactory {

	//TODO: Add a custom exception related to card factory?
	/**
	 * Loads cards from a file
	 * 
	 * @param filename The name of the file to load the cards from
	 * @return The loaded cards
	 * 
	 * @throws IllegalArgumentException If the file does not exist
	 */
	public ArrayList<ICard> loadCards(String filename);
}
