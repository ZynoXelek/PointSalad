package game.scorer;

import java.util.ArrayList;
import java.util.HashMap;

import cards.ICard;
import exceptions.ScorerException;
import players.AbstractPlayer;

/**
 * Interface for the scoring logic of a game.
 */
public interface IScorer {

	/**
	 * Calculates the score of a player. It may depend on other players' hands.
	 * 
	 * @param players The HashMap of players
	 * @param playerID The ID of the player to calculate the score for
	 * 
	 * @return The score of the player
	 * 
	 * @throws ScorerException If there is an error during the scoring process
	 */
	public int calculateScore(HashMap<Integer, AbstractPlayer> players, int playerID) throws ScorerException;

	/**
	 * Calculates the score of a hand. It may depend on other players' hands.
	 * 
	 * @param hand The hand to calculate the score for
	 * @param otherHands The hands of the other players
	 * 
	 * @return The score of the hand
	 * 
	 * @throws ScorerException If there is an error during the scoring process
	 */
	public int calculateScore(ArrayList<ICard> hand, ArrayList<ArrayList<ICard>> otherHands) throws ScorerException;
}
