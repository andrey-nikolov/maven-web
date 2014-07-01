package org.maven.rest;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

@Path("/system")
public class SystemCheck {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String checkSystem() {
		
		StringBuilder info = new StringBuilder();
		
		info.append(getServerReport());
		info.append(Database.getDatabaseReport());
		
		return info.toString();
	}

	@GET
	@Path("/response")
	@Produces(MediaType.TEXT_PLAIN)
	public String checkResponse () {
		return "OK";
	}
	
	public static String getServerReport () {
		
		StringBuilder report = new StringBuilder("Checking server status...\n");
		report.append("  OS name: " + System.getProperty("os.name") + "\n");
		report.append("  OS version: " + System.getProperty("os.version") + "\n");
		report.append("  Memory total: " + Runtime.getRuntime().totalMemory() + "\n");
		report.append("  Memory free:  " + Runtime.getRuntime().freeMemory() + "\n");

		URI uri = UriBuilder.fromUri("http://localhost:8080/MavenWeb").build();
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(uri);

		service = service.path("v1").path("system").path("response");
		Builder builder = service.accept(MediaType.TEXT_PLAIN).type(MediaType.TEXT_PLAIN);
		
		ClientResponse response = builder.get(ClientResponse.class);
		
		if (response.getStatus() == 200)
			report.append("  HTTP server: Success " + response.getStatus() + "\n");
		else
			report.append("  HTTP server: Error " + response.getStatus() + "\n");
		
		return report.toString();
	}
}
