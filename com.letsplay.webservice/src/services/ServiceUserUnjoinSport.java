package services;

import java.sql.Connection;
import java.util.ArrayList;

import com.letsplay.comm.Envelop;

import database.AccessUserUnjoinSport;
import database.DataBaseConnection;
import exception.DataBaseException;

public class ServiceUserUnjoinSport {

	public Envelop execute(Envelop inputEnvelop) throws DataBaseException {

		try {
			ArrayList<String> arrayList = (ArrayList<String>) inputEnvelop.getValue();
			Connection connection = new DataBaseConnection().create();
			new AccessUserUnjoinSport(connection).execute(arrayList);
			connection.close();
			return new Envelop(Envelop.RequestType.unjoin,"success");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(e.getMessage());
		}
	}

}
