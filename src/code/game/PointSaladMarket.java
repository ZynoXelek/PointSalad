package code.game;

import code.cards.ICard;
import code.cards.Pile;
import code.exceptions.MarketException;

import java.util.ArrayList;


/**
 * Represents the market in the PointSalad game.
 */
public class PointSaladMarket implements IMarket {

	/** Number of criteria drawing piles. */
	static public final int NUM_DRAW_PILES = 3;
	/** Number of vegetable cards. */
	static public final int NUM_VEGETABLE_CARDS = 6;
	/** Number of criteria cards to be drafted by a player. */
	static public final int CRITERIA_DRAFT = 1;
	/** Number of vegetable cards to be drafted by a player. */
	static public final int VEGETABLE_DRAFT = 2;
	/** Alphabet to display the vegetable cards codes. */
	static public final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private ArrayList<Pile> criteriaPiles;
	private ArrayList<ICard> vegetableCards;

	/**
	 * Creates an empty market.
	 */
	public PointSaladMarket() {
		criteriaPiles = new ArrayList<Pile>();
		vegetableCards = new ArrayList<ICard>();

		// Create the criteria piles
		for (int i = 0; i<NUM_DRAW_PILES; i++)
		{
			criteriaPiles.add(null);
		}

		// Create the cards space
		for (int i = 0; i<NUM_VEGETABLE_CARDS; i++)
		{
			vegetableCards.add(null);
		}
	}

	/**
	 * Sets the pile at the given index.
	 * 
	 * @param pileIndex The index of the pile
	 * @param pile The pile to set
	 * 
	 * @throws MarketException If the pile index is invalid
	 */
	public void setPile(int pileIndex, Pile pile) throws MarketException {
		if (pileIndex < 0 || pileIndex >= NUM_DRAW_PILES)
		{
			throw new MarketException("Invalid pile index");
		}
		criteriaPiles.set(pileIndex, pile);
	}

	/**
	 * Sets the card at the given index.
	 * 
	 * @param cardIndex The index of the card
	 * @param card The card to set
	 * 
	 * @throws MarketException If the card index is invalid
	 */
	public void setCard(int cardIndex, ICard card) throws MarketException {
		if (cardIndex < 0 || cardIndex >= NUM_VEGETABLE_CARDS)
		{
			throw new MarketException("Invalid card index");
		}
		vegetableCards.set(cardIndex, card);
	}

	/**
	 * Draws a vegetable card from the market.
	 * 
	 * @param cardIndex The index of the card to draw
	 * @return The card drawn, or null if the card is empty
	 * 
	 * @throws MarketException If the card index is invalid
	 */
	public ICard drawVegegetableCard(int cardIndex) throws MarketException {
		if (cardIndex < 0 || cardIndex >= NUM_VEGETABLE_CARDS)
		{
			throw new MarketException("Invalid card index");
		}
		ICard card = vegetableCards.get(cardIndex);
		vegetableCards.set(cardIndex, null);
		return card;
	}

	/**
	 * Draws a criteria card from the market.
	 * 
	 * @param pileIndex The index of the pile to draw from
	 * @return The card drawn, or null if the pile is empty
	 * 
	 * @throws MarketException If the pile index is invalid
	 */
	public ICard drawCriteriaCard(int pileIndex) throws MarketException {
		if (pileIndex < 0 || pileIndex >= NUM_DRAW_PILES)
		{
			throw new MarketException("Invalid pile index");
		}
		Pile pile = criteriaPiles.get(pileIndex);
		ICard card = pile.draw();
		return card;
	}

	@Override
	public boolean isCardsStringValid(String cardsString) {
		int length = cardsString.length();

		//TODO: May have to be changed if the number of cards to be drafted is not strict but "up to x cards"
		// According to the rules, the player must draft either 1 criteria card or 2 vegetable cards though
		if (length != CRITERIA_DRAFT && length != VEGETABLE_DRAFT)
		{
			return false;
		}

		// Checks if each drafted card is unique
		ArrayList<String> usedChars = new ArrayList<String>();
		for (int i = 0; i<length; i++)
		{
			String c = cardsString.substring(i, i + 1);
			if (usedChars.contains(c))
			{
				return false;
			}
			usedChars.add(c);
		}

		return true;
	}

