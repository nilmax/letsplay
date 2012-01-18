package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.letsplay.datamodel.Place;
import com.letsplay.datamodel.SportEntry;
import com.letsplay.datamodel.UserEntry;

import utils.GlobalPositionSystem;
import utils.Utils;
import exception.DataBaseException;


// Esse codigo precisa ser refeito por medidas de performance
public class AccessSpecificSport {

	private Connection connection;
	private AccessUserCoordinates userCoordinates;
	private HashMap<String, SportEntry> mapIdSport = new HashMap<String, SportEntry>();

	public AccessSpecificSport(Connection connection) {
		this.connection = connection;
	}

	public SportEntry execute(String userId, int sportId) throws DataBaseException {
		try {
			userCoordinates = new AccessUserCoordinates(connection, userId);

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT  sports.ID, sports.TYPE, sports.DATE, sports.TIME, sports.PLACE, sports.NUMBER_OF_PLAYERS, "
							+ "places.ID, places.NAME, places.ADDRESS, places.LATITUDE, places.LONGITUDE,"
							+ "users.LOGIN, users.PASS, users.FIRST_NAME, users.LAST_NAME, "
							+ "users.GENDER, users.ZIPCODE, users.PICTURE, users.AGE, "
							+ "users.MILE_RANGE, users.LATITUDE, users.LONGITUDE "
							+ "FROM   sports, places, users "
							+ "WHERE  sports.PLACE = places.ID "
							+ "AND sports.HOST = users.LOGIN");

			ArrayList<SportEntry> listSportEntry = new ArrayList<SportEntry>();

			while (resultSet.next()) {
				// get place
				Place place = new Place();
				place.setId(resultSet.getInt("places.ID"));
				place.setName(resultSet.getString("places.NAME"));
				place.setAddress(resultSet.getString("places.ADDRESS"));
				place.setLatitude(resultSet.getDouble("places.LATITUDE"));
				place.setLongitude(resultSet.getDouble("places.LONGITUDE"));
				
				// get host
				UserEntry userEntry = new UserEntry();
				userEntry.setLogin(resultSet.getString("users.LOGIN"));
				userEntry.setPassword(resultSet.getString("users.PASS"));
				userEntry.setFirstName(resultSet.getString("users.FIRST_NAME"));
				userEntry.setLastName(resultSet.getString("users.LAST_NAME"));
				userEntry.setGender(resultSet.getString("users.GENDER"));
				userEntry.setZipcode(resultSet.getInt("users.ZIPCODE"));
				userEntry.setPicture(Utils.InputStreamToByteArray(resultSet
						.getBinaryStream("users.PICTURE")));
				userEntry.setAge(resultSet.getInt("users.AGE"));
				userEntry.setMileRange(resultSet.getDouble("users.MILE_RANGE"));
				userEntry.setLatitude(resultSet.getDouble("users.LATITUDE"));
				userEntry.setLongitude(resultSet.getDouble("users.LONGITUDE"));
				(new AccessPreferredSports(connection)).execute(userEntry);
				
				// get sport
				SportEntry sportEntry = new SportEntry();
				sportEntry.setPlace(place);
				sportEntry.setHost(userEntry);
				sportEntry.setId(resultSet.getInt("sports.ID"));
				sportEntry.setSportType(resultSet.getInt("sports.TYPE"));
				sportEntry.setDate(Utils.getDate(resultSet
						.getString("sports.DATE")));
				sportEntry.setTime(Utils.getTime(resultSet
						.getString("sports.TIME")));
				sportEntry.setNumberOfPlayers(resultSet
						.getInt("sports.NUMBER_OF_PLAYERS"));
				sportEntry.setConfirmedNumberOfPlayers(0);
				sportEntry.setAmISubscribed(false);
				mapIdSport
						.put(Integer.toString(sportEntry.getId()), sportEntry);

				double distance = GlobalPositionSystem.getDistance(
						userCoordinates.getLatitude(), userCoordinates
								.getLongitude(), place.getLatitude(), place
								.getLongitude(), 'M');
				place.setDistance(distance);
				if (distance <= userCoordinates.getMileRange()) {
					listSportEntry.add(sportEntry);
				}
			}
			resultSet.close();
			statement.close();

			calculateConfirmedNumberOfPlayers();
			return verifyAmISubscribed(userId,sportId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
		}
	}

	private void calculateConfirmedNumberOfPlayers() throws DataBaseException {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT ID_SPORT, COUNT(*) as 'NUMBERS_OF_PLAYERS' "
							+ "FROM REL_USERS_SPORTS " + "GROUP BY ID_SPORT;");

			while (resultSet.next()) {
				String sportID = Integer.toString(resultSet.getInt("ID_SPORT"));
				SportEntry sportEntry = mapIdSport.get(sportID);
				sportEntry.setConfirmedNumberOfPlayers(resultSet
						.getInt("NUMBERS_OF_PLAYERS"));
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(e.getMessage());
		}
	}

	private SportEntry verifyAmISubscribed(String userId, int sportId) throws DataBaseException {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT ID_USER, ID_SPORT "
							+ "FROM REL_USERS_SPORTS " + "WHERE ID_USER = '"
							+ userId + "';");

			while (resultSet.next()) {
				String sportID = Integer.toString(resultSet.getInt("ID_SPORT"));
				SportEntry sportEntry = mapIdSport.get(sportID);
				sportEntry.setAmISubscribed(true);
				if (sportEntry.getId()==sportId) {
					return sportEntry;
				}
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(e.getMessage());
		}
		return null;
	}

}
