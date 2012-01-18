package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import exception.DataBaseException;

public class AccessUserCoordinates {

	private double mileRange;
	private double latitude;
	private double longitude;

	public AccessUserCoordinates(Connection connection, String login) throws DataBaseException {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT LOGIN, PASS, FIRST_NAME, LAST_NAME, "
							+ "GENDER, ZIPCODE, PICTURE, AGE, "
							+ "MILE_RANGE, LATITUDE, LONGITUDE "
							+ "FROM lets_play.users " + "WHERE LOGIN = '"
							+ login + "'");

			if (resultSet.next()) {
				mileRange = resultSet.getDouble("MILE_RANGE");
				latitude = resultSet.getDouble("LATITUDE");
				longitude = resultSet.getDouble("LONGITUDE");
			} else {
				throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
			}
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
		}
	}

	public double getMileRange() {
		return mileRange;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

}
