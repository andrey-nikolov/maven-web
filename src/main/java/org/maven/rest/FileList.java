package org.maven.rest;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/files")
public class FileList {
	
	@GET
	@Path("/{folder:.*}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getFiles(@PathParam("folder") String folder) {
		
		StringBuffer list = new StringBuffer();
		
		File directory = new File("/" + folder);
		
		if (directory.exists()) {
			for (File file : directory.listFiles()) {
				if (file.isFile()) {
					list.append(file.getName()).append("\n");
				}
			}
		}
		
		return list.toString();
	}
}
