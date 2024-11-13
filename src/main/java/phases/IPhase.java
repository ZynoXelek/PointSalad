package phases;

import exceptions.PhaseException;
import states.State;

/**
 * Interface for the phases of the game.
 */
public interface IPhase {
	
	/**
	 * Process the phase, based on the current state of the game.
	 * It will likely modify the current state of the game.
	 * 
	 * @param state The current state of the game
	 * 
	 * @throws PhaseException if something goes wrong during the phase processing
	 */
	public void processPhase(State state) throws PhaseException;

	/**
	 * Proceed to the next phase of the game, modifying the state of the game accordingly.
	 * 
	 * @param state The current state of the game
	 * 
	 * @return true if the game changes phase, false otherwise. False means the game is over
	 * 
	 * @throws PhaseException if something goes wrong during the phase transition
	 */
	public boolean proceedToNextPhase(State state) throws PhaseException;
}
