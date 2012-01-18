package database;

import java.sql.Connection;
import java.sql.DriverManager;

import exception.DataBaseException;

public class DataBaseConnection {

	private final static String userName = "letsplay";
	private final static String password = "letsplay";
	private final static String url = "jdbc:mysql://10.0.1.4/lets_play";

	public Connection create() throws DataBaseException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			return DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_CONNECTION_ERROR);
		}

	}

}
