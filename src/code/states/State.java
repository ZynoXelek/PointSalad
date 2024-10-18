package code.states;

import code.game.IMarket;
import code.network.IServer;
import code.phases.IPhase;
import code.players.AbstractPlayer;

import java.util.ArrayList;

/**
 * The State class represents the state of the game.
 */
public class State {

	private IServer server;
	private ArrayList<AbstractPlayer> players;
	private int playerTurnIndex;
	private IMarket market;
	private IPhase phase;

	/**
	 * Default constructor for the State class.
	 */
	public State() {
		this.server = null;
		this.players = null;
		this.market = null;
		this.phase = null;
		this.playerTurnIndex = -1;
	}

	/**
	 * Constructor for the State class.
	 * 
	 * @param server The server hosting the game
	 * @param players The players in the game
	 * @param playerTurnIndex The index of the player whose turn it is. A value of -1 means it is not any player's turn
	 * @param market The market in the game
	 * @param phase The phase of the game
	 */
	public State(IServer server, ArrayList<AbstractPlayer> players, int playerTurnIndex, IMarket market, IPhase phase) {
		this.server = server;
		this.players = players;
		this.playerTurnIndex = playerTurnIndex;
		this.market = market;
		this.phase = phase;
	}

	/**
	 * Copy constructor for the State class.
	 * 
	 * @param state The state to copy
	 */
	public State copy() {
		return new State(this.server, this.players, this.playerTurnIndex, this.market, this.phase);
	}

	/**
	 * Getter for the server.
	 * 
	 * @return The server hosting the game
	 */
	public IServer getServer() {
		return server;
	}

	/**
	 * Setter for the server.
	 * 
	 * @param server The server hosting the game
	 */
	public void setServer(IServer server) {
		this.server = server;
	}

	/**
	 * Getter for the players.
	 * 
	 * @return The players in the game
	 */
	public ArrayList<AbstractPlayer> getPlayers() {
		return this.players;
	}

	/**
	 * Setter for the players.
	 * 
	 * @param players The players in the game
	 */
	public void setPlayers(ArrayList<AbstractPlayer> players) {
		this.players = players;
	}

	/**
	 * Getter for the player turn index. A value of -1 means it is not any player's turn.
	 * 
	 * @return The index of the player whose turn it is
	 */
	public int getPlayerTurnIndex() {
		return playerTurnIndex;
	}

	/**
	 * Setter for the player turn index. A value of -1 means it is not any player's turn.
	 * 
	 * @param playerTurnIndex The index of the player whose turn it is
	 */
	public void setPlayerTurnIndex(int playerTurnIndex) {
		this.playerTurnIndex = playerTurnIndex;
	}

	/**
	 * Getter for the market.
	 * 
	 * @return The market in the game
	 */
	public IMarket getMarket() {
		return this.market;
	}

	/**
	 * Setter for the market.
	 * 
	 * @param market The market in the game
	 */
	public void setMarket(IMarket market) {
		this.market = market;
	}

	/**
	 * Getter for the phase.
	 * 
	 * @return The phase of the game
	 */
	public IPhase getPhase() {
		return this.phase;
	}

	/**
	 * Setter for the phase.
	 * 
	 * @param phase The phase of the game
	 */
	public void setPhase(IPhase phase) {
		this.phase = phase;
	}

	/**
	 * Returns a string representation of the state.
	 * This representation only covers the phase and the player turn index for it
	 * to be readable for the user.
	 * 
	 * @return A string representation of the state
	 */
	@Override
	public String toString() {
		String stateString = " ---------- State ---------- \n";
		stateString += "Current phase of the game: " + phase.getClass().getSimpleName() + "\n";
		if (playerTurnIndex == -1) {
			stateString += "No player's turn\n";
		} else {
			stateString += "Current player turn (" + playerTurnIndex + "/" + players.size() + "): "
					+ players.get(playerTurnIndex).getName() + "\n";
		}
		stateString += market + "\n";
		stateString += " -------------------------- \n";

		return stateString;
	}
}
