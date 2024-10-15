package java.states.phases;

import java.util.ArrayList;
import java.players.AbstractPlayer;
import java.players.IAPlayer;
import java.cards.ICard;
import java.exceptions.DraftingException;
import java.game.IMarket;
import java.network.IServer;
import java.states.State;

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
				command = bot.getBotLogic().getMove(state);
			}
			catch (ClassCastException e) {
				throw new DraftingException("Player of index " + currentPlayerIndex + " is not a bot while said so.");
			}
		}
		else {
			IServer server = state.getServer();
			String message = "\n\n****************************************************************\nIt's your turn! Your hand is:\n";
			message += player.handToString();
			message += "\nThe piles are: ";
			message += market.toString();

			try {
				server.sendMessage(message, currentPlayerIndex);
			}
			catch (Exception e) {
				throw new DraftingException("Failed to send message to player of index " + currentPlayerIndex + ".");
			}
			try {
				command = server.receiveMessage(currentPlayerIndex);
			}
			catch (Exception e) {
				throw new DraftingException("Failed to receive message from player of index " + currentPlayerIndex + ".");
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
			throw new DraftingException("Failed to draft cards with the given command '" + command + "'.");
		}
	}

}
