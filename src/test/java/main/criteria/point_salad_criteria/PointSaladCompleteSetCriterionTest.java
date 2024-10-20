package main.criteria.point_salad_criteria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cards.ICard;
import cards.PointSaladCard;
import criteria.point_salad_criteria.PointSaladCompleteSetCriterion;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * This class is meant for testing the PointSaladCompleteSetCriterion class.
 */
public class PointSaladCompleteSetCriterionTest {

	private int pointsGranted = 10;
	private PointSaladCompleteSetCriterion criterion;
	private ArrayList<ArrayList<ICard>> otherHands;


	@BeforeEach
	public void setCriterion() {
		// Ensure the criterion is correctly set before each test
		criterion = new PointSaladCompleteSetCriterion(pointsGranted);
	}


	@BeforeEach
	public void setOtherHands() {
		// Ensure the otherHands are correctly set before each test
		// In this case, they are not required for this criterion
		otherHands = new ArrayList<>();
	}



	@Test
	public void testCompleteSet() {
		ArrayList<ICard> hand = new ArrayList<>();

		PointSaladCard carrot = new PointSaladCard(PointSaladCard.Vegetable.CARROT, null);
		PointSaladCard lettuce = new PointSaladCard(PointSaladCard.Vegetable.LETTUCE, null);
		PointSaladCard tomato = new PointSaladCard(PointSaladCard.Vegetable.TOMATO, null);
		PointSaladCard pepper = new PointSaladCard(PointSaladCard.Vegetable.PEPPER, null);
		PointSaladCard onion = new PointSaladCard(PointSaladCard.Vegetable.ONION, null);
		PointSaladCard cabbage = new PointSaladCard(PointSaladCard.Vegetable.CABBAGE, null);

		hand.add(cabbage.copy());
		hand.add(onion.copy());
		hand.add(pepper.copy());
		hand.add(tomato.copy());
		hand.add(lettuce.copy());
		hand.add(carrot.copy());

		assertEquals(pointsGranted, criterion.computePlayerScore(hand, otherHands),
		"Player should have " + pointsGranted + " points for a complete set of vegetables.");
	}


	@Test
	public void testIncompleteSet() {
		ArrayList<ICard> hand = new ArrayList<>();

		PointSaladCard pepper = new PointSaladCard(PointSaladCard.Vegetable.PEPPER, null);
		PointSaladCard onion = new PointSaladCard(PointSaladCard.Vegetable.ONION, null);
		PointSaladCard cabbage = new PointSaladCard(PointSaladCard.Vegetable.CABBAGE, null);

		hand.add(cabbage.copy());
		hand.add(onion.copy());
		hand.add(pepper.copy());

		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have no points for a complete set of vegetables.");
	}


	@Test
	public void testEmptySet() {
		ArrayList<ICard> hand = new ArrayList<>();

		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have no points for an empty set of vegetables.");
	}

	//? String representation test

	@Test
	public void testStringRepresentationCompleteSet() {
		String expected = "COMPLETE SET = " + pointsGranted;
		assertEquals(expected, criterion.toString(),
		"String representation should be '" + expected + "'");
	}
}
