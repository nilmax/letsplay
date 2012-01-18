package database;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import exception.DataBaseException;

public class AccessUserUnjoinSport {

	private Connection connection;

	public AccessUserUnjoinSport(Connection connection) {
		this.connection = connection;
	}

	public void execute(ArrayList<String> arrayList) throws DataBaseException {
		try {
			String id_user = arrayList.get(0);
			String id_sport = arrayList.get(1);

			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM rel_users_sports "
					+ "WHERE ID_USER='" + id_user + "' AND ID_SPORT='"
					+ id_sport + "'");
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
		}
	}
}
