package criteria.point_salad_criteria;

import java.util.ArrayList;
import java.util.HashMap;

import cards.ICard;
import cards.PointSaladCard;
import cards.PointSaladCard.Vegetable;
import exceptions.CriterionException;
import players.AbstractPlayer;

/**
 * Criterion for a number of points per vegetable. It can be used for single or multiple vegetables.
 * Examples: "2 / TOMATO",	"1 / LETTUCE, 1 / TOMATO",	"3 / CARROT,  -2 / ONION",	"4/LETTUCE,  -2/TOMATO,  -2/CABBAGE"
 */
public class PointSaladPerVeggieCriterion extends AbstractPointSaladCriterion {

	private ArrayList<Vegetable> vegetables;
	private ArrayList<Integer> pointsPerVeggie;

	/**
	 * Creates a PointSaladPerVeggieCriterion for a single vegetable with the given points granted.
	 * 
	 * @param vegetable The vegetable of the criterion
	 * @param pointsGranted The points granted by the criterion
	 */
	public PointSaladPerVeggieCriterion(Vegetable vegetable, int pointsGranted) {
		this.vegetables = new ArrayList<>();
		this.vegetables.add(vegetable);
		this.pointsPerVeggie = new ArrayList<>();
		this.pointsPerVeggie.add(pointsGranted);
	}

	/**
	 * Creates a PointSaladPerVeggieCriterion for multiple vegetables with the given points granted.
	 * 
	 * @param criterionDisplay The string to be displayed for this criterion
	 * @param vegetables The vegetables of the criterion
	 * @param pointsPerVeggie The points granted per vegetable by the criterion
	 */
	public PointSaladPerVeggieCriterion(ArrayList<Vegetable> vegetables, ArrayList<Integer> pointsPerVeggie) {
		this.vegetables = vegetables;
		this.pointsPerVeggie = pointsPerVeggie;
	}

	/**
	 * Creates a PointSaladPerVeggieCriterion copy of the given criterion.
	 * 
	 * @param other The criterion to copy
	 */
	public PointSaladPerVeggieCriterion(PointSaladPerVeggieCriterion other) {
		this.vegetables = new ArrayList<>(other.vegetables);
		this.pointsPerVeggie = new ArrayList<>(other.pointsPerVeggie);
	}

	public void checkValidCriterion() throws CriterionException {
		// Checks if the criterion lacks points for some veggies
		if (pointsPerVeggie.size() < vegetables.size()) {
			throw new CriterionException("The number of points per vegetable must be specified for each vegetable.");
		}
	}
	
	@Override
	public int computePlayerScore(HashMap<Integer, AbstractPlayer> players, int playerIndex) throws CriterionException {
		checkValidCriterion();

		// Checks if the criterion is empty
		if (vegetables.isEmpty()) {
			return 0;
		}
		
		return super.computePlayerScore(players, playerIndex);
	}

	@Override
	public int computePlayerScore(ArrayList<ICard> playerHand, ArrayList<ArrayList<ICard>> otherHands) throws CriterionException {
		checkValidCriterion();
		
		// Checks if the criterion is empty
		if (vegetables.isEmpty()) {
			return 0;
		}

		int points = 0;
		ArrayList<PointSaladCard> hand = PointSaladCard.convertHand(playerHand);
		HashMap<Vegetable, Integer> veggieCounts = PointSaladCard.countVeggiesInHand(hand);

		for (Vegetable veggie : vegetables) {
			int veggiePoints = this.pointsPerVeggie.get(vegetables.indexOf(veggie));
			points += veggieCounts.getOrDefault(veggie, 0) * veggiePoints;
		}

		return points;
	}

	@Override
	public String toString() {
		StringBuilder criterionString = new StringBuilder();
		for (int i = 0; i < vegetables.size(); i++) {
			criterionString.append(pointsPerVeggie.get(i) + " / " + vegetables.get(i));
			if (i < vegetables.size() - 1) {
				criterionString.append(", ");
			}
		}
		return criterionString.toString();
	}

	@Override
	public PointSaladPerVeggieCriterion copy() {
		return new PointSaladPerVeggieCriterion(this);
	}
}
