package services;

import java.sql.Connection;
import java.util.ArrayList;

import com.letsplay.comm.Envelop;
import com.letsplay.datamodel.Place;

import database.AccessListOfPlaces;
import database.DataBaseConnection;
import exception.DataBaseException;

public class ServiceListOfPlaces {

	public Envelop execute(Envelop inputEnvelop) throws DataBaseException {

		try {
			String login = (String) inputEnvelop.getValue();
			Connection connection = new DataBaseConnection().create();
			ArrayList<Place> listPlace = (new AccessListOfPlaces(connection))
					.execute(login);
			connection.close();
			return new Envelop(Envelop.RequestType.listOfPlaces, listPlace);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(e.getMessage());
		}
	}

}