package code.players;

import code.cards.ICard;

import java.util.ArrayList;

/**
 * Abstract base class for players.
 */
public abstract class AbstractPlayer {
	
	private int playerID;
	// TODO: implement name support
	private String name;
	private boolean isBot;
	private int score;
	private ArrayList<ICard> hand;

	/**
	 * Creates a player with the given ID, name and bot status.
	 * 
	 * @param playerID The ID of the player
	 * @param name The name of the player
	 * @param isBot True if the player is a bot, false otherwise
	 */
	public AbstractPlayer(int playerID, String name, boolean isBot) {
		this.playerID = playerID;
		this.name = name;
		this.isBot = isBot;
		this.score = 0;
		this.hand = new ArrayList<ICard>();
	}

	/**
	 * Gets the player's ID.
	 * 
	 * @return The player's ID
	 */
	public int getPlayerID() {
		return this.playerID;
	}

	/**
	 * Sets the player's ID.
	 * 
	 * @param playerID The player's ID
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * Gets the player's name.
	 * 
	 * @return The player's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the player's name.
	 * 
	 * @param name The player's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the player's bot status.
	 * 
	 * @return True if the player is a bot, false otherwise
	 */
	public boolean getIsBot() {
		return this.isBot;
	}

	/**
	 * Sets the player's bot status.
	 * 
	 * @param isBot True if the player is a bot, false otherwise
	 */
	public void setIsBot(boolean isBot) {
		this.isBot = isBot;
	}

	/**
	 * Gets the player's score.
	 * 
	 * @return The player's score
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Sets the player's score.
	 * 
	 * @param score The player's score
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * Gets the player's hand.
	 * 
	 * @return The player's hand
	 */
	public ArrayList<ICard> getHand() {
		return this.hand;
	}

	/**
	 * Sets the player's hand.
	 * 
	 * @param hand The player's hand
	 */
	public void setHand(ArrayList<ICard> hand) {
		this.hand = hand;
	}

	/**
	 * Adds a card to the player's hand.
	 */
	public void addCardToHand(ICard card) {
		hand.add(card);
	}

	/**
	 * Adds a list of cards to the player's hand.
	 */
	public void addCardsToHand(ArrayList<ICard> cards) {
		hand.addAll(cards);
	}

	/**
	 * Gets the player's hand as a string.
	 * 
	 * @return The player's hand as a string
	 */
	public String handToString() {
		//TODO: may change this implementation if it is not good looking
		String handString = "";

		for (ICard card : hand) {
			handString += card.toString() + "\n";
		}

		return handString;
	}
}
