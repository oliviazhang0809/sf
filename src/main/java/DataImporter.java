import java.io.IOException;
import java.util.Set;

import org.json.simple.JSONObject;

import redis.clients.jedis.Jedis;

import com.cedarsoftware.util.io.JsonWriter;

/**
 * Save data to redis
 */
public class DataImporter {

	public static void saveMovieToRedis(JSONObject movie, Jedis jedis) {
		addToAutoComplete(jedis, (String) movie.get("title"), "title");
		processFilmData(movie, jedis);
	}

	/**
	 * add sorted prefix set to "category_compl"
	 */
	public static boolean addToAutoComplete(Jedis jedis, String title,
			String category) {
		if (title != null && title.length() > 0) {
			// System.out.println("Loading title" + " " + title
			// + " into redis db for auto complete pre-processing");
			title = title.trim();
			String setName = category + "_compl"; // name of set
			for (int i = 1; i <= title.length(); i++) {
				String prefix = title.substring(0, i);
				jedis.zadd(setName, 0, prefix);
			}
			jedis.zadd(setName, 0, title + "*");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Add serialized movie json object to redis, keyed by title
	 */
	public static boolean processFilmData(JSONObject movie, Jedis jedis) {
		@SuppressWarnings("unchecked")
		Set<String> keyset = movie.keySet();
		// only add data data with location info -- coz we want to show on map
		if (keyset.contains("locations") && movie.get("locations") != null) {
			try {
				String json = JsonWriter.objectToJson(movie);
				jedis.sadd((String) movie.get("title"), json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
