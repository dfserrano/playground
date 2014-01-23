import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamCatcher {

	public static void main(String[] args) {
		TwitterStreamCatcher catcher = new TwitterStreamCatcher();

		String[] locations = { "colombia", "bucaramanga", "bogota", "bogotá",
				"medellin", "medellín", "barranquilla" };

		catcher.catchTweets(0.351560, 12.554564, -78.4021, -70.052491,
				locations, locations);
	}

	public void catchTweets(double latMin, double latMax, double lonMin,
			double lonMax, String[] locations, String[] keywords) {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("X8aXy4ZMp17BYucDYvbg")
				.setOAuthConsumerSecret(
						"5WVeRiW7G2TVXFmZ1sszCrj55H1rehxhPX9gbZY")
				.setOAuthAccessToken(
						"68494040-ZjTVUGwC8RfRcXruzKzQjKrq74dDAByYpq085uEcc")
				.setOAuthAccessTokenSecret(
						"kA7H4hoHAeQ83YrUwt1PWd4HWzmgjE1GY5dHUet50");

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build())
				.getInstance();

		StatusListener listener = new MyStatusListener(latMin, latMax, lonMin,
				lonMax, locations, keywords);
		twitterStream.addListener(listener);
		twitterStream.sample();

	}

}
