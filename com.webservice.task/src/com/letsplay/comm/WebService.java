package com.letsplay.comm;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.methods.PostMethod;

import com.letsplay.comm.Envelop.RequestType;
import com.webservice.task.support.core.ByteBuffer;
import com.webservice.task.support.core.HttpUtils;

import edu.uci.letsplay.datamodel.Place;
import edu.uci.letsplay.datamodel.SportEntry;
import edu.uci.letsplay.datamodel.UserEntry;

public class WebService {

	private String LoginServiceUri;

	public WebService(String serverIPAddress) {
		LoginServiceUri = "http://" + serverIPAddress
				+ ":8080/androidServlet/DataPingServlet";
	}

	public synchronized List<SportEntry> getListOfSports(String login)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(
					Envelop.RequestType.listOfSports, login);
			return (ArrayList<SportEntry>) doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public synchronized List<SportEntry> doLogin(String login)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(Envelop.RequestType.login,
					login);
			return (ArrayList<SportEntry>) doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public synchronized List<UserEntry> getListOfUsers(String login)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(
					Envelop.RequestType.listOfUsers, login);
			return (ArrayList<UserEntry>) doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public synchronized void userJoinSport(List<String> arrayList)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(Envelop.RequestType.join,
					arrayList);
			doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public synchronized void userUnjoinSport(List<String> list)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(Envelop.RequestType.unjoin,
					list);
			doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public synchronized List<Place> getListOfPlaces(String login)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(
					Envelop.RequestType.listOfPlaces, login);
			return (ArrayList<Place>) doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public synchronized void createSportActivity(List list)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(
					Envelop.RequestType.createActivity, list);
			doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public synchronized List<HashMap<String, SportEntry>> checkInbox(
			String login) throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(
					Envelop.RequestType.checkInbox, login);

			return (ArrayList<HashMap<String, SportEntry>>) doRequest(
					messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public synchronized void setFavorite(List<String> list)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(Envelop.RequestType.favorite,
					list);
			doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public synchronized void deleteFavorite(List<String> list)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(
					Envelop.RequestType.deleteFavorite, list);
			doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	private synchronized Envelop doRequest(Envelop messageRequest)
			throws AccessServletException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(messageRequest);

			PostMethod post = HttpUtils.sendMonitoredPOSTRequest(
					LoginServiceUri, null, new ByteBuffer(baos.toByteArray()),
					null);

			ByteBuffer data = HttpUtils.getMonitoredResponse(null, post);
			ObjectInputStream ois = new ObjectInputStream(data.getInputStream());
			Envelop messageFromServlet = (Envelop) ois.readObject();

			if (messageFromServlet.requestType.compareTo(RequestType.error) == 0) {
				String errorMsg = (String) messageFromServlet.getValue();
				throw new AccessServletException(errorMsg);
			}

			return messageFromServlet;
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

}
