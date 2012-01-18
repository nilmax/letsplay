package database;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import exception.DataBaseException;

public class AccessInsertFavoriteUser {

	private Connection connection;

	public AccessInsertFavoriteUser(Connection connection) {
		this.connection = connection;
	}

	public void execute(ArrayList<String> arrayList) throws DataBaseException {
		try {

			String userFrom = arrayList.get(0);
			String userTo = arrayList.get(1);
			// add new favorite user
			Statement statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO favorites (ID_FROM, ID_TO) "
					+ "VALUES ('" + userFrom + "', '" + userTo + "')");
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
		}
	}
	
}
