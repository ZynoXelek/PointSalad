package main.criteria.point_salad_criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cards.ICard;
import cards.PointSaladCard;
import criteria.point_salad_criteria.PointSaladMostTotalCriterion;

/**
 * Test class for the PointSaladMostTotalCriterion class.
 */
public class PointSaladMostTotalCriterionTest {

	private int pointsGranted = 10;
	private PointSaladMostTotalCriterion criterion;
	private ArrayList<ArrayList<ICard>> otherHands;
	private PointSaladCard carrotCard = new PointSaladCard(PointSaladCard.Vegetable.CARROT, null);
	private PointSaladCard lettuceCard = new PointSaladCard(PointSaladCard.Vegetable.LETTUCE, null);
	private PointSaladCard cabbageCard = new PointSaladCard(PointSaladCard.Vegetable.CABBAGE, null);
	

	@BeforeEach
	public void setCriterion() {
		// Ensure the criterion is correctly set before each test
		criterion = new PointSaladMostTotalCriterion(pointsGranted);
	}

	@BeforeEach
	public void setOtherHands() {
		// Ensure the otherHands are correctly set before each test
		// Prepare two hands, one with 2 veggie cards and one with 5 veggie cards
		otherHands = new ArrayList<>();
		
		// Hand with 2 veggie cards
		ArrayList<ICard> hand1 = new ArrayList<>();
		hand1.add(carrotCard.copy());
		hand1.add(lettuceCard.copy());

		// Hand with 5 veggie cards
		ArrayList<ICard> hand2 = new ArrayList<>();
		hand2.add(carrotCard.copy());
		hand2.add(carrotCard.copy());
		hand2.add(cabbageCard.copy());
		hand2.add(cabbageCard.copy());
		hand2.add(cabbageCard.copy());

		otherHands.add(hand1);
		otherHands.add(hand2);
	}


	@Test
	public void testFewestTotal() {
		ArrayList<ICard> hand = new ArrayList<>();

		// Create a hand with 0 veggie cards
		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
				"The player should have 0 points for not having the most veggie cards (which is 5).");

		// Create a hand with 1 veggie card
		hand.add(carrotCard.copy());
		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
				"The player should have 0 points for not having the most veggie cards (which is 5).");

		// Create a hand with 2 veggie cards
		hand.add(cabbageCard.copy());
		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
				"The player should have 0 points for not having the most veggie cards (which is 5).");
	}


	@Test
	public void testMaximumTotal() {
		ArrayList<ICard> hand = new ArrayList<>();

		// Create a hand with 5 veggie cards, which is the most (tied with the other hand)
		hand.add(carrotCard.copy());
		hand.add(carrotCard.copy());
		hand.add(cabbageCard.copy());
		hand.add(cabbageCard.copy());
		hand.add(lettuceCard.copy());
		assertEquals(pointsGranted, criterion.computePlayerScore(hand, otherHands),
				"The player should have " + pointsGranted + " points for having the most veggie cards (5).");

		// Create a hand with 6 veggie cards
		hand.add(cabbageCard.copy());
		assertEquals(pointsGranted, criterion.computePlayerScore(hand, otherHands),
				"The player should have " + pointsGranted + " points for having the most veggie cards (6).");
	}

	@Test
	public void testIntermediateTotal() {
		ArrayList<ICard> hand = new ArrayList<>();

		// Create a hand with 3 veggie cards
		hand.add(carrotCard.copy());
		hand.add(cabbageCard.copy());
		hand.add(lettuceCard.copy());
		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
				"The player should have 0 points for not having the most veggie cards (which is 5).");

		// Create a hand with 4 veggie cards
		hand.add(lettuceCard.copy());
		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
				"The player should have 0 points for not having the most veggie cards (which is 5).");
	}
	
	//? String representation test

	@Test
	public void testStringRepresentationMostTotal() {
		String expected = "MOST TOTAL VEGETABLE = " + pointsGranted;
		assertEquals(expected, criterion.toString(),
		"String representation should be '" + expected + "'");
	}
}
