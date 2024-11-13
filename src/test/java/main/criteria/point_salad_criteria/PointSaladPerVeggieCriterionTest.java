package main.criteria.point_salad_criteria;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cards.ICard;
import cards.PointSaladCard;
import cards.PointSaladCard.Vegetable;
import criteria.point_salad_criteria.PointSaladPerVeggieCriterion;

/**
 * Test class for the PointSaladPerVeggieCriterion class.
 */
public class PointSaladPerVeggieCriterionTest {

	private PointSaladCard carrotCard = new PointSaladCard(Vegetable.CARROT, null);
	private PointSaladCard cabbageCard = new PointSaladCard(Vegetable.CABBAGE, null);
	private PointSaladCard lettuceCard = new PointSaladCard(Vegetable.LETTUCE, null);
	private PointSaladCard pepperCard = new PointSaladCard(Vegetable.PEPPER, null);
	private PointSaladCard tomatoCard = new PointSaladCard(Vegetable.TOMATO, null);
	private PointSaladCard onionCard = new PointSaladCard(Vegetable.ONION, null);
	private ArrayList<ArrayList<ICard>> otherHands;
	
	@BeforeEach
	public void setOtherHands() {
		// Ensure the otherHands are correctly set before each test
		// In this case, they are not required for this criterion
		otherHands = new ArrayList<>();
	}


	@Test
	public void testSingleVeggie() throws Exception {
		// Create a criterion with a single vegetable type
		ArrayList<Vegetable> veggies = new ArrayList<>();
		veggies.add(Vegetable.CARROT);
		ArrayList<Integer> points = new ArrayList<>();
		int point = 2;
		points.add(point);
		PointSaladPerVeggieCriterion criterion = new PointSaladPerVeggieCriterion(veggies, points);

		ArrayList<ICard> hand = new ArrayList<>();

		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have 0 points for not having any vegetable cards.");

		// Add some wrong veggies
		hand.add(cabbageCard.copy());
		hand.add(lettuceCard.copy());

		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have 0 points for not having any carrot cards.");

		// Add some carrot cards
		for (int i = 0; i < 3; i++) {
			hand.add(carrotCard.copy());
			int expectedScore = (i + 1) * point;
			assertEquals(expectedScore, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + expectedScore + " points for having " + (i + 1) + " carrot cards.");
		}
	}


