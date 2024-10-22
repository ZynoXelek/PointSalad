

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cards.ICard;
import cards.ICardFactory;
import cards.Pile;
import cards.PointSaladCard;
import cards.PointSaladCardFactory;
import cards.PointSaladCard.Vegetable;
import criteria.ICriterion;
import criteria.point_salad_criteria.PointSaladCompleteSetCriterion;
import criteria.point_salad_criteria.PointSaladEvenOddCriterion;
import criteria.point_salad_criteria.PointSaladFewestTotalCriterion;
import criteria.point_salad_criteria.PointSaladMostCriterion;
import criteria.point_salad_criteria.PointSaladPerMissingVeggieTypeCriterion;
import exceptions.BotLogicException;
import exceptions.FlippingException;
import exceptions.MarketException;
import game.market.PointSaladMarket;
import main.Host.PointSaladHost;
import network.IServer;
import network.Server;
import phases.PointSaladDraftingPhase;
import phases.PointSaladFlippingPhase;
import phases.PointSaladScoringPhase;
import phases.PointSaladSetupPhase;
import players.AbstractPlayer;
import players.HumanPlayer;
import players.IAPlayer;
import players.IBotLogic;
import players.PointSaladDefaultBotLogic;
import states.State;
import tools.Config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is meant for testing the 14 PointSalad game requirements.
 */
