package com.interview.api.filters;

import java.util.List;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class AuthFilter implements ResourceFilter {

	private static String APIKEY = "9a5de300-e4c5-11e1-aff1-0800200c9a66";
	private static String APIKEY_PARAM = "apikey";
	
	public ContainerRequestFilter getRequestFilter() {
		return new ContainerRequestFilter() {

			public ContainerRequest filter(ContainerRequest request) {
				List<String> apikeys = request.getQueryParameters().get(APIKEY_PARAM);
				String apikey = apikeys.size() > 0 ? apikeys.get(0) : null;
				if (apikey != null && apikey.equals(APIKEY)) {
					return request;
				}
				
				throw new RuntimeException("Invalid apikey");
			}
		};
	}

	public ContainerResponseFilter getResponseFilter() {
		return null;
	}

}
