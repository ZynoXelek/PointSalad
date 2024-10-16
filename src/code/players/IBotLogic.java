package code.players;

import code.exceptions.BotLogicException;
import code.states.State;

/**
 * Interface to implement bot logic for a player.
 */
public interface IBotLogic {
	
	/**
	 * Get the move for the bot, depending on the current state.
	 * 
	 * @param state The current state of the game
	 * 
	 * @return The move to make, as a String command
	 * 
	 * @throws BotLogicException If an error occurs in the bot logic
	 */
	public String getMove(State state) throws BotLogicException;
}
