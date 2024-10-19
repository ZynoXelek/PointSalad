package main.java.players;

import main.java.exceptions.ServerException;
import main.java.network.IServer;
import main.java.states.State;

/**
 * Class for a human player.
 */
public class HumanPlayer extends AbstractPlayer {
	
	/**
	 * Creates a human player with the given ID and name.
	 * 
	 * @param playerID The ID of the player
	 * @param name The name of the player
	 */
	public HumanPlayer(int playerID, String name) {
		super(playerID, name, false);
	}

	@Override
	public String getMove(State state, String instruction) throws ServerException {
		IServer server = state.getServer();
		int playerID = this.getPlayerID();

		if (instruction != null && !instruction.isEmpty())
		{
			server.sendMessageTo(instruction, playerID);
		}

		return server.receiveMessageFrom(playerID);
	}

}
