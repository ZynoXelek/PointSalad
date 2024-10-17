package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.ICard;
import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;

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

	/**
	 * Creates a PointSaladFewestCriterion copy of the given criterion.
	 * 
	 * @param other The criterion to copy
	 */
	public PointSaladFewestCriterion(PointSaladFewestCriterion other) {
		this.vegetable = other.vegetable;
		this.pointsGranted = other.pointsGranted;
	}

	@Override
	public int computePlayerScore(ArrayList<ICard> playerHand, ArrayList<ArrayList<ICard>> otherHands) {
		int minCount = Integer.MAX_VALUE;

		// Player processing
		ArrayList<PointSaladCard> playerHandConverted = PointSaladCard.convertHand(playerHand);
		HashMap<Vegetable, Integer> playerVeggieCount = PointSaladCard.countVeggiesInHand(playerHandConverted);
		int playerCount = playerVeggieCount.getOrDefault(vegetable , 0);

		// Other players processing
		for (int i = 0; i < otherHands.size(); i++) {
			ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(otherHands.get(i));
			HashMap<Vegetable, Integer> veggieCount = PointSaladCard.countVeggiesInHand(hand);

			int count = veggieCount.getOrDefault(vegetable , 0);

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

	@Override
	public PointSaladFewestCriterion copy() {
		return new PointSaladFewestCriterion(this);
	}
}
