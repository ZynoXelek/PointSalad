package java.cards;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Factory for creating Point Salad cards.
 */
public class PointSaladFactory implements ICardFactory{
	
	public PointSaladFactory() {
	}

	/**
	 * Creates each of the vegetable (6) cards related to a single Id from the JSON file.
	 * 
	 * @param cardData The JSON object containing the cards data: id and criteria
	 * @return The cards created
	 */
	public ArrayList<ICard> createCardsFromSingleId(JSONObject cardData) {
		ArrayList<ICard> cards = new ArrayList<ICard>();
		JSONObject criteriaObject = cardData.getJSONObject("criteria");

		for(PointSaladCard.Vegetable vegetable : PointSaladCard.Vegetable.values()) {
			String criteria = criteriaObject.getString(vegetable.toString());
			cards.add(new PointSaladCard(vegetable, criteria));
		}
		
		return cards;
	}

	/**
	 * Creates every vegetable card from a JSON object containing several ids.
	 * 
	 * @param cardsData The JSON object containing the cards data
	 * @return The cards created
	 */
	public ArrayList<ICard> createCards(JSONObject cardsData)
	{
		ArrayList<ICard> cards = new ArrayList<ICard>();

		JSONArray JSONcards = cardsData.getJSONArray("cards");
		for (int i = 0; i < JSONcards.length(); i++)
		{
			JSONObject cardData = JSONcards.getJSONObject(i);
			cards.addAll(createCardsFromSingleId(cardData));
		}

		return cards;
	}

	@Override
	public ArrayList<ICard> loadCards(String filename) {
		ArrayList<ICard> cards = new ArrayList<ICard>();

		// Reads the content of the given file
        try (InputStream fInputStream = new FileInputStream("src/resources/PointSaladManifest.json");
             Scanner scanner = new Scanner(fInputStream, "UTF-8").useDelimiter("\\A")) {

				String jsonString = scanner.hasNext() ? scanner.next() : "";
	
				JSONObject cardsData = new JSONObject(jsonString);
	
				cards = createCards(cardsData);
	
				return cards;
		} catch (Exception e) {
			throw new IllegalArgumentException("The file does not exist");
		}
	}
}
