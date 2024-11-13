package main;

import exceptions.ConfigException;
import tools.Config;
import tools.TerminalInput;

/**
 * Main class to run the user wanted game.
 */
public class Main {

	private final static Game DEFAULT_GAME;

	enum Game {
		POINTSALAD,
	}

	static {
		// Initialize the default game from the Config.
		Game byDefault = Game.POINTSALAD;

		try {
			Config config = Config.getInstance();
			byDefault = Game.valueOf(config.getString("defaultGame").toUpperCase());
		} catch (ConfigException e) {
			e.printStackTrace();
			// Use default value if configuration loading fails
		}

		DEFAULT_GAME = byDefault;
	}
	
	public Main(String[] args) throws Exception {
		Game selectedGame = null;
		if (args.length == 0) {
			selectedGame = askValidGame();
		} else {
			try {
				selectedGame = Game.valueOf(args[0].toUpperCase());
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid game. Please try again.");
				selectedGame = askValidGame();
			}
		}

		String[] newArgs = new String[0];
		if (args.length > 1) {
			newArgs = new String[args.length - 1];
			System.arraycopy(args, 1, newArgs, 0, args.length - 1);
		}
		
		switch (selectedGame) {
			case POINTSALAD:
				new PointSalad(args);
				break;
			
			// Other cases to be added here for other games
			
			default:
				System.out.println("Invalid game.");
				break;
		}
	}

	private Game askValidGame() {
		boolean valid = false;
		Game selectedGame = null;

		while (!valid) {
			System.out.println("Please choose the game between the following: (it is case unsensitive)");
			for (Game game : Game.values()) {
				System.out.println(" - " + game);
			}
			System.out.println("You can skip this by pressing [Enter] without typing anything to run the default game (" +
			DEFAULT_GAME + ").");

			String input = TerminalInput.nextLine();

			if (input.isEmpty()) {
				selectedGame = DEFAULT_GAME;
				valid = true;
			} else {
				try {
					selectedGame = Game.valueOf(input.toUpperCase());
					valid = true;
				} catch (IllegalArgumentException e) {
					System.out.println("Invalid game. Please try again.");
				}
			}
		}

		return selectedGame;
	}

	public static void main(String[] args) throws Exception {
		new Main(args);
	}
}
