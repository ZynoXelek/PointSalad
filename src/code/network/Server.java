package code.network;

import code.exceptions.ServerException;

import java.util.ArrayList;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.ObjectOutputStream;
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

	@Override
	public void startServer() throws ServerException {
		if (this.serverSocket != null) {
			// Server is already running
			System.err.println("Server is already running");
			return;
		}

		try {
			this.serverSocket = new ServerSocket(this.port);
		} catch(Exception e) {
			throw new ServerException("Could not start the server", e);
		}
	}

	@Override
	public void stopServer() throws ServerException {
		if (this.serverSocket == null) {
			// Server is not running
			System.err.println("Server is not running");
			return;
		}

		try {
			// Close the communication with the clients
			for (int i = 0; i < this.clientSockets.size(); i++) {
				this.outToClients.get(i).close();
				this.inFromClients.get(i).close();
				this.clientSockets.get(i).close();
			}

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

	@Override
	public ArrayList<Integer> waitForClients(int numClients) throws ServerException {
		ArrayList<Integer> clientIDs = new ArrayList<Integer>();

		for(int i = 0; i < numClients; i++) {
			clientIDs.add(this.waitForClient());
		}

		return clientIDs;
	}

	@Override
	public void sendMessageToAll(String message) throws ServerException {
		for (int i = 0; i < this.outToClients.size(); i++) {
			sendMessageTo(message, i);
		}
	}

	@Override
	public void sendMessageToAllExceptId(String message, int clientID) throws ServerException {
		for (int i = 0; i < this.outToClients.size(); i++) {
			if (i == clientID) {
				continue;
			}
			sendMessageTo(message, i);
		}
	}

	@Override
	public void sendMessageTo(String message, int clientID) throws ServerException {
		if (clientID < 0 || clientID >= this.outToClients.size()) {
			throw new ServerException("Invalid client ID");
		}
		
		try {
			this.outToClients.get(clientID).writeObject(message);
		} catch(Exception e) {
			throw new ServerException("Could not send the message to the client", e);
		}
	}

	@Override
	public String receiveMessageFrom(int clientID) throws ServerException {
		try {
			return (String) this.inFromClients.get(clientID).readObject();
		} catch(Exception e) {
			throw new ServerException("Could not read the message from the client", e);
		}
	}
}
