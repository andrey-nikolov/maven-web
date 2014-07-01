package org.maven.rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/users")
public class UserQuery {

	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	public JSONObject getUsers (@QueryParam("gender") String gender) {
		
		JSONObject json = new JSONObject();
		List<User> users = new ArrayList<User>();
		
		try {
			users = Database.getUsers(gender);
			json.put("users", users);
		} catch (SQLException e) {
			throw new RuntimeException("Database error.");
		} catch (JSONException e) {
			throw new RuntimeException("Communication error.");
		}
		
		return json;
	}
}
