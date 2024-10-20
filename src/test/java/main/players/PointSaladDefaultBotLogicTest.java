package main.players;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cards.ICard;
import cards.Pile;
import cards.PointSaladCard;
import cards.PointSaladCard.Vegetable;
import criteria.ICriterion;
import criteria.point_salad_criteria.PointSaladCompleteSetCriterion;
import criteria.point_salad_criteria.PointSaladFewestCriterion;
import criteria.point_salad_criteria.PointSaladMostCriterion;
import criteria.point_salad_criteria.PointSaladPerMissingVeggieTypeCriterion;
import criteria.point_salad_criteria.PointSaladPerVeggieCriterion;
import game.IMarket;
import game.PointSaladMarket;
import phases.IPhase;
import phases.PointSaladDraftingPhase;
import players.AbstractPlayer;
import players.IAPlayer;
import players.PointSaladDefaultBotLogic;
import states.State;

public class PointSaladDefaultBotLogicTest {

	private PointSaladCard carrotCard = new PointSaladCard(Vegetable.CARROT, null);
	private PointSaladCard cabbageCard = new PointSaladCard(Vegetable.CABBAGE, null);
	private PointSaladCard lettuceCard = new PointSaladCard(Vegetable.LETTUCE, null);
	private PointSaladCard pepperCard = new PointSaladCard(Vegetable.PEPPER, null);
	private PointSaladCard tomatoCard = new PointSaladCard(Vegetable.TOMATO, null);
	private PointSaladCard onionCard = new PointSaladCard(Vegetable.ONION, null);

	private PointSaladDefaultBotLogic botLogic;

	@BeforeEach
	public void setUpBotLogic() {
		botLogic = new PointSaladDefaultBotLogic();
	}
	
	// Not a before each because won't always be needed
	public IMarket getDummyMarket(ArrayList<ICard> criterionCards) {
		PointSaladMarket market = new PointSaladMarket();

		if (criterionCards != null) {
			if (criterionCards.size() < 3) {
				fail("Invalid criterion cards for the dummy market");
			}
	
			ArrayList<Pile<PointSaladCard>> criterionPiles = new ArrayList<Pile<PointSaladCard>>();
			for (int i=0; i<3; i++) {
				Pile<PointSaladCard> pile = new Pile<PointSaladCard>();
				PointSaladCard card = (PointSaladCard) criterionCards.get(i);
				if (card != null && !card.isCriterionSideUp())
				{
					card.flip();
				}
				pile.addCard(card);
				criterionPiles.add(pile);
			}
			market.setCriterionPiles(criterionPiles);
		}

		ArrayList<PointSaladCard> veggieCards = new ArrayList<PointSaladCard>();
		veggieCards.add(carrotCard);
		veggieCards.add(cabbageCard);
		veggieCards.add(lettuceCard);
		veggieCards.add(pepperCard);
		veggieCards.add(tomatoCard);
		veggieCards.add(onionCard);

		market.setVegetableCards(veggieCards);

		return market;
	}
	
	// Not a before each because won't always be needed
	public State getDummyDraftingState(int nbPlayers, IMarket market) {
		// We need the market and players to be correctly set a drafting phase

		HashMap<Integer, AbstractPlayer> players = new HashMap<Integer, AbstractPlayer>();
		for (int i=0; i<nbPlayers; i++) {
			players.put(i, new IAPlayer(i, "Player " + i, botLogic));
		}
		
		IPhase draftingPhase = new PointSaladDraftingPhase();

		return new State(null, players, -1, market, draftingPhase);
	}
	
	// Not a before each because won't always be needed
	public State getDummyFlippingState(ArrayList<ICard> hand) {
		// We only need players with their hand in a flipping phase


		HashMap<Integer, AbstractPlayer> players = new HashMap<Integer, AbstractPlayer>();
		AbstractPlayer player = new IAPlayer(0, "Player 0", botLogic);
		player.setHand(hand);
		players.put(0, player);

		AbstractPlayer otherPlayer = new IAPlayer(1, "Player 1", botLogic);
		ArrayList<ICard> otherHand = new ArrayList<ICard>(); // Empty hand
		otherPlayer.setHand(otherHand);
		players.put(1, otherPlayer);

		IPhase flippingPhase = new PointSaladDraftingPhase();

		return new State(null, players, 0, null, flippingPhase);
	}
	


