package code.criteria.point_salad_criteria;

import java.util.ArrayList;

import code.criteria.ICriterion;
import code.exceptions.CriterionException;
import code.players.AbstractPlayer;

/**
 * Abstract Class to hold criteria for the Point Salad card.
 */
public abstract class AbstractPointSaladCriterion implements ICriterion {

	@Override
	public String getCriterionDisplay() {
		return this.toString();
	}

	@Override
	public abstract int computePlayerScore(ArrayList<AbstractPlayer> players, int playerIndex) throws CriterionException;

	@Override
	public abstract String toString();
}
