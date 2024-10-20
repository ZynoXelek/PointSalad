package criteria;

import java.util.ArrayList;

import cards.PointSaladCard.Vegetable;
import criteria.point_salad_criteria.*;
import criteria.point_salad_criteria.AbstractPointSaladCriterion.CriterionType;
import exceptions.CriterionException;
import exceptions.CriterionFactoryException;

/**
 * Factory for creating criteria for the Point Salad game.
 */
public class PointSaladCriterionFactory implements ICriterionFactory {

	/**
	 * Gets the criterion type of a formatted string.
	 * 
	 * @param formattedString The formatted string to determine the criterion type
	 * 
	 * @return The criterion type of the formatted string
	 */
	public CriterionType getCriterionType(String formattedString) {
		// First determines the criterion type
		CriterionType type = null;

		if (formattedString.contains("EVEN"))
		{
			type = CriterionType.EVEN_ODD;
		}
		else if (formattedString.contains("+"))
		{
			type = CriterionType.COMBINATION;
		}
		else if (formattedString.contains("MOST TOTAL"))
		{
			type = CriterionType.MOST_TOTAL;
		}
		else if (formattedString.contains("FEWEST TOTAL"))
		{
			type = CriterionType.FEWEST_TOTAL;
		}
		else if (formattedString.contains("COMPLETE SET"))
		{
			type = CriterionType.COMPLETE_SET;
		}
		else if (formattedString.contains("MISSING"))
		{
			type = CriterionType.MISSING_TYPE;
		}
		else if (formattedString.contains("VEGETABLE TYPE"))
		{
			type = CriterionType.PER_VEGGIE_TYPE;
		}
		else if (formattedString.contains("MOST"))
		{
			// Must be last to avoid matching "MOST TOTAL"
			type = CriterionType.MOST;
		}
		else if (formattedString.contains("FEWEST"))
		{
			// Must be last to avoid matching "FEWEST TOTAL"
			type = CriterionType.FEWEST;
		}
		else if (formattedString.contains("/"))
		{
			// Must be last to avoid matching "/ VEGETABLE TYPE" or "/ MISSING VEGETABLE TYPE"
			type = CriterionType.PER_VEGGIE;
		}

		return type;
	}

	/**
	 * Creates a criterion of the given type from a formatted string.
	 * 
	 * @param formattedString The formatted string to create the criterion from
	 * @param type The type of criterion to create
	 * 
	 * @return The criterion created
	 * 
	 * @throws CriterionFactoryException If the formatted string is invalid
	 */
	public AbstractPointSaladCriterion createCriterionOfType(String formattedString, CriterionType type) throws CriterionFactoryException {
		// TODO: If a clean way to move this code in each criterion class is found, it should be done.
		// I have not found a "clean" way to do it yet, as static methods can not be abstract nor overridden.
		// A possible solution would be to create an instance method which creates a new criterion based on the formatted string, but it
		// requires to first create a "blank" instance of the corresponding criterion class, which is not ideal.

		AbstractPointSaladCriterion criterion = null;

		if (type == CriterionType.MOST || type == CriterionType.FEWEST) {
			// Both MOST and FEWEST have the same format
			// example: "MOST LETTUCE = 10"
			// example: "FEWEST CARROT = 7"
			Vegetable veggie = null;
			int points = 0;

			String[] parts = formattedString.split(" ");

			String veggieString = parts[1];
			String pointsString = parts[3];

			try {
				veggie = Vegetable.valueOf(veggieString);
			} catch (IllegalArgumentException e) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': could not parse the vegetable from '" + veggieString + "' of " + type + "-type criterion.", e);
			}

			try {
				points = Integer.parseInt(pointsString);
			} catch (NumberFormatException e) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': could not parse the points from '" + pointsString + "' of " + type + "-type criterion.", e);
			}

