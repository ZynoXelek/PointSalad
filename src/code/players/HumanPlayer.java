package code.players;

/**
 * Class for a human player.
 */
public class HumanPlayer extends AbstractPlayer {
	
	/**
	 * Creates a human player with the given ID and name.
	 * 
	 * @param playerID The ID of the player
	 * @param name The name of the player
	 */
	public HumanPlayer(int playerID, String name) {
		super(playerID, name, false);
	}

}
