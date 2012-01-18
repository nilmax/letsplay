package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.letsplay.datamodel.Place;
import com.letsplay.datamodel.SportEntry;

import utils.Utils;
import exception.DataBaseException;

public class AccessCreateActivity {

	private Connection connection;

	public AccessCreateActivity(Connection connection) {
		this.connection = connection;
	}

	public void execute(ArrayList arrayList) throws DataBaseException {
		try {
			String userLogin = (String) arrayList.get(0);
			SportEntry sportEntry = (SportEntry) arrayList.get(1);
			Place place = sportEntry.getPlace();

			int id_place = -1;
			if (place.getId() != -1) {
				id_place = place.getId();
			} else { // it's a new place
				// add new place
				Statement statement = connection.createStatement();
				statement
						.executeUpdate("INSERT INTO lets_play.places ( NAME, ADDRESS, LATITUDE, LONGITUDE ) "
								+ "VALUES ('"
								+ place.getName()
								+ "', '"
								+ place.getAddress()
								+ "', '"
								+ place.getLatitude()
								+ "', '"
								+ place.getLongitude() + "')");
				statement.close();
				id_place = getLastInsertId();
			}

			// add new sport activity
			String query = "INSERT INTO lets_play.sports (sports.TYPE, sports.DATE, sports.TIME, sports.PLACE, sports.NUMBER_OF_PLAYERS, sports.HOST) "
					+ "VALUES ('"
					+ sportEntry.getSportType()
					+ "', '"
					+ Utils.getShortDateString(sportEntry.getDate())
					+ "', '"
					+ Utils.getTimeString(sportEntry.getTime())
					+ "', '"
					+ id_place
					+ "', '"
					+ sportEntry.getNumberOfPlayers()
					+ "', '"
					+ userLogin + "')";
			Statement statementSports = connection.createStatement();
			statementSports
					.executeUpdate(query);
			statementSports.close();

			// add relationship user doing sport activity
			int idSportEntry = getLastInsertId();
			Statement statementRelationship = connection.createStatement();
			statementRelationship
					.executeUpdate("INSERT INTO lets_play.rel_users_sports (ID_USER, ID_SPORT)"
							+ "VALUES ('"
							+ userLogin
							+ "', '"
							+ idSportEntry
							+ "')");
			statementRelationship.close();

			// RSVP
			sportEntry.setId(idSportEntry);
			new AccessSendInvitation(connection).execute(userLogin, sportEntry);

		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(DataBaseException.DATABASE_ACCESS_ERROR);
		}
	}

	private int getLastInsertId() throws SQLException {
		String queryStr = "select LAST_INSERT_ID()";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(queryStr);
		if (rs.next()) {
			return rs.getInt(1);
		}
		rs.close();
		stmt.close();
		return -1;
	}
}
