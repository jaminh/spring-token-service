package com.jaminh.ws.trust;

import java.net.URI;

public enum RequestType {
	ISSUE(URI.create("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue")),
	RENEW(URI.create("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Renew")),
	CANCEL(URI.create("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Cancel")),
	STS_CANCEL(URI.create("http://docs.oasis-open.org/ws-sx/ws-trust/200512/STSCancel")),
	VALIDATE(URI.create("http://docs.oasis-open.org/ws-sx/ws-trust/200512/Validate"));
	
	private URI uri;
	
	private RequestType(URI uri)
	{
		this.uri = uri;
	}
	
	public URI getUri()
	{
		return uri;
	}
	
	public static RequestType getRequestType(URI uri)
	{
		for (RequestType type : RequestType.values())
		{
			if (type.getUri().equals(uri))
			{
				return type;
			}
		}
		
		return null;
	}

}
