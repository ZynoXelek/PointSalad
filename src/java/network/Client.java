package java.network;

import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import java.exceptions.ClientException;

/**
 * Class for a client.
 */
public class Client implements IClient {
	
	private String host;
	private int port;
	private boolean isConnected;
	private Socket clientSocket;
	private ObjectOutputStream outToServer;
	private ObjectInputStream inFromServer;

	/**
	 * Creates a client with the given host and port.
	 * 
	 * @param host The host of the server
	 * @param port The port of the server
	 */
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
		this.isConnected = false;
		this.clientSocket = null;
		this.outToServer = null;
		this.inFromServer = null;

		try {
			this.connect();
		} catch(Exception e) {
			System.err.println("Client could not connect to the server: " + e.getMessage());
		}
	}

	/**
	 * Gets the host of the server.
	 * 
	 * @return The host of the server
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * Sets the host of the server.
	 * 
	 * @param host The host of the server
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Gets the port of the server.
	 * 
	 * @return The port of the server
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Sets the port of the server.
	 * 
	 * @param port The port of the server
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public void connect() throws ClientException {
		try {
			this.clientSocket = new Socket(this.host, this.port);
			this.outToServer = new ObjectOutputStream(this.clientSocket.getOutputStream());
			this.inFromServer = new ObjectInputStream(this.clientSocket.getInputStream());
			this.isConnected = true;
		} catch(Exception e) {
			throw new ClientException("Could not connect to the server", e);
		}
	}

	public void disconnect() throws ClientException {
		try {
			this.outToServer.close();
			this.inFromServer.close();
			this.clientSocket.close();
			this.isConnected = false;
		} catch(Exception e) {
			throw new ClientException("Could not disconnect from the server", e);
		}
	}

	public void sendMessage(String message) throws ClientException {
		// tries to connect if not connected yet.
		if (!this.isConnected) {
			this.connect();
		}

		if (this.isConnected) {
			try {
				this.outToServer.writeObject(message);
			} catch(Exception e) {
				throw new ClientException("Could not send message to the server", e);
			}
		}
	}

	public String readMessage() throws ClientException {
		// tries to connect if not connected yet.
		if (!this.isConnected) {
			this.connect();
		}

		if (this.isConnected) {
			try {
				return (String) this.inFromServer.readObject();
			} catch(Exception e) {
				throw new ClientException("Could not read message from the server", e);
			}
		}

		return null;
	}

}
