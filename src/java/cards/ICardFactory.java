package java.cards;

import java.util.ArrayList;

/**
 * Interface for a card factory.
 */
public interface ICardFactory {

	/**
	 * Loads cards from a file.
	 * 
	 * @param filename The name of the file to load the cards from
	 * 
	 * @return The loaded cards
	 * 
	 * @throws CardFactoryException If the file does not exist
	 */
	public ArrayList<ICard> loadCards(String filename);
}