	@Override
	public ArrayList<ICard> draftCards(String cardsString) throws MarketException {
		ArrayList<ICard> cards = new ArrayList<ICard>();

		int length = cardsString.length();
		// Converts the string to uppercase to avoid case sensitivity
		cardsString = cardsString.toUpperCase();

		if (!isCardsStringValid(cardsString))
		{
			throw new MarketException("Invalid choice. " +
				"You may either draft " + CRITERIA_DRAFT + " criteria card or " + VEGETABLE_DRAFT + " vegetable cards, where each should be unique.");
		}

		// Process criteria drafting (using numbers)
		if (length == CRITERIA_DRAFT)
		{
			for (int i = 0; i<length; i++)
			{
				String c = cardsString.substring(i, i + 1);
				try
				{
					int pileIndex = Integer.parseInt(c);
					ICard card = drawCriteriaCard(pileIndex);
					cards.add(card);
				}
				catch (NumberFormatException e)
				{
					throw new MarketException("Invalid choice. " +
						"Criteria card identifier must be a number.");
				}
			}
		}

		// Process vegetable drafting (using letters)
		else if (length == VEGETABLE_DRAFT)
		{
			for (int i = 0; i<length; i++)
			{
				char c = cardsString.charAt(i);
				int cardIndex = ALPHABET.indexOf(c);
				try
				{
					ICard card = drawVegegetableCard(cardIndex);
					cards.add(card);
				}
				catch (MarketException e)
				{
					throw new MarketException("Invalid choice. " +
						"Vegetable card identifier must be a letter.");
				}
			}
		}

		return cards;
	}

	/**
	 * Refills the vegetable market by drawing cards from the corresponding criteria piles.
	 */
	public void refillVegetables() {
		for (int i = 0; i<NUM_VEGETABLE_CARDS; i++)
		{
			if (vegetableCards.get(i) == null)
			{
				int pileIndex = i % NUM_DRAW_PILES;
				try {
					ICard card = drawCriteriaCard(pileIndex);
					card.flip();
					this.setCard(i, card);
				}
				catch (MarketException e)
				{
					// Print the error message in the terminal. May happen during testing. Will never happen once the project is completed.
					System.err.println("Index error at PointSaladMarket.refillVegetables() with error message:\n" + 
									e.getMessage());
				}
			}
		}
	}

	/**
	 * Balances the piles in the market.
	 * If a pile is empty, it is refilled with half the bottom cards of the larger pile.
	 */
	public void balancePiles() {
		for (int i = 0; i<NUM_DRAW_PILES; i++)
		{
			Pile pile = criteriaPiles.get(i);

			if (pile.isEmpty())
			{
				try {
					refillPileAt(i);
				}
				catch (MarketException e)
				{
					// Print the error message in the terminal. May happen during testing. Will never happen once the project is completed.
					System.err.println("Index error at PointSaladMarket.balancePiles() with error message:\n" + 
									e.getMessage());
				}
			}
		}
	}

	/**
	 * Refills the pile at the given index if it is empty.
	 * The pile is refilled with half the bottom cards of the larger pile.
	 * 
	 * @param pileIndex The index of the pile to refill
	 * 
	 * @throws MarketException If the pile index is invalid
	 */
	public void refillPileAt(int pileIndex) throws MarketException {
		if (pileIndex < 0 || pileIndex >= NUM_DRAW_PILES)
		{
			throw new MarketException("Invalid pile index");
		}

		Pile pile = criteriaPiles.get(pileIndex);
		if (pile.isEmpty())
		{
			int[] pileSizes = new int[NUM_DRAW_PILES];
			for (int i = 0; i<NUM_DRAW_PILES; i++)
			{
				pileSizes[i] = criteriaPiles.get(i).size();
			}

			int maxPileIndex = 0;
			int maxPileSize = 0;
			for (int i = 0; i<NUM_DRAW_PILES; i++)
			{
				if (pileSizes[i] > maxPileSize)
				{
					maxPileIndex = i;
					maxPileSize = pileSizes[i];
				}
			}

			Pile maxPile = criteriaPiles.get(maxPileIndex);
			this.setPile(pileIndex, maxPile.splitInTwo());
		}
	}

	/**
	 * Updates the market to ensure there are no empty vegetable card slot if it can be refilled,
	 * and that there are no empty criteria pile if it can be balanced.
	 */
	public void updateMarket() {
		balancePiles();
		refillVegetables();
		balancePiles();
	}

	@Override
	public String toString() {
		// TODO: Update this method with a StringFormatter to ensure the columns are aligned

		String marketString = "Market:\n";

		marketString += "Point cards:\t";

		for (int i = 0; i<NUM_DRAW_PILES; i++)
		{
			marketString += "[" + i + "] ";

			if (criteriaPiles.get(i) != null)
			{
				marketString += criteriaPiles.get(i).getTopCard() + "\t";
			}
			else
			{
				marketString += "Empty\t";
			}
		}

		marketString += "\nVeggie cards:\t";

		for (int i = 0; i<NUM_VEGETABLE_CARDS; i++)
		{
			if ((i + 1) % NUM_DRAW_PILES == 0)
			{
				// Gets to the next line to respect the column display
				marketString += "\n\t\t";
			}

			marketString += "[" + ALPHABET.charAt(i) + "] ";

			if (vegetableCards.get(i) != null)
			{
				marketString += vegetableCards.get(i) + "\t";
			}
			else
			{
				marketString += "Empty\t";
			}
		}

		return marketString;
	}
}
