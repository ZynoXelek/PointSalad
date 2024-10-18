package code.main;

import java.util.Scanner;

/**
 * Main class for the PointSalad game. This class contains the main method and is the entry point for the game.
 */
public class PointSalad {

	private static final int HOSTING = 1;
	private static final int JOINING = 2;

	/**
	 * Creates a new instance of the PointSalad game.
	 * It is responsible for identifying whether the game is hosted or joined.
	 * 
	 * @param args Command line arguments
	 * 
	 * @throws Exception If an error occurs while creating the game
	 */
	public PointSalad(String[] args) throws Exception {

		int len = args.length;

		if (len == 0) {
			int gameMode = askGameMode();
			if (gameMode == HOSTING) {
				hostServer();
			} else if (gameMode == JOINING) {
				joinGame();
			}
		} else if (len == 1) {
			int gameMode = Integer.parseInt(args[0]);
			if (gameMode == HOSTING) {
				hostServer();
			} else if (gameMode == JOINING) {
				joinGame();
			}
			else {
				System.out.println(getDummyExample());
			}
		} else if (len == 3) {
			int gameMode = Integer.parseInt(args[0]);
			String host = args[1];
			int port = Integer.parseInt(args[2]);
			if (gameMode == JOINING) {
				try {
					joinGame(host, port);
				} catch (Exception e) {
					System.err.println("Error while joining the game: " + e.getMessage());
					System.err.println(getDummyExample());
				}
			}
		} else if (len == 4) {
			int gameMode = Integer.parseInt(args[0]);
			int port = Integer.parseInt(args[1]);
			int numPlayers = Integer.parseInt(args[2]);
			int numBots = Integer.parseInt(args[3]);

			if (gameMode == HOSTING) {
				try {
					hostServer(port, numPlayers, numBots);
				} catch (Exception e) {
					System.err.println("Error while hosting the server: " + e.getMessage());
					System.err.println(getDummyExample());
				}
			}
		} else {
			System.out.println(getDummyExample());
		}
	}

	/**
	 * Asks the user to select a game mode.
	 * 
	 * @return The selected game mode
	 */
	public static int askGameMode() {
		Scanner scanner = new Scanner(System.in);
		
		int gameMode = -1;
		boolean valid = false;

		while (!valid) {
			System.out.println(" --- PointSalad game mode selection ---");
			System.out.println("Enter " + HOSTING + " to host a game or " + JOINING + " to join a game: ");
			String input = scanner.nextLine();
			try {
				gameMode = Integer.parseInt(input);
				if (gameMode == HOSTING || gameMode == JOINING) {
					valid = true;
				} else {
					System.out.println("Invalid game mode. Please enter a valid game mode id.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid game mode id.");
			}
		}

		scanner.close();

		return gameMode;
	}

	/**
	 * Returns a dummy example of how to use the PointSalad game.
	 * 
	 * @return A dummy example of how to use the PointSalad game
	 */
	public static String getDummyExample() {
		String errorMessage = "Please enter a valid command line argument.\n";
		errorMessage += "java PointSalad [gameMode]\n";
		errorMessage += "1 to host a game, then you can add [port] [numPlayers] [numBots]\n";
		errorMessage += "2 to join a game, then you can add [host] [port]\n";
		return errorMessage;
	}

	/**
	 * Hosts a PointSalad server.
	 * 
	 * @throws Exception If an error occurs while hosting the server
	 */
	public static void hostServer() throws Exception {
		PointSaladHost.createHostFromTerminal();
	}

	/**
	 * Hosts a PointSalad server with the given port, number of players, and number of bots.
	 * 
	 * @param port The port of the server
	 * @param numPlayers The number of players in the game
	 * @param numBots The number of bots in the game
	 * 
	 * @throws Exception If an error occurs while hosting the server
	 */
	public static void hostServer(int port, int numPlayers, int numBots) throws Exception {
		new PointSaladHost(port, numPlayers, numBots);
	}

	/**
	 * Joins a PointSalad game as a client.
	 * 
	 * @throws Exception If an error occurs while joining the game
	 */
	public static void joinGame() throws Exception {
		PointSaladClient.createFromTerminal();
	}

	/**
	 * Joins a PointSalad game as a client with the given host and port.
	 * 
	 * @param host The host of the server
	 * @param port The port of the server
	 * 
	 * @throws Exception If an error occurs while joining the game
	 */
	public static void joinGame(String host, int port) throws Exception {
		new PointSaladClient(host, port);
	}

	/**
	 * Main method for the PointSalad game. This method creates a new instance of the PointSalad game.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		try {
			new PointSalad(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}