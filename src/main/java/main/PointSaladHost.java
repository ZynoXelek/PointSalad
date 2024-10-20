package main;

import java.util.ArrayList;
import java.util.HashMap;

import exceptions.ServerException;
import game.IMarket;
import game.PointSaladMarket;
import network.IServer;
import network.Server;
import phases.IPhase;
import phases.PointSaladSetupPhase;
import players.AbstractPlayer;
import players.HumanPlayer;
import players.IAPlayer;
import players.PointSaladDefaultBotLogic;
import states.IStateManager;
import states.StateManager;
import states.State;
import tools.Config;
import tools.TerminalInput;

/**
 * This class is responsible for the PointSalad game hosting.
 */
public class PointSaladHost extends AbstractHost {

	/** The minimum number players in a PointSalad game. */
	public static final int MIN_NB_PLAYERS;
	/** The maximum number players in a PointSalad game. */
	public static final int MAX_NB_PLAYERS;

	static {
		// Load final variables from the configuration file
		int min_nb_players = 2; // Default value
		int max_nb_players = 6; // Default value

		try {
			Config config = Config.getInstance();
			min_nb_players = config.getInt("PS_minPlayers");
			max_nb_players = config.getInt("PS_maxPlayers");
		} catch (Exception e) {
			e.printStackTrace();
			// Use default values if configuration loading fails
		}

		MIN_NB_PLAYERS = min_nb_players;
		MAX_NB_PLAYERS = max_nb_players;
	}

	private int numberOfPlayers; // The total number of players, including bots
	private int numberOfBots;

	/**
	 * Creates a host at the given port, with the given number of players and bots.
	 * 
	 * @param port The port of the server
	 * @param numberOfPlayers The number of players in the game
	 * @param numberOfBots The number of bots in the game
	 * 
	 * @throws ServerException If an error occurs while creating the host
	 * @throws IllegalArgumentException If the number of players or bots is invalid
	 * @throws Exception If an error occurs while creating the host
	 */
	public PointSaladHost(int port, int numberOfPlayers, int numberOfBots) throws ServerException, IllegalArgumentException, Exception {
		super(new Server(port));

		if (numberOfPlayers < MIN_NB_PLAYERS || numberOfPlayers > MAX_NB_PLAYERS) {
			throw new IllegalArgumentException("The number of players must be between " + MIN_NB_PLAYERS + " and " + MAX_NB_PLAYERS);
		}
		if (numberOfBots < 0 || numberOfBots > numberOfPlayers) {
			throw new IllegalArgumentException("The number of bots must be between 0 and the number of players");
		}

		this.numberOfPlayers = numberOfPlayers;
		this.numberOfBots = numberOfBots;
	}

	/**
	 * Creates a host from the terminal.
	 * 
	 * @return The host created from the terminal
	 * 
	 * @throws ServerException If an error occurs while creating the host
	 * @throws IllegalArgumentException If the number of players or bots is invalid
	 * @throws Exception If an error occurs while creating the host
	 */
	public static PointSaladHost createHostFromTerminal() throws ServerException, IllegalArgumentException, Exception {
		int port = AbstractHost.DEFAULT_PORT;
		int numberOfPlayers = getValidNumberOfPlayersFromTerminal();
		int numberOfBots = getValidNumberOfBotsFromTerminal(numberOfPlayers);

		return new PointSaladHost(port, numberOfPlayers, numberOfBots);
	}

	/**
	 * Creates a host from the terminal at the given port.
	 * 
	 * @param port The port of the server
	 * 
	 * @return The host created from the terminal
	 * 
	 * @throws ServerException If an error occurs while creating the host
	 * @throws IllegalArgumentException If the number of players or bots is invalid
	 * @throws Exception If an error occurs while creating the host
	 */
	public static PointSaladHost createHostFromTerminal(int port) throws ServerException, IllegalArgumentException, Exception {
		int numberOfPlayers = getValidNumberOfPlayersFromTerminal();
		int numberOfBots = getValidNumberOfBotsFromTerminal(numberOfPlayers);

		return new PointSaladHost(port, numberOfPlayers, numberOfBots);
	}

	/**
	 * Gets whether the number of players is valid.
	 * 
	 * @param numberOfPlayers The number of players to check
	 * 
	 * @return True if it is valid, false otherwise
	 */
	public static boolean isNumberOfPlayersValid(int numberOfPlayers) {
		return numberOfPlayers >= MIN_NB_PLAYERS && numberOfPlayers <= MAX_NB_PLAYERS;
	}

	/**
	 * Gets whether the number of bots is valid.
	 * 
	 * @param numberOfBots The number of bots to check
	 * 
	 * @return True if it is valid, false otherwise
	 */
	public static boolean isNumberOfBotsValid(int numberOfBots, int numberOfPlayers) {
		return numberOfBots >= 0 && numberOfBots <= numberOfPlayers;
	}

