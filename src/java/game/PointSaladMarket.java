package java.game;

import java.cards.ICard;
import java.cards.Pile;
import java.util.ArrayList;

/**
 * Represents the market in the PointSalad game
 */
public class PointSaladMarket implements IMarket {

	/** Number of criteria drawing piles */
	static public final int NUM_DRAW_PILES = 3;
	/** Number of vegetable cards */
	static public final int NUM_VEGETABLE_CARDS = 6;
	/** Number of criteria cards to be drafted by a player */
	static public final int CRITERIA_DRAFT = 1;
	/** Number of vegetable cards to be drafted by a player */
	static public final int VEGETABLE_DRAFT = 2;
	/** Alphabet to display the vegetable cards codes */
	static public final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private ArrayList<Pile> criteriaPiles;
	private ArrayList<ICard> vegetableCards;

	/**
	 * Creates an empty market
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
	 * Sets the pile at the given index
	 * 
	 * @param pileIndex The index of the pile
	 * @param pile The pile to set
	 * 
	 * @throws IllegalArgumentException If the pile index is invalid
	 */
	public void setPile(int pileIndex, Pile pile) {
		//TODO: Add a custom exception related to market?
		if (pileIndex < 0 || pileIndex >= NUM_DRAW_PILES)
		{
			throw new IllegalArgumentException("Invalid pile index");
		}
		criteriaPiles.set(pileIndex, pile);
	}

	/**
	 * Sets the card at the given index
	 * 
	 * @param cardIndex The index of the card
	 * @param card The card to set
	 * 
	 * @throws IllegalArgumentException If the card index is invalid
	 */
	public void setCard(int cardIndex, ICard card) {
		//TODO: Add a custom exception related to market?
		if (cardIndex < 0 || cardIndex >= NUM_VEGETABLE_CARDS)
		{
			throw new IllegalArgumentException("Invalid card index");
		}
		vegetableCards.set(cardIndex, card);
	}

	/**
	 * Draws a vegetable card from the market
	 * 
	 * @param cardIndex The index of the card to draw
	 * @return The card drawn, or null if the card is empty
	 * 
	 * @throws IllegalArgumentException If the card index is invalid
	 */
	public ICard drawVegegetableCard(int cardIndex) {
		//TODO: Add a custom exception related to market?
		if (cardIndex < 0 || cardIndex >= NUM_VEGETABLE_CARDS)
		{
			throw new IllegalArgumentException("Invalid card index");
		}
		ICard card = vegetableCards.get(cardIndex);
		vegetableCards.set(cardIndex, null);
		return card;
	}

	/**
	 * Draws a criteria card from the market
	 * 
	 * @param pileIndex The index of the pile to draw from
	 * @return The card drawn, or null if the pile is empty
	 * 
	 * @throws IllegalArgumentException If the pile index is invalid
	 */
	public ICard drawCriteriaCard(int pileIndex) {
		//TODO: Add a custom exception related to market?
		if (pileIndex < 0 || pileIndex >= NUM_DRAW_PILES)
		{
			throw new IllegalArgumentException("Invalid pile index");
		}
		Pile pile = criteriaPiles.get(pileIndex);
		ICard card = pile.draw();
		return card;
	}

	@Override
	public ArrayList<ICard> draftCards(String cardsString) {
		ArrayList<ICard> cards = new ArrayList<ICard>();

		int length = cardsString.length();

		//TODO: May have to be changed if the number of cards to be drafted is not strict but "up to x cards"
		// According to the rules, the player must draft either 1 criteria card or 2 vegetable cards though
		if (length != CRITERIA_DRAFT && length != VEGETABLE_DRAFT)
		{
			throw new IllegalArgumentException("Invalid choice. " +
				"You may either draft " + CRITERIA_DRAFT + " criteria card or " + VEGETABLE_DRAFT + " vegetable cards.");
		}

		// Converts the string to uppercase to avoid case sensitivity
		cardsString = cardsString.toUpperCase();

		// Checks if each drafted card is unique
		ArrayList<String> usedChars = new ArrayList<String>();
		for (int i = 0; i<length; i++)
		{
			String c = cardsString.substring(i, i + 1);
			if (usedChars.contains(c))
			{
				throw new IllegalArgumentException("Invalid choice. Each card must be unique.");
			}
			usedChars.add(c);
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
					throw new IllegalArgumentException("Invalid choice. " +
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
				catch (IllegalArgumentException e)
				{
					throw new IllegalArgumentException("Invalid choice. " +
						"Vegetable card identifier must be a letter.");
				}
			}
		}

		return cards;
	}

	/**
	 * Refills the vegetable market by drawing cards from the corresponding
	 * criteria piles
	 */
	public void refillVegetables() {
		for (int i = 0; i<NUM_VEGETABLE_CARDS; i++)
		{
			if (vegetableCards.get(i) == null)
			{
				int pileIndex = i % NUM_DRAW_PILES;
				ICard card = drawCriteriaCard(pileIndex);
				card.flip();
				vegetableCards.set(i, card);
			}
		}
	}

	/**
	 * Balances the piles in the market
	 */
	public void balancePiles() {
		for (int i = 0; i<NUM_DRAW_PILES; i++)
		{
			Pile pile = criteriaPiles.get(i);

			if (pile.isEmpty())
			{
				refillPileAt(i);
			}
		}
	}

	/**
	 * Refills the pile at the given index if it is empty.
	 * The pile is refilled with half the bottom cards of the larger pile
	 * 
	 * @param pileIndex The index of the pile to refill
	 * 
	 * @throws IllegalArgumentException If the pile index is invalid
	 */
	public void refillPileAt(int pileIndex) {
		//TODO: Add a custom exception related to market?
		if (pileIndex < 0 || pileIndex >= NUM_DRAW_PILES)
		{
			throw new IllegalArgumentException("Invalid pile index");
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
			criteriaPiles.set(pileIndex, maxPile.splitInTwo());
		}
	}

	/**
	 * Updates the market to ensure there are no empty vegetable card slot if it can be refilled,
	 * and that there are no empty criteria pile if it can be balanced
	 */
	public void updateMarket() {
		balancePiles();
		refillVegetables();
		balancePiles();
	}

	@Override
	public void printMarket() {
		System.out.println(this);
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
