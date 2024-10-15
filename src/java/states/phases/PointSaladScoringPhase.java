package java.states.phases;

import java.exceptions.ScoringException;
import java.game.IScorer;
import java.game.PointSaladScorer;
import java.network.IServer;
import java.players.AbstractPlayer;
import java.states.State;
import java.util.ArrayList;

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
		ArrayList<AbstractPlayer> players = state.getPlayers();

		for (AbstractPlayer player : players) {
			player.setScore(scorer.calculateScore(player));
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

		for (int i = 0; i < players.size(); i++) {
			AbstractPlayer player = players.get(i);
			int playerID = player.getPlayerID();
			
			if(player.getPlayerID() == winnerIndex) {
				try {
					server.sendMessageTo("\nCongratulations! Your are the winner with a score of " + maxScore, playerID);
				} catch (Exception e) {
					throw new ScoringException("Failed to send message to player of index " + i + ", corresponding to client of index " + playerID + ".");
				}
			} else {
				try {
					server.sendMessageTo("\nPlayer " + player.getPlayerID() + " is the winner with a score of " + maxScore, playerID);
				} catch (Exception e) {
					throw new ScoringException("Failed to send message to player of index " + i + ", corresponding to client of index " + playerID + ".");
				}
			}
		}
	}
	
}
