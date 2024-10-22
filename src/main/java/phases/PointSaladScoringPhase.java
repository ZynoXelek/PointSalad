package phases;

import java.util.HashMap;

import exceptions.ScorerException;
import exceptions.ScoringException;
import game.scorer.IScorer;
import game.scorer.PointSaladScorer;
import network.IServer;
import players.AbstractPlayer;
import states.State;

/**
 * Scoring phase for the Point Salad game.
 */
public class PointSaladScoringPhase implements IPhase {

	private IScorer scorer;

	/**
	 * Constructor for the PointSaladScoringPhase class.
	 * By default, uses the PointSaladScorer.
	 */
	public PointSaladScoringPhase() {
		this.scorer = new PointSaladScorer();
	}

	/**
	 * Constructor for the PointSaladScoringPhase class.
	 * 
	 * @param scorer The scorer to be used for the scoring phase
	 */
	public PointSaladScoringPhase(IScorer scorer) {
		this.scorer = scorer;
	}

	@Override
	public void processPhase(State state) throws ScoringException {
		
		// Locally, to track the game state
		System.out.println("\n ----------------------------------------------------------------- ");
		System.out.println("The game is over! Computing the final scores...");
		
		IServer server = state.getServer();
		try {
			server.sendMessageToAll("The game is over! Computing the final scores...");
		} catch (Exception e) {
			throw new ScoringException("Failed to send message to all players.", e);
		}

		HashMap<Integer, AbstractPlayer> players = state.getPlayers();

		for (AbstractPlayer player : players.values()) {
			try {
				player.setScore(scorer.calculateScore(players, player.getPlayerID()));
			}
			catch (ScorerException e) {
				throw new ScoringException("Error while calculating the score for player " + player.getPlayerID(), e);
			}
		}

		HashMap<Integer, Integer> scores = new HashMap<>();
		int maxScore = 0;
		int winnerId = -1;

		for (int id : players.keySet()) {
			AbstractPlayer player = players.get(id);
			int score = player.getScore();
			scores.put(player.getPlayerID(), score);
			if (score > maxScore) {
				maxScore = player.getScore();
				winnerId = id;
			}
		}

		AbstractPlayer winningPlayer = players.get(winnerId);
		
		System.out.println("\n" + winningPlayer.getName() + " (Player ID: " + winningPlayer.getPlayerID() +
		") is the winner with a score of " + maxScore + "!\n");

		System.out.println("More details here: ");
		for (AbstractPlayer player : players.values()) {
			int playerID = player.getPlayerID();

			System.out.println(player.getName() + " (Player ID: " + playerID + ") has a score of " + scores.get(playerID) +
			 " with the following hand:\n" + player.handToString() + "\n");

			if (playerID == winnerId || player.getIsBot()) {
				continue;
			}
			try {
				server.sendMessageTo("\nYou lost with a score of " + scores.get(playerID) + "... " + winningPlayer.getName() + " (Player ID: " +
				winningPlayer.getPlayerID() + ") is the winner with a score of " + maxScore + ".", playerID);
			} catch (Exception e) {
				throw new ScoringException("Failed to send message to player " + player.getName() + " (PlayerID: " + playerID + ").", e);
			}
		}
		System.out.println();
		
		if (!winningPlayer.getIsBot()) {
			try {
				server.sendMessageTo("\nCongratulations! Your are the winner with a score of " + maxScore, winningPlayer.getPlayerID());
			} catch (Exception e) {
				throw new ScoringException("Failed to send message to player " + winningPlayer.getName() +
				" (PlayerID: " + winningPlayer.getPlayerID() + ").", e);
			}
		}

		System.out.println("Messages have been sent to all players.");
	}
	
	@Override
	public boolean proceedToNextPhase(State state) {
		// There are no other phases next. The game is over.
		return false;
	}
}
