package services;

import java.sql.Connection;
import java.util.ArrayList;

import com.letsplay.comm.Envelop;

import database.AccessCreateActivity;
import database.DataBaseConnection;
import exception.DataBaseException;

public class ServiceCreateActivity {

	public Envelop execute(Envelop inputEnvelop) throws DataBaseException {
		try {
			ArrayList arraylist = (ArrayList) inputEnvelop.getValue();
			Connection connection = new DataBaseConnection().create();
			(new AccessCreateActivity(connection)).execute(arraylist);
			connection.close();
			return new Envelop(Envelop.RequestType.createActivity, "sucess");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataBaseException(e.getMessage());
		}
	}

}