public class RequirementsTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	private int minPlayers;
	private int maxPlayers;
	private Map<Integer, Integer> nbEachVeggie;
	private String cardsManifest;

	private ArrayList<ICard> cards;
	private PointSaladMarket market;

	private int nextPort = 0;

	public RequirementsTest() {
		nbEachVeggie = PointSaladSetupPhase.NB_EACH_VEGGIE;

		// Load config
		try {
			Config config = Config.getInstance();
			minPlayers = config.getInt("PS_minPlayers");
			maxPlayers = config.getInt("PS_maxPlayers");
			cardsManifest = config.getString("PS_cardsManifest");
		} catch (Exception e) {
			// Use default values if configuration loading fails
			minPlayers = 2;
			maxPlayers = 6;
			cardsManifest = "src/main/resources/PointSaladManifest.json";
		}
	}



	@BeforeEach
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}



	@AfterEach
	public void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}



	// Helper methods
	// These are not BeforeEach as we do not always need cards to be loaded, nor a server to be ready.

	public IServer prepareDummyServer() {
		try {
			// Avoid using the same port as a close server blocks the port for some time and prevent
			// other servers to be created with the same port.
			IServer server = new Server(nextPort++);
			return server;
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when creating a server: " + e.getMessage());
		}
		return null;
	}


	public void loadCards() {
		ICardFactory factory = new PointSaladCardFactory();
		try {
			cards = factory.loadCards(cardsManifest);
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when loading the cards: " + e.getMessage());
		}
	}



	public void setupMarket() {
		market = new PointSaladMarket();

		// Create a dummy setup state for this
		int nbPlayers = 4;
		HashMap<Integer, AbstractPlayer> players = new HashMap<>();
		for (int i = 0; i < nbPlayers; i++) {
			players.put(i, new HumanPlayer(i, "Player " + i));
		}

		PointSaladSetupPhase setupPhase = new PointSaladSetupPhase(cardsManifest);

		State dummyState = new State(null, players, -1, market, setupPhase);

		try {
			setupPhase.processPhase(dummyState);
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when processing the setup phase: " + e.getMessage());
		}

		// The market should now be ready
	}



	//? Requirement 1: The game must be able to host a game with a valid number of players.

	@Test
	public void testReq1NbPlayers() {
		// It depends on the config file.
		for (int i = minPlayers-1; i <= maxPlayers+1; i++) {
			if (i >= minPlayers && i <= maxPlayers) {
				assertTrue(PointSaladHost.isNumberOfPlayersValid(i), "Number of players " + i + " is invalid.");
			} else {
				assertFalse(PointSaladHost.isNumberOfPlayersValid(i), "Number of players " + i + " is valid.");
			}
		}
	}

	@Test
	public void testReq1CreatingHost() {
		// It depends on the config file.
		System.out.println(" ----------- Prints from the implementation of buildGame ----------- ");
		// We create games full of bots, just to test the creation of the host.
		for (int i = minPlayers - 1; i <= maxPlayers + 1; i++) {
			if (i >= minPlayers && i <= maxPlayers) {
				try {
					PointSaladHost host = new PointSaladHost(0, i, i);
					host.buildGame();
					assertEquals(i, host.getGameManager().getState().getPlayers().size(),
							"Invalid number of players in the game. Found " + host.getGameManager().getState().getPlayers().size() + " instead of " + i + ".");
				} catch (Exception e) {
					// Make test fail
					fail("Exception thrown when creating a host with " + i + " players and " + i + " bots: " + e.getMessage());
				}
			} else {
				final int players = i;
				assertThrows(IllegalArgumentException.class, () -> new PointSaladHost(0, players, players),
						"Exception not thrown when creating a host with " + players + " players");
			}
		}
		System.out.println(" -------------------------------------------------------------------- ");
	}

	//? Requirement 2:
	//? - The deck consists of 108 cards.
	//?	- A card has two sides: a vegetable and a criterion.
	//? - There are 6 vegetables.
	//? - There are 18 of each.

	@Test
	public void testReq2NbCards() {
		loadCards();
		assertEquals(108, cards.size(), "Invalid number of cards. Found " + cards.size() + " instead of 108.");
	}

	@Test
	public void testReq2CardSides() {
		loadCards();
		for (ICard card : cards) {
			assertTrue(card instanceof PointSaladCard, "Card " + card + " is not a PointSaladCard");
			PointSaladCard psCard = (PointSaladCard) card;
			assertNotNull(psCard.getVegetable(), "Vegetable side of the card " + card + " is null");
			assertNotNull(psCard.getCriterion(), "Criterion side of the card " + card + " is null");
		}
	}

	@Test
	public void testReq2NbVegetables() {
		assertEquals(6, Vegetable.values().length, "Invalid number of vegetables. Found " + Vegetable.values().length + " instead of 6.");
	}

	@Test
	public void testReq2NbEachVegetable() {
		loadCards();
		ArrayList<PointSaladCard> convertedCards = PointSaladCard.convertHand(cards);
		HashMap<Vegetable, Integer> nbVegetables = PointSaladCard.countVeggiesInHand(convertedCards);
		for (Vegetable veg : Vegetable.values()) {
			assertEquals(18, nbVegetables.get(veg), "Invalid number of " + veg + " cards. Found " + nbVegetables.get(veg) + " instead of 18.");
		}
	}

	//? Requirement 3: Decks should contain the right amount of cards when setting up the game.
	//? Moreover, removed cards should not be revealed.

	@Test
	public void testReq3NbCardsInGame() {
		loadCards();
		for (Integer i: nbEachVeggie.keySet()) {
			int nbPlayers = i;
			Pile<PointSaladCard> deck = PointSaladSetupPhase.getInitialDeck(cards, nbPlayers);
			
			int expectedNbCards = nbEachVeggie.get(i) * Vegetable.values().length;

			assertEquals(expectedNbCards, deck.size(), "Invalid number of cards in the initial deck for " + nbPlayers +
			" players. Found " + deck.size() + " instead of " + expectedNbCards + ".");
		}
	}


	// TODO: How to check the second part of this requirement? Thought of verifying that the `shuffleAndRemoveExtraCards()` method was of void type but I can't.
	// Should we check that a message is NOT sent to the players?

	//@Test
	//public void testReq3RemovedCardsNotRevealed() {
	//	// Create dummy veggiePiles
	//	ArrayList<Pile<PointSaladCard>> veggiePiles = new ArrayList<>();
	//	for (int i = 0; i < 6; i++) {
	//		Pile<PointSaladCard> pile = new Pile<>();
	//		for (int j = 0; j < 18; j++) {
	//			pile.addCard(new PointSaladCard(Vegetable.values()[i], null));
	//		}
	//		veggiePiles.add(pile);
	//	}

	// 	// Assert that we have no way to know which cards were removed
	//	assertNull(PointSaladSetupPhase.shuffleAndRemoveExtraCards(veggiePiles, 6));
	//}



	//? Requirement 4:
	//? - Deck should be shuffle before setting up the market.
	//? - The three piles should be roughly equal.
	//? - The drawing piles should be flipped to the criterion side.

	@Test
	public void testReq4InitialDeckShuffled() {
		loadCards();

		// Create a dummy non shuffled deck
		int nbPlayers = 2;
		int nbVeggieCards = nbEachVeggie.get(nbPlayers);

		// Get piles containing cards of a single type of Vegetable
		ArrayList<Pile<PointSaladCard>> veggiePiles = PointSaladCard.extractVeggiePiles(cards);
		// Shuffle the piles and removes extra cards
		PointSaladSetupPhase.shuffleAndRemoveExtraCards(veggiePiles, nbVeggieCards);

		// The dummy deck
		Pile<PointSaladCard> dummyDeck = new Pile<PointSaladCard>().concatenates(veggiePiles);

		Pile<PointSaladCard> initialDeck = PointSaladSetupPhase.getInitialDeck(cards, nbPlayers);

		// Assert that the decks are not the same
		assertNotEquals(dummyDeck, initialDeck, "Initial deck is not shuffled.");
	}

	@Test
	public void testReq4ThreeRoughlyEqualPiles() {
		loadCards();

		int nbPlayers = 2;

		// Get the initial deck
		Pile<PointSaladCard> initialDeck = PointSaladSetupPhase.getInitialDeck(cards, nbPlayers);

		// Split the deck in piles
		ArrayList<Pile<PointSaladCard>> criterionPiles = initialDeck.splitIn(PointSaladMarket.NUM_DRAW_PILES);

		assertEquals(3, criterionPiles.size(), "Invalid number of piles. Found " + criterionPiles.size() + " instead of 3.");

		ArrayList<Integer> sizes = new ArrayList<>();
		for (Pile<PointSaladCard> p: criterionPiles) {
			sizes.add(p.size());
		}

		for (int i = 0; i < sizes.size(); i++) {
			for (int j = i+1; j < sizes.size(); j++) {
				int diff = Math.abs(sizes.get(i) - sizes.get(j));
				assertTrue(diff <= 1, "Piles are not roughly equal. Found difference of " + diff + " between pile " + i + " and pile " + j + ".");
			}
		}
	}

	@Test
	public void testReq4FlippedPiles() {

		// Create dummy setup state
		int nbPlayers = 2;
		HashMap<Integer, AbstractPlayer> players = new HashMap<>();
		for (int i = 0; i < nbPlayers; i++) {
			players.put(i, new HumanPlayer(i, "Player " + i));
		}

		PointSaladMarket market = new PointSaladMarket();
		
		// Process the setup phase on this dummy state
		PointSaladSetupPhase setupPhase = new PointSaladSetupPhase(cardsManifest);

		State dummyState = new State(null, players, -1, market, setupPhase);

		try {
			setupPhase.processPhase(dummyState);

			// Market is modified in place
			ArrayList<Pile<PointSaladCard>> criterionPiles = market.getCriterionPiles();
			for (Pile<PointSaladCard> pile: criterionPiles) {
				for (PointSaladCard card: pile.getCards()) {
					assertTrue(card.isCriterionSideUp(), "Card " + card + " is not flipped.");
				}
			}
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when processing the setup phase: " + e.getMessage());
		}
	}

	//? Requirement 5: Two cards of each draw pile should be flipped to vegetable side.

	@Test
	public void testReq5TwoCardsFlipped() {
		// Create a dummy market
		loadCards();
		
		int nbPlayers = 2;
		Pile<PointSaladCard> initialDeck = PointSaladSetupPhase.getInitialDeck(cards, nbPlayers);
		ArrayList<Pile<PointSaladCard>> criterionPiles = initialDeck.splitIn(PointSaladMarket.NUM_DRAW_PILES);
		
		// Making copies of the piles, but not deep copies to keep the same cards references
		ArrayList<Pile<PointSaladCard>> copiedPiles = new ArrayList<>();
		for (Pile<PointSaladCard> pile: criterionPiles) {
			Pile<PointSaladCard> copiedPile = new Pile<>();
			copiedPile.getCards().addAll(pile.getCards());
			copiedPiles.add(copiedPile);
		}

		
		PointSaladMarket market = new PointSaladMarket();
		market.setCriterionPiles(criterionPiles);

		market.refill();

		ArrayList<PointSaladCard> vegetables = market.getVegetableCards();
		// Assert that every vegetable slot is filled, with a vegetable card
		for (int i = 0; i < vegetables.size(); i++) {
			Pile<PointSaladCard> cardOriginalPile = copiedPiles.get(i % PointSaladMarket.NUM_DRAW_PILES);
			int originalPilePosition = (cardOriginalPile.size()-1) - // Get top card
										i / PointSaladMarket.NUM_DRAW_PILES; // 0 or 1, to know if it was the top card, or the one right after 
			PointSaladCard marketCard = vegetables.get(i);

			assertFalse(marketCard.isCriterionSideUp(), "Vegetable card at position " + i + " is showing its criterion side.");

			PointSaladCard originalCard = cardOriginalPile.getCards().get(originalPilePosition);

			// Assert the card comes from the right pile
			assertEquals(marketCard, originalCard, "Vegetable card at position " + i + " does not come from the right pile.");
		}
	}

	//? Requirement 6: Start player should be randomly chosen.

	@Test
	public void testReq6RandomStartPlayer() {
		// Creating a dummy setup state
		int nbPlayers = 4;

		HashMap<Integer, AbstractPlayer> players = new HashMap<>();
		for (int i = 0; i < nbPlayers; i++) {
			players.put(i, new HumanPlayer(i, "Player " + i));
		}

		PointSaladSetupPhase setupPhase = new PointSaladSetupPhase(cardsManifest);

		IServer server = prepareDummyServer();

		State dummyState = new State(server, players, -1, null, setupPhase);

		// Try to proceed to next phase and look at the player turn a maximum of 20 times.
		// If we always get the same number, then it fails.
		int playerTurn = -1;
		int lastPlayerTurn = -1;
		int tryIndex = 0;
		int maxTries = 20;
		try {
			setupPhase.proceedToNextPhase(dummyState);
			playerTurn = dummyState.getPlayerTurnIndex();

			while ((lastPlayerTurn == -1 || playerTurn == lastPlayerTurn) && tryIndex < maxTries) {
				lastPlayerTurn = playerTurn;
				// Go back to setup phase and generate another turn index
				dummyState.setPlayerTurnIndex(-1);
				dummyState.setPhase(setupPhase);

				setupPhase.proceedToNextPhase(dummyState);
				
				playerTurn = dummyState.getPlayerTurnIndex();
				tryIndex++;
			}

			System.out.println("Tried " + (tryIndex+1) + " times to find a different player turn index.");
			System.out.println("Ended up finding player turn index: " + playerTurn + " while last player turn index was " + lastPlayerTurn);

			assertNotEquals(tryIndex, maxTries, "Could not find a different player turn index after " + maxTries + " tries.");

		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when proceeding to the next phase: " + e.getMessage());
		}
	}

	//? Requirement 7: On a player's turn, one should be able to either:
	//? - draft a vegetable card.
	//? - draft two vegetable cards.
	//? - draft a criterion card.

	@Test
	public void testReq7DraftingValidCriterion() {
		// Create a dummy market
		setupMarket();

		// Assert a valid criterion works
		try {
			ArrayList<ICard> draftedCards = market.draftCards("0");
			assertEquals(1, draftedCards.size(), "Drafting a criterion card should return a single card.");
			PointSaladCard draftedCard = (PointSaladCard) draftedCards.get(0);
			assertNotNull(draftedCard, "Drafted card should not be null.");
			assertTrue(draftedCard.isCriterionSideUp(), "Drafted card should be a criterion card.");
		} catch (MarketException e) {
			// Make test fail
			fail("Drafting a card with a valid index should not throw an exception.");
		}
	}

	@Test
	public void testReq7DraftingValidVegetables() {
		// Create a dummy market
		setupMarket();

		// Assert a single valid vegetable works
		try {
			ArrayList<ICard> draftedCards = market.draftCards("A");
			assertEquals(1, draftedCards.size(), "Drafting a vegetable card should return a single card.");
			PointSaladCard draftedCard = (PointSaladCard) draftedCards.get(0);
			assertNotNull(draftedCard, "Drafted card should not be null.");
			assertFalse(draftedCard.isCriterionSideUp(), "Drafted card should be a vegetable card.");
		} catch (MarketException e) {
			// Make test fail
			fail("Drafting a card with a valid index should not throw an exception.");
		}


		// Create a dummy market
		setupMarket();


		// Assert two vegetable works
		try {
			ArrayList<ICard> draftedCards = market.draftCards("BD");
			assertEquals(2, draftedCards.size(), "Drafting two vegetable cards should return two cards.");

			PointSaladCard draftedCard1 = (PointSaladCard) draftedCards.get(0);
			assertNotNull(draftedCard1, "Drafted card should not be null.");
			assertFalse(draftedCard1.isCriterionSideUp(), "Drafted card should be a vegetable card.");
			
			PointSaladCard draftedCard2 = (PointSaladCard) draftedCards.get(1);
			assertNotNull(draftedCard2, "Drafted card should not be null.");
			assertFalse(draftedCard2.isCriterionSideUp(), "Drafted card should be a vegetable card.");
		} catch (MarketException e) {
			// Make test fail
			fail("Drafting a card with a valid index should not throw an exception.");
		}
	}

	@Test
	public void testReq7DraftingInvalidCriteria() {
		// Create a dummy market
		setupMarket();

		// Assert an invalid criterion fails
		assertThrows(MarketException.class,
					() -> market.draftCards("-1"),
					"Drafting a card with an invalid index should throw an exception.");
		
		
		// Get a clean market
		setupMarket();
		
		// Assert an invalid criterion fails
		assertThrows(MarketException.class,
					() -> market.draftCards(Integer.toString(PointSaladMarket.NUM_DRAW_PILES + 1)),
					"Drafting a card with an invalid index should throw an exception.");
		
		
		// Get a clean market
		setupMarket();
		
		// Assert no draft fails
		assertThrows(MarketException.class,
					() -> market.draftCards(""),
					"Drafting no card should throw an exception.");
		
		
		// Get a clean market
		setupMarket();
		
		// Assert drafting two criteria fails
		assertThrows(MarketException.class,
					() -> market.draftCards("13"),
					"Drafting two cards should throw an exception.");
	}

	@Test
	public void testReq7DraftingInvalidVegetable() {
		// Create a dummy market
		setupMarket();

		// Assert an invalid vegetable fails
		assertThrows(MarketException.class,
					() -> market.draftCards("G"),
					"Drafting a card with an invalid index should throw an exception.");
		
		
		// Get a clean market
		setupMarket();
		
		// Assert an invalid vegetable fails
		assertThrows(MarketException.class,
					() -> market.draftCards("Z"),
					"Drafting a card with an invalid index should throw an exception.");
		
		
		// Get a clean market
		setupMarket();
		
		// Assert no draft fails
		assertThrows(MarketException.class,
					() -> market.draftCards(""),
					"Drafting no card should throw an exception.");
		
		
		// Get a clean market
		setupMarket();
		
		// Assert drafting three vegetable fails
		assertThrows(MarketException.class,
					() -> market.draftCards("ABC"),
					"Drafting two cards should throw an exception.");
	}

	//? Requirement 8: A player may decide to turn a point card into a vegetable card.

	@Test
	public void testReq8TurnCriterionIntoVegetable() {
		// Create a dummy Bot logic to test the flipping phase

		class DummyBotLogic implements IBotLogic {

			private String command;
			private int timesAdked = 0;

			public DummyBotLogic(String command) {
				this.command = command;
			}

			public void setCommand(String command) {
				this.command = command;
			}

			@Override
			public String getMove(State state, int botPlayerId) throws BotLogicException {
				timesAdked += 1;
				if (timesAdked > 10) {
					throw new BotLogicException("Too many times asked for a command.");
				}
				//System.out.println("Bot asked " + timesAdked + " times.");
				return command;
			}
		}

		//* Create a dummy flipping state

		// first create a dummy hand
		ICriterion criterion1 = new PointSaladCompleteSetCriterion(10);
		ICriterion criterion2 = new PointSaladEvenOddCriterion(Vegetable.CARROT, 7, 3);
		PointSaladCard card1 = new PointSaladCard(Vegetable.CABBAGE, criterion1);
		PointSaladCard card2 = new PointSaladCard(Vegetable.LETTUCE, criterion2);
		if (!card1.isCriterionSideUp()) {
			card1.flip();
		}
		if (!card2.isCriterionSideUp()) {
			card2.flip();
		}
		ArrayList<ICard> hand = new ArrayList<>();
		hand.add(card1);
		hand.add(card2);

		// Then a dummy player
		DummyBotLogic botLogic = new DummyBotLogic("n");
		AbstractPlayer player = new IAPlayer(0, "Bot", botLogic);
		player.setHand(hand);
		HashMap<Integer, AbstractPlayer> players = new HashMap<>();
		players.put(0, player);

		PointSaladFlippingPhase flippingPhase = new PointSaladFlippingPhase();

		IServer server = prepareDummyServer();

		State dummyState = new State(server, players, 0, null, flippingPhase);


		// Try to avoid flipping
		try {
			flippingPhase.processPhase(dummyState);

			// Assert the card was NOT flipped
			PointSaladCard flippedCard = (PointSaladCard) player.getHand().get(0);
			assertTrue(flippedCard.isCriterionSideUp(), "Card should not have been flipped.");
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when processing the flipping phase: " + e.getMessage());
		}

		dummyState.setPhase(flippingPhase);
		botLogic.setCommand("0");

		// Try to process the flipping phase
		try {
			flippingPhase.processPhase(dummyState);

			// Assert the card was flipped
			PointSaladCard flippedCard = (PointSaladCard) player.getHand().get(0);
			assertFalse(flippedCard.isCriterionSideUp(), "Flipped card should be a vegetable card.");
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when processing the flipping phase: " + e.getMessage());
		}

		// Try an invalid flipping command, with a non empty criteria hand (so that the bot is actually asked if it wants to flip)
		// Now that the card is flipped, the same "0" command would fail.
		// This will ask the bot again, which will once again try to flip an invalid card.
		// Once it reach 10 times, it will throw an exception, meaning that the command was invalid.
		dummyState.setPhase(flippingPhase);
		botLogic.setCommand("3");
		
		assertThrows(FlippingException.class, () -> flippingPhase.processPhase(dummyState),
					"Flipping an invalid card should throw an exception.");
	}

	//? Requirement 9: Other players should see the hand of the current player.

	@Test
	public void testReq9ShowHand() {
		// Create a dummy flipping state to test the end of a player's turn

		// first create hands
		ICriterion criterion1 = new PointSaladCompleteSetCriterion(10);
		PointSaladCard card1 = new PointSaladCard(Vegetable.CABBAGE, criterion1);
		card1.flip();
		PointSaladCard card2 = new PointSaladCard(Vegetable.LETTUCE, null);
		PointSaladCard card3 = new PointSaladCard(Vegetable.CARROT, null);

		ArrayList<ICard> hand0 = new ArrayList<>();
		ArrayList<ICard> hand1 = new ArrayList<>();
		hand1.add(card1);
		hand1.add(card2);
		hand1.add(card3);

		// Create a dummy market which is not empty
		setupMarket();

		// Create dummy players
		AbstractPlayer player0 = new HumanPlayer(0, "Player 0");
		AbstractPlayer player1 = new IAPlayer(1, "Player 1", new PointSaladDefaultBotLogic());

		player0.setHand(hand0);
		player1.setHand(hand1);

		HashMap<Integer, AbstractPlayer> players = new HashMap<>();
		players.put(1, player1);
		players.put(0, player0);

		PointSaladFlippingPhase flippingPhase = new PointSaladFlippingPhase();

		IServer server = prepareDummyServer();

		State dummyState = new State(server, players, 1, market, flippingPhase);



		// Try to process the flipping phase
		try {
			flippingPhase.processPhase(dummyState);

			String prints = outContent.toString();

			// Assert the hand of the player was shown
			assertTrue(prints.contains("Player 1") && prints.contains("hand"),
						"Player 1's hand should have been shown.");
			assertTrue(prints.contains("CABBAGE"),
						"Player 1's hand should have been shown.");
			assertTrue(prints.contains("LETTUCE"),
						"Player 1's hand should have been shown.");
			assertTrue(prints.contains("CARROT"),
						"Player 1's hand should have been shown.");
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when processing the flipping phase: " + e.getMessage());
		}
	}


	//? Requirement 10: The market should be refilled with the missing vegetables

	@Test
	public void testReq10MarketRefill() {
		// Create a dummy market
		setupMarket();

		// Draw 2 vegetables
		try {
			market.draftCards("AB");
		} catch (MarketException e) {
			// Make test fail
			fail("Drafting two cards should not throw an exception.");
		}

		// Get the two top cards that should be used to refill the vegetables
		ArrayList<Pile<PointSaladCard>> criterionPiles = market.getCriterionPiles();
		PointSaladCard topCard1 = criterionPiles.get(0).getTopCard();
		PointSaladCard topCard2 = criterionPiles.get(1).getTopCard();

		ArrayList<PointSaladCard> vegetables = market.getVegetableCards();
		PointSaladCard missingCard1 = vegetables.get(0);
		PointSaladCard missingCard2 = vegetables.get(1);

		assertNull(missingCard1, "First drafted card location in the market should be null.");
		assertNull(missingCard2, "Second drafted card location in the market should be null.");

		// Refill the market
		market.refill();

		PointSaladCard newCard1 = vegetables.get(0);
		PointSaladCard newCard2 = vegetables.get(1);

		assertEquals(newCard1, topCard1, "Top card of pile 1 should have become the first replacing vegetable");
		assertEquals(newCard2, topCard2, "Top card of pile 2 should have become the second replacing vegetable");

		assertFalse(newCard1.isCriterionSideUp(), "First replacing vegetable should be vegetable side up.");
		assertFalse(newCard1.isCriterionSideUp(), "First replacing vegetable should be vegetable side up.");

		PointSaladCard newTopCard1 = criterionPiles.get(0).getTopCard();
		PointSaladCard newTopCard2 = criterionPiles.get(1).getTopCard();

		assertNotEquals(newTopCard1, topCard1, "Top card of pile 1 should have been removed.");
		assertNotEquals(newTopCard2, topCard2, "Top card of pile 2 should have been removed.");
	}

	//? Requirement 11: If a draw pile runs out of cards, it should be refilled by half the cards of the most filled pile.

	@Test
	public void testReq11PileRefill() {
		// Create a dummy market
		setupMarket();

		// Make the first pile run out of cards
		ArrayList<Pile<PointSaladCard>> criterionPiles = market.getCriterionPiles();
		Pile<PointSaladCard> pile1 = criterionPiles.get(0);
		pile1.draw(pile1.size());

		// Draw 3 cards of pile 2 to make pile 3 have the most cards
		Pile<PointSaladCard> pile2 = criterionPiles.get(1);
		pile2.draw(3);

		// Get pre-refill data
		Pile<PointSaladCard> pile3 = criterionPiles.get(2);
		int pile1Size = pile1.size();
		int pile2Size = pile2.size();
		int pile3Size = pile3.size();

		int pile3HalfSize = pile3Size / 2;

		PointSaladCard bottomCard3 = pile3.getCards().get(0);

		assertEquals(0, pile1Size, "Pile 1 should be empty.");

		// Balance piles
		market.balancePiles();


		// Update new piles
		pile1 = criterionPiles.get(0);
		pile2 = criterionPiles.get(1);
		pile3 = criterionPiles.get(2);

		// Get post-refill data
		int newPile1Size = pile1.size();
		int newPile2Size = pile2.size();
		int newPile3Size = pile3.size();

		int diff1 = Math.abs(newPile1Size - pile3HalfSize);
		int diff3 = Math.abs(newPile3Size - pile3HalfSize);

		PointSaladCard newBottomCard3 = pile3.getCards().get(0);
		PointSaladCard newBottomCard1 = pile1.getCards().get(0);

		assertEquals(newPile2Size, pile2Size, "Pile 2 should not have been modified.");
		assertTrue(diff1 <= 1, "Pile 1 should have been refilled by roughly half the size of pile 3.");
		assertTrue(diff3 <= 1, "Pile 3 should have been cut by roughly half its size.");

		assertNotEquals(newBottomCard3, bottomCard3, "Bottom of pile 3 should have been used to refill pile1. Bottom card 3 should have changed.");
		assertEquals(newBottomCard1, bottomCard3, "Bottom of pile 3 should have been used to refill pile1. Bottom card 1 should be the same as the old bottom card 3.");
	}

	//? Requirement 12: Keep going as long as there are cards in the market.

	@Test
	public void testReq12GoToFlippingState() {
		// In order to check if the turns go on as they should, we will check if the drafting phase leads to the flipping phase.
		// To do so, we do not need to initialize properly everything in the state.

		PointSaladDraftingPhase draftingPhase = new PointSaladDraftingPhase();

		State state = new State(null, null, 0, null, draftingPhase);

		// Process the phase transition
		try {
			draftingPhase.proceedToNextPhase(state);
			assertEquals(PointSaladFlippingPhase.class, state.getPhase().getClass(), "Next phase should be a flipping phase.");
			assertEquals(0, state.getPlayerTurnIndex(), "Player turn should not have changed.");
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when proceeding to the next phase: " + e.getMessage());
		}
	}

	@Test
	public void testReq12GoToNextTurn() {
		// Create a dummy market
		setupMarket();

		// The market is not empty.
		assertFalse(market.isEmpty(), "Market should not be empty.");

		// Create a dummy flipping state, which is the one determining the next phase on turn end:
		// It is either a new drafting phase for the next player, or the scoring phase if the market is empty.
		// In this case, it should therefore be a new drafting phase for the next player.
		PointSaladFlippingPhase flippingPhase = new PointSaladFlippingPhase();

		int nbPlayers = 3;
		HashMap<Integer, AbstractPlayer> players = new HashMap<>();
		for (int i = 0; i < nbPlayers; i++) {
			players.put(i, new HumanPlayer(i, "Player " + i));
		}

		State state = new State(null, players, 0, market, flippingPhase);

		// Process the phase transition
		try {
			flippingPhase.proceedToNextPhase(state);
			assertEquals(PointSaladDraftingPhase.class, state.getPhase().getClass(), "Next phase should be a drafting phase, not the scoring one.");
			assertEquals(1, state.getPlayerTurnIndex(), "Player turn should have changed to the next player.");
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when proceeding to the next phase: " + e.getMessage());
		}
	}

	@Test
	public void testReq12GoToScoring() {
		// Create an empty dummy market
		PointSaladMarket dummyMarket = new PointSaladMarket();

		assertTrue(dummyMarket.isEmpty(), "Market should be empty.");

		// Create a dummy flipping state, which is the one determining the next phase on turn end:
		// It is either a new drafting phase for the next player, or the scoring phase if the market is empty.
		// In this case, it should therefore be the scoring phase.

		PointSaladFlippingPhase flippingPhase = new PointSaladFlippingPhase();

		int nbPlayers = 3;
		HashMap<Integer, AbstractPlayer> players = new HashMap<>();
		for (int i = 0; i < nbPlayers; i++) {
			players.put(i, new HumanPlayer(i, "Player " + i));
		}

		State state = new State(null, players, 0, dummyMarket, flippingPhase);

		// Process the phase transition
		try {
			flippingPhase.proceedToNextPhase(state);
			assertEquals(PointSaladScoringPhase.class, state.getPhase().getClass(), "Next phase should be a scoring phase, not the drafting one.");
			assertEquals(-1, state.getPlayerTurnIndex(), "It is not any player's turn anymore.");
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when proceeding to the next phase: " + e.getMessage());
		}
	}

	//? Requirement 13: Compute the score of each player.
	//! Please look at the test/java/main/criteria/point_salad_criteria to see the scoring tests of each criterion individually.

	@Test
	public void testReq13ScoreEachPlayer() {
		// Testing that a score is given to each player
		// Create a dummy scoring phase
		PointSaladScoringPhase scoringPhase = new PointSaladScoringPhase();

		IServer server = prepareDummyServer();

		int nbPlayers = 3;
		HashMap<Integer, AbstractPlayer> players = new HashMap<>();
		for (int i = 0; i < nbPlayers; i++) {
			// Use bot players to avoid sending server messages
			players.put(i, new IAPlayer(i, "Player " + i, null));
		}

		// Create some dummy hands
		ArrayList<ICard> hand1 = new ArrayList<>();
		ArrayList<ICard> hand2 = new ArrayList<>();
		ArrayList<ICard> hand3 = new ArrayList<>();

		// Criterion dummy cards (we do not care about its vegetable side)
		PointSaladCard card1 = new PointSaladCard(Vegetable.CABBAGE, new PointSaladMostCriterion(Vegetable.CARROT, 7));
		card1.flip();
		PointSaladCard card2 = new PointSaladCard(Vegetable.CABBAGE, new PointSaladFewestTotalCriterion(10));
		card2.flip();
		PointSaladCard card3 = new PointSaladCard(Vegetable.CABBAGE, new PointSaladEvenOddCriterion(Vegetable.LETTUCE, 7, 3));
		card3.flip();
		PointSaladCard card4 = new PointSaladCard(Vegetable.CABBAGE, new PointSaladPerMissingVeggieTypeCriterion(5));
		card4.flip();

		// Vegetables dummy cards (we do not care about its criterion side)
		PointSaladCard carrotCard = new PointSaladCard(Vegetable.CARROT, new PointSaladCompleteSetCriterion(1));
		PointSaladCard lettuceCard = new PointSaladCard(Vegetable.LETTUCE, new PointSaladCompleteSetCriterion(1));

		// Fill the hands
		hand1.add(card1);
		hand1.add(carrotCard.copy());
		hand1.add(carrotCard.copy());
		hand1.add(carrotCard.copy());
		hand1.add(card4);

		hand2.add(card2);

		hand3.add(card3);
		hand3.add(lettuceCard.copy());
		hand3.add(lettuceCard.copy());

		// Setting the hands
		players.get(0).setHand(hand1);
		players.get(1).setHand(hand2);
		players.get(2).setHand(hand3);

		State state = new State(server, players, -1, null, scoringPhase);

		// Player's scores should be 0 at the beginning
		for (int i = 0; i < nbPlayers; i++) {
			assertEquals(0, players.get(i).getScore(), "Player " + i + " should have a score of 0 before the scoring phase.");
		}

		// Process the phase
		try {
			scoringPhase.processPhase(state);
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when processing the scoring phase: " + e.getMessage());
		}

		// Player's scores should have been updated
		assertEquals(7+5*5, players.get(0).getScore(), "Player 0 should have a score of 32 after the scoring phase.");
		assertEquals(10, players.get(1).getScore(), "Player 1 should have a score of 10 after the scoring phase.");
		assertEquals(7, players.get(2).getScore(), "Player 2 should have a score of 7 after the scoring phase.");
	}

	//? Requirement 14: Announcing the winner
	
	@Test
	public void testReq14WinnerAnnouncement() {
		// We use the same test as the previous one, but we will check the winner announcement at the end of the scoring phase.
		PointSaladScoringPhase scoringPhase = new PointSaladScoringPhase();

		IServer server = prepareDummyServer();

		int nbPlayers = 3;
		HashMap<Integer, AbstractPlayer> players = new HashMap<>();
		for (int i = 0; i < nbPlayers; i++) {
			// Use bot players to avoid sending server messages
			players.put(i, new IAPlayer(i, "Player " + i, null));
		}

		// Create some dummy hands
		ArrayList<ICard> hand1 = new ArrayList<>();
		ArrayList<ICard> hand2 = new ArrayList<>();
		ArrayList<ICard> hand3 = new ArrayList<>();

		// Criterion dummy cards (we do not care about its vegetable side)
		PointSaladCard card1 = new PointSaladCard(Vegetable.CABBAGE, new PointSaladMostCriterion(Vegetable.CARROT, 7));
		card1.flip();
		PointSaladCard card2 = new PointSaladCard(Vegetable.CABBAGE, new PointSaladFewestTotalCriterion(10));
		card2.flip();
		PointSaladCard card3 = new PointSaladCard(Vegetable.CABBAGE, new PointSaladEvenOddCriterion(Vegetable.LETTUCE, 7, 3));
		card3.flip();
		PointSaladCard card4 = new PointSaladCard(Vegetable.CABBAGE, new PointSaladPerMissingVeggieTypeCriterion(5));
		card4.flip();

		// Vegetables dummy cards (we do not care about its criterion side)
		PointSaladCard carrotCard = new PointSaladCard(Vegetable.CARROT, new PointSaladCompleteSetCriterion(1));
		PointSaladCard lettuceCard = new PointSaladCard(Vegetable.LETTUCE, new PointSaladCompleteSetCriterion(1));

		// Fill the hands
		hand1.add(card1);
		hand1.add(carrotCard.copy());
		hand1.add(carrotCard.copy());
		hand1.add(carrotCard.copy());
		hand1.add(card4);

		hand2.add(card2);

		hand3.add(card3);
		hand3.add(lettuceCard.copy());
		hand3.add(lettuceCard.copy());

		// Setting the hands
		players.get(0).setHand(hand1);
		players.get(1).setHand(hand2);
		players.get(2).setHand(hand3);

		State state = new State(server, players, -1, null, scoringPhase);

		// Process the phase
		try {
			scoringPhase.processPhase(state);

			String prints = outContent.toString();

			// Assert the winning player has been announced
			assertTrue(prints.contains("Player 0") && prints.contains("winner") && prints.contains("score") && prints.contains("32"),
						"The winning player should be announced, with its score.");
		} catch (Exception e) {
			// Make test fail
			fail("Exception thrown when processing the scoring phase: " + e.getMessage());
		}
	}
}
