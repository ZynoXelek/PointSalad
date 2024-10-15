package java.cards;

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

}
