package code.players;

import java.util.ArrayList;

import code.cards.ICard;
import code.cards.PointSaladCard;
import code.exceptions.BotLogicException;
import code.game.IScorer;
import code.game.PointSaladMarket;
import code.game.PointSaladScorer;
import code.phases.IPhase;
import code.phases.PointSaladDraftingPhase;
import code.phases.PointSaladFlippingPhase;
import code.states.State;

/**
 * Default bot logic for the Point Salad game.
 * 
 * On a drafting phase, it randomly decide whether to draft a criterion or a vegetable card. 
 * To draft a criterion card, it uses a scorer to select the best current criterion card.
 * To draft a vegetable card, it selects the first two available cards.
 * 
 * On a flipping phase, it flips the worst criterion card, only if it improves its score.
 */
public class PointSaladDefaultBotLogic implements IBotLogic {

	private IScorer scorer;

	/**
	 * Default constructor.
	 * By default, it uses a PointSaladScorer to score the cards.
	 */
	public PointSaladDefaultBotLogic() {
		this.scorer = new PointSaladScorer();
	}

	/**
	 * Constructor with a custom scorer.
	 * 
	 * @param scorer The scorer to use to score the cards.
	 */
	public PointSaladDefaultBotLogic(IScorer scorer) {
		this.scorer = scorer;
	}

	/**
	 * Get the scorer used by the bot.
	 * 
	 * @return The scorer used by the bot.
	 */
	public IScorer getScorer() {
		return scorer;
	}

	/**
	 * Set the scorer used by the bot.
	 * 
	 * @param scorer The scorer to use to score the cards.
	 */
	public void setScorer(IScorer scorer) {
		this.scorer = scorer;
	}

	/**
	 * Get the best criterion card(s) to draft, based on the current market.
	 * 
	 * @param hand The current hand of the bot
	 * @param otherHands The hands of the other players
	 * @param market The current market
	 * 
	 * @return The drafting command string for the criterion card(s)
	 * 
	 * @throws BotLogicException If an error occurs in the bot score calculation
	 */
	public String getCriterionDraft(ArrayList<ICard> hand, ArrayList<ArrayList<ICard>> otherHands, PointSaladMarket market) throws BotLogicException {
		// Draft criterion card(s)

		ArrayList<Integer> scores = new ArrayList<Integer>();

		ArrayList<PointSaladCard> criterionCards = market.getAvailableCriteria();

		for (int i = 0; i < criterionCards.size(); i++) {
			PointSaladCard card = criterionCards.get(i);

			if (card == null) {
				// Should never happen based on the definition of market.getAvailableCriteria()
				continue;
			}

			// Create dummy hand
			ArrayList<ICard> dummyHand = new ArrayList<ICard>(hand);
			dummyHand.add(card);

			try {
				int score = scorer.calculateScore(dummyHand, otherHands);
				scores.add(score);
			}
			catch (Exception e) {
				throw new BotLogicException("Failed to calculate score for criterion card n°" + (i+1) + " from the given market.", e);
			}
		}

		// In increasing order
		ArrayList<Integer> sortedScores = new ArrayList<Integer>(scores);
		sortedScores.sort(null);

		// In decreasing order
		int nbScores = scores.size();
		ArrayList<Integer> bestIndices = new ArrayList<Integer>();
		for (int i = nbScores; i >= 0; i--) {
			int currentScore = sortedScores.get(i);
			int j = scores.indexOf(currentScore);
			bestIndices.add(j);
		}

		// Select the best criterion card(s)
		String draftString = "";
		for (int i = 0; i < PointSaladMarket.CRITERION_DRAFT; i++) {
			int index = bestIndices.get(i);
			draftString += Integer.toString(index);
		}

		return draftString;
	}

	/**
	 * Get the vegetable card(s) to draft, based on the current market.
	 * 
	 * @param market The current market
	 * 
	 * @return The drafting command string for the vegetable card(s)
	 */
	public String getVegetableDraft(PointSaladMarket market) {
		// Draft vegetable cards

		String draftString = "";

		ArrayList<String> availableVegetableStrings = market.getAvailableVegetableStrings();

		// Select the first two available vegetable cards
		for (int i = 0; i < PointSaladMarket.VEGETABLE_DRAFT; i++) {
			draftString += availableVegetableStrings.get(i);
		}

		return draftString;
	}

