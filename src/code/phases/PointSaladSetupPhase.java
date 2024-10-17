package code.phases;

import code.cards.ICard;
import code.cards.ICardFactory;
import code.cards.Pile;
import code.cards.PointSaladCardFactory;
import code.cards.PointSaladCard;
import code.cards.PointSaladCard.Vegetable;
import code.exceptions.ConfigException;
import code.exceptions.SetupException;
import code.game.Config;
import code.game.IMarket;
import code.game.PointSaladMarket;
import code.players.AbstractPlayer;
import code.states.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Setup phase for the Point Salad game.
 */
public class PointSaladSetupPhase implements IPhase {

	/** The default path used to the Point Salad cards json file. */
	public static final String DEFAULT_PATH;
	/** The number of each veggie card to be put in the deck, based on the number of players. */
	public static final Map<Integer, Integer> NB_EACH_VEGGIE;

	static {
		// Load final variables from the configuration file

		int minPlayers = 2; // Default value
		int maxPlayers = 6; // Default value
		String defaultPath = "src/resources/cards/PointSaladManifest.json";
        int nbCardsPerPlayer = 18; // Default value
        Map<Integer, Integer> nbEachVeggieMap = new HashMap<>();

        try {
            Config config = Config.getInstance();
			minPlayers = config.getInt("PS_minPlayers");
			maxPlayers = config.getInt("PS_maxPlayers");
            defaultPath = config.getString("PS_cardsManifest");
            nbCardsPerPlayer = config.getInt("PS_nbCardsPerPlayer");
        } catch (ConfigException e) {
            e.printStackTrace();
            // Use default values if configuration loading fails
        }

        DEFAULT_PATH = defaultPath;

		int nbVeggies = Vegetable.values().length;
        // Calculate the number of each veggie card based on the number of players
        for (int players = minPlayers; players <= maxPlayers; players++) {
            int nbEachVeggie = (players * nbCardsPerPlayer) / nbVeggies;
            nbEachVeggieMap.put(players, nbEachVeggie);
        }

        NB_EACH_VEGGIE = Collections.unmodifiableMap(nbEachVeggieMap);
	}

	private String cardsPath;
	private ICardFactory cardFactory;

	/**
	 * Constructor for the PointSaladSetupPhase class.
	 * By default, uses the PointSaladFactory and the default path for the cards.
	 */
	public PointSaladSetupPhase() {
		this.cardFactory = new PointSaladCardFactory();
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
		this.cardFactory = new PointSaladCardFactory();
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
	public static ArrayList<Pile<PointSaladCard>> extractVeggiePiles(ArrayList<ICard> cards) {
		ArrayList<PointSaladCard> pointSaladCards = PointSaladCard.convertHand(cards);

		ArrayList<Pile<PointSaladCard>> piles = new ArrayList<Pile<PointSaladCard>>();
		HashMap<Vegetable, Pile<PointSaladCard>> veggiePiles = new HashMap<Vegetable, Pile<PointSaladCard>>();

		for (Vegetable veggie : Vegetable.values()) {
			Pile<PointSaladCard> pile = new Pile<PointSaladCard>();
			veggiePiles.put(veggie, pile);
		}

		for (PointSaladCard card : pointSaladCards) {
			Pile<PointSaladCard> pile = veggiePiles.get(card.getVegetable());
			pile.addCard(card);
		}

		for (Vegetable veggie : Vegetable.values()) {
			Pile<PointSaladCard> pile = veggiePiles.get(veggie);
			piles.add(pile);
		}

		return piles;

		// return extractVeggiePiles(pointSaladCards);
	}

	/**
	 * Extracts the Vegetable cards from a pile and build several piles,
	 * each containing a single type of Vegetable cards.
	 * 
	 * @param pile The pile to extract the Vegetable cards from
	 * 
	 * @return The list of piles, each containing a single type of Vegetable cards
	 */
	public static ArrayList<Pile<PointSaladCard>> extractVeggiePiles(Pile<ICard> pile) {
		
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
		ArrayList<ICard> cards = null;
		try {
			cards = this.cardFactory.loadCards(this.cardsPath);
		} catch (Exception e) {
			throw new SetupException("Failed to load cards from path '" + this.cardsPath + "'", e);
		}

		// Get piles containing cards of a single type of Vegetable
		ArrayList<Pile<PointSaladCard>> veggiePiles = extractVeggiePiles(cards);

		for (Pile<PointSaladCard> pile : veggiePiles) {
			// Remove the correct number of cards from each pile
			int removeNumber = pile.size() - nbVeggieCards;
			pile.draw(removeNumber);
		}

		// Prepare the initial deck
		Pile<PointSaladCard> deck = new Pile<PointSaladCard>().concatenates(veggiePiles);
		deck.shuffle();
		deck.flip();

		// Split it in three criteriion piles
		ArrayList<Pile<PointSaladCard>> criterionPiles = deck.splitIn(PointSaladMarket.NUM_DRAW_PILES);

		// Put the final piles in the market
		PointSaladMarket pointSaladMarket = (PointSaladMarket) market;
		for (int i = 0; i < PointSaladMarket.NUM_DRAW_PILES; i++) {
			try {
				pointSaladMarket.setPile(i, criterionPiles.get(i));
			} catch (Exception e) {
				// Print the error message in the terminal. May happen during testing. Will never happen once the project is completed.
				System.err.println("Index error at PointSaladSetupPhase.processPhase() with error message:\n" + 
								e.getMessage());
			}
		}

		pointSaladMarket.refillVegetables();
	}

	@Override
	public boolean proceedToNextPhase(State state) {
		// The next Phase for the PointSalad game is the Drafting Phase for the first player

		// Randomly chooses the first player
		int startingPlayerIndex = (int) (Math.random() * state.getPlayers().size());
		state.setPlayerTurnIndex(startingPlayerIndex);
		
		state.setPhase(new PointSaladDraftingPhase());

		return true;
	}
}
