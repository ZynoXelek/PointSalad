package java.cards;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a pile of cards
 */
public class Pile {
	
	private ArrayList<ICard> cards;

	/**
	 * Creates an empty pile
	 */
	public Pile() {
		this.cards = new ArrayList<ICard>();
	}

	/**
	 * Creates a pile with the given cards
	 * 
	 * @param cards The cards to put in the pile
	 */
	public Pile(ArrayList<ICard> cards) {
		this.cards = cards;
	}

	/**
	 * Checks if the pile is empty
	 * 
	 * @return True if the pile is empty, false otherwise
	 */
	public boolean isEmpty() {
		return cards.isEmpty();
	}

	/**
	 * Gets the number of cards in the pile
	 * @return The number of cards in the pile
	 */
	public int size() {
		return cards.size();
	}

	/**
	 * Get the top card of the pile, without removing it from the pile.
	 * 
	 * @return The top card of the pile, or null if the pile is empty
	 */
	public ICard getTopCard() {
		if(isEmpty()) {
			return null;
		} else {
			return cards.get(cards.size() - 1);
		}
	}

	/**
	 * Draws a card from the pile, removing it from the pile.
	 * 
	 * @return The card drawn, or null if the pile is empty
	 */
	public ICard draw() {
		if(isEmpty()) {
			return null;
		} else {
			// Draws from the top of the pile
			ICard card = cards.get(cards.size() - 1);
			cards.remove(cards.size() - 1);
			return card;
		}
	}

	/**
	 * Draws a number of cards from the pile
	 * 
	 * @param numCards The number of cards to draw
	 * @return The cards drawn, or an empty list if the pile is empty
	 */
	public ArrayList<ICard> draw(int numCards) {
		if (numCards > cards.size()) {
			numCards = cards.size();
		}

		ArrayList<ICard> drawnCards = new ArrayList<ICard>();
		for(int i = 0; i < numCards; i++) {
			drawnCards.add(draw());
		}
		return drawnCards;
	}

	/**
	 * Splits the pile in two. Creates a new pile starting from the bottom of the current pile.
	 * The current pile will contain the top half of the cards.
	 * 
	 * @return The created pile. It may be empty if the current pile is of size <= 1.
	 */
	public Pile splitInTwo() {
		int halfSize = cards.size() / 2;
		ArrayList<ICard> newCards = new ArrayList<ICard>(cards.subList(0, halfSize));
		cards = new ArrayList<ICard>(cards.subList(halfSize, cards.size()));
		return new Pile(newCards);
	}

	/**
	 * Shuffles the pile
	 */
	public void shuffle() {
		
		Collections.shuffle(cards);
	}
}
