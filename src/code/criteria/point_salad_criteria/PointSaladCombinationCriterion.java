package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;
import code.players.AbstractPlayer;

/**
 * Criterion for a combination of vegetables.
 * Examples: "LETTUCE + LETTUCE = 5",    "CABBAGE + ONION = 5",    "CARROT + CARROT + CARROT = 8"
 */
public class PointSaladCombinationCriterion extends AbstractPointSaladCriterion {

	private ArrayList<Vegetable> vegetables;
	private int pointsGranted;

	/**
	 * Creates a PointSaladCombinationCriterion for multiple vegetables with the given points granted.
	 * 
	 * @param vegetables The vegetables of the criterion
	 * @param pointsGranted The points granted by the criterion
	 */
	public PointSaladCombinationCriterion(ArrayList<Vegetable> vegetables, int pointsGranted) {
		this.vegetables = vegetables;
		this.pointsGranted = pointsGranted;
	}

	@Override
	public int computePlayerScore(ArrayList<AbstractPlayer> players, int playerIndex) {
		// Checks if the criterion is empty
		if (vegetables.isEmpty()) {
			return 0;
		}

		int points = 0;
		AbstractPlayer player = players.get(playerIndex);
		ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(player.getHand());
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
		points = minCombinations * pointsGranted;

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
		criterionDisplay += " = " + pointsGranted;
		return criterionDisplay;
	}
}
