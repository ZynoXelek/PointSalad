package code.phases;

import code.cards.ICard;
import code.cards.ICardFactory;
import code.cards.Pile;
import code.cards.PointSaladFactory;
import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;
import code.exceptions.SetupException;
import code.game.IMarket;
import code.game.PointSaladMarket;
import code.players.AbstractPlayer;
import code.states.State;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Setup phase for the Point Salad game.
 */
public class PointSaladSetupPhase implements IPhase {

	// TODO: Make both of them customizable through game config (?)
	/** The default path used to the Point Salad cards json file. */
	public static final String DEFAULT_PATH = "src/resources/cards/PointSaladManifest.json";
	/** The number of each veggie card to be put in the deck, based on the number of players. */
	public static final HashMap<Integer, Integer> NB_EACH_VEGGIE = new HashMap<Integer, Integer>() {
		{
			put(2, 6);
			put(3, 9);
			put(4, 12);
			put(5, 15);
			put(6, 18);
		}
	};

	private String cardsPath;
	private ICardFactory cardFactory;

	/**
	 * Constructor for the PointSaladSetupPhase class.
	 * By default, uses the PointSaladFactory and the default path for the cards.
	 */
	public PointSaladSetupPhase() {
		this.cardFactory = new PointSaladFactory();
		this.cardsPath = DEFAULT_PATH;
	}

	/**
	 * Constructor for the PointSaladSetupPhase class.
	 * By default, uses the default path for the cards.
	 * 
	 * @param cardFactory The factory for the cards creation
	 */
	public PointSaladSetupPhase(ICardFactory cardFactory) {
		this.cardFactory = cardFactory;
		this.cardsPath = DEFAULT_PATH;
	}

	/**
	 * Constructor for the PointSaladSetupPhase class.
	 * By default, uses the PointSaladFactory.
	 * 
	 * @param cardsPath The path to the cards json file
	 */
	public PointSaladSetupPhase(String cardsPath) {
		this.cardFactory = new PointSaladFactory();
		this.cardsPath = cardsPath;
	}

	/**
	 * Constructor for the PointSaladSetupPhase class.
	 * 
	 * @param cardFactory The factory for the cards creation
	 * @param cardsPath The path to the cards json file
	 */
	public PointSaladSetupPhase(ICardFactory cardFactory, String cardsPath) {
		this.cardFactory = cardFactory;
		this.cardsPath = cardsPath;
	}

	/**
	 * Extracts the Vegetable cards from a list of cards and build several piles,
	 * each containing a single type of Vegetable cards.
	 * 
	 * @param cards The list of cards to extract the Vegetable cards from
	 * 
	 * @return The list of piles, each containing a single type of Vegetable cards
	 */
	public static ArrayList<Pile> extractVeggiePiles(ArrayList<ICard> cards) {
		ArrayList<Pile> piles = new ArrayList<Pile>();
		HashMap<Vegetable, Pile> veggiePiles = new HashMap<Vegetable, Pile>();
		
		for (Vegetable veggie : Vegetable.values()) {
			Pile pile = new Pile();
			veggiePiles.put(veggie, pile);
		}

		for (ICard card : cards) {
			PointSaladCard pointSaladCard = (PointSaladCard) card;
			Pile pile = veggiePiles.get(pointSaladCard.getVegetable());
			pile.addCard(card);
		}

		for (Vegetable veggie : Vegetable.values()) {
			Pile pile = veggiePiles.get(veggie);
			piles.add(pile);
		}

		return piles;
	}

	/**
	 * Extracts the Vegetable cards from a pile and build several piles,
	 * each containing a single type of Vegetable cards.
	 * 
	 * @param pile The pile to extract the Vegetable cards from
	 * 
	 * @return The list of piles, each containing a single type of Vegetable cards
	 */
	public static ArrayList<Pile> extractVeggiePiles(Pile pile) {
		
		return extractVeggiePiles(pile.getCards());
	}

	@Override
	public void processPhase(State state) throws SetupException {
		ArrayList<AbstractPlayer> players = state.getPlayers();
		IMarket market = state.getMarket();

		// Setup phase for Point Salad is preparing the market
		int nbPlayers = players.size();

		if (!NB_EACH_VEGGIE.containsKey(nbPlayers)) {
			throw new SetupException("Invalid number of players for Point Salad: " + nbPlayers);
		}

		int nbVeggieCards = NB_EACH_VEGGIE.get(nbPlayers);
		ArrayList<ICard> cards = this.cardFactory.loadCards(this.cardsPath);

		// Get piles containing cards of a single type of Vegetable
		ArrayList<Pile> veggiePiles = extractVeggiePiles(cards);

		for (Pile pile : veggiePiles) {
			// Remove the correct number of cards from each pile
			int removeNumber = pile.size() - nbVeggieCards;
			pile.draw(removeNumber);
		}

		// Prepare the initial deck
		Pile deck = new Pile().concatenates(veggiePiles);
		deck.shuffle();
		deck.flip();

		// Split it in three criteria piles
		ArrayList<Pile> criteriaPiles = deck.splitIn(PointSaladMarket.NUM_DRAW_PILES);

		// Put the final piles in the market
		PointSaladMarket pointSaladMarket = (PointSaladMarket) market;
		for (int i = 0; i < PointSaladMarket.NUM_DRAW_PILES; i++) {
			try {
				pointSaladMarket.setPile(i, criteriaPiles.get(i));
			} catch (Exception e) {
				// Print the error message in the terminal. May happen during testing. Will never happen once the project is completed.
				System.err.println("Index error at PointSaladSetupPhase.processPhase() with error message:\n" + 
								e.getMessage());
			}
		}

		pointSaladMarket.refillVegetables();
	}
}
