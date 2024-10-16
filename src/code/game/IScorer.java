package code.game;

import java.util.ArrayList;

import code.exceptions.ScorerException;
import code.players.AbstractPlayer;

/**
 * Interface for the scoring logic of a game.
 */
public interface IScorer {

	/**
	 * Calculates the score of a player. It may depend on other players' hands.
	 * 
	 * @param players The list of players
	 * @param playerID The ID of the player to calculate the score for
	 * 
	 * @return The score of the player
	 * 
	 * @throws ScorerException If there is an error during the scoring process
	 */
	public int calculateScore(ArrayList<AbstractPlayer> players, int playerID) throws ScorerException;
}
