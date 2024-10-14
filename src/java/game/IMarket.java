package java.game;

import java.cards.ICard;
import java.exceptions.MarketException;
import java.util.ArrayList;

/**
 * Interface for a market.
 */
public interface IMarket {
	
	/**
	 * Prints the market in the terminal.
	 */
	public void printMarket();

	/**
	 * Buys one or several cards from the market based on a String identifier.
	 * 
	 * @param cardsString String identifier of the cards to buy
	 * 
	 * @return The cards bought
	 * 
	 * @throws MarketException If the cardsString is invalid
	 */
	public ArrayList<ICard> draftCards(String cardsString) throws MarketException;
}
