import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.json.simple.JSONObject;

import redis.clients.jedis.Jedis;

import com.cedarsoftware.util.io.JsonReader;

public class Model {
	public static Jedis jedis = new Jedis("localhost");

	public static ArrayList<JSONObject> getMovieByTitle(String title,
			Jedis jedis) {
		Set<String> res = jedis.smembers(title);
		ArrayList<JSONObject> ls = new ArrayList<JSONObject>();
		for (String str : res) {
			try {
				ls.add((JSONObject) JsonReader.jsonToJava(str));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ls;
	}

	public static ArrayList<String> getAutoCompleteResult(Jedis jedis,
			String category, String prefix, int maxLen) {
		String setName = category + "_compl";
		ArrayList<String> res = new ArrayList<String>();
		int batchSize = 200;
		if (jedis.zrank(setName, prefix) == null) {
			return res;
		} else {
			long start = jedis.zrank(setName, prefix);
			while (res.size() < maxLen) {
				Set<String> current = jedis.zrange(setName, start, start
						+ batchSize - 1);
				start += batchSize;
				if (current == null || current.size() == 0) {
					break;
				}

				for (String entry : current) {
					int minLen = Math.min(entry.length(), prefix.length());
					if (!entry.substring(0, minLen).equalsIgnoreCase(
							prefix.substring(0, minLen))) {
						maxLen = res.size();
						break;
					}
					if (entry.endsWith("*") && res.size() < maxLen) {
						// only add whole title name
						res.add(entry.substring(0, entry.length() - 1));
					}
				}
			}
		}
		return res;
	}
}
