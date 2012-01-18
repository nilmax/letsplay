package services;

import java.sql.Connection;
import java.util.ArrayList;

import com.letsplay.comm.Envelop;
import com.letsplay.datamodel.UserEntry;

import database.AccessListOfUsers;
import database.DataBaseConnection;
import exception.DataBaseException;

public class ServiceListOfUsers {

	public Envelop execute(Envelop inputEnvelop) throws DataBaseException {

		try {
			String login = (String) inputEnvelop.getValue();
			Connection connection = new DataBaseConnection().create();
			ArrayList<UserEntry> listUserEntry = (new AccessListOfUsers(
					connection)).execute(login);
			connection.close();
			return new Envelop(Envelop.RequestType.listOfUsers,
					listUserEntry);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(e.getMessage());
		}
	}

}
