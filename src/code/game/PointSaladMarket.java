package code.game;

import code.cards.ICard;
import code.cards.Pile;
import code.exceptions.MarketException;

import java.util.ArrayList;


/**
 * Represents the market in the PointSalad game.
 */
public class PointSaladMarket implements IMarket {

	/** Number of criterion drawing piles. */
	static public final int NUM_DRAW_PILES = 3;
	/** Number of vegetable cards. */
	static public final int NUM_VEGETABLE_CARDS = 6;
	/** Number of criterion cards to be drafted by a player. */
	static public final int CRITERION_DRAFT = 1;
	/** Number of vegetable cards to be drafted by a player. */
	static public final int VEGETABLE_DRAFT = 2;
	/** Alphabet to display the vegetable cards codes. */
	static public final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private ArrayList<Pile> criterionPiles;
	private ArrayList<ICard> vegetableCards;

	/**
	 * Creates an empty market.
	 */
	public PointSaladMarket() {
		criterionPiles = new ArrayList<Pile>();
		vegetableCards = new ArrayList<ICard>();

		// Create the criterion piles
		for (int i = 0; i<NUM_DRAW_PILES; i++)
		{
			criterionPiles.add(null);
		}

		// Create the cards space
		for (int i = 0; i<NUM_VEGETABLE_CARDS; i++)
		{
			vegetableCards.add(null);
		}
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i<NUM_DRAW_PILES; i++)
		{
			Pile pile = criterionPiles.get(i);
			if (pile != null && !pile.isEmpty())
			{
				return false;
			}
		}

		for (int i = 0; i<NUM_VEGETABLE_CARDS; i++)
		{
			if (vegetableCards.get(i) != null)
			{
				return false;
			}
		}

		return true;
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
		criterionPiles.set(pileIndex, pile);
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
	 * Draws a criterion card from the market.
	 * 
	 * @param pileIndex The index of the pile to draw from
	 * @return The card drawn, or null if the pile is empty
	 * 
	 * @throws MarketException If the pile index is invalid
	 */
	public ICard drawCriterionCard(int pileIndex) throws MarketException {
		if (pileIndex < 0 || pileIndex >= NUM_DRAW_PILES)
		{
			throw new MarketException("Invalid pile index");
		}
		Pile pile = criterionPiles.get(pileIndex);
		ICard card = pile.draw();
		return card;
	}

	@Override
	public boolean isCardsStringValid(String cardsString) {
		int length = cardsString.length();

		//TODO: May have to be changed if the number of cards to be drafted is not strict but "up to x cards"
		// According to the rules, the player must draft either 1 criterion card or 2 vegetable cards though
		if (length != CRITERION_DRAFT && length != VEGETABLE_DRAFT)
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
				"You may either draft " + CRITERION_DRAFT + " criterion card or " + VEGETABLE_DRAFT + " vegetable cards, where each should be unique.");
		}

		// Process criterion drafting (using numbers)
		if (length == CRITERION_DRAFT)
		{
			for (int i = 0; i<length; i++)
			{
				String c = cardsString.substring(i, i + 1);
				try
				{
					int pileIndex = Integer.parseInt(c);
					ICard card = drawCriterionCard(pileIndex);
					cards.add(card);
				}
				catch (NumberFormatException e)
				{
					throw new MarketException("Invalid choice. " +
						"Criterion card identifier must be a number.", e);
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
						"Vegetable card identifier must be a letter.", e);
				}
			}
		}

		return cards;
	}

	/**
	 * Refills the vegetable market by drawing cards from the corresponding criterion piles.
	 */
	public void refillVegetables() {
		for (int i = 0; i<NUM_VEGETABLE_CARDS; i++)
		{
			if (vegetableCards.get(i) == null)
			{
				int pileIndex = i % NUM_DRAW_PILES;
				try {
					ICard card = drawCriterionCard(pileIndex);
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
			Pile pile = criterionPiles.get(i);

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

		Pile pile = criterionPiles.get(pileIndex);
		if (pile.isEmpty())
		{
			int[] pileSizes = new int[NUM_DRAW_PILES];
			for (int i = 0; i<NUM_DRAW_PILES; i++)
			{
				pileSizes[i] = criterionPiles.get(i).size();
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

			Pile maxPile = criterionPiles.get(maxPileIndex);
			this.setPile(pileIndex, maxPile.splitInTwo());
		}
	}

	/**
	 * Updates the market to ensure there are no empty vegetable card slot if it can be refilled,
	 * and that there are no empty criterion pile if it can be balanced.
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

			if (criterionPiles.get(i) != null)
			{
				marketString += criterionPiles.get(i).getTopCard() + "\t";
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
