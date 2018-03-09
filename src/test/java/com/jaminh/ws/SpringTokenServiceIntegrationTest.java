package com.jaminh.ws;

import org.jdom2.Element;
import org.jdom2.transform.JDOMResult;
import org.jdom2.transform.JDOMSource;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.ws.client.core.WebServiceTemplate;

public class SpringTokenServiceIntegrationTest {
	
	@Test
	@Ignore
	public void test()
	{
		WebServiceTemplate client = new WebServiceTemplate();
		client.afterPropertiesSet();
		
		Element request = new Element("RequestSecurityToken", "http://docs.oasis-open.org/ws-sx/ws-trust/200512/");
		JDOMResult response = new JDOMResult();
		client.sendSourceAndReceiveToResult("http://localhost:8080/spring-token-service/ws", new JDOMSource(request), response);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getDocument());
		Element respElement = response.getDocument().getRootElement();
		Assert.assertNotNull(respElement);
		Assert.assertEquals("RequestSecurityTokenResponse", respElement.getName());
	}

}
