package java.players;

import java.states.State;

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
	 */
	public String getMove(State state);
}
