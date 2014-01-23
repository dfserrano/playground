import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import twitter4j.GeoLocation;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class MyStatusListener implements StatusListener {

	private double latMin;
	private double latMax;
	private double lonMin;
	private double lonMax;
	private String[] locations;
	private String[] keywords;

	private Connection conexion = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public MyStatusListener(double latMin, double latMax, double lonMin,
			double lonMax, String[] locations, String[] keywords) {
		this.latMin = latMin;
		this.latMax = latMax;
		this.lonMin = lonMin;
		this.lonMax = lonMax;
		this.locations = locations;
		this.keywords = keywords;
	}

	public void onStatus(Status status) {

		String location = status.getUser().getLocation().toLowerCase();
		GeoLocation geoLocation = status.getGeoLocation();
		String text = status.getText();
		boolean targetLocation = false;
		boolean targetGeoLocation = false;
		boolean targetKeyword = false;

		for (String loc : locations) {
			if (location.contains(loc)) {
				targetLocation = true;
			}
		}

		for (String keyword : keywords) {
			if (text.contains(keyword)) {
				targetKeyword = true;
			}
		}

		if (geoLocation != null && geoLocation.getLatitude() >= latMin
				&& geoLocation.getLatitude() <= latMax
				&& geoLocation.getLongitude() >= lonMin
				&& geoLocation.getLongitude() <= lonMax) {
			targetGeoLocation = true;
		}

		if (targetGeoLocation || targetLocation || targetKeyword) {
			writeToDatabase(status);
			System.out.println("@" + status.getUser().getScreenName() + " - "
					+ status.getText());
		}

	}

	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
		// System.out.println("Got a status deletion notice id:"+
		// statusDeletionNotice.getStatusId());
	}

	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		// System.out.println("Got track limitation notice:"+
		// numberOfLimitedStatuses);
	}

	public void onScrubGeo(long userId, long upToStatusId) {
		// System.out.println("Got scrub_geo event userId:" + userId +
		// " upToStatusId:" + upToStatusId);
	}

	public void onStallWarning(StallWarning warning) {
		// System.out.println("Got stall warning:" + warning);
	}

	public void onException(Exception ex) {
		ex.printStackTrace();
	}

	private void writeToDatabase(Status status) {
		try {
			String databaseName = "twitter";
			String username = "root";
			String password = "Giba007G";

			// This will load the MySQL driver, each DB has its own
			// driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			conexion = DriverManager.getConnection("jdbc:mysql://localhost/"
					+ databaseName + "?" + "user=" + username + "&password="
					+ password);

			preparedStatement = conexion
					.prepareStatement("INSERT INTO ColombianTweet(id, text, created_at, lat, "
							+ "lon, country_code, user_id, is_retweeted, location) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			preparedStatement.setLong(1, status.getId());
			preparedStatement.setString(2, status.getText());
			preparedStatement.setTimestamp(3, new Timestamp(status
					.getCreatedAt().getTime()));

			if (status.getGeoLocation() != null) {
				preparedStatement.setDouble(4, status.getGeoLocation()
						.getLatitude());
				preparedStatement.setDouble(5, status.getGeoLocation()
						.getLongitude());
			} else {
				preparedStatement.setDouble(4, 0);
				preparedStatement.setDouble(5, 0);
			}

			if (status.getPlace() != null) {
				preparedStatement.setString(6, status.getPlace()
						.getCountryCode());
			} else {
				preparedStatement.setString(6, null);
			}
			preparedStatement.setLong(7, status.getUser().getId());

			int isRetweet = (status.isRetweet()) ? 1 : 0;
			preparedStatement.setInt(8, isRetweet);
			preparedStatement.setString(9, status.getUser().getLocation());

			preparedStatement.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (conexion != null) {
				conexion.close();
			}
		} catch (Exception e) {

		}
	}
}
