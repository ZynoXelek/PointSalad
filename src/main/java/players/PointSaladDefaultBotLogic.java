package players;

import java.util.ArrayList;
import java.util.HashMap;

import cards.ICard;
import cards.PointSaladCard;
import exceptions.BotLogicException;
import game.IScorer;
import game.PointSaladMarket;
import game.PointSaladScorer;
import phases.IPhase;
import phases.PointSaladDraftingPhase;
import phases.PointSaladFlippingPhase;
import states.State;

/**
 * Default bot logic for the Point Salad game.
 * 
 * On a drafting phase, it randomly decide whether to draft a criterion or a vegetable card. 
 * To draft a criterion card, it uses a scorer to select the best current criterion card.
 * To draft a vegetable card, it selects the first two available cards.
 * 
 * On a flipping phase, if flipping a criterion card would make the bot gain points, it will flip it.
 * In this cas, it flips the criterion card that makes it score the most points.
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
	 * It always draft the maximum number of cards possible (1 by default).
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
		ArrayList<String> criterionStrings = market.getAvailableCriteriaStrings();

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
		for (int i = nbScores - 1; i >= 0; i--) {
			int currentScore = sortedScores.get(i);
			int j = scores.indexOf(currentScore);
			bestIndices.add(j);
		}

		// Select the best criterion card(s)
		int nbCardsDrafted = Integer.min(PointSaladMarket.CRITERION_DRAFT, criterionCards.size());

		String draftString = "";
		for (int i = 0; i < nbCardsDrafted; i++) {
			int index = bestIndices.get(i);
			draftString += criterionStrings.get(index);
		}

		return draftString;
	}

	/**
	 * Get the vegetable card(s) to draft, based on the current market.
	 * It will always draft the maximum number of cards possible (2 by default), or 1 if
	 * there is only 1 card in the market.
	 * 
	 * @param market The current market
	 * 
	 * @return The drafting command string for the vegetable card(s)
	 */
	public String getVegetableDraft(PointSaladMarket market) {
		// Draft vegetable cards

		String draftString = "";

		ArrayList<String> availableVegetableStrings = market.getAvailableVegetableStrings();
		int nbCardsDrafted = Integer.min(PointSaladMarket.VEGETABLE_DRAFT, availableVegetableStrings.size());

		// Select the first two available vegetable cards
		for (int i = 0; i < nbCardsDrafted; i++) {
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

		HashMap<Integer, AbstractPlayer> players = state.getPlayers();
		ArrayList<AbstractPlayer> playersList = state.getPlayersList();

		ArrayList<ICard> hand = players.get(botPlayerId).getHand();
		ArrayList<ArrayList<ICard>> otherHands = AbstractPlayer.getOtherHands(playersList, botPlayerId);

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

		HashMap<Integer, AbstractPlayer> players = state.getPlayers();
		ArrayList<AbstractPlayer> playersList = state.getPlayersList();
		ArrayList<ICard> hand = players.get(botPlayerId).getHand();
		ArrayList<ArrayList<ICard>> otherHands = AbstractPlayer.getOtherHands(playersList, botPlayerId);

		ArrayList<PointSaladCard> convertedHand = PointSaladCard.convertHand(hand);
		ArrayList<PointSaladCard> criterionCards = PointSaladCard.getCriteriaHand(convertedHand);

		if (criterionCards.isEmpty()) {
			// No criterion card to flip
			return flipString;
		}

		int currentScore = 0;

		try {
			currentScore = scorer.calculateScore(hand, otherHands);
		}
		catch (Exception e) {
			throw new BotLogicException("Failed to calculate the current score of the bot.", e);
		}

		ArrayList<Integer> scores = new ArrayList<Integer>();
		for (int i = 0; i < criterionCards.size(); i++) {
			PointSaladCard card = criterionCards.get(i);
			ArrayList<PointSaladCard> dummyHand = PointSaladCard.copyHand(convertedHand);

			// Flip the card in the dummy hand
			PointSaladCard cardToFlip = dummyHand.get(convertedHand.indexOf(card));
			cardToFlip.flip();

			ArrayList<ICard> dummyHandICard = PointSaladCard.convertToICardHand(dummyHand);

			try {
				int score = scorer.calculateScore(dummyHandICard, otherHands);
				scores.add(score);
			}
			catch (Exception e) {
				throw new BotLogicException("Failed to calculate the score of criterion card n°" + (i+1) + " of the bot.", e);
			}
		}

		// Get the maximum score and index
		int maxScore = currentScore;
		int maxIndex = -1;

		for (int i = 0; i < scores.size(); i++) {
			int score = scores.get(i);
			if (score > maxScore) {
				maxScore = score;
				maxIndex = i;
			}
		}

		if (maxIndex == -1) {
			// The maximum score is the current score of the bot.
			// Therefore it won't flip any card.
			return flipString;
		}
		
		// Flip the card that makes the bot score the most points
		flipString = Integer.toString(maxIndex);

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
