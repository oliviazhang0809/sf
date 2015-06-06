import static spark.Spark.get;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import redis.clients.jedis.Jedis;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.google.gson.Gson;

public class SFMovieLocator {
	private static Model model = new Model();
	public static Jedis jedis = new Jedis("localhost");

	public static void main(String[] args) {
		Spark.staticFileLocation("/web");

		get(new Route("/autocomplete/:prefix") {
			@Override
			public Object handle(Request request, Response response) {
				ArrayList<String> res = model.getAutoCompleteResult(jedis,
						"title", request.params(":prefix"), 8);
				String json = new Gson().toJson(res);
				return json;
			}
		});

		Spark.post(new Route("/movie") {
			@Override
			public Object handle(Request request, Response response) {
				String title = request.body().split("=")[1].replace("+", " ");
				ArrayList<JSONObject> movieInfo = model.getMovieByTitle(title,
						jedis);
				String json = new Gson().toJson(movieInfo);
				return json;
			}
		});
	}
}