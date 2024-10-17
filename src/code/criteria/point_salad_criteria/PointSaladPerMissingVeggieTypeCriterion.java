package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.ICard;
import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;

/**
 * Criterion for the Point Salad game that awards points for each missing type of vegetable.
 * Example: "5 / MISSING VEGETABLE TYPE".
 */
public class PointSaladPerMissingVeggieTypeCriterion extends AbstractPointSaladCriterion {
	
	private int pointsPerMissingVeggieType;

	/**
	 * Creates a PointSaladPerMissingVeggieTypeCriterion with the given points per missing vegetable type.
	 * 
	 * @param pointsPerMissingVeggieType The points granted per missing vegetable type
	 */
	public PointSaladPerMissingVeggieTypeCriterion(int pointsPerMissingVeggieType) {
		this.pointsPerMissingVeggieType = pointsPerMissingVeggieType;
	}

	/**
	 * Creates a PointSaladPerMissingVeggieTypeCriterion copy of the given criterion.
	 * 
	 * @param other The criterion to copy
	 */
	public PointSaladPerMissingVeggieTypeCriterion(PointSaladPerMissingVeggieTypeCriterion other) {
		this.pointsPerMissingVeggieType = other.pointsPerMissingVeggieType;
	}

	@Override
	public int computePlayerScore(ArrayList<ICard> playerHand, ArrayList<ArrayList<ICard>> otherHands) {
		int points = 0;

		ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(playerHand);
		HashMap<Vegetable, Integer> veggieCount = PointSaladCard.countVeggiesInHand(hand);
		
		for (Vegetable veggie : Vegetable.values()) {
			if (veggieCount.getOrDefault(veggie, 0) == 0) {
				points += pointsPerMissingVeggieType;
			}
		}
		
		return points;
	}

	@Override
	public String toString() {
		return pointsPerMissingVeggieType + " / MISSING VEGETABLE TYPE";
	}

	@Override
	public PointSaladPerMissingVeggieTypeCriterion copy() {
		return new PointSaladPerMissingVeggieTypeCriterion(this);
	}
}
