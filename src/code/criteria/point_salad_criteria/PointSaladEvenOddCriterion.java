package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;
import code.players.AbstractPlayer;

/**
 * Criterion for the even or odd number of a certain type of vegetable card.
 * Example: "ONION: EVEN=7, ODD=3".
 */
public class PointSaladEvenOddCriterion extends AbstractPointSaladCriterion {

	private Vegetable vegetable;
	private int evenPoints;
	private int oddPoints;

	/**
	 * Creates a PointSaladEvenOddCriterion with the given vegetable, even points, and odd points.
	 * 
	 * @param vegetable The vegetable of the criterion
	 * @param evenPoints The points granted for an even number of the vegetable
	 * @param oddPoints The points granted for an odd number of the vegetable
	 */
	public PointSaladEvenOddCriterion(Vegetable vegetable, int evenPoints, int oddPoints) {
		this.vegetable = vegetable;
		this.evenPoints = evenPoints;
		this.oddPoints = oddPoints;
	}
	

	@Override
	public int computePlayerScore(ArrayList<AbstractPlayer> players, int playerIndex) {
		int veggieCount = 0;

		AbstractPlayer player = players.get(playerIndex);
		ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(player.getHand());
		HashMap<Vegetable, Integer> veggieCounts = PointSaladCard.countVeggiesInHand(hand);

		if (veggieCounts.containsKey(vegetable)) {
			veggieCount = veggieCounts.get(vegetable);
		}

		if (veggieCount % 2 == 0) {
			return evenPoints;
		} else {
			return oddPoints;
		}
	}

	@Override
	public String toString() {
		return vegetable + ": EVEN=" + evenPoints + ", ODD=" + oddPoints;
	}
}
