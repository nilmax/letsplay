package com.letsplay.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserEntry implements Serializable {

	private String login;
	private String password;
	private String firstName;
	private String lastName;
	private String telefone;
	private String gender;
	private int age;
	private int expertise;
	private byte[] picture;
	private int zipcode;
	private double mileRange;
	private double latitude;
	private double longitude;
	private double distance;
	private List<String> preferredSports = new ArrayList<String>();
	private boolean favorite;

	public UserEntry(){
		login = "";
		password = "";
		firstName = "";
		lastName = "";
		telefone = "";
		gender = "";
		picture = new byte[0];
	}
	
	public boolean isFavorite() {
		return favorite;
	}


	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}


	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int[] getPreferredSports() {
		int[] result = new int[preferredSports.size()];
		int i = 0;
		for (String sport : preferredSports) {
			result[i++] = Integer.parseInt(sport);
		}
		return result;
	}

	public void addPreferredSports(int type) {
		this.preferredSports.add(String.valueOf(type));
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getExpertise() {
		return expertise;
	}

	public void setExpertise(int expertise) {
		this.expertise = expertise;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public double getMileRange() {
		return mileRange;
	}

	public void setMileRange(double mileRange) {
		this.mileRange = mileRange;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
