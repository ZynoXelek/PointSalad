package tools;

import java.util.Scanner;

/**
 * Singleton class to access a Scanner object for terminal input.
 */
public class TerminalInput {
	
	private static Scanner terminalInput = null;

	static {
		// Make it available from the start
		terminalInput = new Scanner(System.in);
	}

	/**
	 * Private constructor to prevent instantiation.
	 */
	private TerminalInput() {
	}

	/**
	 * Gets the instance of the terminal scanner.
	 * 
	 * @return The instance of the terminal scanner
	 */
	public static Scanner getScanner () {
		if (terminalInput == null) {
			terminalInput = new Scanner(System.in);
		}
		return terminalInput;
	}

	/**
	 * Closes the Scanner object. Should be called at the very end of the program only.
	 */
	public static void closeScanner() {
		if (terminalInput != null) {
			terminalInput.close();
			terminalInput = null;
		}
	}

	/**
	 * Reads the next String from the terminal input.
	 * 
	 * @return The next String from the terminal input
	 */
	public static String nextLine() {
		return terminalInput.nextLine();
	}

	/**
	 * Reads the next int from the terminal input.
	 * It gets the next int and then consumes the rest of the line.
	 * 
	 * @return The next int from the terminal input
	 */
	public static int nextInt() {
		int input = terminalInput.nextInt();
		terminalInput.nextLine();
		return input;
	}
}
