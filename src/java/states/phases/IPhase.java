package java.states.phases;

import java.states.State;

/**
 * Interface for the phases of the game.
 */
public interface IPhase {
	
	/**
	 * Proceeds the phase, based on the current state of the game.
	 * 
	 * @param state The current state of the game
	 * 
	 * @throws Exception if something goes wrong during the phase processing
	 */
	public void processPhase(State state) throws Exception;
}
