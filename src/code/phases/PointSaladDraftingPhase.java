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

		AbstractPlayer player = state.getCurrentPlayer();

		IMarket market = state.getMarket();

		String instruction = "\n\n****************************************************************\nIt's your turn! Your hand is:\n";
		instruction += player.handToString();
		instruction += "\nThe piles are: ";
		instruction += market.toString();
		instruction += market.getDraftingInstruction() + ":\n";

		try {
			command = player.getMove(state, instruction);
		}
		catch (Exception e) {
			throw new DraftingException("Failed to get move from player (Bot? " + player.getIsBot() + ") of ID " + player.getPlayerID() + ".", e);
		}

		//TODO: To be completely removed in the end
		// if (player.getIsBot()) {
		// 	// Use Bot Logic
		// 	IAPlayer bot = null;
		// 	try {
		// 		bot = (IAPlayer) player;
		// 	}
		// 	catch (ClassCastException e) {
		// 		throw new DraftingException("Player of index " + currentPlayerIndex + " is not a bot while said so.", e);
		// 	}

		// 	try {
		// 		command = bot.getMove(state);
		// 	}
		// 	catch (Exception e) {
		// 		throw new DraftingException("Failed to get move from bot of index " + currentPlayerIndex + ".", e);
		// 	}
		// }
		// else {
		// 	IServer server = state.getServer();
		// 	int playerID = player.getPlayerID();
		// 	String message = 

		// 	try {
		// 		server.sendMessageTo(message, playerID);
		// 	}
		// 	catch (Exception e) {
		// 		throw new DraftingException("Failed to send message to player of index " + currentPlayerIndex +
		// 		", corresponding to Client of index " + playerID + ".", e);
		// 	}
		// 	try {
		// 		command = server.receiveMessageFrom(playerID);
		// 	}
		// 	catch (Exception e) {
		// 		throw new DraftingException("Failed to send message to player of index " + currentPlayerIndex +
		// 		", corresponding to Client of index " + playerID + ".", e);
		// 	}
		// }

		return command;
	}



	@Override
	public void processPhase(State state) throws DraftingException {
		boolean validCommand = false;
		IMarket market = state.getMarket();
		AbstractPlayer player = state.getCurrentPlayer();
		int playerID = player.getPlayerID();

		String command = "";

		while (!validCommand) {
			command = getPlayerCommand(state);

			if (market.isCardsStringValid(command)) {
				validCommand = true;
			}

			if (!validCommand && !player.getIsBot()) {
				IServer server = state.getServer();
				try {
					server.sendMessageTo("Invalid draft. Please try again.", playerID);
				}
				catch (Exception e) {
					throw new DraftingException("Failed to send message to player of ID " + player.getPlayerID() +
					", corresponding to Client of index " + playerID + ".", e);
				}
			}
		}

		try {
			ArrayList<ICard> cards = market.draftCards(command);
			player.addCardsToHand(cards);
		}
		catch (Exception e) {
			// Should never happen since the command has been verified first.
			throw new DraftingException("Failed to draft cards with the given command '" + command + "'.", e);
		}

		market.refill();
	}

	@Override
	public boolean proceedToNextPhase(State state) {
		// The next Phase for the PointSalad game is the Flipping Phase for the same player

		state.setPhase(new PointSaladFlippingPhase());

		return true;
	}
}
