package sampleservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.ServiceCheckInbox;
import services.ServiceCreateActivity;
import services.ServiceInsertFavoriteUser;
import services.ServiceListOfPlaces;
import services.ServiceListOfSports;
import services.ServiceListOfUsers;
import services.ServiceUserJoinSport;
import services.ServiceUserUnjoinSport;

import com.letsplay.comm.Envelop;
import com.webservice.task.support.core.ByteBuffer;


/**
 * "http://localhost/androidServlet/servlet/sampleservice.DataPingServlet"
 */
public class DataPingServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse res)
			throws ServletException, IOException {

		ByteBuffer inputBB = new ByteBuffer(request.getInputStream());
		ByteBuffer outputBB = null;

		Envelop outputEnvelop = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(inputBB
					.getInputStream());

			Envelop inputEnvelop = (Envelop) ois.readObject();
			
			switch (inputEnvelop.requestType) {
			case login:
				outputEnvelop = new ServiceListOfSports().execute(inputEnvelop);
				break;
			case listOfUsers:
				outputEnvelop = new ServiceListOfUsers().execute(inputEnvelop);
				break;
			case listOfSports:
				outputEnvelop = new ServiceListOfSports().execute(inputEnvelop);
				break;
			case join:
				outputEnvelop = new ServiceUserJoinSport().execute(inputEnvelop);
				break;
			case unjoin:
				outputEnvelop = new ServiceUserUnjoinSport().execute(inputEnvelop);
				break;
			case listOfPlaces:
				outputEnvelop = new ServiceListOfPlaces().execute(inputEnvelop);
				break;
			case createActivity:
				outputEnvelop = new ServiceCreateActivity().execute(inputEnvelop);
				break;
			case checkInbox:
				outputEnvelop = new ServiceCheckInbox().execute(inputEnvelop);
				break;
			case favorite:
				outputEnvelop = new ServiceInsertFavoriteUser().execute(inputEnvelop);
				break;
			} // end-switch		
		} catch (Exception e) {
			e.printStackTrace();
			outputEnvelop = new Envelop(Envelop.RequestType.error,e.getMessage());
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(outputEnvelop);
			outputBB = new ByteBuffer(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ServletOutputStream sos = res.getOutputStream();
		if (outputBB != null) {
			res.setContentType("application/octet-stream");
			res.setContentLength(outputBB.getSize());
			sos.write(outputBB.getBytes());
		} else {
			res.setContentType("application/octet-stream");
			res.setContentLength(inputBB.getSize());
			sos.write(inputBB.getBytes());
		}
		sos.flush();
		sos.close();
	}

}
