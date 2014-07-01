package org.maven.rest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class User implements Comparable<User> {

	private String name;
	private String gender;
	private int age;
	
	public User () {
		
	}
	
	public User (String name, String gender, int age) {
		this.name = name;
		this.gender = gender;
		this.age = age;
	}

	public String getName () {
		return this.name;
	}
	
	public String getGender () {
		return this.gender;
	}
	
	public int getAge () {
		return this.age;
	}
	
	public int compareTo(User user) {
		return this.name.compareTo(user.getName());
	}
	
	public String toString() {
		
		JSONObject json = new JSONObject();
		try {
			json.put("name", this.name);
			json.put("gender", this.gender);
			json.put("age", this.age);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}
}
