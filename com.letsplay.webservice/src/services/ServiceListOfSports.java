package services;

import java.sql.Connection;
import java.util.ArrayList;

import com.letsplay.comm.Envelop;
import com.letsplay.datamodel.SportEntry;

import database.AccessListOfSports;
import database.DataBaseConnection;
import exception.DataBaseException;

public class ServiceListOfSports {

	public Envelop execute(Envelop inputEnvelop) throws DataBaseException {

		try {
			String login = (String) inputEnvelop.getValue();
			Connection connection = new DataBaseConnection().create();
			ArrayList<SportEntry> listSport = (new AccessListOfSports(
					connection)).execute(login);
			connection.close();
			return new Envelop(Envelop.RequestType.listOfSports,
					listSport);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(e.getMessage());
		}
	}

}