	@Test
	public void testMultipleVeggies() throws Exception {
		// Create a criterion with multiple vegetable types
		ArrayList<Vegetable> veggies = new ArrayList<>();
		veggies.add(Vegetable.CARROT);
		veggies.add(Vegetable.LETTUCE);
		ArrayList<Integer> points = new ArrayList<>();
		points.add(2);
		points.add(3);
		PointSaladPerVeggieCriterion criterion = new PointSaladPerVeggieCriterion(veggies, points);

		ArrayList<ICard> hand = new ArrayList<>();

		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have 0 points for not having any vegetable cards.");

		// Add some wrong veggies
		hand.add(cabbageCard.copy());
		hand.add(pepperCard.copy());

		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have 0 points for not having any carrot or lettuce cards.");

		int nbCarrot = 0;
		int nbLettuce = 0;
		// Add some carrot cards
		for (int i = 0; i < 3; i++) {
			hand.add(carrotCard.copy());
			nbCarrot++;
			int expectedScore = nbCarrot * points.get(0) + nbLettuce * points.get(1);
			assertEquals(expectedScore, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + expectedScore + " points for having " + nbCarrot + " carrot cards and " + nbLettuce + " lettuce cards.");
		}

		// Add some lettuce cards
		for (int i = 0; i < 3; i++) {
			hand.add(lettuceCard.copy());
			nbLettuce++;
			int expectedScore = nbCarrot * points.get(0) + nbLettuce * points.get(1);
			assertEquals(expectedScore, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + expectedScore + " points for having " + nbCarrot + " carrot cards and " + nbLettuce + " lettuce cards.");
		}
	}

	
	@Test
	public void testNegativePoints() throws Exception {
		// Create a criterion with multiple vegetable types and a negative score
		ArrayList<Vegetable> veggies = new ArrayList<>();
		veggies.add(Vegetable.CARROT);
		veggies.add(Vegetable.LETTUCE);
		veggies.add(Vegetable.ONION);
		ArrayList<Integer> points = new ArrayList<>();
		points.add(2);
		points.add(2);
		points.add(-4);
		PointSaladPerVeggieCriterion criterion = new PointSaladPerVeggieCriterion(veggies, points);

		ArrayList<ICard> hand = new ArrayList<>();

		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have 0 points for not having any vegetable cards.");

		// Add some wrong veggies
		hand.add(cabbageCard.copy());
		hand.add(tomatoCard.copy());

		assertEquals(0, criterion.computePlayerScore(hand, otherHands),
		"Player should have 0 points for not having any carrot or lettuce cards.");

		// Add one of each positive
		hand.add(carrotCard.copy());
		hand.add(lettuceCard.copy());

		assertEquals(points.get(0) + points.get(1), criterion.computePlayerScore(hand, otherHands),
		"Player should have " + (points.get(0) + points.get(1)) + " points for having one carrot card and one lettuce card.");

		int nbOnion = 0;
		// Add some onion cards
		for (int i = 0; i < 3; i++) {
			hand.add(onionCard.copy());
			nbOnion++;
			int expectedScore = points.get(0) + points.get(1) + nbOnion * points.get(2);
			assertEquals(expectedScore, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + expectedScore + " points for having 1 carrot card, 1 lettuce card, and " + nbOnion + " onion cards.");
		}
	}
	
	//? String representation test

	@Test
	public void testStringRepresentationPerVeggieTypeSingle() {
		// Create a criterion with a single vegetable type
		ArrayList<Vegetable> veggies = new ArrayList<>();
		veggies.add(Vegetable.CARROT);
		ArrayList<Integer> points = new ArrayList<>();
		points.add(2);
		PointSaladPerVeggieCriterion criterion = new PointSaladPerVeggieCriterion(veggies, points);
		String expected = "2 / CARROT";

		assertEquals(expected, criterion.toString(), "Criterion should be represented as '2 / CARROT'.");
	}

	@Test
	public void testStringRepresentationPerVeggieTypeDuo() {
		// Create a criterion with a duo vegetable type
		ArrayList<Vegetable> veggies = new ArrayList<>();
		veggies.add(Vegetable.LETTUCE);
		veggies.add(Vegetable.CARROT);
		ArrayList<Integer> points = new ArrayList<>();
		points.add(3);
		points.add(-2);
		PointSaladPerVeggieCriterion criterion = new PointSaladPerVeggieCriterion(veggies, points);
		String expected = "3 / LETTUCE, -2 / CARROT";

		assertEquals(expected, criterion.toString(), "Criterion should be represented as '3 / LETTUCE, -2 / CARROT'.");
	}

	@Test
	public void testStringRepresentationPerVeggieTypeTrio() {
		// Create a criterion with a duo vegetable type
		ArrayList<Vegetable> veggies = new ArrayList<>();
		veggies.add(Vegetable.CARROT);
		veggies.add(Vegetable.PEPPER);
		veggies.add(Vegetable.CABBAGE);
		ArrayList<Integer> points = new ArrayList<>();
		points.add(2);
		points.add(1);
		points.add(-2);
		PointSaladPerVeggieCriterion criterion = new PointSaladPerVeggieCriterion(veggies, points);
		String expected = "2 / CARROT, 1 / PEPPER, -2 / CABBAGE";

		assertEquals(expected, criterion.toString(), "Criterion should be represented as '2 / CARROT, 1 / PEPPER, -2 / CABBAGE'.");
	}
}