	/**
	 * Gets the number of players in the game.
	 * 
	 * @return The number of players in the game
	 */
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	/**
	 * Sets the number of players in the game.
	 * 
	 * @param numberOfPlayers The number of players in the game
	 */
	public void setNumberOfPlayers(int numberOfPlayers) {
		if (!isNumberOfPlayersValid(numberOfPlayers)) {
			System.err.println("The number of players must be between " + MIN_NB_PLAYERS + " and " + MAX_NB_PLAYERS);
			return;
		}
		this.numberOfPlayers = numberOfPlayers;
	}

	/**
	 * Gets the number of bots in the game.
	 * 
	 * @return The number of bots in the game
	 */
	public int getNumberOfBots() {
		return numberOfBots;
	}

	/**
	 * Sets the number of bots in the game.
	 * 
	 * @param numberOfBots The number of bots in the game
	 */
	public void setNumberOfBots(int numberOfBots) {
		if (!isNumberOfBotsValid(numberOfBots, numberOfPlayers)) {
			System.err.println("The number of bots must be between 0 and the number of players");
			return;
		}
		this.numberOfBots = numberOfBots;
	}

	/**
	 * Gets a valid number of players from the terminal.
	 * If an invalid number is entered, the user is prompted again.
	 */
	public static int getValidNumberOfPlayersFromTerminal() {
		System.out.println("Enter the number of players (between " + MIN_NB_PLAYERS + " and " + MAX_NB_PLAYERS + "):");

		boolean validInput = false;
		int nbPlayers = 0;

		while (!validInput) {
			nbPlayers = TerminalInput.nextInt();
			if (isNumberOfPlayersValid(nbPlayers)) {
				validInput = true;
			} else {
				System.err.println("Invalid number of players. Please enter a number between " + MIN_NB_PLAYERS + " and " + MAX_NB_PLAYERS + ":");
			}
		}

		return nbPlayers;
	}

	/**
	 * Gets a valid number of bots from the terminal.
	 * If an invalid number is entered, the user is prompted again.
	 */
	public static int getValidNumberOfBotsFromTerminal(int nbPlayers) {
		System.out.println("Enter the number of bots (between 0 and the number of players):");

		boolean validInput = false;
		int nbBots = 0;

		while (!validInput) {
			nbBots = TerminalInput.nextInt();
			if (isNumberOfBotsValid(nbBots, nbPlayers)) {
				validInput = true;
			} else {
				System.err.println("Invalid number of bots. Please enter a number between 0 and the number of players:");
			}
		}

		return nbBots;
	}

	/**
	 * @throws ServerException If an error occurs while getting the server ready
	 */
	@Override
	public void buildGame() throws ServerException {
		System.out.println("Preparing the game...");

		IServer server = getServer();
		HashMap<Integer, AbstractPlayer> players = new HashMap<Integer, AbstractPlayer>();

		// Wait for the clients to connect
		int nbHumans = numberOfPlayers - numberOfBots;
		if (nbHumans > 0) {
			System.out.println("Waiting for " + nbHumans + " human player(s) to connect...");
		}
		ArrayList<Integer> clientIDs = server.waitForClients(nbHumans);

		System.out.println("Creating the players...");
		int maxClientID = 0;
		// Create the human players
		for (int playerID: clientIDs) {
			if (playerID > maxClientID) {
				maxClientID = playerID;
			}
			AbstractPlayer player = new HumanPlayer(playerID, "Player " + playerID);
			players.put(playerID, player);
		}
		// Create the bot players
		for (int i = 0; i < numberOfBots; i++) {
			int botID = maxClientID + 1 + i;
			AbstractPlayer player = new IAPlayer(botID, "Bot Player " + i, new PointSaladDefaultBotLogic());
			players.put(botID, player);
		}

		int playerTurnIndex = -1; // It is no player turn at the start of the game

		IMarket market = new PointSaladMarket();

		IPhase initialPhase = new PointSaladSetupPhase();


		State initialState = new State(server, players, playerTurnIndex, market, initialPhase);
		IStateManager gameManager = new StateManager(initialState);

		setGameManager(gameManager);

		System.out.println("Game is ready to start!");
	}

	@Override
	public boolean isGameReady() {
		
		IServer server = getServer();
		if (server == null) {
			return false;
		}

		IStateManager gameManager = getGameManager();
		if (gameManager == null) {
			return false;
		}

		State state = gameManager.getState();
		if (state == null) {
			return false;
		}

		HashMap<Integer, AbstractPlayer> players = state.getPlayers();
		IMarket market = state.getMarket();

		if (players == null || market == null) {
			return false;
		}

		if (players.size() != numberOfPlayers) {
			return false;
		}

		return true;
	}
	
}
