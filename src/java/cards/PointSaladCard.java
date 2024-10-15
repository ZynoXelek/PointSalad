package java.cards;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A card for the PointSalad game.
 * It possesses a vegetable side and a criteria side.
 */
public class PointSaladCard implements ICard {

	public enum Vegetable {
		PEPPER, LETTUCE, CARROT, CABBAGE, ONION, TOMATO
	}

	private Vegetable vegetable;
	private String criteria;
	private boolean criteriaSideUp = false;

	/**
	 * Creates a PointSaladCard with the given vegetable and criteria.
	 * 
	 * @param vegetable The vegetable of the card
	 * @param criteria The criteria of the card
	 */
	public PointSaladCard(Vegetable vegetable, String criteria) {
		this.vegetable = vegetable;
		this.criteria = criteria;
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
	 * Gets the criteria of the card.
	 * 
	 * @return The criteria of the card
	 */
	public String getCriteria() {
		return criteria;
	}

	/**
	 * Gets whether the criteria side of the card is up.
	 * 
	 * @return Whether the criteria side of the card is up
	 */
	public boolean isCriteriaSideUp() {
		return criteriaSideUp;
	}

	@Override
	public void flip() {
		criteriaSideUp = !criteriaSideUp;
	}

	@Override
	public void printCard() {
		System.out.println(this);
	}

	@Override
	public String toString() {
		if(criteriaSideUp) {
			return criteria + " (" + vegetable + ")";
		} else {
			return vegetable.toString();
		}
	}

	/**
	 * Gets the criteria hand from the given hand, which means a list of every card in the hand that has its criteria side up.
	 * 
	 * @param hand The hand to get the criteria hand from
	 * 
	 * @return The criteria hand from the given hand
	 */
	public static ArrayList<PointSaladCard> getCriteriaHand(ArrayList<PointSaladCard> hand)
	{
		ArrayList<PointSaladCard> criteriaHand = new ArrayList<>();

		for (PointSaladCard card : hand)
		{
			if (card.isCriteriaSideUp())
			{
				criteriaHand.add(card);
			}
		}

		return criteriaHand;
	}

	/**
	 * Gets the veggie hand from the given hand, which means a list of every card in the hand that has its veggie side up.
	 * 
	 * @param hand The hand to get the veggie hand from
	 * 
	 * @return The veggie hand from the given hand
	 */
	public static ArrayList<PointSaladCard> getVeggieHand(ArrayList<PointSaladCard> hand)
	{
		ArrayList<PointSaladCard> veggieHand = new ArrayList<>();

		for (PointSaladCard card : hand)
		{
			if (!card.isCriteriaSideUp())
			{
				veggieHand.add(card);
			}
		}

		return veggieHand;
	}

	/**
	 * Counts the number of each vegetable in the given hand.
	 * 
	 * @param hand The hand to count the vegetables from
	 * 
	 * @return A map of each vegetable and the number of times it appears in the hand
	 */
	public static HashMap<Vegetable, Integer> countVeggiesInHand(ArrayList<PointSaladCard> hand)
	{
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

}
