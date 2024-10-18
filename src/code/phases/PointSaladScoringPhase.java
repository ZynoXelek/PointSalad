package code.phases;

import code.exceptions.ScorerException;
import code.exceptions.ScoringException;
import code.game.IScorer;
import code.game.PointSaladScorer;
import code.network.IServer;
import code.players.AbstractPlayer;
import code.states.State;

import java.util.ArrayList;
import java.util.HashMap;

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
		HashMap<Integer, AbstractPlayer> players = state.getPlayers();

		for (AbstractPlayer player : players.values()) {
			try {
				player.setScore(scorer.calculateScore(players, player.getPlayerID()));
			}
			catch (ScorerException e) {
				throw new ScoringException("Error while calculating the score for player " + player.getPlayerID(), e);
			}
		}

		int maxScore = 0;
		int winnerIndex = -1;

		for (int i = 0; i < players.size(); i++) {
			AbstractPlayer player = players.get(i);
			if (player.getScore() > maxScore) {
				maxScore = player.getScore();
				winnerIndex = i;
			}
		}
		
		IServer server = state.getServer();
		try {
			server.sendMessageToAll("The game is over! Computing the final scores...");
		} catch (Exception e) {
			throw new ScoringException("Failed to send message to all players.", e);
		}

		for (int i = 0; i < players.size(); i++) {
			AbstractPlayer player = players.get(i);
			int playerID = player.getPlayerID();
			
			if(player.getPlayerID() == winnerIndex) {
				try {
					server.sendMessageTo("\nCongratulations! Your are the winner with a score of " + maxScore, playerID);
				} catch (Exception e) {
					throw new ScoringException("Failed to send message to player of index " + i + ", corresponding to client ID " + playerID + ".", e);
				}
			} else {
				try {
					server.sendMessageTo("\nPlayer " + player.getPlayerID() + " is the winner with a score of " + maxScore, playerID);
				} catch (Exception e) {
					throw new ScoringException("Failed to send message to player of index " + i + ", corresponding to client ID " + playerID + ".", e);
				}
			}
		}
	}
	
	@Override
	public boolean proceedToNextPhase(State state) {
		// There are no other phases next. The game is over.
		return false;
	}
}
