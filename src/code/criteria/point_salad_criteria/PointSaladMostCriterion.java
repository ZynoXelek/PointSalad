package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;
import code.players.AbstractPlayer;

/**
 * Criterion for the most of a certain type of vegetable card.
 * Example: "MOST LETTUCE = 10".
 */
public class PointSaladMostCriterion extends AbstractPointSaladCriterion {
	
	private int pointsGranted;
	private Vegetable vegetable;

	/**
	 * Creates a PointSaladMostCriterion with the given vegetable and points granted.
	 * 
	 * @param vegetable The vegetable of the criterion
	 * @param pointsGranted The points granted by the criterion
	 */
	public PointSaladMostCriterion(Vegetable vegetable, int pointsGranted) {
		this.vegetable = vegetable;
		this.pointsGranted = pointsGranted;
	}

	@Override
	public int computePlayerScore(ArrayList<AbstractPlayer> players, int playerIndex) {
		int maxCount = 0;
		int playerCount = 0;

		for (int i = 0; i < players.size(); i++) {
			AbstractPlayer player = players.get(i);
			ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(player.getHand());
			HashMap<Vegetable, Integer> veggieCount = PointSaladCard.countVeggiesInHand(hand);

			int count = veggieCount.getOrDefault(vegetable, 0);

			if (i == playerIndex) {
				playerCount = count;
			}

			if (count > maxCount) {
				maxCount = count;
			}
		}

		if (playerCount == maxCount) {
			return pointsGranted;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "MOST " + vegetable + " = " + pointsGranted;
	}
}
