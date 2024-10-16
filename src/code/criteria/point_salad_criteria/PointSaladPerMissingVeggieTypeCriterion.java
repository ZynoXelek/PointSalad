package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;
import code.players.AbstractPlayer;

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

	@Override
	public int computePlayerScore(ArrayList<AbstractPlayer> players, int playerIndex) {
		int points = 0;

		AbstractPlayer player = players.get(playerIndex);
		ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(player.getHand());
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
}
