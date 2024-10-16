package code.network;

import code.exceptions.ServerException;

import java.util.ArrayList;

/**
 * Interface for a server.
 */
public interface IServer {

	/**
	 * Starts the server.
	 * 
	 * @throws ServerException If an error occurs while starting the server
	 */
	public void startServer() throws ServerException;

	/**
	 * Stops the server.
	 * 
	 * @throws ServerException If an error occurs while stopping the server
	 */
	public void stopServer() throws ServerException;

	/**
	 * Waits for a specified number of clients to connect.
	 * 
	 * @param numClients The number of clients to wait for
	 * 
	 * @return The IDs of the clients that connected
	 * 
	 * @throws ServerException If an error occurs while waiting for the clients
	 */
	public ArrayList<Integer> waitForClients(int numClients) throws ServerException;

	/**
	 * Sends a message to all clients.
	 * 
	 * @param message The message to send to all clients
	 * 
	 * @throws ServerException If an error occurs while sending the message
	 */
	public void sendMessageToAll(String message) throws ServerException;

	/**
	 * Sends a message to a client.
	 * 
	 * @param message The message to send
	 * @param clientID The ID of the client to send the message to
	 * 
	 * @throws ServerException If an error occurs while sending the message
	 */
	public void sendMessageTo(String message, int clientID) throws ServerException;

	/**
	 * Receives a message from a client.
	 * 
	 * @param clientID The ID of the client to receive the message from
	 * 
	 * @return The message from the client
	 * 
	 * @throws ServerException If an error occurs while receiving the message
	 */
	public String receiveMessageFrom(int clientID) throws ServerException;
}