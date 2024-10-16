package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;
import code.players.AbstractPlayer;

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

	@Override
	public int computePlayerScore(ArrayList<AbstractPlayer> players, int playerIndex) {
		AbstractPlayer player = players.get(playerIndex);
		ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(player.getHand());
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
}
