package game;

import java.util.ArrayList;

import cards.ICard;
import cards.Pile;
import cards.PointSaladCard;
import exceptions.ConfigException;
import exceptions.MarketException;
import tools.Config;


/**
 * Represents the market in the PointSalad game.
 */
public class PointSaladMarket implements IMarket {

	/** Enum used to detect the type of a player's draft */
	private enum DraftType {
		CRITERION,
		VEGETABLE
	}

	/** Number of criterion drawing piles. */
	public static final int NUM_DRAW_PILES;
	/** Number of vegetable cards. */
	public static final int NUM_VEGETABLE_CARDS;
	/** Number of criterion cards to be drafted by a player. */
	public static final int CRITERION_DRAFT;
	/** Number of vegetable cards to be drafted by a player. */
	public static final int VEGETABLE_DRAFT;
	/** Alphabet to display the vegetable cards codes. */
	public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	static {
		// Load final variables from the configuration file
		int numDrawPiles = 3;
        int numVegetableCards = 6;
        int criterionDraft = 1;
        int vegetableDraft = 2;

        try {
            Config config = Config.getInstance();
            numDrawPiles = config.getInt("PS_numDrawPiles");
            numVegetableCards = config.getInt("PS_numVegetableCards");
            criterionDraft = config.getInt("PS_numCriterionDraft");
            vegetableDraft = config.getInt("PS_numVegetableDraft");
        } catch (ConfigException e) {
            e.printStackTrace();
            // Use default values if configuration loading fails
        }

        NUM_DRAW_PILES = numDrawPiles;
        NUM_VEGETABLE_CARDS = numVegetableCards;
        CRITERION_DRAFT = criterionDraft;
        VEGETABLE_DRAFT = vegetableDraft;
	}

	private ArrayList<Pile<PointSaladCard>> criterionPiles;
	private ArrayList<PointSaladCard> vegetableCards;

