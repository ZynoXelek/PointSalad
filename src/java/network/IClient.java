package java.network;

import java.exceptions.ClientException;

/**
 * Interface for a client.
 */
public interface IClient {
	
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
