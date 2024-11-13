package main.criteria.point_salad_criteria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cards.ICard;
import cards.PointSaladCard;
import cards.PointSaladCard.Vegetable;
import criteria.point_salad_criteria.PointSaladFewestCriterion;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Test class for the PointSaladFewestCriterion class.
 */
public class PointSaladFewestCriterionTest {
	
	private int pointsGranted = 5;
	private Vegetable vegetable = Vegetable.CABBAGE;
	private PointSaladFewestCriterion criterion;
	private ArrayList<ArrayList<ICard>> otherHands;


	@BeforeEach
	public void setCriterion() {
		// Ensure the criterion is correctly set before each test
		criterion = new PointSaladFewestCriterion(vegetable, pointsGranted);
	}

	@BeforeEach
	public void setOtherHands() {
		// Ensure the otherHands are correctly set before each test
		// Prepare two hands, one with 2 veggie cards and one with 5 veggie cards
		otherHands = new ArrayList<>();
		
		PointSaladCard veggieCard = new PointSaladCard(vegetable, null);

		// Hand with 2 veggie cards
		ArrayList<ICard> hand1 = new ArrayList<>();
		hand1.add(veggieCard.copy());
		hand1.add(veggieCard.copy());

		// Hand with 5 veggie cards
		ArrayList<ICard> hand2 = new ArrayList<>();
		hand2.add(veggieCard.copy());
		hand2.add(veggieCard.copy());
		hand2.add(veggieCard.copy());
		hand2.add(veggieCard.copy());
		hand2.add(veggieCard.copy());

		otherHands.add(hand1);
		otherHands.add(hand2);
	}


	@Test
	public void testFewest() {
		ArrayList<ICard> hand = new ArrayList<>();
		PointSaladCard veggieCard = new PointSaladCard(vegetable, null);

		// Test with 0 veggie cards
		assertEquals(pointsGranted, criterion.computePlayerScore(hand, otherHands),
				"Player should have " + pointsGranted + " points with 0 " + vegetable + " cards.");
		
		// Test with 1 veggie card
		hand.add(veggieCard.copy());
		assertEquals(pointsGranted, criterion.computePlayerScore(hand, otherHands),
				"Player should have " + pointsGranted + " points with 1 " + vegetable + " card.");

		// Test with 2 veggie cards (it is still the minimum number of vegetable cards in the game, tied with the other player)
		hand.add(veggieCard.copy());
		assertEquals(pointsGranted, criterion.computePlayerScore(hand, otherHands),
				"Player should have " + pointsGranted + " points with 2 " + vegetable + " cards.");
	}


	@Test
	public void testMost() {
		ArrayList<ICard> hand = new ArrayList<>();
		PointSaladCard veggieCard = new PointSaladCard(vegetable, null);

		// Test with 5 veggie cards
		hand.add(veggieCard.copy());
		hand.add(veggieCard.copy());
		hand.add(veggieCard.copy());
		hand.add(veggieCard.copy());
		hand.add(veggieCard.copy());
		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
				"Player should have 0 points with 5 " + vegetable + " cards.");

		// Test with 6 veggie cards
		hand.add(veggieCard.copy());
		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
				"Player should have 0 points with 6 " + vegetable + " cards.");
	}

	public void testIntermediate() {
		ArrayList<ICard> hand = new ArrayList<>();
		PointSaladCard veggieCard = new PointSaladCard(vegetable, null);

		// Test with 3 veggie cards
		hand.add(veggieCard.copy());
		hand.add(veggieCard.copy());
		hand.add(veggieCard.copy());
		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
				"Player should have 0 points with 3 " + vegetable + " cards.");

		// Test with 4 veggie cards
		hand.add(veggieCard.copy());
		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
				"Player should have 0 points with 4 " + vegetable + " cards.");
	}
	
	//? String representation test

	@Test
	public void testStringRepresentationFewest() {
		String expected = "FEWEST " + vegetable + " = " + pointsGranted;
		assertEquals(expected, criterion.toString(),
		"String representation should be '" + expected + "'");
	}
}
