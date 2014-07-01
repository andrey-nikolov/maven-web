package org.maven.rest;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class TestClient {

	public static void main(String[] args) {
		
		TestClient test = new TestClient();
		
		// authenticate
		test.authenticate1();
		test.authenticate2();
		test.authenticate3();
		
		// users
		test.users1();
		test.users2();
		test.users3();
		
		// system
		test.system1();
		
		// files
		test.files1();
		test.files2();
		test.files3();
	}
	
	private static String authenticate (String username, String password) {

		URI uri = UriBuilder.fromUri("http://localhost:8080/MavenWeb").build();
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(config);
		WebResource service = client.resource(uri);

		service = service.path("v1").path("authenticate");
		Builder builder = service.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);

		Credentials credentials = new Credentials(username, password);
		ClientResponse response = builder.post(ClientResponse.class, credentials);

		if (response.getStatus() != 200)
			return "Failure: HTTP error " + response.getStatus();
		else
			return "Success: " + response.getEntity(String.class);
	}
	
	private static String users (String gender) {
		
		URI uri = UriBuilder.fromUri("http://localhost:8080/MavenWeb").build();
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(config);
		WebResource service = client.resource(uri);
		
		service = service.path("v1").path("users").queryParam("gender", gender);
		Builder builder = service.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
		
		ClientResponse response = builder.get(ClientResponse.class);
		
		if (response.getStatus() != 200)
			return "Failure: HTTP error " + response.getStatus();
		else
			return "Success: " + quote(response.getEntity(String.class));
	}
	
	private static String system () {
		
		URI uri = UriBuilder.fromUri("http://localhost:8080/MavenWeb").build();
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(config);
		WebResource service = client.resource(uri);

		service = service.path("v1").path("system");
		Builder builder = service.accept(MediaType.TEXT_PLAIN).type(MediaType.TEXT_PLAIN);
		
		ClientResponse response = builder.get(ClientResponse.class);
		
		if (response.getStatus() != 200)
			return "Failure: HTTP error " + response.getStatus();
		else
			return "Success: " + quote(response.getEntity(String.class));
	}
	
	private static String files (String folder) {

		URI uri = UriBuilder.fromUri("http://localhost:8080/MavenWeb").build();
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(config);
		WebResource service = client.resource(uri);

		service = service.path("v1").path("files").path(folder);
		Builder builder = service.accept(MediaType.TEXT_PLAIN).type(MediaType.TEXT_PLAIN);
		
		ClientResponse response = builder.get(ClientResponse.class);
		
		if (response.getStatus() != 200)
			return "Failure: HTTP error " + response.getStatus();
		else
			return "Success: " + quote(response.getEntity(String.class));
	}
	
	@Test
	public void authenticate1 () {
		System.out.println("\nTest: /authenticate (Clara/Password)");
		String response = TestClient.authenticate("Clara", "Password");
		System.out.println(response);
		Assert.assertEquals("Success: {\"authenticated\":false,\"message\":\"Unknown username.\"}", response);
	}
	
	@Test
	public void authenticate2 () {
		System.out.println("\nTest: /authenticate (Andrey/Nikolov)");
		String response = TestClient.authenticate("Andrey", "Nikolov");
		System.out.println(response);
		Assert.assertEquals("Success: {\"authenticated\":false,\"message\":\"Wrong password.\"}", response);
	}
	
	@Test
	public void authenticate3 () {
		System.out.println("\nTest: /authenticate (Andrey/Password)");
		String response = TestClient.authenticate("Andrey", "Password");
		System.out.println(response);
		Assert.assertEquals("Success: {\"authenticated\":true,\"message\":\"Correct credentials.\"}", response);
	}
	
	@Test
	public void users1 () {
		System.out.println("\nTest: /users");
		String response = TestClient.users("");
		System.out.println(response);
		Assert.assertEquals(
				"Success: {\"users\":[" +
				"{\"name\":\"Andrey\",\"gender\":\"Male\",\"age\":36}," +
				"{\"name\":\"Boris\",\"gender\":\"Male\",\"age\":33}," +
				"{\"name\":\"Carlo\",\"gender\":\"Male\",\"age\":32}," +
				"{\"name\":\"Maria\",\"gender\":\"Female\",\"age\":31}" +
				"]}", response);
	}
	
	@Test
	public void users2 () {
		System.out.println("\nTest: /users?gender=Male");
		String response = TestClient.users("Male");
		System.out.println(response);
		Assert.assertEquals(
				"Success: {\"users\":[" +
				"{\"name\":\"Andrey\",\"gender\":\"Male\",\"age\":36}," +
				"{\"name\":\"Boris\",\"gender\":\"Male\",\"age\":33}," +
				"{\"name\":\"Carlo\",\"gender\":\"Male\",\"age\":32}" +
				"]}", response);
	}
	
	@Test
	public void users3 () {
		System.out.println("\nTest: /users?gender=Female");
		String response = TestClient.users("Female");
		System.out.println(response);
		Assert.assertEquals(
				"Success: {\"users\":[" +
				"{\"name\":\"Maria\",\"gender\":\"Female\",\"age\":31}" +
				"]}", response);
	}
	
	@Test
	public void system1 () {
		System.out.println("\nTest: /system");
		String response = TestClient.system();
		System.out.println(response);
		Assert.assertNotNull(response);
	}
	
	@Test
	public void files1 () {
		System.out.println("\nTest: /files/bin");
		String response = TestClient.files("bin");
		System.out.println(response);
		Assert.assertNotNull(response);
	}
	
	@Test
	public void files2 () {
		System.out.println("\nTest: /files/Users/Andrey/Downloads");
		String response = TestClient.files("Users/Andrey/Downloads");
		System.out.println(response);
		Assert.assertNotNull(response);
	}
	
	@Test
	public void files3 () {
		System.out.println("\nTest: /files/Dummy/Folder");
		String response = TestClient.files("Dummy/Folder");
		System.out.println(response);
		Assert.assertEquals("Success: ", response);
	}
	
	private static String quote (String json) {
		json = json.replace("\\", "");
		json = json.replace("\"{", "{");
		json = json.replace("}\"", "}");
		return json;
	}
}
