package main.criteria.point_salad_criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cards.ICard;
import cards.PointSaladCard;
import cards.PointSaladCard.Vegetable;
import criteria.point_salad_criteria.PointSaladPerVeggieTypeCriterion;

/**
 * Test class for the PointSaladPerVeggieTypeCriterion class.
 */
public class PointSaladPerVeggieTypeCriterionTest {

	private PointSaladCard carrotCard = new PointSaladCard(Vegetable.CARROT, null);
	private PointSaladCard cabbageCard = new PointSaladCard(Vegetable.CABBAGE, null);
	private PointSaladCard lettuceCard = new PointSaladCard(Vegetable.LETTUCE, null);
	private PointSaladCard pepperCard = new PointSaladCard(Vegetable.PEPPER, null);
	private PointSaladCard tomatoCard = new PointSaladCard(Vegetable.TOMATO, null);
	private PointSaladCard onionCard = new PointSaladCard(Vegetable.ONION, null);

	private int pointsPerVeggieType = 5;
	private int minNumberOfVeggies = 3;
	private PointSaladPerVeggieTypeCriterion criterion;
	private ArrayList<ArrayList<ICard>> otherHands;
	
	@BeforeEach
	public void setOtherHands() {
		// Ensure the otherHands are correctly set before each test
		// In this case, they are not required for this criterion
		otherHands = new ArrayList<>();
	}

	@BeforeEach
	public void setCriterion() {
		// Ensure the criterion is correctly set before each test
		criterion = new PointSaladPerVeggieTypeCriterion(minNumberOfVeggies, pointsPerVeggieType);
	}


	@Test
	public void testNotEnoughVeggies() {
		ArrayList<ICard> hand = new ArrayList<>();

		// Add some vegetable cards, but not enough to meet the minimum
		hand.add(carrotCard.copy());
		hand.add(cabbageCard.copy());
		hand.add(cabbageCard.copy());
		hand.add(lettuceCard.copy());
		hand.add(pepperCard.copy());
		hand.add(pepperCard.copy());


		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have 0 points for not having enough vegetable types.");
	}


	@Test
	public void testSingleEnough() {
		ArrayList<ICard> hand = new ArrayList<>();

		// Add some vegetable cards, but not enough to meet the minimum
		hand.add(carrotCard.copy());
		hand.add(cabbageCard.copy());
		hand.add(cabbageCard.copy());
		hand.add(lettuceCard.copy());
		hand.add(pepperCard.copy());
		hand.add(pepperCard.copy());

		// Add more veggies to meet the minimum
		int nbPepper = 2;
		int nbPepperToAdd = minNumberOfVeggies - nbPepper;
		// Go to the limit of the minimum number of veggies
		for (int i = 0; i < nbPepperToAdd - 1; i++) {
			hand.add(pepperCard.copy());
		}

		for (int i = 0; i < 3; i++) { // Should not depend on how much more we have
			hand.add(pepperCard.copy());
			assertEquals(pointsPerVeggieType, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + pointsPerVeggieType + " points for having enough vegetable of a single type.");
		}
	}


	@Test
	public void testEnoughOfEveryType() {
		ArrayList<ICard> hand = new ArrayList<>();
		ArrayList<PointSaladCard> pointSaladCards = new ArrayList<>();
		pointSaladCards.add(carrotCard);
		pointSaladCards.add(cabbageCard);
		pointSaladCards.add(lettuceCard);
		pointSaladCards.add(pepperCard);
		pointSaladCards.add(tomatoCard);
		pointSaladCards.add(onionCard);


		// Go to just under limit
		for (int i = 0; i < minNumberOfVeggies - 1; i++) {
			for (PointSaladCard card : pointSaladCards) {
				hand.add(card.copy());
			}
		}

		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have 0 points for having enough of every vegetable type.");

		// Test each one
		for (int i = 0; i < pointSaladCards.size(); i++) {
			hand.add(pointSaladCards.get(i).copy());
			int nbAboveLimit = i+1;

			assertEquals(nbAboveLimit * pointsPerVeggieType, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + nbAboveLimit * pointsPerVeggieType + " points for having enough of " + nbAboveLimit + " vegetable type.");
		}
	}
	
	//? String representation test

	@Test
	public void testStringRepresentationPerVeggieType() {
		String expected = pointsPerVeggieType + " / VEGETABLE TYPE >= " + minNumberOfVeggies;
		assertEquals(expected, criterion.toString(),
		"String representation should be '" + expected + "'");
	}
}
