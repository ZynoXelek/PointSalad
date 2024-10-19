package main.java.phases;

import java.util.ArrayList;

import main.java.cards.ICard;
import main.java.exceptions.DraftingException;
import main.java.game.IMarket;
import main.java.network.IServer;
import main.java.players.AbstractPlayer;
import main.java.states.State;

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
		instruction += "\n" + market.getDraftingInstruction() + ":\n";

		try {
			command = player.getMove(state, instruction);
		}
		catch (Exception e) {
			throw new DraftingException("Failed to get move from player (Bot? " + player.getIsBot() + ") of ID " + player.getPlayerID() + ".", e);
		}

		return command;
	}



	@Override
	public void processPhase(State state) throws DraftingException {
		IServer server = state.getServer();
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
				try {
					server.sendMessageTo("Invalid draft. Please try again.", playerID);
				}
				catch (Exception e) {
					throw new DraftingException("Failed to send message to player of ID " + player.getPlayerID() +
					", corresponding to Client of index " + playerID + ".", e);
				}
			}
		}

		System.out.println(player.getName() + " (Player ID: " + playerID + ") drafted: " + command);

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
