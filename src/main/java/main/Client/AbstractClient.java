package main.Client;

import java.util.Scanner;
import java.util.regex.Pattern;

import exceptions.ClientException;
import main.Host.AbstractHost;
import network.IClientConnection;
import tools.TerminalInput;

import java.util.regex.Matcher;

/**
 * Abstract class for a client.
 */
public abstract class AbstractClient {
	
	private IClientConnection connection;
	private Scanner scanner;

	/**
	 * Creates a client with the given connection.
	 * 
	 * @param connection The connection of the client
	 * 
	 * @throws ClientException If an error occurs while creating or processing the client
	 */
	public AbstractClient(IClientConnection connection) throws ClientException {
		this.connection = connection;
		this.scanner = TerminalInput.getScanner();
	}

	/**
	 * Gets the connection of the client.
	 * 
	 * @return The connection of the client
	 */
	public IClientConnection getConnection() {
		return connection;
	}

	/**
	 * Sets the connection of the client.
	 * 
	 * @param connection The connection of the client
	 */
	public void setConnection(IClientConnection connection) {
		this.connection = connection;
	}

	/**
	 * Gets the scanner of the client.
	 * 
	 * @return The scanner of the client
	 */
	public Scanner getScanner() {
		return scanner;
	}

	/**
	 * Sets the scanner of the client.
	 * 
	 * @param scanner The scanner of the client
	 */
	public void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	/**
	 * Runs the client.
	 * 
	 * @throws ClientException If an error occurs while running the client
	 */
	public void run() throws ClientException {
		connect();
		play();
		quit();
	}

	/**
	 * Connects the client.
	 * 
	 * @throws ClientException If an error occurs while connecting the client
	 */
	public void connect() throws ClientException {
		if (connection == null) {
			throw new ClientException("The client connection object is null.");
		}
		if (!connection.isConnected()) {
			System.out.println("Connecting to the host...");
			connection.connect();
			System.out.println("Connected to the host.");
		}
	}

	/**
	 * Plays the game hosted on the server.
	 * 
	 * @throws ClientException If an error occurs while playing
	 */
	public abstract void play() throws ClientException;

	/**
	 * Quits the game and stops the client.
	 * 
	 * @throws ClientException If an error occurs while stopping the client
	 */
	public void quit() throws ClientException {
		System.out.println("Stopping the client...");
		connection.disconnect();
		TerminalInput.closeScanner();
		System.out.println("Client stopped.");

		System.exit(0);
	}

	/**
	 * Checks if a host is valid.
	 * The given host should either be "localhost" or a valid IPv4 address with the format X.X.X.X
	 * where X is a number between 0 and 255.
	 * 
	 * @param host The host to check
	 * 
	 * @return True if the host is valid, false otherwise
	 */
	public static boolean isHostValid(String host) {
		if (host == null || host.isEmpty()) {
			return false;
		}
		else if (host.equals("localhost")) {
			return true;
		}

		String ipPartRegex = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
		String hostRegex = "^(" + ipPartRegex + "\\.){3}" + ipPartRegex + "$";
		Pattern pattern = Pattern.compile(hostRegex);
		Matcher matcher = pattern.matcher(host);
		return matcher.matches();
	}

	/**
	 * Checks if a port is valid.
	 * 
	 * @param port The port to check
	 * 
	 * @return True if the port is valid, false otherwise
	 */
	public static boolean isPortValid(int port) {
		return port >= 0 && port <= 65535;
	}
	
	/**
	 * Gets a valid host String from the terminal.
	 * 
	 * @return A valid host String from the terminal
	 */
	public static String getValidHostFromTerminal() {
		String host = null;
		boolean isValid = false;

		while (!isValid) {
			System.out.print("Enter the host of the server: ");
			host = TerminalInput.nextLine();
			
			if (host == null || host.isEmpty()) {
				System.out.println("The host cannot be empty.");
			} else if (!isHostValid(host)) {
				System.out.println("The host must be either 'localhost' or a valid IPv4 address,"+
				" meaning it must be in the form of X.X.X.X where each X is between 0 and 255.");
			} else {
				isValid = true;
			}
		}
		
		return host;
	}

	/**
	 * Gets a valid port from the terminal.
	 * 
	 * @return A valid port from the terminal
	 */
	public static int getValidPortFromTerminal() {
		int port = 0;
		boolean isValid = false;
		
		while (!isValid) {
			System.out.print("Enter the port of the server (Do not enter anything to use the default port " + AbstractHost.DEFAULT_PORT + "): ");
			String portString = TerminalInput.nextLine();

			if (portString == null || portString.isEmpty()) {
				port = AbstractHost.DEFAULT_PORT;
				isValid = true;
				System.out.println("Using the default port " + port + ".");
			} else {
				try {
					port = Integer.parseInt(portString);
					
					if (!isPortValid(port)) {
						System.out.println("The port must be between 0 and 65535.");
					} else {
						isValid = true;
					}
				} catch(NumberFormatException e) {
					System.out.println("The port must be an integer.");
				}
			}
		}
		
		return port;
	}
}