			if (type == CriterionType.MOST) {
				criterion = new PointSaladMostCriterion(veggie, points);
			} else {
				criterion = new PointSaladFewestCriterion(veggie, points);
			}
		}

		else if (type == CriterionType.EVEN_ODD) {
			// example: "ONION: EVEN=7, ODD=3"
			Vegetable veggie = null;
			int evenPoints = 0;
			int oddPoints = 0;
			
			String veggieString = formattedString.split(":")[0];

			String[] parts = formattedString.replace(",", "").split(" ");
			String evenString = parts[1].split("=")[1];
			String oddString = parts[2].split("=")[1];

			try {
				veggie = Vegetable.valueOf(veggieString);
			} catch (IllegalArgumentException e) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': could not parse the vegetable from '" + veggieString + "' of " + type + "-type criterion.", e);
			}

			try {
				evenPoints = Integer.parseInt(evenString);
			} catch (NumberFormatException e) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': could not parse the even points from '" + evenString + "' for even " + type + "-type criterion.", e);
			}

			try {
				oddPoints = Integer.parseInt(oddString);
			} catch (NumberFormatException e) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': could not parse the odd points from '" + oddString + "' for odd " + type + "-type criterion.", e);
			}

			criterion = new PointSaladEvenOddCriterion(veggie, evenPoints, oddPoints);
		}

		else if (type == CriterionType.PER_VEGGIE) {
			// examples: "2 / TOMATO",	"1 / LETTUCE, 1 / TOMATO",	"3 / CARROT, -2 / ONION",	"4/LETTUCE, -2/TOMATO, -2/CABBAGE"
			ArrayList<Vegetable> veggies = new ArrayList<>();
			ArrayList<Integer> points = new ArrayList<>();

			String[] commaParts = formattedString.split(",");
			for (int i = 0; i < commaParts.length; i++) {
				String commaPart = commaParts[i];
				String[] parts = commaPart.split("/");
				Vegetable veggie = null;
				int veggiePoints = 0;

				String pointsString = parts[0].trim();
				String veggieString = parts[1].trim();

				try {
					veggie = Vegetable.valueOf(veggieString);
				} catch (IllegalArgumentException e) {
					throw new CriterionFactoryException("Invalid formatted string '" +
					formattedString + "': could not parse the vegetable from '" + veggieString + "' in part '" + commaPart + 
					"' of " + type + "-type criterion.", e);
				}

				try {
					veggiePoints = Integer.parseInt(pointsString);
				} catch (NumberFormatException e) {
					throw new CriterionFactoryException("Invalid formatted string '" +
					formattedString + "': could not parse the points from '" + pointsString + "' in part '" + commaPart + 
					"' of " + type + "-type criterion.", e);
				}

				veggies.add(veggie);
				points.add(veggiePoints);
			}

			criterion = new PointSaladPerVeggieCriterion(veggies, points);
		}

		else if (type == CriterionType.COMBINATION) {
			// example: "LETTUCE + LETTUCE = 5",	"CABBAGE + ONION = 5",	"CARROT + CARROT + CARROT = 8"
			ArrayList<Vegetable> veggies = new ArrayList<>();
			int points = 0;

			String[] parts = formattedString.split("=");
			String[] veggieParts = parts[0].replaceAll("\\s", "").split("\\+");
			String pointsString = parts[1].trim();

			for (int i = 0; i < veggieParts.length; i++) {
				Vegetable veggie = null;

				String veggieString = veggieParts[i];

				try {
					veggie = Vegetable.valueOf(veggieString);
				} catch (IllegalArgumentException e) {
					throw new CriterionFactoryException("Invalid formatted string '" +
					formattedString + "': could not parse the vegetable from '" + veggieString + "' of " + type + "-type criterion.", e);
				}

				veggies.add(veggie);
			}

			try {
				points = Integer.parseInt(pointsString);
			} catch (NumberFormatException e) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': could not parse the points from '" + pointsString + "' of " + type + "-type criterion.", e);
			}

			if (veggies.size() < 1) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': a combination criterion should have at least two vegetables.");
			} else if (veggies.size() == 1) {
				System.err.println("Warning: a combination criterion should have at least two vegetables. Creating a PointSaladPerVeggieCriterion instead.");
				ArrayList<Integer> pointsList = new ArrayList<>();
				pointsList.add(points);
				criterion = new PointSaladPerVeggieCriterion(veggies, pointsList);
			}
			else {
				try {
					criterion = new PointSaladCombinationCriterion(veggies, points);
				} catch (CriterionException e) {
					// Should never happen thanks to the check above
					throw new CriterionFactoryException("Invalid formatted string '" +
					formattedString + "' for a Combination criterion: " + e.getMessage(), e);
				}
			}
		}
		
		else if (type == CriterionType.MOST_TOTAL || type == CriterionType.FEWEST_TOTAL) {
			// Most and Fewest Total have the same format
			// example: "MOST TOTAL VEGETABLE = 10",	"FEWEST TOTAL VEGETABLE = 7"
			int points = 0;

			String[] parts = formattedString.split("=");
			String pointsString = parts[1].trim();

			try {
				points = Integer.parseInt(pointsString);
			} catch (NumberFormatException e) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': could not parse the points from '" + pointsString + "' of " + type + "-type criterion.", e);
			}

			if (type == CriterionType.MOST_TOTAL) {
				criterion = new PointSaladMostTotalCriterion(points);
			} else {
				criterion = new PointSaladFewestTotalCriterion(points);
			}
		}

		else if (type == CriterionType.COMPLETE_SET) {
			// example: "COMPLETE SET = 12"
			int points = 0;

			String[] parts = formattedString.split("=");
			String pointsString = parts[1].trim();

			try {
				points = Integer.parseInt(pointsString);
			} catch (NumberFormatException e) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': could not parse the points from '" + pointsString + "' of " + type + "-type criterion.", e);
			}

			criterion = new PointSaladCompleteSetCriterion(points);
		}

		else if (type == CriterionType.MISSING_TYPE) {
			// example: "5 / MISSING VEGETABLE TYPE"
			int points = 0;

			String[] parts = formattedString.split("/");
			String pointsString = parts[0].trim();

			try {
				points = Integer.parseInt(pointsString);
			} catch (NumberFormatException e) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': could not parse the points from '" + pointsString + "' of " + type + "-type criterion.", e);
			}

			criterion = new PointSaladPerMissingVeggieTypeCriterion(points);
		}

		else if (type == CriterionType.PER_VEGGIE_TYPE) {
			// example: "5 / VEGETABLE TYPE >=3", "3 / VEGETABLE TYPE >=2"
			int minNumberOfEachVeggie = 0;
			int pointsPerVeggieType = 0;

			String[] pointParts = formattedString.split("/");
			String[] nbParts = formattedString.split(">=");
			String pointsString = pointParts[0].trim();
			String nbString = nbParts[1].trim();

			try {
				pointsPerVeggieType = Integer.parseInt(pointsString);
			} catch (NumberFormatException e) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': could not parse the points from '" + pointsString + "' of " + type + "-type criterion.", e);
			}

			try {
				minNumberOfEachVeggie = Integer.parseInt(nbString);
			} catch (NumberFormatException e) {
				throw new CriterionFactoryException("Invalid formatted string '" +
				formattedString + "': could not parse the minimum number of each vegetable from '" + nbString + "' of " + type + "-type criterion.", e);
			}

			criterion = new PointSaladPerVeggieTypeCriterion(minNumberOfEachVeggie, pointsPerVeggieType);
		}

		else {
			// Given type is not supported
			throw new CriterionFactoryException("Invalid criterion type: " + type);
		}

		return criterion;
	}

	@Override
	public AbstractPointSaladCriterion createCriterionFromFormattedString(String formattedString) throws CriterionFactoryException {
		// First determines the criterion type
		CriterionType type = getCriterionType(formattedString);

		// Then creates the criterion based on the type
		AbstractPointSaladCriterion criterion = createCriterionOfType(formattedString, type);

		return criterion;
	}
}
