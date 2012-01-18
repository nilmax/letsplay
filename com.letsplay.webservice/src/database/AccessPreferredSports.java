package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.letsplay.datamodel.UserEntry;

import exception.DataBaseException;

public class AccessPreferredSports {

	private Connection connection;
	private AccessUserCoordinates userCoordinates;

	public AccessPreferredSports(Connection connection) {
		this.connection = connection;
	}

	public void execute(UserEntry userEntry) throws DataBaseException {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery(" SELECT (rel_users_sports_type.ID_SPORT_TYPE) "
							+ "FROM rel_users_sports_type "
							+ "WHERE rel_users_sports_type.ID_USER = '"
							+ userEntry.getLogin() + "'");

			while (resultSet.next()) {
				userEntry.addPreferredSports(resultSet.getInt("ID_SPORT_TYPE"));
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
		}
	}
}
