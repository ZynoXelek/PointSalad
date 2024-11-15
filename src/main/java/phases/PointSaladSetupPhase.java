package phases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cards.ICard;
import cards.ICardFactory;
import cards.Pile;
import cards.PointSaladCard;
import cards.PointSaladCardFactory;
import cards.PointSaladCard.Vegetable;
import exceptions.ConfigException;
import exceptions.SetupException;
import game.market.IMarket;
import game.market.PointSaladMarket;
import network.IServer;
import states.State;
import tools.Config;

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
		String defaultPath = "src/main/resources/PointSaladManifest.json";
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
	 * Shuffles the piles and removes extra cards to have the correct number of cards.
	 * The piles are modified in place.
	 * 
	 * @param veggiePiles The list of piles to shuffle and remove cards from
	 * @param nbVeggieCards The number of cards to keep in each pile
	 */
	public static void shuffleAndRemoveExtraCards(ArrayList<Pile<PointSaladCard>> veggiePiles, int nbVeggieCards) {
		for (Pile<PointSaladCard> pile : veggiePiles) {
			// Shuffle the pile to be sure to remove random cards
			pile.shuffle();
			// Remove the correct number of cards from each pile
			int removeNumber = pile.size() - nbVeggieCards;
			pile.draw(removeNumber);
		}
	}

	/**
	 * Get the initial deck of cards for the Point Salad game from the full set of cards.
	 * This method removes the right amount of random vegetable cards of each type to have the correct number of cards.
	 * It then shuffles the deck and flips it to the criterion side.
	 * 
	 * @param cards The full set of cards to extract the initial deck from
	 * 
	 * @return The initial deck of cards
	 */
	public static Pile<PointSaladCard> getInitialDeck(ArrayList<ICard> cards, int nbPlayers) {
		int nbVeggieCards = NB_EACH_VEGGIE.get(nbPlayers);

		// Get piles containing cards of a single type of Vegetable
		ArrayList<Pile<PointSaladCard>> veggiePiles = PointSaladCard.extractVeggiePiles(cards);

		// Shuffle the piles and removes extra cards
		shuffleAndRemoveExtraCards(veggiePiles, nbVeggieCards);

		// Prepare the initial deck
		Pile<PointSaladCard> deck = new Pile<PointSaladCard>().concatenates(veggiePiles);
		deck.shuffle();
		deck.flip();

		return deck;
	}

	@Override
	public void processPhase(State state) throws SetupException {
		IMarket market = state.getMarket();

		if (market == null) {
			throw new SetupException("Market is not set in the state");
		}

		// Setup phase for Point Salad consists of preparing the market
		int nbPlayers = state.getPlayers().size();

		if (!NB_EACH_VEGGIE.containsKey(nbPlayers)) {
			throw new SetupException("Invalid number of players for Point Salad: " + nbPlayers);
		}

		ArrayList<ICard> cards = null;
		try {
			cards = this.cardFactory.loadCards(this.cardsPath);
		} catch (Exception e) {
			throw new SetupException("Failed to load cards from path '" + this.cardsPath + "'", e);
		}

		// Get the initial deck of cards
		Pile<PointSaladCard> deck = getInitialDeck(cards, nbPlayers);

		// Split it in three criterion piles
		ArrayList<Pile<PointSaladCard>> criterionPiles = deck.splitIn(PointSaladMarket.NUM_DRAW_PILES);

		// Put the final piles in the market
		PointSaladMarket pointSaladMarket = (PointSaladMarket) market;
		pointSaladMarket.setCriterionPiles(criterionPiles);

		pointSaladMarket.refillVegetables();
	}

	@Override
	public boolean proceedToNextPhase(State state) throws SetupException {
		// The next Phase for the PointSalad game is the Drafting Phase for the first player

		// Randomly chooses the first player
		int startingPlayerIndex = (int) (Math.random() * state.getPlayers().size());
		state.setPlayerTurnIndex(startingPlayerIndex);
		
		state.setPhase(new PointSaladDraftingPhase());

		IServer server = state.getServer();
		try {
			server.sendMessageToAll("The game is starting!");
		} catch (Exception e) {
			throw new SetupException("Failed to send message to all clients", e);
		}

		return true;
	}
}
