package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.letsplay.datamodel.SportEntry;

import exception.DataBaseException;

public class AccessCheckInbox {

	private Connection connection;
	private AccessUserCoordinates userCoordinates;

	public AccessCheckInbox(Connection connection) {
		this.connection = connection;
	}

	public ArrayList<HashMap<String,SportEntry>> execute(String login) throws DataBaseException {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT inbox.MESSAGE, inbox.ID_USER, inbox.ID_SPORT, inbox.READ, "
							+ "FROM lets_play.inbox ");

			ArrayList list = new ArrayList();
			
			while (resultSet.next()) {
				HashMap map = new HashMap();
				String message = resultSet.getString("inbox.MESSAGE");
				String userId = resultSet.getString("inbox.ID_USER");
				int sportId = resultSet.getInt("inbox.ID_SPORT");
				
				SportEntry sportEntry = new AccessSpecificSport(connection).execute(userId, sportId);
				map.put(message, sportEntry);
				list.add(map);
			}
			resultSet.close();
			statement.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
		}
	}
}
