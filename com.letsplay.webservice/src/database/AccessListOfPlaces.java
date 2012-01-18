package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.letsplay.datamodel.Place;

import utils.GlobalPositionSystem;
import exception.DataBaseException;

public class AccessListOfPlaces {

	private Connection connection;
	private AccessUserCoordinates userCoordinates;

	public AccessListOfPlaces(Connection connection) {
		this.connection = connection;
	}

	public ArrayList<Place> execute(String login) throws DataBaseException {
		try {
			userCoordinates = new AccessUserCoordinates(connection, login);

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT places.ID, places.NAME, places.ADDRESS, places.LATITUDE, places.LONGITUDE "
							+ "FROM places ");

			ArrayList<Place> listPlace = new ArrayList<Place>();

			while (resultSet.next()) {
				Place place = new Place();
				place.setId(resultSet.getInt("places.ID"));
				place.setName(resultSet.getString("places.NAME"));
				place.setAddress(resultSet.getString("places.ADDRESS"));
				place.setLatitude(resultSet.getDouble("places.LATITUDE"));
				place.setLongitude(resultSet.getDouble("places.LONGITUDE"));

				double distance = GlobalPositionSystem.getDistance(
						userCoordinates.getLatitude(), userCoordinates
								.getLongitude(), place.getLatitude(), place
								.getLongitude(), 'M');
				place.setDistance(distance);
				if (distance <= userCoordinates.getMileRange()) {
					listPlace.add(place);
				}
			}
			resultSet.close();
			statement.close();
			return listPlace;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
		}
	}
}
