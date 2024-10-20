package main.criteria.point_salad_criteria;

import cards.ICard;
import cards.PointSaladCard;
import cards.PointSaladCard.Vegetable;
import criteria.point_salad_criteria.PointSaladEvenOddCriterion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

/**
 * This class is meant for testing the PointSaladEvenOddCriterion class.
 */
public class PointSaladEvenOddCriterionTest {

	private int evenPoints = 5;
	private int oddPoints = 3;
	private Vegetable vegetable = Vegetable.CARROT;
	private PointSaladEvenOddCriterion criterion;
	private ArrayList<ArrayList<ICard>> otherHands;


	@BeforeEach
	public void setCriterion() {
		// Ensure the criterion is correctly set before each test
		criterion = new PointSaladEvenOddCriterion(vegetable, evenPoints, oddPoints);
	}


	@BeforeEach
	public void setOtherHands() {
		// Ensure the otherHands are correctly set before each test
		// In this case, they are not required for this criterion
		otherHands = new ArrayList<>();
	}


	@Test
	public void testEven() {
		PointSaladCard vegetableCard = new PointSaladCard(vegetable, null);
		ArrayList<ICard> hand = new ArrayList<>();
		
		// 0 cards is even by definition
		assertEquals(evenPoints, criterion.computePlayerScore(hand, otherHands),
		"Player should have " + evenPoints + " points for 0 (even) " + vegetable + " cards.");

		for (int i = 0; i < 3; i++) {
			hand.add(vegetableCard.copy());
			hand.add(vegetableCard.copy());
			assertEquals(evenPoints, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + evenPoints + " points for " + 2*(i+1) + " (even) " + vegetable + " cards.");
		}
	}


	@Test
	public void testOdd() {
		PointSaladCard vegetableCard = new PointSaladCard(vegetable, null);
		ArrayList<ICard> hand = new ArrayList<>();
		
		// 1 card is odd
		hand.add(vegetableCard.copy());
		assertEquals(oddPoints, criterion.computePlayerScore(hand, otherHands),
		"Player should have " + oddPoints + " points for 1 (odd) " + vegetable + " cards.");

		for (int i = 0; i < 3; i++) {
			hand.add(vegetableCard.copy());
			hand.add(vegetableCard.copy());
			assertEquals(oddPoints, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + oddPoints + " points for " + 3+2*i + " (odd) " + vegetable + " cards.");
		}
	}
	
	//? String representation test

	@Test
	public void testStringRepresentationEvenOdd() {
		String expected = vegetable + ": EVEN=" + evenPoints + ", ODD=" + oddPoints;
		assertEquals(expected, criterion.toString(),
		"String representation should be '" + expected + "'");
	}
}
