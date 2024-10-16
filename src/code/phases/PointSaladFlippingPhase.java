package code.phases;

import code.cards.PointSaladCard;
import code.exceptions.FlippingException;
import code.game.IMarket;
import code.network.IServer;
import code.players.AbstractPlayer;
import code.players.IAPlayer;
import code.states.State;

import java.util.ArrayList;

/**
 * Flipping phase for the Point Salad game, where a player can flip a criterion card back to a vegetable.
 */
public class PointSaladFlippingPhase implements IPhase {

	/**
	 * Helper method to get the command from the current player, based on the current state of the game.
	 * 
	 * @param state The current state of the game
	 * 
	 * @return The command from the player
	 * 
	 * @throws FlippingException if an error occurs while getting the command from the player
	 */
	public String getPlayerCommand(State state) throws FlippingException {
		String command = "";

		int currentPlayerIndex = state.getPlayerTurnIndex();
		AbstractPlayer player = state.getPlayers().get(currentPlayerIndex);

		if (player.getIsBot()) {
			// Use Bot Logic
			IAPlayer bot = null;
			try {
				bot = (IAPlayer) player;
			}
			catch (ClassCastException e) {
				throw new FlippingException("Player of index " + currentPlayerIndex + " is not a bot while said so.", e);
			}

			try {
				command = bot.getMove(state);
			}
			catch (Exception e) {
				throw new FlippingException("Failed to get move from bot of index " + currentPlayerIndex + ".", e);
			}
		}
		else {
			IServer server = state.getServer();
			int playerID = player.getPlayerID();
			String message = "\n";
			message += player.handToString(); //TODO: As said in AbstractPlayer class, may have to redefine this method to make it look nicer
			message += "\nWould you like to turn a criterion card into a veggie card? (Syntax example: n or 2)";

			try {
				server.sendMessageTo(message, playerID);
			}
			catch (Exception e) {
				throw new FlippingException("Failed to send message to player of index " + currentPlayerIndex +
				", corresponding to Client of index " + playerID + ".", e);
			}
			try {
				command = server.receiveMessageFrom(playerID);
			}
			catch (Exception e) {
				throw new FlippingException("Failed to send message to player of index " + currentPlayerIndex +
				", corresponding to Client of index " + playerID + ".", e);
			}
		}

		return command;
	}
	
	@Override
	public void processPhase(State state) throws FlippingException {
		ArrayList<AbstractPlayer> players = state.getPlayers();
		int currentPlayerIndex = state.getPlayerTurnIndex();

		if (currentPlayerIndex == -1) {
			throw new FlippingException("It is not a player's turn.");
		}

		if (currentPlayerIndex < 0 || currentPlayerIndex >= players.size()) {
			throw new FlippingException("Invalid player index.");
		}

		AbstractPlayer player = players.get(currentPlayerIndex);

		boolean validCommand = false;
		String command = "";

		IServer server = state.getServer();
		int playerID = player.getPlayerID();

		while (!validCommand) {
			command = getPlayerCommand(state);

			// Logic for a valid flipping command
			if (command.matches("\\d")) {
				int cardIndex = Integer.parseInt(command);
				if (cardIndex >= 0 && cardIndex < player.getHand().size()) {
					PointSaladCard card = (PointSaladCard) player.getHand().get(cardIndex);
					if (card.isCriterionSideUp())
					{
						card.flip();
						validCommand = true;
					}
				}
			}
			else if (command.equals("n")) {
				validCommand = true;
			}

			if (!validCommand) {
				try {
					server.sendMessageTo("Invalid answer. Please try again.", playerID);
				}
				catch (Exception e) {
					throw new FlippingException("Failed to send message to player of index " + currentPlayerIndex +
					", corresponding to Client of index " + playerID + ".", e);
				}
			}
		}

		// Player's turn is completed.
		try {
			server.sendMessageTo("\nYour turn is completed\n****************************************************************\n\n", playerID);
		}
		catch (Exception e) {
			throw new FlippingException("Failed to send end of turn message to player of index " + currentPlayerIndex +
			", corresponding to Client of index " + playerID + ".", e);
		}

		try {
			String message = "Player " + playerID + "'s hand is now: \n" + player.handToString() + "\n";
			server.sendMessageToAll(message);
		}
		catch (Exception e) {
			throw new FlippingException("Failed to send updated hand message to all players.", e);
		}
	}

	@Override
	public boolean proceedToNextPhase(State state) {
		// The next Phase for the PointSalad game is either the Drafting Phase for the next player or the Scoring Phase if the market is empty.

		IMarket market = state.getMarket();
		if (market.isEmpty()) {
			state.setPhase(new PointSaladScoringPhase());
		}
		else {
			int nbPlayers = state.getPlayers().size();
			int currentPlayerIndex = state.getPlayerTurnIndex();
			int nextPlayerIndex = (currentPlayerIndex + 1) % nbPlayers;
			state.setPlayerTurnIndex(nextPlayerIndex);
			state.setPhase(new PointSaladDraftingPhase());
		}

		return true;
	}
}