	//? Testing methods


	@Test
	public void testDraftingChance() {
		// Testing the probability of drafting a criterion card

		int nbTries = 10000;
		double epsilon = .02;

		int nbCriterionDraft = 0;

		ArrayList<ICard> criterionCards = new ArrayList<ICard>();
		for (int i = 0; i < 3; i++) {
			ICriterion criterion = new PointSaladMostCriterion(Vegetable.values()[i], 7);
			criterionCards.add(new PointSaladCard(Vegetable.values()[i], criterion));
		}

		IMarket market = getDummyMarket(criterionCards);
		State state = getDummyDraftingState(3, market);

		// Do nbTries tries
		for (int i = 0; i < nbTries; i++) {
			try {
				String move = botLogic.getDraftingMove(state, 0);

				try {
					Integer.parseInt(move);
					nbCriterionDraft++;
				} catch (NumberFormatException e) {
					// Not a criterion draft
				}
			} catch (Exception e) {
				fail("An error occurred while getting the bot's drafting move: " + e.getMessage());
			}
		}

		double experimentalProbability = (double) nbCriterionDraft / nbTries;
		double diff = Math.abs(experimentalProbability - botLogic.getCriterionDraftChance());

		System.out.println("Found Experimental probability: " + experimentalProbability);

		assertTrue(diff < epsilon, "The bot's criterion draft chance is not correct for " + nbTries + " tries and epsilon " + epsilon +
		". Found a probability of " + experimentalProbability + " instead of " + botLogic.getCriterionDraftChance());
	}



	@Test
	public void testDrafting2Vegetable() {
		// Bot logic will always draft the two first vegetables in the market, or the first possible if there is only one.
		// Here it will therefore draft 'AB'

		// Get market with 6 Vegetable cards
		PointSaladMarket market = (PointSaladMarket) getDummyMarket(null);

		try {
			String move = botLogic.getVegetableDraft(market);

			assertTrue(move.equals("AB"), "The bot did not draft two vegetable card: " + move);
		} catch (Exception e) {
			fail("An error occurred while getting the bot's drafting move: " + e.getMessage());
		}
	}


	@Test
	public void testDrafting1Vegetable() {
		// Get a market with only 1 vegetable card, which will be in slot B.
		ArrayList<PointSaladCard> veggieCards = new ArrayList<PointSaladCard>();
		for (int i=0; i<6; i++) {
			veggieCards.add(null);
		}
		veggieCards.set(1, cabbageCard);

		PointSaladMarket market = (PointSaladMarket) getDummyMarket(null);
		market.setVegetableCards(veggieCards);

		try {
			String move = botLogic.getVegetableDraft(market);

			assertTrue(move.equals("B"), "The bot did not draft a single vegetable card: " + move);
		} catch (Exception e) {
			fail("An error occurred while getting the bot's drafting move: " + e.getMessage());
		}
	}


	@Test
	public void testFirstCriterionCard() {
		// When the bot has not cards, and the market has 3 criterion cards which do not give any points,
		// It will always draft the first criterion card.
		
		ArrayList<ICard> criterionCards = new ArrayList<ICard>();
		for (int i = 0; i < 3; i++) {
			ICriterion criterion = new PointSaladMostCriterion(Vegetable.values()[i], 7);
			criterionCards.add(new PointSaladCard(Vegetable.values()[i], criterion));
		}

		PointSaladMarket market = (PointSaladMarket) getDummyMarket(criterionCards);
		
		// Empty hands
		ArrayList<ICard> hand = new ArrayList<ICard>();
		ArrayList<ArrayList<ICard>> otherHands = new ArrayList<ArrayList<ICard>>();

		try {
			String move = botLogic.getCriterionDraft(hand, otherHands, market);

			assertTrue(move.equals("0"), "The bot did not draft the first criterion card: " + move);
		} catch (Exception e) {
			fail("An error occurred while getting the bot's drafting move: " + e.getMessage());
		}
	}


