package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;
import code.players.AbstractPlayer;

/**
 * Criterion for the player with the fewest total vegetables.
 * Example: "FEWEST TOTAL VEGETABLE = 7".
 */
public class PointSaladFewestTotalCriterion extends AbstractPointSaladCriterion {
	
	private int pointsGranted;

	/**
	 * Creates a PointSaladFewestTotalCriterion with the given points granted.
	 * 
	 * @param pointsGranted The points granted by the criterion
	 */
	public PointSaladFewestTotalCriterion(int pointsGranted) {
		this.pointsGranted = pointsGranted;
	}

	@Override
	public int computePlayerScore(ArrayList<AbstractPlayer> players, int playerIndex) {
		int minTotalCount = Integer.MAX_VALUE;
		int playerTotalCount = 0;

		for (int i = 0; i < players.size(); i++) {
			AbstractPlayer player = players.get(i);
			ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(player.getHand());
			HashMap<Vegetable, Integer> veggieCount = PointSaladCard.countVeggiesInHand(hand);
			int totalCount = 0;
			for (Vegetable veggie : veggieCount.keySet()) {
				totalCount += veggieCount.getOrDefault(veggie, 0);
			}

			if (i == playerIndex) {
				playerTotalCount = totalCount;
			}

			if (totalCount < minTotalCount) {
				minTotalCount = totalCount;
			}
		}

		if (playerTotalCount == minTotalCount) {
			return pointsGranted;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "FEWEST TOTAL VEGETABLE = " + pointsGranted;
	}
}
