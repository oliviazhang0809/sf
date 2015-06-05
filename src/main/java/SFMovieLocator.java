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

		get(new Route("/test/:prefix") {
			@Override
			public Object handle(Request request, Response response) {
				System.out.println("dog dog dog");
				return "hello dog";
			}
		});

		get(new Route("/autocomplete/:prefix") {
			@Override
			public Object handle(Request request, Response response) {
				ArrayList<String> res = model.getAutoCompleteResult(jedis,
						"title", request.params(":prefix"), 10);
				String json = new Gson().toJson(res);
				return json;
			}
		});

		get(new Route("/movie/:title") {
			@Override
			public Object handle(Request request, Response response) {
				ArrayList<JSONObject> movieInfo = model.getMovieByTitle(
						request.params(":title"), jedis);
				return movieInfo;
			}
		});
	}
}