package com.letsplay.comm;
import java.io.Serializable;

public class Envelop implements Serializable {

	public enum RequestType {
		error, login, listOfSports, listOfUsers, listOfPlaces, createActivity, join, unjoin, checkInbox, favorite, deleteFavorite
	}

	public RequestType requestType;
	private Object value;

	public Envelop() {

	}

	public Envelop(RequestType requestType, Object value) {
		this.requestType = requestType;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
