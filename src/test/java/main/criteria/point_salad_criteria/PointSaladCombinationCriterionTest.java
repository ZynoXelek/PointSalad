package main.criteria.point_salad_criteria;

import cards.ICard;
import cards.PointSaladCard;
import cards.PointSaladCard.Vegetable;
import criteria.point_salad_criteria.PointSaladCombinationCriterion;
import exceptions.CriterionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * This class is meant for testing the PointSaladCombinationCriterion class.
 */
public class PointSaladCombinationCriterionTest {

	private ArrayList<ArrayList<ICard>> otherHands;


	@BeforeEach
	public void setOtherHands() {
		// Ensure the otherHands are correctly set before each test
		// In this case, they are not required for this criterion
		otherHands = new ArrayList<>();
	}


	
	@Test
	public void testDoubleVegetable() throws CriterionException {
		ArrayList<Vegetable> vegetables = new ArrayList<>();
		vegetables.add(Vegetable.LETTUCE);
		vegetables.add(Vegetable.LETTUCE);

		int pointsPerCombination = 5;
		PointSaladCombinationCriterion criterion = new PointSaladCombinationCriterion(vegetables, pointsPerCombination);
		
		ArrayList<ICard> hand = new ArrayList<>();
		PointSaladCard carrot = new PointSaladCard(Vegetable.CARROT, null);
		PointSaladCard lettuce = new PointSaladCard(Vegetable.LETTUCE, null);
		hand.add(carrot.copy());

		// No combination
		assertEquals(0, criterion.computePlayerScore(hand, otherHands));

		for (int i = 0; i < 5; i++) {
			hand.add(lettuce.copy());
			int nbOfDoubles = (i + 1) / 2;
			assertEquals(nbOfDoubles * pointsPerCombination, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + nbOfDoubles * pointsPerCombination + " points for " + nbOfDoubles + " double(s) of lettuce.");
		}
	}



	@Test
	public void testPairVegetable() throws CriterionException {
		ArrayList<Vegetable> vegetables = new ArrayList<>();
		vegetables.add(Vegetable.LETTUCE);
		vegetables.add(Vegetable.CARROT);

		int pointsPerCombination = 5;
		PointSaladCombinationCriterion criterion = new PointSaladCombinationCriterion(vegetables, pointsPerCombination);
		
		ArrayList<ICard> hand = new ArrayList<>();
		PointSaladCard carrot = new PointSaladCard(Vegetable.CARROT, null);
		PointSaladCard lettuce = new PointSaladCard(Vegetable.LETTUCE, null);
		hand.add(carrot.copy());
		hand.add(carrot.copy());

		int nbCarrots = 2;

		// No combination
		assertEquals(0, criterion.computePlayerScore(hand, otherHands));

		for (int i = 0; i < 5; i++) {
			hand.add(lettuce.copy());
			int nbLettuces = i + 1;
			int nbOfCombinations = Math.min(nbLettuces, nbCarrots);
			assertEquals(nbOfCombinations * pointsPerCombination, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + nbOfCombinations * pointsPerCombination + " points for " + nbOfCombinations + " combination(s) of lettuce + carrot.");
		}
	}
	
	@Test
	public void testTripleVegetable() throws CriterionException {
		ArrayList<Vegetable> vegetables = new ArrayList<>();
		vegetables.add(Vegetable.LETTUCE);
		vegetables.add(Vegetable.LETTUCE);
		vegetables.add(Vegetable.LETTUCE);

		int pointsPerCombination = 7;
		PointSaladCombinationCriterion criterion = new PointSaladCombinationCriterion(vegetables, pointsPerCombination);
		
		ArrayList<ICard> hand = new ArrayList<>();
		PointSaladCard carrot = new PointSaladCard(Vegetable.CARROT, null);
		PointSaladCard lettuce = new PointSaladCard(Vegetable.LETTUCE, null);
		hand.add(carrot.copy());

		// No combination
		assertEquals(0, criterion.computePlayerScore(hand, otherHands));

		for (int i = 0; i < 7; i++) {
			hand.add(lettuce.copy());
			int nbOfTriples = (i + 1) / 3;
			assertEquals(nbOfTriples * pointsPerCombination, criterion.computePlayerScore(hand, otherHands),
			"Player should have " + nbOfTriples * pointsPerCombination + " points for " + nbOfTriples + " triple(s) of lettuce.");
		}
	}
	
