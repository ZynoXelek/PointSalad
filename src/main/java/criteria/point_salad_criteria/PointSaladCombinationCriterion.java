package criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import cards.ICard;
import cards.PointSaladCard;
import cards.PointSaladCard.Vegetable;
import exceptions.CriterionException;
import players.AbstractPlayer;

/**
 * Criterion for a combination of vegetables.
 * Examples: "LETTUCE + LETTUCE = 5",    "CABBAGE + ONION = 5",    "CARROT + CARROT + CARROT = 8"
 */
public class PointSaladCombinationCriterion extends AbstractPointSaladCriterion {

	private ArrayList<Vegetable> vegetables;
	private int pointsPerCombination;

	/**
	 * Creates a PointSaladCombinationCriterion with the given vegetables and points per combination.
	 * 
	 * @param vegetables The vegetables required for the combination
	 * @param pointsPerCombination The points granted by the combination
	 */
	public PointSaladCombinationCriterion(ArrayList<Vegetable> vegetables, int pointsPerCombination) {
		this.vegetables = vegetables;
		this.pointsPerCombination = pointsPerCombination;
	}

	/**
	 * Creates a PointSaladCombinationCriterion copy of the given criterion.
	 * 
	 * @param other The criterion to copy
	 */
	public PointSaladCombinationCriterion(PointSaladCombinationCriterion other) {
		this.vegetables = new ArrayList<>(other.vegetables);
		this.pointsPerCombination = other.pointsPerCombination;
	}

	@Override
	public int computePlayerScore(HashMap<Integer, AbstractPlayer> players, int playerIndex) throws CriterionException {
		// Checks if the criterion is empty
		if (vegetables.isEmpty()) {
			return 0;
		}
		
		return super.computePlayerScore(players, playerIndex);
	}

	@Override
	public int computePlayerScore(ArrayList<ICard> playerHand, ArrayList<ArrayList<ICard>> otherHands) {
		// Checks if the criterion is empty
		if (vegetables.isEmpty()) {
			return 0;
		}

		int points = 0;
		ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(playerHand);
		HashMap<Vegetable, Integer> veggieCounts = PointSaladCard.countVeggiesInHand(hand);

		// Get the number of each vegetable required for the criterion
		HashMap<Vegetable, Integer> veggiesRequired = new HashMap<>();
		for (Vegetable veggie : vegetables) {
			if (veggiesRequired.containsKey(veggie)) {
				veggiesRequired.put(veggie, veggiesRequired.get(veggie) + 1);
			} else {
				veggiesRequired.put(veggie, 1);
			}
		}

		// Get the number of combinations doable for each vegetable
		int minCombinations = Integer.MAX_VALUE;
		for (Vegetable veggie : vegetables) {
			int required = veggiesRequired.get(veggie);
			int available = veggieCounts.getOrDefault(veggie, 0);
			int combinations = available / required;
			if (combinations < minCombinations) {
				minCombinations = combinations;
			}
		}

		// minCombinations can not be Integer.MAX_VALUE anymore here because at least one vegetable is required
		points = minCombinations * pointsPerCombination;

		return points;
	}

	@Override
	public String toString() {
		String criterionDisplay = "";
		for (int i = 0; i < vegetables.size(); i++) {
			criterionDisplay += vegetables.get(i).toString();
			if (i < vegetables.size() - 1) {
				criterionDisplay += " + ";
			}
		}
		criterionDisplay += " = " + pointsPerCombination;
		return criterionDisplay;
	}

	@Override
	public PointSaladCombinationCriterion copy() {
		return new PointSaladCombinationCriterion(this);
	}
}
