package code.cards;

import java.util.ArrayList;
import java.util.HashMap;

import code.criteria.ICriterion;

/**
 * A card for the PointSalad game.
 * It possesses a vegetable side and a criterion side.
 */
public class PointSaladCard implements ICard {

	/**
	 * The vegetables that can be on a PointSalad card.
	 */
	public enum Vegetable {
		PEPPER, LETTUCE, CARROT, CABBAGE, ONION, TOMATO
	}

	private Vegetable vegetable;
	private ICriterion criterion;
	private boolean criterionSideUp = false;

	/**
	 * Creates a PointSaladCard with the given vegetable and criterion.
	 * 
	 * @param vegetable The vegetable of the card
	 * @param criterion The criterion of the card
	 */
	public PointSaladCard(Vegetable vegetable, ICriterion criterion) {
		this.vegetable = vegetable;
		this.criterion = criterion;
	}

	/**
	 * Gets the vegetable of the card.
	 * 
	 * @return The vegetable of the card
	 */
	public Vegetable getVegetable() {
		return vegetable;
	}

	/**
	 * Gets the criterion of the card.
	 * 
	 * @return The criterion of the card
	 */
	public ICriterion getCriterion() {
		return criterion;
	}

	/**
	 * Gets whether the criterion side of the card is up.
	 * 
	 * @return Whether the criterion side of the card is up
	 */
	public boolean isCriterionSideUp() {
		return criterionSideUp;
	}

	@Override
	public void flip() {
		criterionSideUp = !criterionSideUp;
	}

	@Override
	public String toString() {
		if(criterionSideUp) {
			return criterion.getCriterionDisplay() + " (" + vegetable + ")";
		} else {
			return vegetable.toString();
		}
	}

	@Override
	public String handToString(ArrayList<ICard> hand) {
		return PointSaladCard.getHandAsString(PointSaladCard.convertHand(hand));
	}

	/**
	 * Gets the criteria hand from the given hand, which means a list of every card in the hand that has its criterion side up.
	 * 
	 * @param hand The hand to get the criteria hand from
	 * 
	 * @return The criteria hand from the given hand
	 */
	public static ArrayList<PointSaladCard> getCriteriaHand(ArrayList<PointSaladCard> hand) {
		ArrayList<PointSaladCard> criterionHand = new ArrayList<>();

		for (PointSaladCard card : hand)
		{
			if (card.isCriterionSideUp())
			{
				criterionHand.add(card);
			}
		}

		return criterionHand;
	}

	/**
	 * Gets the veggie hand from the given hand, which means a list of every card in the hand that has its veggie side up.
	 * 
	 * @param hand The hand to get the veggie hand from
	 * 
	 * @return The veggie hand from the given hand
	 */
	public static ArrayList<PointSaladCard> getVeggieHand(ArrayList<PointSaladCard> hand) {
		ArrayList<PointSaladCard> veggieHand = new ArrayList<>();

		for (PointSaladCard card : hand)
		{
			if (!card.isCriterionSideUp())
			{
				veggieHand.add(card);
			}
		}

		return veggieHand;
	}

	/**
	 * Counts the number of each vegetable in the given hand.
	 * It will only count the cards that have their veggie side up.
	 * 
	 * @param hand The hand to count the vegetables from
	 * 
	 * @return A map of each vegetable and the number of times it appears in the hand
	 */
	public static HashMap<Vegetable, Integer> countVeggiesInHand(ArrayList<PointSaladCard> hand) {
		// Keeps only the veggie cards
		hand = getVeggieHand(hand);
		
		HashMap<Vegetable, Integer> veggieCount = new HashMap<>();

		for (PointSaladCard card : hand)
		{
			Vegetable veggie = card.getVegetable();
			if (veggieCount.containsKey(veggie))
			{
				veggieCount.put(veggie, veggieCount.get(veggie) + 1);
			}
			else
			{
				veggieCount.put(veggie, 1);
			}
		}

		return veggieCount;
	}

	/**
	 * Converts a hand of ICard to a hand of PointSaladCard.
	 * 
	 * @param hand The hand to convert
	 * 
	 * @return The hand converted to PointSaladCard
	 */
	public static ArrayList<PointSaladCard> convertHand(ArrayList<ICard> hand) {
		ArrayList<PointSaladCard> finalHand = new ArrayList<>();

		for (ICard card: hand)
		{
			if (card instanceof PointSaladCard)
			{
				finalHand.add((PointSaladCard) card);
			}
		}

		return finalHand;
	}

	/**
	 * Constructs a fancy string representation of a hand of PointSaladCard.
	 * It will display the criteria and the vegetables in the hand, separately.
	 * 
	 * @param hand The hand to convert to a string
	 * 
	 * @return The string representation of the hand
	 */
	public static String getHandAsString(ArrayList<PointSaladCard> hand) {
		StringBuilder handString = new StringBuilder();

		ArrayList<PointSaladCard> criterionHand = getCriteriaHand(hand);
		ArrayList<PointSaladCard> veggieHand = getVeggieHand(hand);

		HashMap<Vegetable, Integer> veggieCount = countVeggiesInHand(veggieHand);

		handString.append("Criteria: ");
		for (int i = 0; i < criterionHand.size(); i++)
		{
			PointSaladCard card = criterionHand.get(i);
			handString.append("[" + i + "] " + card.toString() + "\t\t");
		}

		handString.append("\nVegetables: ");
		for (Vegetable veggie : Vegetable.values())
		{
			int count = veggieCount.getOrDefault(veggie, 0);
			if (count == 0)
			{
				continue;
			}
			handString.append(veggie + ": " + count + "\t\t");
		}

		return handString.toString();
	}
}
