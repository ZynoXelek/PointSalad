package main.java.states;

import main.java.exceptions.PhaseException;

/**
 * Interface for the state manager.
 */
public interface IStateManager {
	
	/**
	 * Sets the state of the game.
	 * 
	 * @param state The state to set.
	 */
	public void setState(State state);

	/**
	 * Gets the state of the game.
	 * 
	 * @return The state.
	 */
	public State getState();

	/**
	 * Updates the state of the game.
	 * This runs the game loop from the current state.
	 */
	public void update() throws PhaseException;
}
