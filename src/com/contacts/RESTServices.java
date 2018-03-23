package com.contacts;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("api")
public class RESTServices extends ResourceConfig {
	
	public RESTServices() {
		packages("com.fasterxml.jackson.jaxrs.json");
        packages("com.contacts");
	}

}
