package phases;

import java.util.ArrayList;

import cards.ICard;
import cards.PointSaladCard;
import exceptions.FlippingException;
import game.market.IMarket;
import network.IServer;
import players.AbstractPlayer;
import states.State;

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
	private String getPlayerCommand(State state) throws FlippingException {
		String command = "";

		AbstractPlayer player = state.getCurrentPlayer();

		String instruction = "\n";
		instruction += player.handToString();
		instruction += "\nWould you like to turn a criterion card into a veggie card? (Syntax examples: 'n' or '0', '1'...)\n";

		try {
			command = player.getMove(state, instruction);
		}
		catch (Exception e) {
			throw new FlippingException("Failed to get move from player (Bot? " + player.getIsBot() + ") of ID " + player.getPlayerID() + ".", e);
		}

		return command;
	}
	
	@Override
	public void processPhase(State state) throws FlippingException {
		AbstractPlayer player = state.getCurrentPlayer();
		ArrayList<ICard> hand = player.getHand();
		ArrayList<PointSaladCard> pointSaladHand = PointSaladCard.convertHand(hand);
		ArrayList<PointSaladCard> criteriaHand = PointSaladCard.getCriteriaHand(pointSaladHand);

		IServer server = state.getServer();
		int playerID = player.getPlayerID();


		if (!criteriaHand.isEmpty()) {
			boolean validCommand = false;
			String command = "";

			while (!validCommand) {
				command = getPlayerCommand(state);

				// Logic for a valid flipping command
				if (command.matches("\\d")) {
					int cardIndex = Integer.parseInt(command);
					if (cardIndex >= 0 && cardIndex < criteriaHand.size()) {
						PointSaladCard card = criteriaHand.get(cardIndex);
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

				if (!validCommand && !player.getIsBot()) {
					try {
						server.sendMessageTo("Invalid answer. Please try again.", playerID);
					}
					catch (Exception e) {
						throw new FlippingException("Failed to send message to player of ID " + player.getPlayerID() +
						", corresponding to Client of index " + playerID + ".", e);
					}
				}
			}
			
			System.out.println(player.getName() + " (Player ID: " + playerID + ") flipped: " + command);
		}

		// Player's turn is completed.
		if (!player.getIsBot()) {
			try {
				server.sendMessageTo("\nYour turn is completed\n****************************************************************\n\n", playerID);
			}
			catch (Exception e) {
				throw new FlippingException("Failed to send end of turn message to player of ID " + player.getPlayerID() +
				", corresponding to Client of index " + playerID + ".", e);
			}
		}

		try {
			String message = player.getName() + "'s hand is now: \n" + player.handToString() + "\n";
			server.sendMessageToAllExceptId(message, playerID);
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
			state.setPlayerTurnIndex(-1); // It is not a player's turn anymore
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
