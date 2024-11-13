package main.criteria.point_salad_criteria;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cards.ICard;
import cards.PointSaladCard;
import cards.PointSaladCard.Vegetable;
import criteria.point_salad_criteria.PointSaladPerMissingVeggieTypeCriterion;

/**
 * Test class for the PointSaladPerMissingVeggieTypeCriterion class.
 */
public class PointSaladPerMissingVeggieTypeCriterionTest {

	private int pointsPerMissingType = 5;
	private PointSaladPerMissingVeggieTypeCriterion criterion;
	private ArrayList<ArrayList<ICard>> otherHands;
	private PointSaladCard carrotCard = new PointSaladCard(Vegetable.CARROT, null);
	private PointSaladCard cabbageCard = new PointSaladCard(Vegetable.CABBAGE, null);
	private PointSaladCard lettuceCard = new PointSaladCard(Vegetable.LETTUCE, null);
	private PointSaladCard pepperCard = new PointSaladCard(Vegetable.PEPPER, null);
	private PointSaladCard tomatoCard = new PointSaladCard(Vegetable.TOMATO, null);
	private PointSaladCard onionCard = new PointSaladCard(Vegetable.ONION, null);


	@BeforeEach
	public void setCriterion() {
		// Ensure the criterion is correctly set before each test
		criterion = new PointSaladPerMissingVeggieTypeCriterion(pointsPerMissingType);
	}

	@BeforeEach
	public void setOtherHands() {
		// Ensure the otherHands are correctly set before each test
		// In this case, they are not required for this criterion
		otherHands = new ArrayList<>();
	}
	
	@Test
	public void testWithNoMissingType() {
		ArrayList<ICard> hand = new ArrayList<>();

		// Add all the vegetable cards
		hand.add(carrotCard.copy());
		hand.add(cabbageCard.copy());
		hand.add(lettuceCard.copy());
		hand.add(pepperCard.copy());
		hand.add(tomatoCard.copy());
		hand.add(onionCard.copy());

		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have 0 points for having all the vegetable types.");

		// In any quantities
		hand.add(carrotCard.copy());
		hand.add(cabbageCard.copy());
		hand.add(tomatoCard.copy());
		hand.add(tomatoCard.copy());
		hand.add(tomatoCard.copy());

		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have 0 points for having all the vegetable types.");
	}

	@Test
	public void testMissingType() {
		ArrayList<ICard> hand = new ArrayList<>();

		// Every type is missing
		int nbType = Vegetable.values().length;
		assertEquals(pointsPerMissingType * nbType, criterion.computePlayerScore(hand, otherHands),
		"Player should have 30 points for missing all the vegetable types.");

		// Will add these cards in this order to the hand
		ArrayList<ICard> cardsToAdd = new ArrayList<>();
		cardsToAdd.add(carrotCard.copy());
		cardsToAdd.add(carrotCard.copy()); // Should not depend on the quantity
		cardsToAdd.add(cabbageCard.copy());
		cardsToAdd.add(lettuceCard.copy());
		cardsToAdd.add(lettuceCard.copy());
		cardsToAdd.add(lettuceCard.copy());
		cardsToAdd.add(pepperCard.copy());
		cardsToAdd.add(tomatoCard.copy());
		cardsToAdd.add(onionCard.copy());

		// Corresponding number of missing types to compute expected score
		ArrayList<Integer> nbMissingType = new ArrayList<>();
		nbMissingType.add(nbType - 1);
		nbMissingType.add(nbType - 1);
		nbMissingType.add(nbType - 2);
		nbMissingType.add(nbType - 3);
		nbMissingType.add(nbType - 3);
		nbMissingType.add(nbType - 3);
		nbMissingType.add(nbType - 4);
		nbMissingType.add(nbType - 5);
		nbMissingType.add(nbType - 6);

		for (int i = 0; i < cardsToAdd.size(); i++) {
			hand.add(cardsToAdd.get(i));
			int expectedScore = pointsPerMissingType * nbMissingType.get(i);
			assertEquals(expectedScore, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + expectedScore + " points for missing " + nbMissingType.get(i) + " vegetable types.");
		}
	}
	
	//? String representation test

	@Test
	public void testStringRepresentationPerMissingVeggieType() {
		String expected = pointsPerMissingType + " / MISSING VEGETABLE TYPE";
		assertEquals(expected, criterion.toString(),
		"String representation should be '" + expected + "'");
	}
}
