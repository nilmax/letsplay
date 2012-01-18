package database;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import exception.DataBaseException;

public class AccessUserJoinSport {

	private Connection connection;

	public AccessUserJoinSport(Connection connection) {
		this.connection = connection;
	}

	public void execute(ArrayList<String> arrayList) throws DataBaseException {
		try {
			String id_user = arrayList.get(0);
			String id_sport = arrayList.get(1);

			Statement statement = connection.createStatement();
			statement
					.executeUpdate("INSERT INTO rel_users_sports (ID_USER, ID_SPORT) "
							+ "VALUES ('" + id_user + "', '" + id_sport + "')");
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
		}
	}
}
