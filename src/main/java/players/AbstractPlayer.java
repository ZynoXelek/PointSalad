package players;

import java.util.ArrayList;

import cards.ICard;
import states.State;

/**
 * Abstract base class for players.
 */
public abstract class AbstractPlayer {
	
	private int playerID;
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
		String handString = "Empty Hand";

		if (hand.size() > 0) {
			ICard card = hand.get(0);
			handString = card.handToString(hand);
		}

		return handString;
	}

	/**
	 * Get the player's move based on the current state and instruction.
	 * 
	 * @param state The current state of the game
	 * @param instruction The instruction to the player
	 * 
	 * @return The player's move
	 * 
	 * @throws Exception If an error occurs while getting the player's move
	 */
	public abstract String getMove(State state, String instruction) throws Exception;

	/**
	 * Gets the players' hands.
	 * 
	 * @param players The list of players
	 * 
	 * @return The players' hands
	 */
	public static ArrayList<ArrayList<ICard>> getHands(ArrayList<AbstractPlayer> players) {
		ArrayList<ArrayList<ICard>> hands = new ArrayList<ArrayList<ICard>>();

		for (AbstractPlayer player : players) {
			hands.add(player.getHand());
		}

		return hands;
	}

	/**
	 * Gets the other players in the game.
	 * 
	 * @param players The list of players
	 * @param playerID The ID of the player to be excluded from the created list
	 * 
	 * @return The other players in the game
	 */
	public static ArrayList<AbstractPlayer> getOtherPlayers(ArrayList<AbstractPlayer> players, int playerID) {
		ArrayList<AbstractPlayer> otherPlayers = new ArrayList<AbstractPlayer>();

		for (int i = 0; i < players.size(); i++) {
			AbstractPlayer player = players.get(i);
			if (player.getPlayerID() != playerID) {
				otherPlayers.add(players.get(i));
			}
		}

		return otherPlayers;
	}

	/**
	 * Gets the hands of the other players in the game.
	 * 
	 * @param players The list of players
	 * @param playerID The ID of the player to exclude from the created list
	 * 
	 * @return The hands of the other players in the game
	 */
	public static ArrayList<ArrayList<ICard>> getOtherHands(ArrayList<AbstractPlayer> players, int playerID) {
		ArrayList<AbstractPlayer> otherPlayers = getOtherPlayers(players, playerID);
		return getHands(otherPlayers);
	}
}
