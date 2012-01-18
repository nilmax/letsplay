package test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.methods.PostMethod;

import utils.Utils;

import com.letsplay.comm.Envelop;
import com.letsplay.comm.Envelop.RequestType;
import com.letsplay.datamodel.Place;
import com.letsplay.datamodel.SportEntry;
import com.letsplay.datamodel.SportTypes;
import com.letsplay.datamodel.UserEntry;
import com.webservice.task.support.core.ByteBuffer;
import com.webservice.task.support.core.HttpUtils;


public class WebService {

	public static final String LoginServiceUri = "http://127.0.0.1:8080/androidServlet/DataPingServlet";

	public ArrayList<SportEntry> getListOfSports(String login)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(
					Envelop.RequestType.listOfSports, login);
			return (ArrayList<SportEntry>) doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public ArrayList<SportEntry> doLogin(String login)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(Envelop.RequestType.login,
					login);
			return (ArrayList<SportEntry>) doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public ArrayList<UserEntry> getListOfUsers(String login)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(
					Envelop.RequestType.listOfUsers, login);
			return (ArrayList<UserEntry>) doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public void userJoinSport(ArrayList<String> arrayList)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(Envelop.RequestType.join,
					arrayList);
			doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public void userUnjoinSport(List<String> list)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(Envelop.RequestType.unjoin,
					list);
			doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public ArrayList<Place> getListOfPlaces(String login)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(
					Envelop.RequestType.listOfPlaces, login);
			return (ArrayList<Place>) doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public void createSportActivity(List list) throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(
					Envelop.RequestType.createActivity, list);
			doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public ArrayList<HashMap<String, SportEntry>> checkInbox(String login)
			throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(
					Envelop.RequestType.checkInbox, login);

			return (ArrayList<HashMap<String, SportEntry>>) doRequest(
					messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	public void setFavorite(List<String> list) throws AccessServletException {
		try {
			Envelop messageRequest = new Envelop(Envelop.RequestType.favorite,
					list);
			doRequest(messageRequest).getValue();
		} catch (Exception e) {
			throw new AccessServletException(e.getMessage());
		}
	}

	private Envelop doRequest(Envelop messageRequest)
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

	public static void main(String[] args) {
		// testListOfUsers();
		testSetFavorite();
		System.out.println();
	}

	private static void testSetFavorite() {
		WebService webService = new WebService();
		try {
			ArrayList<String> list = new ArrayList<String>();
			list.add("6099029862");
			list.add("6099029865");
			webService.setFavorite(list);
		} catch (AccessServletException e) {
			e.printStackTrace();
		}
	}

	private static void testCheckInbox() {
		WebService webService = new WebService();
		try {
			ArrayList<HashMap<String, SportEntry>> list = webService
					.checkInbox("6099029862");
			System.out.println();
		} catch (AccessServletException e) {
			e.printStackTrace();
		}
	}

	private static void testCreateActivity() {
		WebService webService = new WebService();
		try {
			ArrayList list = new ArrayList<String>();

			// TODO estou testando com uma atividade NOVA
			Place place = new Place();
			place.setAddress("test invitation address");
			place.setLatitude(33.904616);
			place.setLongitude(-117.784424);
			place.setName("casa da vovo");

			SportEntry sportEntry = new SportEntry();
			sportEntry.setDate(new Date(2009, 03, 20));
			sportEntry.setId(-1);
			sportEntry.setPlace(place);
			sportEntry.setSportType(SportTypes.FOOTBALL);
			sportEntry.setTime(Utils.getTime(1, 35));

			list.add("6099029862");
			list.add(sportEntry);
			webService.createSportActivity(list);
		} catch (AccessServletException e) {
			e.printStackTrace();
		}
	}

	private static void testListOfPlaces() {
		WebService webService = new WebService();
		try {
			ArrayList<Place> list = webService.getListOfPlaces("6099029862");
			for (Place place : list) {
				System.out.println(place.getName());
			}
		} catch (AccessServletException e) {
			e.printStackTrace();
		}
	}

	private static void testUserUnjoinSport() {
		WebService webService = new WebService();
		try {
			ArrayList<String> list = new ArrayList<String>();
			list.add("6099029864");
			list.add("1");
			webService.userUnjoinSport(list);
		} catch (AccessServletException e) {
			e.printStackTrace();
		}
	}

	private static void TestUserJoinSport() {
		WebService webService = new WebService();
		try {
			ArrayList<String> list = new ArrayList<String>();
			list.add("6099029863");
			list.add("1");
			webService.userJoinSport(list);
		} catch (AccessServletException e) {
			e.printStackTrace();
		}
	}

	private static void testListOfSports() {
		WebService webService = new WebService();
		try {
			ArrayList<SportEntry> list = webService
					.getListOfSports("6099029862");
			for (SportEntry sportEntry : list) {
				System.out.println(sportEntry.getPlace().getName());
				System.out.println(sportEntry.getHost().getFirstName());
				System.out.println(sportEntry.getHost().getPreferredSports());
			}
		} catch (AccessServletException e) {
			e.printStackTrace();
		}
	}

	private static void testListOfUsers() {
		WebService webService = new WebService();
		try {
			ArrayList<UserEntry> list = webService.getListOfUsers("6099029862");
			for (UserEntry userEntry : list) {
				System.out.println(userEntry.getLogin());
				System.out.println(userEntry.getDistance());
				System.out.println(userEntry.getPreferredSports());
			}
		} catch (AccessServletException e) {
			e.printStackTrace();
		}
	}

}
