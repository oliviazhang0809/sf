import static spark.Spark.get;
import spark.Request;
import spark.Response;
import spark.Route;

public class SFMovieLocator {
	public static void main(String[] args) {
		get(new Route("/index") {
			@Override
			public Object handle(Request request, Response response) {
				return "Welcome to SF movie locator";
			}
		});
	}
}