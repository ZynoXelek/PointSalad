package code.criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import code.cards.ICard;
import code.criteria.ICriterion;
import code.exceptions.CriterionException;
import code.players.AbstractPlayer;

/**
 * Abstract Class to hold criteria for the Point Salad card.
 */
public abstract class AbstractPointSaladCriterion implements ICriterion {

	/**
	 * The types of criteria found in PointSalad cards.
	 */
	public enum CriterionType {
		MOST, 				// example: "MOST LETTUCE = 10"
		FEWEST, 			// example: "FEWEST CARROT = 7"
		EVEN_ODD, 			// example: "ONION: EVEN=7, ODD=3"
		PER_VEGGIE, 		// examples: "2 / TOMATO",	"1 / LETTUCE, 1 / TOMATO",	"3 / CARROT, -2 / ONION",	"4/LETTUCE,  -2/TOMATO,  -2/CABBAGE"
		COMBINATION, 		// example: "LETTUCE + LETTUCE = 5",	"CABBAGE + ONION = 5",	"CARROT + CARROT + CARROT = 8"
		MOST_TOTAL, 		// example: "MOST TOTAL VEGETABLE = 10"
		FEWEST_TOTAL, 		// example: "FEWEST TOTAL VEGETABLE = 7"
		COMPLETE_SET, 		// example: "COMPLETE SET = 12",
		MISSING_TYPE, 		// example: "5 / MISSING VEGETABLE TYPE"
		PER_VEGGIE_TYPE, 	// example: "5 / VEGETABLE TYPE >=3", "3 / VEGETABLE TYPE >=2"
	}

	@Override
	public String getCriterionDisplay() {
		return this.toString();
	}

	@Override
	public int computePlayerScore(HashMap<Integer, AbstractPlayer> players, int playerID) throws CriterionException {
		AbstractPlayer player = players.get(playerID);
		ArrayList<AbstractPlayer> playersList = new ArrayList<>(players.values());
		ArrayList<AbstractPlayer> otherPlayers = AbstractPlayer.getOtherPlayers(playersList, playerID);
		ArrayList<ICard> hand = player.getHand();
		ArrayList<ArrayList<ICard>> otherHands = AbstractPlayer.getHands(otherPlayers);

		return computePlayerScore(hand, otherHands);
	}

	@Override
	public abstract int computePlayerScore(ArrayList<ICard> playerHand, ArrayList<ArrayList<ICard>> otherHands) throws CriterionException;

	@Override
	public abstract String toString();

	@Override
	public abstract AbstractPointSaladCriterion copy();
}
