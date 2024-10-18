package code.game;

import code.cards.ICard;
import code.exceptions.MarketException;

import java.util.ArrayList;

/**
 * Interface for a market.
 */
public interface IMarket {
	
	/**
	 * Gets the string representation of the market.
	 * 
	 * @return The string representation of the market
	 */
	@Override
	public String toString();

	/**
	 * Gets the drafting instruction for the market.
	 * 
	 * @return The drafting instruction for the market
	 */
	public String getDraftingInstruction();

	/**
	 * Checks if the market is empty.
	 * 
	 * @return True if the market is empty, false otherwise
	 */
	public boolean isEmpty();

	/**
	 * Checks if a string identifier of cards is valid.
	 * 
	 * @param cardsString The string identifier of the cards
	 * 
	 * @return True if the string is valid, false otherwise
	 */
	public boolean isCardsStringValid(String cardsString);

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

	/**
	 * Refills the market to get it ready for a draft.
	 */
	public void refill();
}
