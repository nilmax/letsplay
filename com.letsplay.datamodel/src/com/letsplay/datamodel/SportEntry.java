package com.letsplay.datamodel;

import java.io.Serializable;
import java.util.Date;

public class SportEntry implements Serializable {
	private int id;
	private int type;
	private Date date;
	private Date time;
	private Place place;
	private UserEntry host;
	private int numberOfPlayers;
	private int confirmedNumberOfPlayers;
	private boolean amISubscribed;

	public SportEntry(int id, int type, String place, String address,
			Date date, double distance) {
		this();
		this.type = type;
		this.date = date;
		this.place.setName(place);
		this.place.setAddress(address);
		this.place.setDistance(distance);
	}

	public SportEntry() {
		this.place = new Place();
		this.host = new UserEntry();
		this.time = new Date();
		this.numberOfPlayers = 0;
		this.confirmedNumberOfPlayers = 0;
	}

	public UserEntry getHost() {
		return host;
	}
	
	public void setHost(UserEntry host) {
		this.host = host;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSportType() {
		return type;
	}

	public void setSportType(int type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	public int getConfirmedNumberOfPlayers() {
		return confirmedNumberOfPlayers;
	}

	public void setConfirmedNumberOfPlayers(int confirmedNumberOfPlayers) {
		this.confirmedNumberOfPlayers = confirmedNumberOfPlayers;
	}

	public boolean isAmISubscribed() {
		return amISubscribed;
	}

	public void setAmISubscribed(boolean amISubscribed) {
		this.amISubscribed = amISubscribed;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}
}