	/**
	 * Get the move for the bot on a drafting phase.
	 * See the class description for more details.
	 * 
	 * @param state The current state of the game
	 * @param botPlayerId The id of the bot player
	 * 
	 * @return The move to make, as a String command
	 * 
	 * @throws BotLogicException If an error occurs in the bot logic
	 */
	public String getDraftingMove(State state, int botPlayerId) throws BotLogicException {
		String draftString = "";

		PointSaladMarket market = null;
		try {
			market = (PointSaladMarket) state.getMarket();
		}
		catch (ClassCastException e) {
			throw new BotLogicException("The market is not a PointSaladMarket.", e);
		}

		ArrayList<AbstractPlayer> players = state.getPlayers();

		ArrayList<ICard> hand = players.get(botPlayerId).getHand();
		ArrayList<ArrayList<ICard>> otherHands = AbstractPlayer.getOtherHands(players, botPlayerId);

		int choice = (int) (Math.random() * 2);

		if (choice == 0) {
			draftString = getCriterionDraft(hand, otherHands, market);

			if (draftString.isEmpty()) {
				// Try drafting vegetable cards instead
				draftString = getVegetableDraft(market);
			}
		}

		else {
			// Draft vegetable cards
			draftString = getVegetableDraft(market);

			if (draftString.isEmpty()) {
				// Try drafting criterion card(s) instead
				draftString = getCriterionDraft(hand, otherHands, market);
			}
		}

		if (draftString.isEmpty()) {
			// May happen while testing, but should not happen in a real game because it means the market
			// is empty, and we did not switch to the scoring phase
			throw new BotLogicException("Failed to draft any card.");
		}

		return draftString;
	}

	/**
	 * Get the move for the bot on a flipping phase.
	 * See the class description for more details.
	 * 
	 * @param state The current state of the game
	 * @param botPlayerId The id of the bot player
	 * 
	 * @return The move to make, as a String command
	 * 
	 * @throws BotLogicException If an error occurs in the bot logic
	 */
	public String getFlippingMove(State state, int botPlayerId) throws BotLogicException {
		// By default, won't flip any card
		String flipString = "n";

		ArrayList<AbstractPlayer> players = state.getPlayers();
		ArrayList<ICard> hand = players.get(botPlayerId).getHand();
		ArrayList<ArrayList<ICard>> otherHands = AbstractPlayer.getOtherHands(players, botPlayerId);

		ArrayList<PointSaladCard> convertedHand = PointSaladCard.convertHand(hand);
		ArrayList<PointSaladCard> criterionCards = PointSaladCard.getCriteriaHand(convertedHand);

		ArrayList<Integer> criterionScores = new ArrayList<Integer>();
		for (int i = 0; i < criterionCards.size(); i++) {
			PointSaladCard card = criterionCards.get(i);

			try {
				int score = card.getCriterion().computePlayerScore(hand, otherHands);
				criterionScores.add(score);
			}
			catch (Exception e) {
				throw new BotLogicException("Failed to calculate the score of criterion card n°" + (i+1) + " of the bot.", e);
			}
		}

		// In increasing order
		ArrayList<Integer> sortedScores = new ArrayList<Integer>(criterionScores);
		sortedScores.sort(null);

		if (sortedScores.size() > 0) {
			int worstScore = sortedScores.get(0);
			int worstIndex = criterionScores.indexOf(worstScore);

			if (worstScore < 0) {
				// Flip the worst criterion card that makes the bot lose points
				flipString = Integer.toString(worstIndex);
			}
		}

		return flipString;
	}

	@Override
	public String getMove(State state, int botPlayerId) throws BotLogicException {
		IPhase phase = state.getPhase();

		if (phase instanceof PointSaladDraftingPhase) {
			return getDraftingMove(state, botPlayerId);
		} else if (phase instanceof PointSaladFlippingPhase) {
			return getFlippingMove(state, botPlayerId);
		}
		else {
			throw new BotLogicException("Unsupported phase for the bot: " + phase.getClass().getName());
		}
	}
	
}
