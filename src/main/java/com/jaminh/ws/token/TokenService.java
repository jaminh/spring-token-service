package com.jaminh.ws.token;

import org.jdom2.Element;

public interface TokenService {
	
	public Element issue(Element request);
	
	public Element renew(Element request);
	
	public Element cancel(Element request);
	
	public Element stsCancel(Element request);
	
	public Element validate(Element request);

}
