package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.ICard;
import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;

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

	/**
	 * Creates a PointSaladFewestTotalCriterion copy of the given criterion.
	 * 
	 * @param other The criterion to copy
	 */
	public PointSaladFewestTotalCriterion(PointSaladFewestTotalCriterion other) {
		this.pointsGranted = other.pointsGranted;
	}

	@Override
	public int computePlayerScore(ArrayList<ICard> playerHand, ArrayList<ArrayList<ICard>> otherHands) {
		int minTotalCount = Integer.MAX_VALUE;

		// Player processing
		ArrayList<PointSaladCard> playerHandConverted = PointSaladCard.convertHand(playerHand);
		HashMap<Vegetable, Integer> playerVeggieCount = PointSaladCard.countVeggiesInHand(playerHandConverted);
		int playerTotalCount = 0;
		for (Vegetable veggie : playerVeggieCount.keySet()) {
			playerTotalCount += playerVeggieCount.getOrDefault(veggie, 0);
		}

		// Other players processing
		for (int i = 0; i < otherHands.size(); i++) {
			ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(otherHands.get(i));
			HashMap<Vegetable, Integer> veggieCount = PointSaladCard.countVeggiesInHand(hand);
			int totalCount = 0;
			for (Vegetable veggie : veggieCount.keySet()) {
				totalCount += veggieCount.getOrDefault(veggie, 0);
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

	@Override
	public PointSaladFewestTotalCriterion copy() {
		return new PointSaladFewestTotalCriterion(this);
	}
}
