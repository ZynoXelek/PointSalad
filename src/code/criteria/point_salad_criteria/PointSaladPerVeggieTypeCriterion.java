package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;
import code.players.AbstractPlayer;

/**
 * A criterion for the Point Salad game that awards points for having a minimum number of each vegetable type.
 * Examples: "5 / VEGETABLE TYPE >=3",	"3 / VEGETABLE TYPE >=2".
 */
public class PointSaladPerVeggieTypeCriterion extends AbstractPointSaladCriterion {
	
	private int minNumberOfEachVeggie;
	private int pointsPerVeggieType;

	/**
	 * Creates a PointSaladPerVeggieTypeCriterion with the given minimum number of each vegetable and points per vegetable type.
	 * 
	 * @param minNumberOfEachVeggie The minimum number of a vegetable type required to fulfill the criterion
	 * @param pointsPerVeggieType The points granted per vegetable type that fulfills the criterion
	 */
	public PointSaladPerVeggieTypeCriterion(int minNumberOfEachVeggie, int pointsPerVeggieType) {
		this.minNumberOfEachVeggie = minNumberOfEachVeggie;
		this.pointsPerVeggieType = pointsPerVeggieType;
	}

	@Override
	public int computePlayerScore(ArrayList<AbstractPlayer> players, int playerIndex) {
		int points = 0;

		AbstractPlayer player = players.get(playerIndex);
		ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(player.getHand());
		HashMap<Vegetable, Integer> veggieCount = PointSaladCard.countVeggiesInHand(hand);
		
		for (Vegetable veggie : Vegetable.values()) {
			if (veggieCount.getOrDefault(veggie, 0) >= minNumberOfEachVeggie) {
				points += pointsPerVeggieType;
			}
		}
		
		return points;
	}

	@Override
	public String toString() {
		return pointsPerVeggieType + " / VEGETABLE TYPE >= " + minNumberOfEachVeggie;
	}
}
