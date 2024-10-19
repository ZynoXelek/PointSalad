package main.java.main;

import main.java.exceptions.ClientException;
import main.java.network.ClientConnection;

/**
 * This class is the main class on the player side to join a PointSalad game as a Client and play the game.
 */
public class PointSaladClient extends AbstractClient {

	/**
	 * Creates a PointSalad client with the given host and port.
	 * 
	 * @param host The host of the server
	 * @param port The port of the server
	 * 
	 * @throws ClientException If an error occurs while creating or processing the client
	 */
	public PointSaladClient(String host, int port) throws ClientException {
		super(new ClientConnection(host, port));
	}

	/**
	 * Creates a PointSalad client from the terminal.
	 * 
	 * @return A PointSalad client created from the terminal
	 * 
	 * @throws ClientException If an error occurs while creating or processing the client
	 */
	public static PointSaladClient createFromTerminal() throws ClientException {
		String host = getValidHostFromTerminal();
		int port = getValidPortFromTerminal();
		
		return new PointSaladClient(host, port);
	}

	/**
	 * Process the client side of the game.
	 * For the PointSalad game, the client keeps receiving messages until the game ends.
	 * The final received message is the only one containing the keyword "winner".
	 * Messages to which the client must answer are the ones containing the keyword "Please".
	 * 
	 * @throws ClientException If an error occurs while processing the client
	 */
	@Override
	public void play() throws ClientException {
		ClientConnection connection = (ClientConnection) getConnection();
		String receivedMessage = "";
		String answer = "";

		while (!receivedMessage.contains("winner")) {
			receivedMessage = connection.readMessage();
			System.out.println(receivedMessage);
			if (receivedMessage.contains("example")) {
				answer = getScanner().nextLine();
				connection.sendMessage(answer);
			}
		}
	}
}
