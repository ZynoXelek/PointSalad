package code.phases;

import code.players.AbstractPlayer;
import code.players.IAPlayer;
import code.cards.ICard;
import code.exceptions.DraftingException;
import code.game.IMarket;
import code.network.IServer;
import code.states.State;

import java.util.ArrayList;

/**
 * Drafting phase for the Point Salad game.
 */
public class PointSaladDraftingPhase implements IPhase {
	
	/**
	 * Helper method to get the command from the current player, based on the current state of the game.
	 * 
	 * @param state The current state of the game
	 * 
	 * @return The command from the player
	 * 
	 * @throws DraftingException if an error occurs while getting the command from the player
	 */
	public String getPlayerCommand(State state) throws DraftingException {
		String command = "";

		int currentPlayerIndex = state.getPlayerTurnIndex();
		AbstractPlayer player = state.getPlayers().get(currentPlayerIndex);

		IMarket market = state.getMarket();


		if (player.getIsBot()) {
			// Use Bot Logic
			try {
				IAPlayer bot = (IAPlayer) player;
				command = bot.getMove(state);
			}
			catch (ClassCastException e) {
				throw new DraftingException("Player of index " + currentPlayerIndex + " is not a bot while said so.", e);
			}
		}
		else {
			IServer server = state.getServer();
			int playerID = player.getPlayerID();
			String message = "\n\n****************************************************************\nIt's your turn! Your hand is:\n";
			message += player.handToString();
			message += "\nThe piles are: ";
			message += market.toString();

			try {
				server.sendMessageTo(message, playerID);
			}
			catch (Exception e) {
				throw new DraftingException("Failed to send message to player of index " + currentPlayerIndex +
				", corresponding to Client of index " + playerID + ".", e);
			}
			try {
				command = server.receiveMessageFrom(playerID);
			}
			catch (Exception e) {
				throw new DraftingException("Failed to send message to player of index " + currentPlayerIndex +
				", corresponding to Client of index " + playerID + ".", e);
			}
		}

		return command;
	}



	@Override
	public void processPhase(State state) throws DraftingException {
		ArrayList<AbstractPlayer> players = state.getPlayers();
		int currentPlayerIndex = state.getPlayerTurnIndex();

		if (currentPlayerIndex == -1) {
			throw new DraftingException("It is not a player's turn.");
		}

		if (currentPlayerIndex < 0 || currentPlayerIndex >= players.size()) {
			throw new DraftingException("Invalid player index.");
		}

		boolean validCommand = false;
		IMarket market = state.getMarket();

		String command = "";

		while (!validCommand) {
			command = getPlayerCommand(state);

			if (market.isCardsStringValid(command)) {
				validCommand = true;
			}
		}

		try {
			ArrayList<ICard> cards = market.draftCards(command);
			AbstractPlayer player = players.get(currentPlayerIndex);
			player.addCardsToHand(cards);
		}
		catch (Exception e) {
			// Should never happen since the command has been verified first.
			throw new DraftingException("Failed to draft cards with the given command '" + command + "'.", e);
		}
	}

	@Override
	public boolean proceedToNextPhase(State state) {
		// The next Phase for the PointSalad game is the Flipping Phase for the same player

		state.setPhase(new PointSaladFlippingPhase());

		return true;
	}
}
