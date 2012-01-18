package services;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.letsplay.comm.Envelop;
import com.letsplay.datamodel.SportEntry;

import database.AccessCheckInbox;
import database.DataBaseConnection;
import exception.DataBaseException;

public class ServiceCheckInbox {

	public Envelop execute(Envelop inputEnvelop) throws DataBaseException {
		try {
			String login = (String) inputEnvelop.getValue();
			Connection connection = new DataBaseConnection().create();
			ArrayList<HashMap<String,SportEntry>> arrayList = (new AccessCheckInbox(connection)).execute(login);
			connection.close();
			//return new Envelop(Envelop.RequestType.checkInbox, arrayList); //TODO
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(e.getMessage());
		}
		return null;
	}

}