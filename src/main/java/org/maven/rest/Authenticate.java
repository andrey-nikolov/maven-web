package org.maven.rest;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/authenticate")
public class Authenticate {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authenticate (Credentials credentials) {

		JSONObject json = new JSONObject();
		
		try {
			if (!Database.checkUsername(credentials.getUsername())) {
				json.put("authenticated", new Boolean(false));
				json.put("message", "Unknown username.");
			} else if (!Database.checkPassword(credentials.getUsername(), credentials.getPassword())) {
				json.put("authenticated", new Boolean(false));
				json.put("message", "Wrong password.");
			} else {
				json.put("authenticated", new Boolean(true));
				json.put("message", "Correct credentials.");
			}
		} catch (SQLException e) {
			try {
				json.put("authenticated", new Boolean(false));
				json.put("message", "Database error.");
			} catch (JSONException e1) {
				throw new RuntimeException("Communication error.");
			}
		} catch (JSONException e) {
			throw new RuntimeException("Communication error.");
		}
		
		return Response.status(200).entity(json).build();
	}
}
