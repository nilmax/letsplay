package services;

import java.sql.Connection;
import java.util.ArrayList;

import com.letsplay.comm.Envelop;

import database.AccessUserJoinSport;
import database.DataBaseConnection;
import exception.DataBaseException;

public class ServiceUserJoinSport {

	public Envelop execute(Envelop inputEnvelop) throws DataBaseException {

		try {
			ArrayList<String> arrayList = (ArrayList<String>) inputEnvelop.getValue();
			Connection connection = new DataBaseConnection().create();
			new AccessUserJoinSport(connection).execute(arrayList);
			connection.close();
			return new Envelop(Envelop.RequestType.join,"success");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(e.getMessage());
		}
	}

}
