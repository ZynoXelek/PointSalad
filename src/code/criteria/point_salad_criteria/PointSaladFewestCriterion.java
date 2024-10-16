package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;
import code.players.AbstractPlayer;

/**
 * Criterion for the fewest of a certain type of vegetable card. 
 * Example: "FEWEST CARROT = 7".
 */
public class PointSaladFewestCriterion extends AbstractPointSaladCriterion {
	
	private int pointsGranted;
	private Vegetable vegetable;

	/**
	 * Creates a PointSaladFewestCriterion with the given vegetable and points granted.
	 * 
	 * @param vegetable The vegetable of the criterion
	 * @param pointsGranted The points granted by the criterion
	 */
	public PointSaladFewestCriterion(Vegetable vegetable, int pointsGranted) {
		this.vegetable = vegetable;
		this.pointsGranted = pointsGranted;
	}

	@Override
	public int computePlayerScore(ArrayList<AbstractPlayer> players, int playerIndex) {
		int minCount = Integer.MAX_VALUE;
		int playerCount = 0;

		for (int i = 0; i < players.size(); i++) {
			AbstractPlayer player = players.get(i);
			ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(player.getHand());
			HashMap<Vegetable, Integer> veggieCount = PointSaladCard.countVeggiesInHand(hand);

			int count = veggieCount.getOrDefault(vegetable , 0);

			if (i == playerIndex) {
				playerCount = count;
			}

			if (count < minCount) {
				minCount = count;
			}
		}

		if (playerCount == minCount) {
			return pointsGranted;
		} else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		return "FEWEST " + vegetable + " = " + pointsGranted;
	}
}
