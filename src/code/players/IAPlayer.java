package code.players;

import code.exceptions.BotLogicException;
import code.states.State;

/**
 * Class for a bot player.
 */
public class IAPlayer extends AbstractPlayer {
	
	private IBotLogic botLogic;

	/**
	 * Creates a bot player with the given ID and name.
	 * 
	 * @param playerID The ID of the player
	 * @param name The name of the player
	 * @param botLogic The logic of the bot
	 */
	public IAPlayer(int playerID, String name, IBotLogic botLogic) {
		super(playerID, name, true);
		this.botLogic = botLogic;
	}
	
	/**
	 * Gets the logic of the bot.
	 * 
	 * @return The logic of the bot
	 */
	public IBotLogic getBotLogic() {
		return this.botLogic;
	}

	/**
	 * Sets the logic of the bot.
	 * 
	 * @param botLogic The logic of the bot
	 */
	public void setBotLogic(IBotLogic botLogic) {
		this.botLogic = botLogic;
	}

	/**
	 * Helper method to get the move from the bot logic.
	 * 
	 * @param state The current state of the game
	 * @param botPlayerId The ID of the bot player
	 * 
	 * @return The move to make, as a String command
	 * 
	 * @throws BotLogicException If an error occurs in the bot logic
	 */
	public String getMove(State state) throws BotLogicException {
		return botLogic.getMove(state, this.getPlayerID());
	}
}
