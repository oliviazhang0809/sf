import java.util.ArrayList;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class Model {
	public static Set<String> getMovieByTitle(String title, Jedis jedis) {
		return jedis.smembers(title);
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
