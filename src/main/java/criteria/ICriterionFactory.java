package main.java.criteria;

import main.java.exceptions.CriterionFactoryException;

/**
 * Interface for criteria factories.
 */
public interface ICriterionFactory {
	
	/**
	 * Creates a criterion from a formatted string.
	 * 
	 * @param formattedString The formatted string
	 * @return The criterion created
	 * 
	 * @throws CriterionFactoryException If the formatted string is invalid
	 */
	public ICriterion createCriterionFromFormattedString(String formattedString) throws CriterionFactoryException;
}