	@Test
	public void testBestCriterionCard() {
		// The bot will always draft the best criterion card, based on the current market and hands.
		// We can verify it by putting a PerMissingVeggieType criterion in the market, and giving everyone empty hands.
		
		ArrayList<ICard> criterionCards = new ArrayList<ICard>();
		for (int i = 0; i < 3; i++) {
			ICriterion criterion = new PointSaladMostCriterion(Vegetable.values()[i], 7);
			criterionCards.add(new PointSaladCard(Vegetable.values()[i], criterion));
		}
		criterionCards.set(1, new PointSaladCard(Vegetable.CABBAGE, new PointSaladPerMissingVeggieTypeCriterion(5)));


		PointSaladMarket market = (PointSaladMarket) getDummyMarket(criterionCards);
		
		// Empty hands
		ArrayList<ICard> hand = new ArrayList<ICard>();
		ArrayList<ArrayList<ICard>> otherHands = new ArrayList<ArrayList<ICard>>();

		try {
			String move = botLogic.getCriterionDraft(hand, otherHands, market);

			assertTrue(move.equals("1"), "The bot did not draft the criterion card that grants the most points: " + move);
		} catch (Exception e) {
			fail("An error occurred while getting the bot's drafting move: " + e.getMessage());
		}
	}

	


	@Test
	public void testFirstDraftCriterionCardAmongAvailable() {
		// If the market is partially empty, the bot will draft among the available criterion card.
		
		ArrayList<ICard> criterionCards = new ArrayList<ICard>();
		for (int i = 0; i < 3; i++) {
			criterionCards.add(null);
		}
		ICriterion criterion = new PointSaladMostCriterion(Vegetable.CARROT, 7);
		criterionCards.set(1, new PointSaladCard(Vegetable.CARROT, criterion));
		criterionCards.set(2, new PointSaladCard(Vegetable.CABBAGE, new PointSaladPerMissingVeggieTypeCriterion(5)));


		// Here, the market criterion cards are:
		// [Empty] [MOST CARROT = 7] [5 / MISSING VEGGIE TYPE]

		// The proposition to any player is then:
		// [0] MOST CARROT = 7		[1] 5 / MISSING VEGGIE TYPE

		// The best card is the last one, which is index 1 AMONG the available cards.
		PointSaladMarket market = (PointSaladMarket) getDummyMarket(criterionCards);
		
		// Empty hands
		ArrayList<ICard> hand = new ArrayList<ICard>();
		ArrayList<ArrayList<ICard>> otherHands = new ArrayList<ArrayList<ICard>>();

		try {
			String move = botLogic.getCriterionDraft(hand, otherHands, market);

			assertTrue(move.equals("1"), "The bot did not draft the best card among the available criterion card: " + move);
		} catch (Exception e) {
			fail("An error occurred while getting the bot's drafting move: " + e.getMessage());
		}
	}



	// Test flipping cards

	@Test
	public void testNoFlippngIfNoCriterion() {

		ArrayList<ICard> hand = new ArrayList<ICard>();
		hand.add(carrotCard);

		// Hand is here: CARROT

		State state = getDummyFlippingState(hand);

		try {
			String move = botLogic.getFlippingMove(state, 0);

			assertTrue(move.equals("n"), "The bot should not flip any card if it has no criterion card: " + move);
		} catch (Exception e) {
			fail("An error occurred while getting the bot's flipping move: " + e.getMessage());
		}
	}

