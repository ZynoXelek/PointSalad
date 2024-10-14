package java.network;

import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.ObjectOutputStream;
import java.exceptions.ServerException;
import java.io.ObjectInputStream;

/**
 * Class for a server.
 */
public class Server implements IServer {
	
	private int port;
	private ServerSocket serverSocket;
	private ArrayList<Socket> clientSockets;
	private ArrayList<ObjectOutputStream> outToClients;
	private ArrayList<ObjectInputStream> inFromClients;

	/**
	 * Creates a server with the given port.
	 * 
	 * @param port The port of the server
	 * 
	 * @throws ServerException If an error occurs while creating the server
	 */
	public Server(int port) throws ServerException {
		this.port = port;
		this.clientSockets = new ArrayList<Socket>();
		this.outToClients = new ArrayList<ObjectOutputStream>();
		this.inFromClients = new ArrayList<ObjectInputStream>();

		try {
			startServer();
		} catch(Exception e) {
			throw new ServerException("Could not create the server socket", e);
		}
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

	public void startServer() throws ServerException {
		if (this.serverSocket != null) {
			// Server is already running
			return;
		}

		try {
			this.serverSocket = new ServerSocket(this.port);
		} catch(Exception e) {
			throw new ServerException("Could not start the server", e);
		}
	}

	public void stopServer() throws ServerException {
		if (this.serverSocket == null) {
			// Server is not running
			return;
		}

		try {
			this.serverSocket.close();

			// Reset client-related parameters
			this.clientSockets = new ArrayList<Socket>();
			this.outToClients = new ArrayList<ObjectOutputStream>();
			this.inFromClients = new ArrayList<ObjectInputStream>();
		} catch(Exception e) {
			throw new ServerException("Could not stop the server", e);
		}
	}
	
	/**
	 * Waits for a client to connect.
	 * 
	 * @return The ID of the client that connected
	 * 
	 * @throws ServerException If an error occurs while waiting for the client
	 */
	public int waitForClient() throws ServerException {
		try {
			Socket connectionSocket = serverSocket.accept();
			this.clientSockets.add(connectionSocket);
			this.outToClients.add(new ObjectOutputStream(connectionSocket.getOutputStream()));
			this.inFromClients.add(new ObjectInputStream(connectionSocket.getInputStream()));

			return this.clientSockets.size() - 1;
		} catch(Exception e) {
			throw new ServerException("Could not wait for the client", e);
		}
	}

	public ArrayList<Integer> waitForClients(int numClients) throws ServerException {
		ArrayList<Integer> clientIDs = new ArrayList<Integer>();

		for(int i = 0; i < numClients; i++) {
			clientIDs.add(this.waitForClient());
		}

		return clientIDs;
	}

	public void sendMessage(String message, int clientID) throws ServerException {
		try {
			this.outToClients.get(clientID).writeObject(message);
		} catch(Exception e) {
			throw new ServerException("Could not send the message to the client", e);
		}
	}

	public String receiveMessage(int clientID) throws ServerException {
		try {
			return (String) this.inFromClients.get(clientID).readObject();
		} catch(Exception e) {
			throw new ServerException("Could not read the message from the client", e);
		}
	}
}
