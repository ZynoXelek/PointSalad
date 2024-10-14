package java.players;

/**
 * Class for a bot player.
 */
public class IAPlayer extends AbstractPlayer {
	
	private ILogic botLogic;

	/**
	 * Creates a bot player with the given ID and name.
	 * 
	 * @param playerID The ID of the player
	 * @param name The name of the player
	 * @param botLogic The logic of the bot
	 */
	public IAPlayer(int playerID, String name, ILogic botLogic) {
		super(playerID, name, true);
		this.botLogic = botLogic;
	}
	
	/**
	 * Gets the logic of the bot.
	 * 
	 * @return The logic of the bot
	 */
	public ILogic getBotLogic() {
		return this.botLogic;
	}

	/**
	 * Sets the logic of the bot.
	 * 
	 * @param botLogic The logic of the bot
	 */
	public void setBotLogic(ILogic botLogic) {
		this.botLogic = botLogic;
	}
}
