package code.game;

import code.players.AbstractPlayer;

/**
 * Interface for the scoring logic of a game.
 */
public interface IScorer {

	/**
	 * Calculate the score for a player.
	 * 
	 * @param player The player to calculate the score for
	 * 
	 * @return The score for the player
	 */
	public int calculateScore(AbstractPlayer player);
}
