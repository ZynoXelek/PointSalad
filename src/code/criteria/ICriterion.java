package code.criteria;

import code.exceptions.CriterionException;
import code.players.AbstractPlayer;

import java.util.ArrayList;

/**
 * Interface for scoring criteria.
 */
public interface ICriterion {
	
	/**
	 * Computes the score of a player based on the criterion.
	 * This score can depend on the other players in the game.
	 * It should NOT update the player's score.
	 * 
	 * @param players The list of players in the game
	 * @param playerIndex The index of the player to compute the score for
	 * 
	 * @return The score of the player
	 * 
	 * @throws CriterionException If the criterion object is not correctly initialized
	 */
	public int computePlayerScore(ArrayList<AbstractPlayer> players, int playerIndex) throws CriterionException;

	/**
	 * Gets the string representation of the criterion.
	 * 
	 * @return The string representation of the criterion
	 */
	public String getCriterionDisplay();
}