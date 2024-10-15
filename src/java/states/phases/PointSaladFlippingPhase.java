package java.states.phases;

import java.cards.PointSaladCard;
import java.exceptions.FlippingException;
import java.network.IServer;
import java.players.AbstractPlayer;
import java.players.IAPlayer;
import java.states.State;
import java.util.ArrayList;

/**
 * Flipping phase for the Point Salad game, where a player can flip a criteria card back to a vegetable.
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
			try {
				IAPlayer bot = (IAPlayer) player;
				command = bot.getMove(state);
			}
			catch (ClassCastException e) {
				throw new FlippingException("Player of index " + currentPlayerIndex + " is not a bot while said so.");
			}
		}
		else {
			IServer server = state.getServer();
			String message = "\n";
			message += player.handToString(); //TODO: As said in AbstractPlayer class, may have to redefine this method to make it look nicer
			message += "\nWould you like to turn a criteria card into a veggie card? (Syntax example: n or 2)";

			try {
				server.sendMessageTo(message, currentPlayerIndex);
			}
			catch (Exception e) {
				throw new FlippingException("Failed to send message to player of index " + currentPlayerIndex + ".");
			}
			try {
				command = server.receiveMessageFrom(currentPlayerIndex);
			}
			catch (Exception e) {
				throw new FlippingException("Failed to receive message from player of index " + currentPlayerIndex + ".");
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

		while (!validCommand) {
			command = getPlayerCommand(state);

			// Logic for a valid flipping command
			if (command.matches("\\d")) {
				int cardIndex = Integer.parseInt(command);
				if (cardIndex >= 0 && cardIndex < player.getHand().size()) {
					PointSaladCard card = (PointSaladCard) player.getHand().get(cardIndex);
					if (card.isCriteriaSideUp())
					{
						card.flip();
						validCommand = true;
					}
				}
			}
			else if (command.equals("n")) {
				validCommand = true;
			}
		}
	}
}
