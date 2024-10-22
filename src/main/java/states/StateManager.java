package states;

import exceptions.PhaseException;
import phases.IPhase;

/**
 * Manages the state of the game.
 */
public class StateManager implements IStateManager {

	private State gameState;

	/**
	 * Initializes the state manager with the given game state.
	 * 
	 * @param gameState The game state to initialize the manager with.
	 */
	public StateManager(State gameState) {
		this.gameState = gameState;
	}

	@Override
	public void setState(State gameState) {
		this.gameState = gameState;
	}

	@Override
	public State getState() {
		return gameState;
	}

	@Override
	public void update() throws PhaseException {
		boolean finished = false;

		while (!finished) {
			IPhase phase = gameState.getPhase();
			phase.processPhase(gameState);
			finished = !phase.proceedToNextPhase(gameState);
		}
	}
}
