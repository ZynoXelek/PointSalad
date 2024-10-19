package main.java.players;

import main.java.exceptions.BotLogicException;
import main.java.states.State;

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

	@Override
	public String getMove(State state, String instruction) throws BotLogicException {
		return botLogic.getMove(state, this.getPlayerID());
	}
}
