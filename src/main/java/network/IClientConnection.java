package main.java.network;

import main.java.exceptions.ClientException;

/**
 * Interface for a client connection.
 */
public interface IClientConnection {

	/**
	 * Connects to the server.
	 * 
	 * @throws ClientException If an error occurs while connecting
	 */
	public void connect() throws ClientException;

	/**
	 * Disconnects from the server.
	 * 
	 * @throws ClientException If an error occurs while disconnecting
	 */
	public void disconnect() throws ClientException;

	/**
	 * Checks if the client is connected to the server.
	 * 
	 * @return True if the client is connected, false otherwise
	 */
	public boolean isConnected();

	/**
	 * Sends a message to the server.
	 * 
	 * @param message The message to send
	 * 
	 * @throws ClientException If an error occurs while sending the message
	 */
	public void sendMessage(String message) throws ClientException;

	/**
	 * Reads a message from the server.
	 * 
	 * @return The message from the server
	 * 
	 * @throws ClientException If an error occurs while reading the message
	 */
	public String readMessage() throws ClientException;

}
