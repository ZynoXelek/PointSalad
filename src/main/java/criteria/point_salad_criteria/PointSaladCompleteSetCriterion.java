package criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import cards.ICard;
import cards.PointSaladCard;
import cards.PointSaladCard.Vegetable;

/**
 * Criterion for having a at least one of every vegetable.
 * Example: "COMPLETE SET = 12".
 */
public class PointSaladCompleteSetCriterion extends AbstractPointSaladCriterion {

	private int pointsGranted;
	
	/**
	 * Creates a PointSaladCompleteSetCriterion with the given points granted.
	 * 
	 * @param pointsGranted The points granted by the criterion
	 */
	public PointSaladCompleteSetCriterion(int pointsGranted) {
		this.pointsGranted = pointsGranted;
	}

	/**
	 * Creates a PointSaladCompleteSetCriterion copy of the given criterion.
	 * 
	 * @param other The criterion to copy
	 */
	public PointSaladCompleteSetCriterion(PointSaladCompleteSetCriterion other) {
		this.pointsGranted = other.pointsGranted;
	}

	@Override
	public int computePlayerScore(ArrayList<ICard> playerHand, ArrayList<ArrayList<ICard>> otherHands) {
		ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(playerHand);
		HashMap<Vegetable, Integer> veggieCount = PointSaladCard.countVeggiesInHand(hand);
		
		for (Vegetable veggie : Vegetable.values()) {
			if (veggieCount.getOrDefault(veggie, 0) == 0) {
				return 0;
			}
		}
		
		return pointsGranted;
	}

	@Override
	public String toString() {
		return "COMPLETE SET = " + pointsGranted;
	}

	@Override
	public PointSaladCompleteSetCriterion copy() {
		return new PointSaladCompleteSetCriterion(this);
	}
}
