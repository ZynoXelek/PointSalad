package code.game;

import code.cards.PointSaladCard;
import code.exceptions.CriterionException;
import code.exceptions.ScorerException;
import code.players.AbstractPlayer;

import java.util.ArrayList;

/**
 * Scorer for the Point Salad game.
 * It gets the criteria from the player's hand itself, and computes the total score based on each of them.
 */
public class PointSaladScorer implements IScorer {

	@Override
	public int calculateScore(ArrayList<AbstractPlayer> players, int playerID) throws ScorerException {
		int score = 0;

		AbstractPlayer player = players.get(playerID);

		ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(player.getHand());
		ArrayList<PointSaladCard> criteriaHand = PointSaladCard.getCriteriaHand(hand);

		for (PointSaladCard card : criteriaHand) {
			try {
				score += card.getCriterion().computePlayerScore(players, playerID);
			}
			catch (CriterionException e) {
				throw new ScorerException("Error while computing the score for player " + playerID, e);
			}
		}

		return score;
	}
	
}
