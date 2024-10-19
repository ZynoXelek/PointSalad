package main.java.game;

import java.util.ArrayList;
import java.util.HashMap;

import main.java.cards.ICard;
import main.java.cards.PointSaladCard;
import main.java.exceptions.CriterionException;
import main.java.exceptions.ScorerException;
import main.java.players.AbstractPlayer;

/**
 * Scorer for the Point Salad game.
 * It gets the criteria from the player's hand itself, and computes the total score based on each of them.
 */
public class PointSaladScorer implements IScorer {

	@Override
	public int calculateScore(HashMap<Integer, AbstractPlayer> players, int playerID) throws ScorerException {
		AbstractPlayer player = players.get(playerID);
		ArrayList<AbstractPlayer> everyPlayers = new ArrayList<>(players.values());
		ArrayList<AbstractPlayer> otherPlayers = AbstractPlayer.getOtherPlayers(everyPlayers, playerID);
		ArrayList<ICard> hand = player.getHand();
		ArrayList<ArrayList<ICard>> otherHands = AbstractPlayer.getHands(otherPlayers);

		return calculateScore(hand, otherHands);
	}

	@Override
	public int calculateScore(ArrayList<ICard> hand, ArrayList<ArrayList<ICard>> otherHands) throws ScorerException {
		int score = 0;

		ArrayList<PointSaladCard> pointSaladHand = PointSaladCard.convertHand(hand);
		ArrayList<PointSaladCard> criteriaHand = PointSaladCard.getCriteriaHand(pointSaladHand);

		for (int i = 0; i < criteriaHand.size(); i++) {
			PointSaladCard card = criteriaHand.get(i);
			try {
				score += card.getCriterion().computePlayerScore(hand, otherHands);
			}
			catch (CriterionException e) {
				throw new ScorerException("Error while computing the score for criterion card n°" + (i+1), e);
			}
		}

		return score;
	}
	
}