	@Test
	public void testNotEnoughVegetable() {
		ArrayList<Vegetable> vegetables = new ArrayList<>();

		int pointsPerCombination = 7;
		assertThrows(CriterionException.class, () -> new PointSaladCombinationCriterion(vegetables, pointsPerCombination),
				"Should throw an exception if there are not enough vegetables in the combination.");

		vegetables.add(Vegetable.LETTUCE);
		assertThrows(CriterionException.class, () -> new PointSaladCombinationCriterion(vegetables, pointsPerCombination),
				"Should throw an exception if there are not enough vegetables in the combination.");

		vegetables.add(Vegetable.CARROT);
		assertDoesNotThrow(() -> new PointSaladCombinationCriterion(vegetables, pointsPerCombination),
				"Should not throw an exception if there are enough vegetables in the combination.");
	}

	//? String representation tests

	@Test
	public void testStringRepresentationDouble() throws CriterionException {
		// Double
		ArrayList<Vegetable> vegetables = new ArrayList<>();
		vegetables.add(Vegetable.CARROT);
		vegetables.add(Vegetable.CARROT);

		int pointsPerCombination = 5;
		PointSaladCombinationCriterion criterion = new PointSaladCombinationCriterion(vegetables, pointsPerCombination);
		
		assertEquals("CARROT + CARROT = 5", criterion.toString(),
				"String representation of the criterion should be 'CARROT + CARROT = 5'.");
	}



	@Test
	public void testStringRepresentationPair() throws CriterionException {
		// Pair
		ArrayList<Vegetable> vegetables = new ArrayList<>();
		vegetables.add(Vegetable.CABBAGE);
		vegetables.add(Vegetable.PEPPER);

		int pointsPerCombination = 5;
		PointSaladCombinationCriterion criterion = new PointSaladCombinationCriterion(vegetables, pointsPerCombination);
		
		assertEquals("CABBAGE + PEPPER = 5", criterion.toString(),
				"String representation of the criterion should be 'CARROT + CARROT = 5'.");
	}



	@Test
	public void testStringRepresentationTriple() throws CriterionException {
		// Triple
		ArrayList<Vegetable> vegetables = new ArrayList<>();
		vegetables.add(Vegetable.LETTUCE);
		vegetables.add(Vegetable.LETTUCE);
		vegetables.add(Vegetable.LETTUCE);

		int pointsPerCombination = 8;
		PointSaladCombinationCriterion criterion = new PointSaladCombinationCriterion(vegetables, pointsPerCombination);
		
		assertEquals("LETTUCE + LETTUCE + LETTUCE = 8", criterion.toString(),
				"String representation of the criterion should be 'LETTUCE + LETTUCE + LETTUCE = 8'.");
	}

	



	@Test
	public void testStringRepresentationThreeCombination() throws CriterionException {
		// Three combination
		ArrayList<Vegetable> vegetables = new ArrayList<>();
		vegetables.add(Vegetable.LETTUCE);
		vegetables.add(Vegetable.CARROT);
		vegetables.add(Vegetable.ONION);

		int pointsPerCombination = 8;
		PointSaladCombinationCriterion criterion = new PointSaladCombinationCriterion(vegetables, pointsPerCombination);
		
		assertEquals("LETTUCE + CARROT + ONION = 8", criterion.toString(),
				"String representation of the criterion should be 'LETTUCE + CARROT + ONION = 8'.");
	}
}
