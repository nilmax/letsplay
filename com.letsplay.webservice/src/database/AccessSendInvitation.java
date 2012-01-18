package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.letsplay.datamodel.Place;
import com.letsplay.datamodel.SportEntry;

import utils.GlobalPositionSystem;
import exception.DataBaseException;

public class AccessSendInvitation {

	private Connection connection;
	private final static String INVITATION_MESSAGE = "Invitation for activities";

	public AccessSendInvitation(Connection connection) {
		this.connection = connection;
	}

	public void execute(String login, SportEntry sportEntry)
			throws DataBaseException {
		try {

			Place place = sportEntry.getPlace();
			double placeLatitude = place.getLatitude();
			double placeLongitude = place.getLongitude();

			// search all users and get the closest ones
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT LOGIN, PASS, FIRST_NAME, LAST_NAME, "
							+ "GENDER, ZIPCODE, PICTURE, AGE, "
							+ "MILE_RANGE, LATITUDE, LONGITUDE "
							+ "FROM lets_play.users");

			while (resultSet.next()) {
				String localUserId = resultSet.getString("LOGIN");
				double mileRange = resultSet.getDouble("MILE_RANGE");
				double userLatitude = resultSet.getDouble("LATITUDE");
				double userLongitude = resultSet.getDouble("LONGITUDE");

				double distance = GlobalPositionSystem.getDistance(
						placeLatitude, placeLongitude, userLatitude,
						userLongitude, 'M');

				if (distance <= mileRange) {
					// send Invitation
					Statement statementInsert = connection.createStatement();
					statementInsert
							.executeUpdate("INSERT INTO inbox (inbox.MESSAGE, inbox.ID_USER, inbox.ID_SPORT, inbox.READ) "
									+ "VALUES ('"
									+ INVITATION_MESSAGE
									+ "', '"
									+ localUserId
									+ "', "
									+ sportEntry.getId()
									+ ", " + 0 + ")");
					statementInsert.close();
				}
			}

			resultSet.close();
			statement.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
		}
	}
}