	/**
	 * Creates an empty market.
	 */
	public PointSaladMarket() {
		criterionPiles = new ArrayList<Pile<PointSaladCard>>();
		vegetableCards = new ArrayList<PointSaladCard>();

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
			Pile<PointSaladCard> pile = criterionPiles.get(i);
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
	 * Gets the criterion piles.
	 * 
	 * @return The criterion piles
	 */
	public ArrayList<Pile<PointSaladCard>> getCriterionPiles() {
		return criterionPiles;
	}

	/**
	 * Sets the criterion piles.
	 * 
	 * @param criterionPiles The criterion piles to set
	 */
	public void setCriterionPiles(ArrayList<Pile<PointSaladCard>> criterionPiles) {
		this.criterionPiles = criterionPiles;
	}

	/**
	 * Gets the vegetable cards.
	 * 
	 * @return The vegetable cards
	 */
	public ArrayList<PointSaladCard> getVegetableCards() {
		return vegetableCards;
	}

	/**
	 * Sets the vegetable cards.
	 * 
	 * @param vegetableCards The vegetable cards to set
	 */
	public void setVegetableCards(ArrayList<PointSaladCard> vegetableCards) {
		this.vegetableCards = vegetableCards;
	}

	/**
	 * Gets the available criteria in the market.
	 * 
	 * @return The available criteria
	 */
	public ArrayList<PointSaladCard> getAvailableCriteria() {
		ArrayList<PointSaladCard> availableCriteria = new ArrayList<PointSaladCard>();
		for (int i = 0; i<NUM_DRAW_PILES; i++)
		{
			Pile<PointSaladCard> pile = criterionPiles.get(i);
			if (pile != null && !pile.isEmpty())
			{
				availableCriteria.add(pile.getTopCard());
			}
		}
		return availableCriteria;
	}

	/**
	 * Gets the available criteria strings in the market.
	 * For instance, if the market is full, the strings will be "0", "1", "2".
	 * 
	 * @return The available criteria strings
	 */
	public ArrayList<String> getAvailableCriteriaStrings() {
		ArrayList<String> availableCriteriaStrings = new ArrayList<String>();
		for (int i = 0; i<NUM_DRAW_PILES; i++)
		{
			Pile<PointSaladCard> pile = criterionPiles.get(i);
			if (pile != null && !pile.isEmpty())
			{
				availableCriteriaStrings.add(Integer.toString(i));
			}
		}
		return availableCriteriaStrings;
	}

	/**
	 * Gets the available vegetables in the market.
	 * 
	 * @return The available vegetables
	 */
	public ArrayList<PointSaladCard> getAvailableVegetables() {
		ArrayList<PointSaladCard> availableVegetables = new ArrayList<PointSaladCard>();
		for (int i = 0; i<NUM_VEGETABLE_CARDS; i++)
		{
			PointSaladCard card = vegetableCards.get(i);
			if (card != null)
			{
				availableVegetables.add(card);
			}
		}
		return availableVegetables;
	}

	/**
	 * Gets the available vegetable strings in the market.
	 * For instance, if the market is full, the strings will be "A", "B", "C", "D", "E", "F".
	 * 
	 * @return The available vegetable strings
	 */
	public ArrayList<String> getAvailableVegetableStrings() {
		ArrayList<String> availableVegetableStrings = new ArrayList<String>();
		for (int i = 0; i<NUM_VEGETABLE_CARDS; i++)
		{
			PointSaladCard card = vegetableCards.get(i);
			if (card != null)
			{
				availableVegetableStrings.add(ALPHABET.substring(i, i + 1));
			}
		}
		return availableVegetableStrings;
	}

	/**
	 * Gets the pile at the given index.
	 * 
	 * @param pileIndex The index of the pile
	 * @return The pile at the given index
	 * 
	 * @throws MarketException If the pile index is invalid
	 */
	public Pile<PointSaladCard> getPile(int pileIndex) throws MarketException {
		if (pileIndex < 0 || pileIndex >= NUM_DRAW_PILES)
		{
			throw new MarketException("Invalid pile index");
		}
		return criterionPiles.get(pileIndex);
	}

	/**
	 * Sets the pile at the given index.
	 * 
	 * @param pileIndex The index of the pile
	 * @param pile The pile to set
	 * 
	 * @throws MarketException If the pile index is invalid
	 */
	public void setPile(int pileIndex, Pile<PointSaladCard> pile) throws MarketException {
		if (pileIndex < 0 || pileIndex >= NUM_DRAW_PILES)
		{
			throw new MarketException("Invalid pile index");
		}
		criterionPiles.set(pileIndex, pile);
	}

	/**
	 * Gets the card at the given index.
	 * 
	 * @param cardIndex The index of the card
	 * @return The card at the given index
	 * 
	 * @throws MarketException If the card index is invalid
	 */
	public PointSaladCard getCard(int cardIndex) throws MarketException {
		if (cardIndex < 0 || cardIndex >= NUM_VEGETABLE_CARDS)
		{
			throw new MarketException("Invalid card index");
		}
		return vegetableCards.get(cardIndex);
	}

	/**
	 * Sets the card at the given index.
	 * 
	 * @param cardIndex The index of the card
	 * @param card The card to set
	 * 
	 * @throws MarketException If the card index is invalid
	 */
	public void setCard(int cardIndex, PointSaladCard card) throws MarketException {
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
	public PointSaladCard drawVegetableCard(int cardIndex) throws MarketException {
		if (cardIndex < 0 || cardIndex >= NUM_VEGETABLE_CARDS)
		{
			throw new MarketException("Invalid card index");
		}
		PointSaladCard card = vegetableCards.get(cardIndex);
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
	public PointSaladCard drawCriterionCard(int pileIndex) throws MarketException {
		if (pileIndex < 0 || pileIndex >= NUM_DRAW_PILES)
		{
			throw new MarketException("Invalid pile index");
		}
		Pile<PointSaladCard> pile = criterionPiles.get(pileIndex);
		PointSaladCard card = pile.draw();
		return card;
	}

	@Override
	public String getDraftingInstruction() {
		return "Please draft a single type of cards, up to " + CRITERION_DRAFT + " criterion card, or " + VEGETABLE_DRAFT +
		" vegetable cards, where each should be unique. (examples: '1' or 'AC', 'B')";
	}

	private DraftType getDraftType(String cardsString) {
		DraftType draftType = null;
		try {
			Integer.parseInt(cardsString);
			draftType = DraftType.CRITERION;
		} catch (NumberFormatException e) {
			draftType = DraftType.VEGETABLE;
		}
		return draftType;
	}

	@Override
	public boolean isCardsStringValid(String cardsString) {
		int length = cardsString.length();

		if (length <= 0 || length > Integer.max(CRITERION_DRAFT, VEGETABLE_DRAFT))
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

		// Verify that only criterion or vegetable cards are drafted at the same time, not a mix of both.
		// Also verifies if every card is available in the market.
		DraftType draftType = getDraftType(cardsString);

		if (draftType.equals(DraftType.CRITERION)) {
			for (String c: usedChars) {
				try {
					int index = Integer.parseInt(c);
					Pile<PointSaladCard> pile = getPile(index);
					if (pile == null || pile.isEmpty()) {
						return false;
					}
				} catch (NumberFormatException e) {
					return false;
				} catch (MarketException e) {
					return false;
				}
			}
		}
		
		else if (draftType.equals(DraftType.VEGETABLE)) {
			for (String c: usedChars) {
				try {
					// Will return -1 if the character is not found, which will detect that the draft is invalid
					int index = ALPHABET.indexOf(c);
					PointSaladCard card = getCard(index);
					if (card == null) {
						return false;
					}
				} catch (MarketException e) {
					return false;
				}
			}
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
				"You may either draft a single type of card, up to " + CRITERION_DRAFT + " criterion card, or " +
				 VEGETABLE_DRAFT + " vegetable cards, where each should be unique.");
		}

		// We know the string is valid, so we just need to check the type of draft it is.
		DraftType draftType = getDraftType(cardsString);


		// Process criterion drafting (using numbers)
		if (draftType.equals(DraftType.CRITERION))
		{
			for (int i = 0; i<length; i++)
			{
				String c = cardsString.substring(i, i + 1);
				try
				{
					int pileIndex = Integer.parseInt(c);
					PointSaladCard card = drawCriterionCard(pileIndex);
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
		else if (draftType.equals(DraftType.VEGETABLE))
		{
			for (int i = 0; i<length; i++)
			{
				char c = cardsString.charAt(i);
				int cardIndex = ALPHABET.indexOf(c);
				try
				{
					PointSaladCard card = drawVegetableCard(cardIndex);
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
					PointSaladCard card = drawCriterionCard(pileIndex);
					if (card != null)
					{
						card.flip();
						this.setCard(i, card);
					}
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
			Pile<PointSaladCard> pile = criterionPiles.get(i);

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

		Pile<PointSaladCard> pile = criterionPiles.get(pileIndex);
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

			Pile<PointSaladCard> maxPile = criterionPiles.get(maxPileIndex);
			this.setPile(pileIndex, maxPile.splitInTwo());
		}
	}

	/**
	 * Updates the market to ensure there are no empty vegetable card slot if it can be refilled,
	 * and that there are no empty criterion pile if it can be balanced.
	 */
	@Override
	public void refill() {
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
			if (i != 0 && i % NUM_DRAW_PILES == 0)
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
