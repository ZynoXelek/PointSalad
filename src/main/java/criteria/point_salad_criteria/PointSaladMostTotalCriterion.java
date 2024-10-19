package main.java.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import main.java.cards.ICard;
import main.java.cards.PointSaladCard;
import main.java.cards.PointSaladCard.Vegetable;

/**
 * Criterion for the player with the most total vegetables.
 * Example: "MOST TOTAL VEGETABLE = 10".
 */
public class PointSaladMostTotalCriterion extends AbstractPointSaladCriterion {
	
	private int pointsGranted;

	/**
	 * Creates a PointSaladMostTotalCriterion with the given points granted.
	 * 
	 * @param pointsGranted The points granted by the criterion
	 */
	public PointSaladMostTotalCriterion(int pointsGranted) {
		this.pointsGranted = pointsGranted;
	}

	/**
	 * Creates a PointSaladMostTotalCriterion copy of the given criterion.
	 * 
	 * @param other The criterion to copy
	 */
	public PointSaladMostTotalCriterion(PointSaladMostTotalCriterion other) {
		this.pointsGranted = other.pointsGranted;
	}

	@Override
	public int computePlayerScore(ArrayList<ICard> playerHand, ArrayList<ArrayList<ICard>> otherHands) {
		int maxTotalCount = 0;

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

			if (totalCount > maxTotalCount) {
				maxTotalCount = totalCount;
			}
		}

		if (playerTotalCount == maxTotalCount) {
			return pointsGranted;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "MOST TOTAL VEGETABLE = " + pointsGranted;
	}

	@Override
	public PointSaladMostTotalCriterion copy() {
		return new PointSaladMostTotalCriterion(this);
	}
}
