package cards;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import criteria.ICriterion;
import criteria.ICriterionFactory;
import criteria.PointSaladCriterionFactory;
import exceptions.CardFactoryException;

/**
 * Factory for creating Point Salad cards.
 */
public class PointSaladCardFactory implements ICardFactory{

	private ICriterionFactory criterionFactory = new PointSaladCriterionFactory();
	
	/**
	 * Constructor for the PointSaladFactory.
	 * By default, uses the PointSaladCriterionFactory.
	 */
	public PointSaladCardFactory() {}

	/**
	 * Constructor for the PointSaladFactory.
	 * 
	 * @param criterionFactory The factory for the criteria creation to be used
	 */
	public PointSaladCardFactory(ICriterionFactory criterionFactory) {
		this.criterionFactory = criterionFactory;
	}

	/**
	 * Creates each of the vegetable (6) cards related to a single Id from the JSON file.
	 * 
	 * @param cardData The JSON object containing the cards data: id and criteria
	 * 
	 * @return The cards created
	 * 
	 * @throws CardFactoryException If the card data is not formatted correctly
	 */
	private ArrayList<ICard> createCardsFromSingleId(JSONObject cardData) throws CardFactoryException {
		ArrayList<ICard> cards = new ArrayList<ICard>();
		JSONObject criteriaObject = cardData.getJSONObject("criteria");

		for(PointSaladCard.Vegetable vegetable : PointSaladCard.Vegetable.values()) {
			String criterionString = criteriaObject.getString(vegetable.toString());
			try {
				ICriterion criterion = criterionFactory.createCriterionFromFormattedString(criterionString);
				PointSaladCard card = new PointSaladCard(vegetable, criterion);
				cards.add(card);
			} catch (Exception e) {
				throw new CardFactoryException("The card data is not formatted correctly", e);
			}
		}
		
		return cards;
	}

	/**
	 * Creates every vegetable card from a JSON object containing several ids.
	 * 
	 * @param cardsData The JSON object containing the cards data
	 * 
	 * @return The cards created
	 * 
	 * @throws CardFactoryException If the card data is not formatted correctly
	 */
	private ArrayList<ICard> createCards(JSONObject cardsData) throws CardFactoryException {
		ArrayList<ICard> cards = new ArrayList<ICard>();

		JSONArray JSONcards = cardsData.getJSONArray("cards");
		for (int i = 0; i < JSONcards.length(); i++)
		{
			JSONObject cardData = JSONcards.getJSONObject(i);
			try {
				ArrayList<ICard> cardsAtId = createCardsFromSingleId(cardData);
				cards.addAll(cardsAtId);
			}
			catch (Exception e) {
				throw new CardFactoryException("The card data is not formatted correctly at id " + i, e);
			}
		}

		return cards;
	}

	@Override
	public ArrayList<ICard> loadCards(String filename) throws CardFactoryException {
		ArrayList<ICard> cards = new ArrayList<ICard>();

		// Reads the content of the given file
        try (InputStream fInputStream = new FileInputStream(filename);
             Scanner scanner = new Scanner(fInputStream, "UTF-8").useDelimiter("\\A")) {

				String jsonString = scanner.hasNext() ? scanner.next() : "";
	
				JSONObject cardsData = new JSONObject(jsonString);
	
				cards = createCards(cardsData);
	
				return cards;
		} catch (FileNotFoundException e) {
			throw new CardFactoryException("The file does not exist", e);
		} catch (CardFactoryException e) {
			throw e;
		} catch (Exception e) {
			throw new CardFactoryException("An error occurred while loading the cards", e);
		}
	}
}
