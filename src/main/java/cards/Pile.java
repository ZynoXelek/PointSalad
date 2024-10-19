package cards;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a generic pile of cards. These cards should implement the ICard interface.
 */
public class Pile<T extends ICard> {
	
	private ArrayList<T> cards;

	/**
	 * Creates an empty pile.
	 */
	public Pile() {
		this.cards = new ArrayList<T>();
	}

	/**
	 * Creates a pile with the given cards.
	 * 
	 * @param cards The cards to put in the pile
	 */
	public Pile(ArrayList<T> cards) {
		this.cards = cards;
	}

	/**
	 * Checks if the pile is empty.
	 * 
	 * @return True if the pile is empty, false otherwise
	 */
	public boolean isEmpty() {
		return cards.isEmpty();
	}

	/**
	 * Gets the number of cards in the pile.
	 * 
	 * @return The number of cards in the pile
	 */
	public int size() {
		return cards.size();
	}

	/**
	 * Gets the list of cards in the pile.
	 * 
	 * @return The list of cards in the pile
	 */
	public ArrayList<T> getCards() {
		return cards;
	}

	/**
	 * Adds a card to the pile.
	 * 
	 * @param card The card to add
	 */
	public void addCard(T card) {
		cards.add(card);
	}

	/**
	 * Adds a list of cards to the pile.
	 * 
	 * @param cards The cards to add
	 */
	public void addCards(ArrayList<T> cards) {
		cards.addAll(cards);
	}

	/**
	 * Adds all the cards from another pile to this pile.
	 * 
	 * @param pile The pile to add the cards from
	 */
	public void addCards(Pile<T> pile) {
		cards.addAll(pile.cards);
	}

	/**
	 * Get the top card of the pile, without removing it from the pile.
	 * 
	 * @return The top card of the pile, or null if the pile is empty
	 */
	public T getTopCard() {
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
	public T draw() {
		if(isEmpty()) {
			return null;
		} else {
			// Draws from the top of the pile
			T card = cards.get(cards.size() - 1);
			cards.remove(cards.size() - 1);
			return card;
		}
	}

	/**
	 * Draws a number of cards from the pile, removing them from the pile.
	 * 
	 * @param numCards The number of cards to draw
	 * @return The cards drawn, or an empty list if the pile is empty
	 */
	public ArrayList<T> draw(int numCards) {
		if (numCards > cards.size()) {
			numCards = cards.size();
		}

		ArrayList<T> drawnCards = new ArrayList<T>();
		for(int i = 0; i < numCards; i++) {
			drawnCards.add(draw());
		}
		return drawnCards;
	}

	/**
	 * Splits the pile in two. Creates a new pile starting from the bottom of the current pile.
	 * The current pile is modified and will contain the top half of the cards.
	 * 
	 * @return The created pile. It may be empty if the current pile is of size <= 1.
	 */
	public Pile<T> splitInTwo() {
		int halfSize = cards.size() / 2;
		ArrayList<T> newCards = new ArrayList<T>(cards.subList(0, halfSize));
		cards = new ArrayList<T>(cards.subList(halfSize, cards.size()));
		return new Pile<T>(newCards);
	}

	/**
	 * Splits the pile in the given number of piles.
	 * The original pile is not modified.
	 * 
	 * @param numPiles The number of piles to split the pile in
	 * @return The created piles. They may be empty if the current pile is too small
	 */
	public ArrayList<Pile<T>> splitIn(int numPiles) {
		ArrayList<Pile<T>> piles = new ArrayList<Pile<T>>();
		int numCards = cards.size() / numPiles;
		int remainingCards = cards.size() % numPiles;
		int start = 0;
		for(int i = 0; i < numPiles; i++) {
			int end = start + numCards;
			if(remainingCards > 0) {
				end++;
				remainingCards--;
			}
			ArrayList<T> newCards = new ArrayList<T>(cards.subList(start, end));
			piles.add(new Pile<T>(newCards));
			start = end;
		}
		return piles;
	}

	/**
	 * Concatenates the given pile to this pile.
	 * 
	 * @param pile The pile to concatenate
	 * @return This pile
	 */
	public Pile<T> concatenates(Pile<T> pile) {
		this.addCards(pile);
		return this;
	}

	/**
	 * Concatenates the given piles to this pile.
	 * 
	 * @param piles The piles to concatenate
	 * @return This pile
	 */
	public Pile<T> concatenates(ArrayList<Pile<T>> piles) {
		for(Pile<T> pile : piles) {
			this.addCards(pile);
		}
		return this;
	}

	/**
	 * Flips all the cards in the pile.
	 */
	public void flip() {
		for(T card : cards) {
			card.flip();
		}
	}

	/**
	 * Shuffles the pile.
	 */
	public void shuffle() {
		
		Collections.shuffle(cards);
	}
}
