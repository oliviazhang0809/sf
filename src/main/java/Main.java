import org.json.simple.JSONArray;

public class Main {

	public static void main(String[] args) throws Exception {

		final String filePath = "https://data.sfgov.org/resource/yitu-d5am.json";

		JSONArray array = DataFetcher.readJsonFromUrl(filePath);

		// for (int i = 0; i < array.size(); i++) {
		// JSONObject movie = (JSONObject) array.get(i);
		// DataImporter.saveMovieToRedis(movie, jedis);
		// }
		//
		// // sample print
		// ArrayList<String> res = Model.getAutoCompleteResult(jedis, "title",
		// "A", 20);
		// System.out.println(res);
	}
}
