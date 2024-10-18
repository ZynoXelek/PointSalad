package code.states;

import code.exceptions.PhaseException;
import code.phases.IPhase;

/**
 * Manages the state of the game for the Point Salad game.
 */
public class PointSaladStateManager implements IStateManager {

	private State gameState;

	/**
	 * Initializes the state manager with the given game state.
	 * 
	 * @param gameState The game state to initialize the manager with.
	 */
	public PointSaladStateManager(State gameState) {
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
			System.out.println("\nCurrent phase of the game:\n" + phase.getClass().getSimpleName());
			phase.processPhase(gameState);
			System.out.println("It has been processed. Current state of the game:\n" + gameState);
			finished = !phase.proceedToNextPhase(gameState);

			System.out.println("Finished: " + finished + "\n");
		}
	}
}
