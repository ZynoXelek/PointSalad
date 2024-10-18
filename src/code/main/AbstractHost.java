package code.main;

import code.game.Config;
import code.network.IServer;
import code.states.IStateManager;

/**
 * Abstract class responsible for the game hosting.
 */
public abstract class AbstractHost {

	/** The default port used by the host. */
	public static final int DEFAULT_PORT;

	static {
		// Load final variables from the configuration file
		int default_port = 0; // Default value

		try {
			Config config = Config.getInstance();
			default_port = config.getInt("defaultHostPort");
		} catch (Exception e) {
			e.printStackTrace();
			// Use default values if configuration loading fails
		}

		DEFAULT_PORT = default_port;
	}

	private IServer server;
	private IStateManager gameManager;

	/**
	 * Creates a host with the given server.
	 * 
	 * @param server The server of the host
	 * 
	 * @throws Exception If an error occurs while creating the host
	 */
	public AbstractHost(IServer server) throws Exception {
		this.server = server;

		buildGame();

		startGame();

		stopGame();
	}

	/**
	 * Gets the server of the host.
	 * 
	 * @return The server of the host
	 */
	public IServer getServer() {
		return server;
	}

	/**
	 * Gets the game manager of the host.
	 * 
	 * @return The game manager of the host
	 */
	public IStateManager getGameManager() {
		return gameManager;
	}

	/**
	 * Sets the game manager of the host.
	 * 
	 * @param gameManager The game manager of the host
	 */
	public void setGameManager(IStateManager gameManager) {
		this.gameManager = gameManager;
	}

	/**
	 * Gets the game ready for play.
	 * It should wait for the players to join the game, build the initial game state, and build the game manager.
	 * 
	 * @throws Exception If an error occurs while building the game
	 */
	public abstract void buildGame() throws Exception;

	/**
	 * Checks if the game is ready to start.
	 * 
	 * @return True if the game is ready to start, false otherwise
	 */
	public abstract boolean isGameReady();

	/**
	 * Starts the game.
	 */
	public void startGame() {
		if (isGameReady()) {
			System.out.println("Starting the game...");
			try {
				gameManager.update();
			} catch (Exception e) {
				System.out.println("An error occurred while running the game, exiting here.");
				stopGame(1);
			}
		}
	}

	/**
	 * Stops the game.
	 */
	public void stopGame() {
		stopGame(0);
	}

	private void stopGame(int status) {
		System.out.println("Stopping the game...");
		try {
			server.stopServer();
		} catch (Exception e) {
			System.out.println("An error occurred while stopping the server, exiting here.");
			System.exit(-1);
		}

		System.exit(status);
	}
}
