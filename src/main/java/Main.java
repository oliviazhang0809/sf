import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import redis.clients.jedis.Jedis;

public class Main {

	public static void main(String[] args) throws Exception {
		Jedis jedis = new Jedis("localhost");
		final String filePath = "https://data.sfgov.org/resource/yitu-d5am.json";

		JSONArray array = (JSONArray) DataFetcher.readJsonFromUrl(filePath);

		for (int i = 0; i < array.size(); i++) {
			JSONObject movie = (JSONObject) array.get(i);
			DataImporter.saveMovieToRedis(movie, jedis);
		}

		// sample print
		ArrayList<String> res = Model.getAutoCompleteResult(jedis, "title",
				"Babies", 20);
		System.out.println(res);
		ArrayList<JSONObject> movie = Model.getMovieByTitle("Babies", jedis);
		System.out.println(movie);
	}
}
