package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.letsplay.datamodel.Place;
import com.letsplay.datamodel.UserEntry;

import utils.GlobalPositionSystem;
import utils.Utils;
import exception.DataBaseException;

public class AccessListOfUsers {

	private Connection connection;
	private AccessUserCoordinates userCoordinates;

	public AccessListOfUsers(Connection connection) {
		this.connection = connection;
	}

	public ArrayList<UserEntry> execute(String login)
			throws DataBaseException {
		try {
			userCoordinates = new AccessUserCoordinates(connection,login);

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT LOGIN, PASS, FIRST_NAME, LAST_NAME, "
							+ "GENDER, ZIPCODE, PICTURE, AGE, "
							+ "MILE_RANGE, LATITUDE, LONGITUDE "
							+ "FROM lets_play.users");

			ArrayList<UserEntry> listUserEntry = new ArrayList<UserEntry>();

			while (resultSet.next()) {
				UserEntry userEntry = new UserEntry();
				userEntry.setLogin(resultSet.getString("LOGIN"));
				userEntry.setPassword(resultSet.getString("PASS"));
				userEntry.setFirstName(resultSet.getString("FIRST_NAME"));
				userEntry.setLastName(resultSet.getString("LAST_NAME"));
				userEntry.setGender(resultSet.getString("GENDER"));
				userEntry.setZipcode(resultSet.getInt("ZIPCODE"));
				userEntry.setPicture(Utils.InputStreamToByteArray(resultSet
						.getBinaryStream("PICTURE")));
				userEntry.setAge(resultSet.getInt("AGE"));
				userEntry.setMileRange(resultSet.getDouble("MILE_RANGE"));
				userEntry.setLatitude(resultSet.getDouble("LATITUDE"));
				userEntry.setLongitude(resultSet.getDouble("LONGITUDE"));
				(new AccessPreferredSports(connection)).execute(userEntry);

				double distance = GlobalPositionSystem.getDistance(userCoordinates.getLatitude(),
						userCoordinates.getLongitude(), userEntry.getLatitude(), userEntry
								.getLongitude(), 'M');
				userEntry.setDistance(distance);
				if (distance <= userCoordinates.getMileRange()) {
					if (!userEntry.getLogin().equalsIgnoreCase(login)) {
						listUserEntry.add(userEntry);
						
						String query = "SELECT ID_FROM, ID_TO "
								+ "FROM lets_play.favorites " 
								+ "WHERE (ID_FROM = '"+ login + "' AND ID_TO = '" +userEntry.getLogin()+ "') OR "
								+ "(ID_FROM = '"+ userEntry.getLogin() + "' AND ID_TO = '" + login + "')";
						
						Statement statementFavorite = connection.createStatement();
						ResultSet resultSetFavorite = statementFavorite
								.executeQuery(query);

						while (resultSetFavorite.next()) {
							userEntry.setFavorite(true);
						}
						
					}
				}
			}
			resultSet.close();
			statement.close();

			return listUserEntry;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
		}
	}
}