	@Test
	public void testNoFlippingIfGoodCard() {
		// The bot will not flip a card if it has a good criterion card in its hand.

		ArrayList<ICard> hand = new ArrayList<ICard>();
		PointSaladCard criterion = new PointSaladCard(Vegetable.CARROT, new PointSaladFewestCriterion(Vegetable.CARROT, 7));
		criterion.flip(); // Put it on its criterion side
		hand.add(criterion);
		
		// Hand is here: FEWEST CARROT = 7 (CARROT)

		State state = getDummyFlippingState(hand);

		// Test when the card score points (7 here)
		try {
			String move = botLogic.getFlippingMove(state, 0);

			assertTrue(move.equals("n"), "The bot should not flip a criterion card if it makes it lose points: " + move);
		} catch (Exception e) {
			fail("An error occurred while getting the bot's flipping move: " + e.getMessage());
		}


		hand.add(carrotCard);
		state = getDummyFlippingState(hand);

		// Hand is here: FEWEST CARROT = 7 (CARROT), CARROT
		// And the other player's hand is empty

		// Test when the card scores no points (but not negative)
		try {
			String move = botLogic.getFlippingMove(state, 0);

			assertTrue(move.equals("n"), "The bot should not flip a criterion card if it does not make it score more points: " + move);
		} catch (Exception e) {
			fail("An error occurred while getting the bot's flipping move: " + e.getMessage());
		}
	}

	@Test
	public void testFlippingForMorePointsCase1() {
		// The bot will flip a card if it makes it score more points.

		ArrayList<ICard> hand = new ArrayList<ICard>();

		// Create criteria: "2 / CARROT" and "COMPLETE SET = 10"
		ArrayList<Vegetable> veggieTypes = new ArrayList<Vegetable>();
		veggieTypes.add(Vegetable.CARROT);
		ArrayList<Integer> points = new ArrayList<Integer>();
		points.add(2);
		PointSaladCard criterion1 = new PointSaladCard(Vegetable.ONION, new PointSaladPerVeggieCriterion(veggieTypes, points));
		criterion1.flip(); // Put it on its criterion side
		hand.add(criterion1);
		PointSaladCard criterion2 = new PointSaladCard(Vegetable.CARROT, new PointSaladCompleteSetCriterion(10));
		criterion2.flip(); // Put it on its criterion side
		hand.add(criterion2);

		State state = getDummyFlippingState(hand);

		// Hand is here: [2 / CARROT (ONION)], [10 / COMPLETE SET (CARROT)]
		// The bot would gain two points by flipping the second card.

		// The bot currently has no vegetable card, but could gain points by flipping its Complete Set criterion.
		try {
			String move = botLogic.getFlippingMove(state, 0);

			assertTrue(move.equals("1"), "The bot should flip the second card to score more points: " + move);
		} catch (Exception e) {
			fail("An error occurred while getting the bot's flipping move: " + e.getMessage());
		}
	}

	@Test
	public void testFlippingForMorePointsCase2() {
		// The bot will flip a card if it makes it score more points.

		ArrayList<ICard> hand = new ArrayList<ICard>();

		// Create criterion: "2 / CARROT, 2 / CABBAGE, -4 / ONION"
		ArrayList<Vegetable> veggieTypes = new ArrayList<Vegetable>();
		veggieTypes.add(Vegetable.CARROT);
		veggieTypes.add(Vegetable.CABBAGE);
		veggieTypes.add(Vegetable.ONION);
		ArrayList<Integer> points = new ArrayList<Integer>();
		points.add(2);
		points.add(2);
		points.add(-4);
		ICriterion criterion = new PointSaladPerVeggieCriterion(veggieTypes, points);

		PointSaladCard criterionCard = new PointSaladCard(Vegetable.CARROT, criterion);
		criterionCard.flip(); // Put it on its criterion side
		hand.add(criterionCard);
		hand.add(onionCard);

		State state = getDummyFlippingState(hand);

		// Hand is here: [2 / CARROT, 2 / CABBAGE, -4 / ONION (CARROT)], ONION

		// The bot currently has a ONION vegetable card, which makes it lose 4 points.
		// It could go back to 0 points by flipping its criterion card.
		try {
			String move = botLogic.getFlippingMove(state, 0);

			assertTrue(move.equals("0"), "The bot should flip its criterion card to score more points: " + move);
		} catch (Exception e) {
			fail("An error occurred while getting the bot's flipping move: " + e.getMessage());
		}
	}
}
